package com.supermarket.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermarket.logistics.common.Result;
import com.supermarket.logistics.entity.Supplier;
import com.supermarket.logistics.service.SupplierService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商控制器
 */
@RestController
@RequestMapping("/supplier")
@CrossOrigin
public class SupplierController {
    
    @Autowired
    private SupplierService supplierService;
    
    /**
     * 分页查询供应商
     */
    @GetMapping("/page")
    public Result<Page<Supplier>> page(@RequestParam(defaultValue = "1") Integer current,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String contact,
                                        @RequestParam(required = false) Integer status) {
        Page<Supplier> page = new Page<>(current, size);
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(Supplier::getName, name);
        }
        if (StringUtils.isNotBlank(contact)) {
            wrapper.like(Supplier::getContact, contact);
        }
        if (status != null) {
            wrapper.eq(Supplier::getStatus, status);
        }
        wrapper.eq(Supplier::getDeleted, 0);
        wrapper.orderByDesc(Supplier::getCreateTime);
        
        supplierService.page(page, wrapper);
        return Result.success(page);
    }
    
    /**
     * 获取所有启用的供应商
     */
    @GetMapping("/list")
    public Result<List<Supplier>> list() {
        List<Supplier> suppliers = supplierService.getAllEnabled();
        return Result.success(suppliers);
    }
    
    /**
     * 根据ID获取供应商
     */
    @GetMapping("/{id}")
    public Result<Supplier> getById(@PathVariable Long id) {
        Supplier supplier = supplierService.getById(id);
        if (supplier == null) {
            return Result.error("供应商不存在");
        }
        return Result.success(supplier);
    }
    
    /**
     * 添加供应商
     */
    @PostMapping
    public Result<Supplier> save(@RequestBody Supplier supplier) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Supplier::getName, supplier.getName());
        wrapper.eq(Supplier::getDeleted, 0);
        if (supplierService.count(wrapper) > 0) {
            return Result.error("供应商名称已存在");
        }
        
        boolean success = supplierService.save(supplier);
        if (success) {
            return Result.success("添加成功", supplier);
        }
        return Result.error("添加失败");
    }
    
    /**
     * 更新供应商
     */
    @PutMapping
    public Result<Supplier> update(@RequestBody Supplier supplier) {
        Supplier existSupplier = supplierService.getById(supplier.getId());
        if (existSupplier == null) {
            return Result.error("供应商不存在");
        }
        
        if (!existSupplier.getName().equals(supplier.getName())) {
            LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Supplier::getName, supplier.getName());
            wrapper.eq(Supplier::getDeleted, 0);
            wrapper.ne(Supplier::getId, supplier.getId());
            if (supplierService.count(wrapper) > 0) {
                return Result.error("供应商名称已存在");
            }
        }
        
        boolean success = supplierService.updateById(supplier);
        if (success) {
            return Result.success("更新成功", supplier);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 删除供应商
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = supplierService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
