package com.supermarket.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supermarket.logistics.entity.Supplier;

import java.util.List;

/**
 * 供应商服务接口
 */
public interface SupplierService extends IService<Supplier> {
    
    List<Supplier> getAllEnabled();
}
