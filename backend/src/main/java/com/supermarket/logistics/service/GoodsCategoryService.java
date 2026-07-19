package com.supermarket.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supermarket.logistics.entity.GoodsCategory;

import java.util.List;
import java.util.Map;

/**
 * 商品分类服务接口
 */
public interface GoodsCategoryService extends IService<GoodsCategory> {
    
    GoodsCategory getWithParent(Long id);
    
    List<GoodsCategory> getAllEnabled();
    
    List<Map<String, Object>> countByParent();
    
    /**
     * 获取父分类及其所有子分类的ID列表
     * @param parentId 父分类ID
     * @return 包含父分类及所有子分类的ID列表
     */
    List<Long> getAllChildIds(Long parentId);
}
