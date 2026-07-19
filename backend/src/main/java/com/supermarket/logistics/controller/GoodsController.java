package com.supermarket.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermarket.logistics.common.Result;
import com.supermarket.logistics.entity.Goods;
import com.supermarket.logistics.service.GoodsCategoryService;
import com.supermarket.logistics.service.GoodsService;
import com.supermarket.logistics.utils.FileUploadUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/goods")
@CrossOrigin
public class GoodsController {
    
    @Autowired
    private GoodsService goodsService;
    
    @Autowired
    private GoodsCategoryService categoryService;
    
    @Autowired
    private FileUploadUtils fileUploadUtils;
    
    /**
     * 分页查询商品
     * @param current 当前页码
     * @param size 每页条数
     * @param name 商品名称（模糊查询）
     * @param categoryId 商品分类ID（支持父分类递归查询，不然就只会返回父级分类对应的商品列表）
     * @param status 商品状态下拉选择框
     *
     * 下面字段前端多条件筛选暂时停用，因为用上了条件筛选栏太长了不好看，先保留
     * @param barcode 商品条形码（模糊查询）
     *
     * @param field 排序字段
     * @param order 排序方式（asc/desc）
     * @return 分页结果，包含商品列表和分页信息
     */
    @GetMapping("/page")
    public Result<Page<Goods>> page(@RequestParam(defaultValue = "1") Integer current,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) Long categoryId,
                                     @RequestParam(required = false) Integer status,
                                     @RequestParam(required = false) String barcode,
                                     @RequestParam(required = false, defaultValue = "id") String field,
                                     @RequestParam(required = false, defaultValue = "asc") String order) {
        // 创建分页对象，传入当前页码和每页条数
        Page<Goods> page = new Page<>(current, size);
        // 创建条件构造器，用于拼接WHERE条件
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        
        // 商品名称模糊查询
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(Goods::getName, name);
        }
        // 分类查询：获取父分类及其所有子分类的ID，支持查询父分类下的所有商品
        if (categoryId != null) {
            List<Long> categoryIds = categoryService.getAllChildIds(categoryId);
            wrapper.in(Goods::getCategoryId, categoryIds);
        }
        // 状态精确查询
        if (status != null) {
            wrapper.eq(Goods::getStatus, status);
        }
        // 条形码模糊查询
        if (StringUtils.isNotBlank(barcode)) {
            wrapper.like(Goods::getBarcode, barcode);
        }
        // 过滤已删除的记录
        wrapper.eq(Goods::getDeleted, 0);
        
        // 排序处理
        if ("asc".equalsIgnoreCase(order)) {
            wrapper.orderByAsc(Goods::getId);
        } else {
            wrapper.orderByDesc(Goods::getId);
        }
        
        // 执行分页查询，MyBatis-Plus会自动执行COUNT查询和数据查询
        goodsService.page(page, wrapper);
        
        // 遍历结果，为每个商品填充分类名称（用于前端展示）
        page.getRecords().forEach(goods -> {
            if (goods.getCategoryId() != null) {
                goods.setCategoryName(categoryService.getById(goods.getCategoryId()) != null ? 
                    categoryService.getById(goods.getCategoryId()).getName() : null);
            }
        });
        
        // 返回分页结果，Page对象包含：records、total、size、current、pages等属性
        return Result.success(page);
    }
    
    /**
     * 获取所有商品
     */
    @GetMapping("/list")
    public Result<List<Goods>> list() {
        List<Goods> goodsList = goodsService.getAllWithCategory();
        return Result.success(goodsList);
    }
    
    /**
     * 根据ID获取商品
     */
    @GetMapping("/{id}")
    public Result<Goods> getById(@PathVariable Long id) {
        Goods goods = goodsService.getWithCategory(id);
        if (goods == null) {
            return Result.error("商品不存在");
        }
        return Result.success(goods);
    }
    
    /**
     * 添加商品
     */
    @PostMapping
    public Result<Goods> save(@RequestBody Goods goods) {
        boolean success = goodsService.save(goods);
        if (success) {
            return Result.success("添加成功", goods);
        }
        return Result.error("添加失败");
    }
    
    /**
     * 更新商品
     */
    @PutMapping
    public Result<Goods> update(@RequestBody Goods goods) {
        Goods existGoods = goodsService.getById(goods.getId());
        if (existGoods == null) {
            return Result.error("商品不存在");
        }
        
        boolean success = goodsService.updateById(goods);
        if (success) {
            return Result.success("更新成功", goods);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = goodsService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    /**
     * 上传商品图片
     */
    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imagePath = fileUploadUtils.uploadFile(file, "goods");
        if (imagePath != null) {
            return Result.success("上传成功", imagePath);
        }
        return Result.error("上传失败");
    }
}
