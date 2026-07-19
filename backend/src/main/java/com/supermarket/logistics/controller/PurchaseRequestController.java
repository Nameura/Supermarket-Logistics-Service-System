package com.supermarket.logistics.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermarket.logistics.common.Result;
import com.supermarket.logistics.entity.FinanceRecord;
import com.supermarket.logistics.entity.PurchaseRequest;
import com.supermarket.logistics.service.*;
import com.supermarket.logistics.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 采购请求控制器
 */
@RestController
@RequestMapping("/purchase")
@CrossOrigin
public class PurchaseRequestController {
    
    @Autowired
    private PurchaseRequestService purchaseRequestService;
    
    @Autowired
    private GoodsService goodsService;
    
    @Autowired
    private SupplierService supplierService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FinanceRecordService financeRecordService;
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 分页查询采购请求
     */
    @GetMapping("/page")
    public Result<Page<PurchaseRequest>> page(@RequestParam(defaultValue = "1") Integer current,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               @RequestParam(required = false) String requestNo,
                                               @RequestParam(required = false) String goodsName,
                                               @RequestParam(required = false) Integer approvalStatus,
                                               @RequestParam(required = false) Long applicantId) {
        
        // 使用关联查询获取所有符合条件的记录
        List<PurchaseRequest> allRecords = purchaseRequestService.getPageWithDetails(requestNo, goodsName, approvalStatus, applicantId);
        
        // 手动分页
        int total = allRecords.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        Page<PurchaseRequest> page = new Page<>(current, size);
        page.setTotal(total);
        page.setRecords(start < total ? allRecords.subList(start, end) : new java.util.ArrayList<>());
        
        return Result.success(page);
    }
    
    /**
     * 获取所有采购请求
     */
    @GetMapping("/list")
    public Result<List<PurchaseRequest>> list() {
        List<PurchaseRequest> requests = purchaseRequestService.getAllWithDetails();
        return Result.success(requests);
    }
    
    /**
     * 根据ID获取采购请求
     */
    @GetMapping("/{id}")
    public Result<PurchaseRequest> getById(@PathVariable Long id) {
        PurchaseRequest request = purchaseRequestService.getWithDetails(id);
        if (request == null) {
            return Result.error("采购请求不存在");
        }
        return Result.success(request);
    }
    
    /**
     * 添加采购请求
     */
    @PostMapping
    public Result<PurchaseRequest> save(@RequestBody PurchaseRequest request,
                                         @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        
        request.setRequestNo(purchaseRequestService.generateRequestNo());
        request.setApplicantId(userId);
        request.setApprovalStatus(0);
        request.setStatus(1);
        
        if (request.getQuantity() != null && request.getUnitPrice() != null) {
            request.setTotalPrice(request.getUnitPrice().multiply(new java.math.BigDecimal(request.getQuantity())));
        }
        
        boolean success = purchaseRequestService.save(request);
        if (success) {
            return Result.success("提交成功", request);
        }
        return Result.error("提交失败");
    }
    
