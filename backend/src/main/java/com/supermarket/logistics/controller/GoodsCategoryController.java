package com.supermarket.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermarket.logistics.common.Result;
import com.supermarket.logistics.entity.GoodsCategory;
import com.supermarket.logistics.service.GoodsCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品分类控制器
 */
@RestController
@RequestMapping("/category")
@CrossOrigin
public class GoodsCategoryController {
    
    @Autowired
    private GoodsCategoryService categoryService;
    
    /**
     * 分页查询商品分类
     * 支持按分类名称模糊查询和状态筛选
     */
    @GetMapping("/page")
    public Result<Page<GoodsCategory>> page(@RequestParam(defaultValue = "1") Integer current,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) Integer status,
                                             @RequestParam(defaultValue = "sort") String orderBy) {
        Page<GoodsCategory> page = new Page<>(current, size);
        LambdaQueryWrapper<GoodsCategory> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(GoodsCategory::getName, name);
        }
        if (status != null) {
            wrapper.eq(GoodsCategory::getStatus, status);
        }
        wrapper.eq(GoodsCategory::getDeleted, 0);
        
        if ("id".equals(orderBy)) {
            wrapper.orderByAsc(GoodsCategory::getId);
        } else {
            wrapper.orderByAsc(GoodsCategory::getSort);
            wrapper.orderByAsc(GoodsCategory::getId);
        }
        
        categoryService.page(page, wrapper);
        
        page.getRecords().forEach(category -> {
            if (category.getParentId() != null && category.getParentId() > 0) {
                GoodsCategory parent = categoryService.getById(category.getParentId());
                if (parent != null) {
                    category.setParentName(parent.getName());
                }
            }
        });
        
        return Result.success(page);
    }
    
    /**
     * 获取所有启用的商品分类列表
     */
    @GetMapping("/list")
    public Result<List<GoodsCategory>> list() {
        List<GoodsCategory> categories = categoryService.getAllEnabled();
        return Result.success(categories);
    }

    /**
     * 获取树形结构的商品分类
     * 用于前端下拉选择框展示层级分类
     */
    @GetMapping("/tree")
    public Result<List<Map<String, Object>>> tree() {
        List<GoodsCategory> categories = categoryService.getAllEnabled();
        List<Map<String, Object>> tree = new ArrayList<>();
        
        categories.stream()
            .filter(c -> c.getParentId() == null || c.getParentId() == 0)
            .forEach(category -> {
                Map<String, Object> node = new java.util.HashMap<>();
                node.put("id", category.getId());
                node.put("name", category.getName());
                node.put("children", getChildren(categories, category.getId()));
                tree.add(node);
            });
        
        return Result.success(tree);
    }
    
    /**
     * 递归获取子分类
     * @param categories 所有分类列表
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    private List<Map<String, Object>> getChildren(List<GoodsCategory> categories, Long parentId) {
        List<Map<String, Object>> children = new ArrayList<>();
        categories.stream()
            .filter(c -> parentId.equals(c.getParentId()))
            .forEach(category -> {
                Map<String, Object> node = new java.util.HashMap<>();
                node.put("id", category.getId());
                node.put("name", category.getName());
                node.put("children", getChildren(categories, category.getId()));
                children.add(node);
            });
        return children;
    }
    
    /**
     * 根据ID获取商品分类详情
     */
    @GetMapping("/{id}")
    public Result<GoodsCategory> getById(@PathVariable Long id) {
        GoodsCategory category = categoryService.getWithParent(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        return Result.success(category);
    }
    
    /**
     * 添加商品分类
     * 校验分类名称是否已存在
     */
    @PostMapping
    public Result<GoodsCategory> save(@RequestBody GoodsCategory category) {
        LambdaQueryWrapper<GoodsCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsCategory::getName, category.getName());
        wrapper.eq(GoodsCategory::getDeleted, 0);
        if (categoryService.count(wrapper) > 0) {
            return Result.error("分类名称已存在");
        }
        
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        if (category.getSort() == null) {
            category.setSort(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        
        boolean success = categoryService.save(category);
        if (success) {
            return Result.success("添加成功", category);
        }
        return Result.error("添加失败");
    }
    
    /**
     * 更新商品分类
     * 校验分类名称是否与其他分类重复
     */
    @PutMapping
    public Result<GoodsCategory> update(@RequestBody GoodsCategory category) {
        GoodsCategory existCategory = categoryService.getById(category.getId());
        if (existCategory == null) {
            return Result.error("分类不存在");
        }
        
        if (!existCategory.getName().equals(category.getName())) {
            LambdaQueryWrapper<GoodsCategory> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GoodsCategory::getName, category.getName());
            wrapper.eq(GoodsCategory::getDeleted, 0);
            wrapper.ne(GoodsCategory::getId, category.getId());
            if (categoryService.count(wrapper) > 0) {
                return Result.error("分类名称已存在");
            }
        }
        
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        
        boolean success = categoryService.updateById(category);
        if (success) {
            return Result.success("更新成功", category);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 删除商品分类
     * 如果分类下存在子分类则不允许删除
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        LambdaQueryWrapper<GoodsCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsCategory::getParentId, id);
        wrapper.eq(GoodsCategory::getDeleted, 0);
        if (categoryService.count(wrapper) > 0) {
            return Result.error("该分类下存在子分类，无法删除");
        }
        
        boolean success = categoryService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
