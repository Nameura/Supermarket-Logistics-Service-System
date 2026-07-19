package com.supermarket.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermarket.logistics.common.Result;
import com.supermarket.logistics.entity.Department;
import com.supermarket.logistics.service.DepartmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门控制器，暂时停用
 */
@RestController
@RequestMapping("/department")
@CrossOrigin
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    /**
     * 分页查询部门
     */
    @GetMapping("/page")
    public Result<Page<Department>> page(@RequestParam(defaultValue = "1") Integer current,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) Integer status) {
        Page<Department> page = new Page<>(current, size);
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(Department::getName, name);
        }
        if (status != null) {
            wrapper.eq(Department::getStatus, status);
        }
        wrapper.eq(Department::getDeleted, 0);
        wrapper.orderByAsc(Department::getSort);
        
        departmentService.page(page, wrapper);
        
        page.getRecords().forEach(dept -> {
            if (dept.getParentId() != null && dept.getParentId() > 0) {
                Department parent = departmentService.getById(dept.getParentId());
                if (parent != null) {
                    dept.setParentName(parent.getName());
                }
            }
        });
        
        return Result.success(page);
    }
    
    /**
     * 获取所有启用的部门
     */
    @GetMapping("/list")
    public Result<List<Department>> list() {
        List<Department> departments = departmentService.getAllEnabled();
        return Result.success(departments);
    }
    
    /**
     * 根据ID获取部门
     */
    @GetMapping("/{id}")
    public Result<Department> getById(@PathVariable Long id) {
        Department department = departmentService.getWithParent(id);
        if (department == null) {
            return Result.error("部门不存在");
        }
        return Result.success(department);
    }
    
    /**
     * 添加部门
     */
    @PostMapping
    public Result<Department> save(@RequestBody Department department) {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getName, department.getName());
        wrapper.eq(Department::getDeleted, 0);
        if (departmentService.count(wrapper) > 0) {
            return Result.error("部门名称已存在");
        }
        
        boolean success = departmentService.save(department);
        if (success) {
            return Result.success("添加成功", department);
        }
        return Result.error("添加失败");
    }
    
    /**
     * 更新部门
     */
    @PutMapping
    public Result<Department> update(@RequestBody Department department) {
        Department existDept = departmentService.getById(department.getId());
        if (existDept == null) {
            return Result.error("部门不存在");
        }
        
        if (!existDept.getName().equals(department.getName())) {
            LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Department::getName, department.getName());
            wrapper.eq(Department::getDeleted, 0);
            wrapper.ne(Department::getId, department.getId());
            if (departmentService.count(wrapper) > 0) {
                return Result.error("部门名称已存在");
            }
        }
        
        boolean success = departmentService.updateById(department);
        if (success) {
            return Result.success("更新成功", department);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = departmentService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
