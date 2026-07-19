package com.supermarket.logistics.controller;

import com.supermarket.logistics.common.Result;
import com.supermarket.logistics.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页数据统计控制器
 */
@RestController
@RequestMapping("/dashboard")
@CrossOrigin
public class DashboardController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private GoodsService goodsService;
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private PurchaseRequestService purchaseRequestService;
    
    @Autowired
    private FinanceRecordService financeRecordService;
    
    @Autowired
    private ScheduleService scheduleService;
    
    /**
     * 获取首页统计数据
     * 本质上是通过各种Service获取数据，然后组装成Map返回
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> data = new HashMap<>();
        
        data.put("userCount", userService.count());
        data.put("userByRole", userService.countByRole());
        data.put("userByDepartment", userService.countByDepartment());
        
        data.put("goodsCount", goodsService.count());
        data.put("goodsByCategory", goodsService.countByCategory());
        data.put("goodsTotalValue", goodsService.calculateTotalValue());
        data.put("goodsByBrand", goodsService.countByBrand());
        
        data.put("inventoryTotal", inventoryService.getTotalQuantity());
        data.put("inventoryWarning", inventoryService.getWarningCount());
        data.put("inventoryByWarehouse", inventoryService.countByWarehouse());
        
        data.put("purchaseByStatus", purchaseRequestService.countByStatus());
        data.put("purchaseApprovedTotal", purchaseRequestService.calculateApprovedTotal());
        data.put("purchaseMonthly", purchaseRequestService.monthlyStatistics());
        
        data.put("financeTotalIncome", financeRecordService.calculateTotalIncome());
        data.put("financeTotalExpense", financeRecordService.calculateTotalExpense());
        data.put("financeByType", financeRecordService.sumByType());
        data.put("financeByCategory", financeRecordService.sumByCategory());
        data.put("financeMonthly", financeRecordService.monthlySumByType());
        
        data.put("scheduleByShift", scheduleService.countByShift());
        data.put("scheduleByUser", scheduleService.countByUser());
        
        return Result.success(data);
    }
    
    /**
     * 获取员工首页统计数据
     */
    @GetMapping("/employee")
    public Result<Map<String, Object>> getEmployeeStatistics() {
        Map<String, Object> data = new HashMap<>();
        
        data.put("goodsCount", goodsService.count());
        data.put("inventoryWarning", inventoryService.getWarningCount());
        data.put("inventoryTotal", inventoryService.getTotalQuantity());
        
        return Result.success(data);
    }
    
    /**
     * 获取经理首页统计数据
     */
    @GetMapping("/manager")
    public Result<Map<String, Object>> getManagerStatistics() {
        Map<String, Object> data = new HashMap<>();
        
        data.put("userCount", userService.count());
        data.put("goodsCount", goodsService.count());
        data.put("inventoryTotal", inventoryService.getTotalQuantity());
        data.put("inventoryWarning", inventoryService.getWarningInventory());
        data.put("purchaseByStatus", purchaseRequestService.countByStatus());
        data.put("purchaseApprovedTotal", purchaseRequestService.calculateApprovedTotal());
        data.put("financeTotalIncome", financeRecordService.calculateTotalIncome());
        data.put("financeTotalExpense", financeRecordService.calculateTotalExpense());
        data.put("financeMonthly", financeRecordService.monthlySumByType());
        
        return Result.success(data);
    }
}
