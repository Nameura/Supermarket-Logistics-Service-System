package com.supermarket.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermarket.logistics.entity.Goods;
import com.supermarket.logistics.mapper.GoodsMapper;
import com.supermarket.logistics.service.GoodsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品服务实现类
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    
    @Override
    public Goods getWithCategory(Long id) {
        return baseMapper.selectWithCategory(id);
    }
    
    @Override
    public List<Goods> getAllWithCategory() {
        return baseMapper.selectAllWithCategory();
    }
    
    @Override
    public List<Map<String, Object>> countByCategory() {
        return baseMapper.countByCategory();
    }
    
    @Override
    public BigDecimal calculateTotalValue() {
        BigDecimal value = baseMapper.calculateTotalValue();
        return value != null ? value : BigDecimal.ZERO;
    }
    
    @Override
    public List<Map<String, Object>> countByBrand() {
        return baseMapper.countByBrand();
    }
}
