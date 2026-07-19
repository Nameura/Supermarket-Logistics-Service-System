package com.supermarket.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supermarket.logistics.entity.Department;

import java.util.List;

/**
 * 部门服务接口
 */
public interface DepartmentService extends IService<Department> {
    
    Department getWithParent(Long id);
    
    List<Department> getAllEnabled();
}
