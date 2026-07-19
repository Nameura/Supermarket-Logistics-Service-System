package com.supermarket.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.logistics.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 库存Mapper接口
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
    
    @Select("SELECT i.*, g.name as goods_name, g.image as goods_image, g.barcode, g.unit, g.sale_price, " +
            "c.name as category_name FROM inventory i " +
            "LEFT JOIN goods g ON i.goods_id = g.id " +
            "LEFT JOIN goods_category c ON g.category_id = c.id " +
            "WHERE i.id = #{id}")
    Inventory selectWithGoods(Long id);
    
    @Select("<script>" +
            "SELECT i.*, g.name as goods_name, g.image as goods_image, g.barcode, g.unit, g.sale_price, " +
            "c.name as category_name FROM inventory i " +
            "LEFT JOIN goods g ON i.goods_id = g.id " +
            "LEFT JOIN goods_category c ON g.category_id = c.id " +
            "WHERE i.deleted = 0 " +
            "<if test='goodsName != null and goodsName != \"\"'>" +
            "AND g.name LIKE CONCAT('%', #{goodsName}, '%') " +
            "</if>" +
            "<if test='categoryIds != null and categoryIds.size() > 0'>" +
            "AND g.category_id IN " +
            "<foreach collection='categoryIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</if>" +
            "<if test='warehouse != null and warehouse != \"\"'>" +
            "AND i.warehouse LIKE CONCAT('%', #{warehouse}, '%') " +
            "</if>" +
            "<if test='status != null'>" +
            "AND i.status = #{status} " +
            "</if>" +
            "ORDER BY i.${orderField} ${orderDir}" +
            "</script>")
    List<Inventory> selectPageWithGoods(String goodsName, List<Long> categoryIds, String warehouse, Integer status, 
                                         String orderField, String orderDir);
    
    @Select("SELECT i.*, g.name as goods_name, g.image as goods_image, g.barcode, g.unit, g.sale_price, " +
            "c.name as category_name FROM inventory i " +
            "LEFT JOIN goods g ON i.goods_id = g.id " +
            "LEFT JOIN goods_category c ON g.category_id = c.id " +
            "WHERE i.deleted = 0 ORDER BY i.create_time DESC")
    List<Inventory> selectAllWithGoods();
    
    @Select("SELECT i.*, g.name as goods_name, g.image as goods_image, g.barcode, g.unit, g.sale_price, " +
            "c.name as category_name FROM inventory i " +
            "LEFT JOIN goods g ON i.goods_id = g.id " +
            "LEFT JOIN goods_category c ON g.category_id = c.id " +
            "WHERE i.deleted = 0 AND i.quantity <= i.warning_quantity ORDER BY i.quantity ASC")
    List<Inventory> selectWarningInventory();
    
    @Select("SELECT SUM(quantity) as total FROM inventory WHERE deleted = 0")
    Integer getTotalQuantity();
    
    @Select("SELECT COUNT(*) as count FROM inventory WHERE deleted = 0 AND quantity <= warning_quantity")
    Integer getWarningCount();
    
    @Select("SELECT warehouse, SUM(quantity) as total FROM inventory WHERE deleted = 0 GROUP BY warehouse")
    List<Map<String, Object>> countByWarehouse();
}
