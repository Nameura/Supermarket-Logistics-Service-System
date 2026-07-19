package com.supermarket.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermarket.logistics.entity.Supplier;
import com.supermarket.logistics.mapper.SupplierMapper;
import com.supermarket.logistics.service.SupplierService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商服务实现类
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {
    
    @Override
    public List<Supplier> getAllEnabled() {
        return baseMapper.selectAllEnabled();
    }
}
