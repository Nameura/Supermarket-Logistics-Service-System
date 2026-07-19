package com.supermarket.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermarket.logistics.entity.Department;
import com.supermarket.logistics.mapper.DepartmentMapper;
import com.supermarket.logistics.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门服务实现类
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    
    @Override
    public Department getWithParent(Long id) {
        return baseMapper.selectWithParent(id);
    }
    
    @Override
    public List<Department> getAllEnabled() {
        return baseMapper.selectAllEnabled();
    }
}
