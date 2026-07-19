package com.supermarket.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermarket.logistics.common.Result;
import com.supermarket.logistics.entity.FinanceRecord;
import com.supermarket.logistics.service.FinanceRecordService;
import com.supermarket.logistics.service.UserService;
import com.supermarket.logistics.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 财务记录控制器
 */
@RestController
@RequestMapping("/finance")
@CrossOrigin
public class FinanceRecordController {
    
    @Autowired
    private FinanceRecordService financeRecordService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 分页查询财务记录
     */
    @GetMapping("/page")
    public Result<Page<FinanceRecord>> page(@RequestParam(defaultValue = "1") Integer current,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             @RequestParam(required = false) String recordNo,
                                             @RequestParam(required = false) Integer type,
                                             @RequestParam(required = false) String category) {
        Page<FinanceRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<FinanceRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(recordNo)) {
            wrapper.like(FinanceRecord::getRecordNo, recordNo);
        }
        if (type != null) {
            wrapper.eq(FinanceRecord::getType, type);
        }
        if (StringUtils.isNotBlank(category)) {
            wrapper.like(FinanceRecord::getCategory, category);
        }
        wrapper.eq(FinanceRecord::getDeleted, 0);
        wrapper.orderByDesc(FinanceRecord::getRecordDate);
        
        financeRecordService.page(page, wrapper);
        
        page.getRecords().forEach(record -> {
            if (record.getOperatorId() != null) {
                record.setOperatorName(userService.getById(record.getOperatorId()) != null ? 
                    userService.getById(record.getOperatorId()).getRealName() : null);
            }
            record.setTypeName(getTypeName(record.getType()));
        });
        
        return Result.success(page);
    }
    
    /**
     * 获取所有财务记录
     */
    @GetMapping("/list")
    public Result<List<FinanceRecord>> list() {
        List<FinanceRecord> records = financeRecordService.getAllWithOperator();
        records.forEach(record -> record.setTypeName(getTypeName(record.getType())));
        return Result.success(records);
    }
    
    /**
     * 根据ID获取财务记录
     */
    @GetMapping("/{id}")
    public Result<FinanceRecord> getById(@PathVariable Long id) {
        FinanceRecord record = financeRecordService.getWithOperator(id);
        if (record == null) {
            return Result.error("财务记录不存在");
        }
        record.setTypeName(getTypeName(record.getType()));
        return Result.success(record);
    }
    
    /**
     * 添加财务记录
     */
    @PostMapping
    public Result<FinanceRecord> save(@RequestBody FinanceRecord record,
                                       @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        
        record.setRecordNo(financeRecordService.generateRecordNo());
        record.setOperatorId(userId);
        record.setStatus(1);
        
        boolean success = financeRecordService.save(record);
        if (success) {
            return Result.success("添加成功", record);
        }
        return Result.error("添加失败");
    }
    
    /**
     * 更新财务记录
     */
    @PutMapping
    public Result<FinanceRecord> update(@RequestBody FinanceRecord record) {
        FinanceRecord existRecord = financeRecordService.getById(record.getId());
        if (existRecord == null) {
            return Result.error("财务记录不存在");
        }
        
        boolean success = financeRecordService.updateById(record);
        if (success) {
            return Result.success("更新成功", record);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 删除财务记录
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = financeRecordService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    /**
     * 获取财务统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("totalIncome", financeRecordService.calculateTotalIncome());
        data.put("totalExpense", financeRecordService.calculateTotalExpense());
        data.put("sumByType", financeRecordService.sumByType());
        data.put("sumByCategory", financeRecordService.sumByCategory());
        data.put("monthlySumByType", financeRecordService.monthlySumByType());
        return Result.success(data);
    }
    
    /**
     * 获取类型名称
     */
    private String getTypeName(Integer type) {
        if (type == null) return "未知";
        switch (type) {
            case 1: return "收入";
            case 2: return "支出";
            default: return "未知";
        }
    }
}