    /**
     * 更新采购请求
     */
    @PutMapping
    public Result<PurchaseRequest> update(@RequestBody PurchaseRequest request) {
        PurchaseRequest existRequest = purchaseRequestService.getById(request.getId());
        if (existRequest == null) {
            return Result.error("采购请求不存在");
        }
        
        if (existRequest.getApprovalStatus() != 0) {
            return Result.error("已审批的请求不能修改");
        }
        
        if (request.getQuantity() != null && request.getUnitPrice() != null) {
            request.setTotalPrice(request.getUnitPrice().multiply(new java.math.BigDecimal(request.getQuantity())));
        }
        
        boolean success = purchaseRequestService.updateById(request);
        if (success) {
            return Result.success("更新成功", request);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 审批采购请求
     * 审批通过时自动创建财务支出记录并增加库存
     */
    @PutMapping("/approve/{id}")
    public Result<PurchaseRequest> approve(@PathVariable Long id,
                                            @RequestBody Map<String, Object> params,
                                            @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) { // 去掉token前缀
            token = token.substring(7);
        }
        // 从token中获取当前登录用户ID
        Long userId = jwtUtils.getUserIdFromToken(token);
        PurchaseRequest request = purchaseRequestService.getWithDetails(id);
        // 校验请求是否存在及是否已审批
        if (request == null) {
            return Result.error("采购请求不存在");
        }

        if (request.getApprovalStatus() != 0) { // 已审批的请求不能再审批
            return Result.error("该请求已审批");
        }

        // 获取审批状态、审批备注
        Integer approvalStatus = (Integer) params.get("approvalStatus"); // 审批状态
        String approvalRemark = (String) params.get("approvalRemark"); // 审批备注

        // 更新审批状态及审批时间
        request.setApprovalStatus(approvalStatus);
        request.setApproverId(userId);
        request.setApprovalRemark(approvalRemark);
        request.setApprovalTime(LocalDateTime.now());

        // 更新该采购订单信息
        boolean success = purchaseRequestService.updateById(request);
        if (success) {
            // 审批通过时，自动创建财务支出记录
            if (approvalStatus == 1 && request.getTotalPrice() != null) {
                FinanceRecord financeRecord = new FinanceRecord();
                // 生成财务记录编号
                financeRecord.setRecordNo(financeRecordService.generateRecordNo());
                // 设置支出类型
                financeRecord.setType(2);
                // 设置支出分类
                financeRecord.setCategory("采购支出");
                // 设置支出金额
                financeRecord.setAmount(request.getTotalPrice());
                // 设置记录描述，各项信息按顺序拼接
                financeRecord.setDescription("采购申请[" + request.getRequestNo() + "]审批通过 - " + 
                    request.getGoodsName() + " x " + request.getQuantity() + "，供应商：" + request.getSupplierName());
                // 设置操作人、当前订单状态、记录时间
                financeRecord.setOperatorId(userId);
                financeRecord.setStatus(1);
                financeRecord.setRecordDate(LocalDateTime.now());
                financeRecordService.save(financeRecord);
                
                // 审批通过时，自动增加库存数量
                if (request.getGoodsId() != null && request.getQuantity() != null) {
                    inventoryService.increaseStock(request.getGoodsId(), request.getQuantity(), "一号仓库");
                }
            }
            return Result.success("审批成功", request);
        }
        return Result.error("审批失败");
    }
    
    /**
     * 删除采购请求
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        PurchaseRequest request = purchaseRequestService.getById(id);
        if (request == null) {
            return Result.error("采购请求不存在");
        }
        
        if (request.getApprovalStatus() == 1) {
            return Result.error("已通过的请求不能删除");
        }
        
        boolean success = purchaseRequestService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    /**
     * 批量审批采购请求
     * 审批通过时自动创建财务支出记录并增加库存
     * @param ids 采购请求ID列表
     * @param params 包含approvalStatus和approvalRemark
     * @param token JWT Token
     * @return 审批结果
     */
    @PutMapping("/batch-approve")
    public Result<Map<String, Object>> batchApprove(@RequestBody Map<String, Object> params,
                                                     @RequestHeader("Authorization") String token) {
        // 获取采购请求ID列表
        @SuppressWarnings("unchecked")
        List<Integer> ids = (List<Integer>) params.get("ids");
        Integer approvalStatus = (Integer) params.get("approvalStatus");
        String approvalRemark = (String) params.get("approvalRemark");
        
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要审批的采购请求");
        }
        
        // 解析Token获取用户ID
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtils.getUserIdFromToken(token);
        
        // 在审批备注前添加"批量审批 - "前缀
        String finalRemark = "批量审批 - " + (approvalRemark != null ? approvalRemark : "");
        
        int successCount = 0;
        int failCount = 0;
        
        for (Integer id : ids) {
            PurchaseRequest request = purchaseRequestService.getWithDetails(id.longValue());
            if (request == null || request.getApprovalStatus() != 0) {
                failCount++;
                continue;
            }
            
            // 更新审批状态
            request.setApprovalStatus(approvalStatus);
            request.setApproverId(userId);
            request.setApprovalRemark(finalRemark);
            request.setApprovalTime(LocalDateTime.now());
            
            boolean success = purchaseRequestService.updateById(request);
            if (success) {
                // 审批通过时，自动创建财务支出记录并增加库存
                if (approvalStatus == 1 && request.getTotalPrice() != null) {
                    FinanceRecord financeRecord = new FinanceRecord();
                    financeRecord.setRecordNo(financeRecordService.generateRecordNo());
                    financeRecord.setType(2);
                    financeRecord.setCategory("采购支出");
                    financeRecord.setAmount(request.getTotalPrice());
                    financeRecord.setDescription("采购申请[" + request.getRequestNo() + "]审批通过 - " + 
                        request.getGoodsName() + " x " + request.getQuantity() + "，供应商：" + request.getSupplierName());
                    financeRecord.setOperatorId(userId);
                    financeRecord.setStatus(1);
                    financeRecord.setRecordDate(LocalDateTime.now());
                    financeRecordService.save(financeRecord);
                    
                    if (request.getGoodsId() != null && request.getQuantity() != null) {
                        inventoryService.increaseStock(request.getGoodsId(), request.getQuantity(), "一号仓库");
                    }
                }
                successCount++;
            } else {
                failCount++;
            }
        }
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        
        if (failCount == 0) {
            return Result.success("批量审批成功，共审批" + successCount + "条记录", result);
        } else {
            return Result.success("批量审批完成，成功" + successCount + "条，失败" + failCount + "条", result);
        }
    }
    
    /**
     * 获取采购统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("statusDistribution", purchaseRequestService.countByStatus());
        data.put("approvedTotal", purchaseRequestService.calculateApprovedTotal());
        data.put("monthlyStatistics", purchaseRequestService.monthlyStatistics());
        return Result.success(data);
    }
}
