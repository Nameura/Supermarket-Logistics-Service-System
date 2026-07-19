package com.supermarket.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supermarket.logistics.entity.Goods;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品服务接口
 */
public interface GoodsService extends IService<Goods> {
    
    Goods getWithCategory(Long id);
    
    List<Goods> getAllWithCategory();
    
    List<Map<String, Object>> countByCategory();
    
    BigDecimal calculateTotalValue();
    
    List<Map<String, Object>> countByBrand();
}
