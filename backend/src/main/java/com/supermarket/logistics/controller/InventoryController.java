package com.supermarket.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermarket.logistics.common.Result;
import com.supermarket.logistics.entity.Inventory;
import com.supermarket.logistics.service.GoodsCategoryService;
import com.supermarket.logistics.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 库存控制器
 */
@RestController
@RequestMapping("/inventory")
@CrossOrigin
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private GoodsCategoryService categoryService;
    
    /**
     * 分页查询库存
     */
    @GetMapping("/page")
    public Result<Page<Inventory>> page(@RequestParam(defaultValue = "1") Integer current,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam(required = false) String goodsName,
                                         @RequestParam(required = false) Long categoryId,
                                         @RequestParam(required = false) String warehouse,
                                         @RequestParam(required = false) Integer status,
                                         @RequestParam(required = false, defaultValue = "id") String field,
                                         @RequestParam(required = false, defaultValue = "asc") String order) {
        
        String orderField = "id".equals(field) ? "id" : 
                           "quantity".equals(field) ? "quantity" : 
                           "warning_quantity".equals(field) ? "warning_quantity" : "id";
        String orderDir = "asc".equalsIgnoreCase(order) ? "ASC" : "DESC";
        
        // 分类查询：获取父分类及其所有子分类的ID，支持查询父分类下的所有库存
        List<Long> categoryIds = null;
        if (categoryId != null) {
            categoryIds = categoryService.getAllChildIds(categoryId);
        }

        //
        List<Inventory> allRecords = inventoryService.getPageWithGoods(goodsName, categoryIds, warehouse, status, orderField, orderDir);
        
        int total = allRecords.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        Page<Inventory> page = new Page<>(current, size);
        page.setTotal(total);
        page.setRecords(start < total ? allRecords.subList(start, end) : new java.util.ArrayList<>());
        
        return Result.success(page);
    }
    
    /**
     * 获取所有库存
     */
    @GetMapping("/list")
    public Result<List<Inventory>> list() {
        List<Inventory> inventories = inventoryService.getAllWithGoods();
        return Result.success(inventories);
    }
    
    /**
     * 获取库存预警列表
     */
    @GetMapping("/warning")
    public Result<List<Inventory>> warning() {
        List<Inventory> inventories = inventoryService.getWarningInventory();
        return Result.success(inventories);
    }
    
    /**
     * 根据ID获取库存
     */
    @GetMapping("/{id}")
    public Result<Inventory> getById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getWithGoods(id);
        if (inventory == null) {
            return Result.error("库存记录不存在");
        }
        return Result.success(inventory);
    }
    
    /**
     * 添加库存记录
     */
    @PostMapping
    public Result<Inventory> save(@RequestBody Inventory inventory) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getGoodsId, inventory.getGoodsId());
        wrapper.eq(Inventory::getDeleted, 0);
        if (inventoryService.count(wrapper) > 0) {
            return Result.error("该商品已有库存记录");
        }
        
        boolean success = inventoryService.save(inventory);
        if (success) {
            return Result.success("添加成功", inventory);
        }
        return Result.error("添加失败");
    }
    
    /**
     * 更新库存记录
     */
    @PutMapping
    public Result<Inventory> update(@RequestBody Inventory inventory) {
        Inventory existInventory = inventoryService.getById(inventory.getId());
        if (existInventory == null) {
            return Result.error("库存记录不存在");
        }
        
        boolean success = inventoryService.updateById(inventory);
        if (success) {
            return Result.success("更新成功", inventory);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 删除库存记录
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = inventoryService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    /**
     * 获取库存统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("totalQuantity", inventoryService.getTotalQuantity());
        data.put("warningCount", inventoryService.getWarningCount());
        data.put("warehouseDistribution", inventoryService.countByWarehouse());
        return Result.success(data);
    }
}
