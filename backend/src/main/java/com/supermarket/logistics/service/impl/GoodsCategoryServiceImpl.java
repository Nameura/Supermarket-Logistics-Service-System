package com.supermarket.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermarket.logistics.entity.GoodsCategory;
import com.supermarket.logistics.mapper.GoodsCategoryMapper;
import com.supermarket.logistics.service.GoodsCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品分类服务实现类
 */
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements GoodsCategoryService {
    
    @Override
    public GoodsCategory getWithParent(Long id) {
        return baseMapper.selectWithParent(id);
    }
    
    @Override
    public List<GoodsCategory> getAllEnabled() {
        return baseMapper.selectAllEnabled();
    }
    
    @Override
    public List<Map<String, Object>> countByParent() {
        return baseMapper.countByParent();
    }
    
    /**
     * 获取父分类及其所有子分类的ID列表
     * 用于分类查询时支持查询父分类下的所有子分类商品
     * @param parentId 父分类ID
     * @return 包含父分类及所有子分类的ID列表
     */
    @Override
    public List<Long> getAllChildIds(Long parentId) {
        List<Long> ids = new ArrayList<>();
        ids.add(parentId);  // 先把父分类ID加入列表
        
        List<GoodsCategory> allCategories = getAllEnabled();  // 获取所有启用的分类
        collectChildIds(allCategories, parentId, ids);  // 递归收集子分类ID
        
        return ids;
    }
    
    /**
     * 递归收集所有子分类ID
     * @param allCategories 所有分类列表
     * @param parentId 当前父分类ID
     * @param ids 收集结果的ID列表
     */
    private void collectChildIds(List<GoodsCategory> allCategories, Long parentId, List<Long> ids) {
        // 找出当前父分类的直接子分类
        List<GoodsCategory> children = allCategories.stream()
            .filter(c -> parentId.equals(c.getParentId()))
            .collect(Collectors.toList());
        
        // 遍历子分类，加入列表，并继续递归查找孙子分类
        for (GoodsCategory child : children) {
            ids.add(child.getId());
            collectChildIds(allCategories, child.getId(), ids);  // 递归
        }
    }
}
