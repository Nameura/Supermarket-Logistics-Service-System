package com.supermarket.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.logistics.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品Mapper接口
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
    
    @Select("SELECT g.*, c.name as category_name FROM goods g " +
            "LEFT JOIN goods_category c ON g.category_id = c.id " +
            "WHERE g.id = #{id}")
    Goods selectWithCategory(Long id);
    
    @Select("SELECT g.*, c.name as category_name FROM goods g " +
            "LEFT JOIN goods_category c ON g.category_id = c.id " +
            "WHERE g.deleted = 0 ORDER BY g.create_time DESC")
    List<Goods> selectAllWithCategory();
    
    @Select("SELECT c.name as category_name, COUNT(g.id) as count FROM goods g " +
            "LEFT JOIN goods_category c ON g.category_id = c.id " +
            "WHERE g.deleted = 0 GROUP BY c.name ORDER BY count DESC")
    List<Map<String, Object>> countByCategory();
    
    @Select("SELECT SUM(sale_price * (SELECT COALESCE(SUM(quantity), 0) FROM inventory WHERE goods_id = goods.id AND deleted = 0)) as total_value FROM goods WHERE deleted = 0")
    BigDecimal calculateTotalValue();
    
    @Select("SELECT brand, COUNT(*) as count FROM goods WHERE deleted = 0 GROUP BY brand ORDER BY count DESC LIMIT 10")
    List<Map<String, Object>> countByBrand();
}
