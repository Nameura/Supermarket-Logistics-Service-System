package com.supermarket.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.logistics.entity.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品分类Mapper接口
 */
@Mapper
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategory> {
    
    @Select("SELECT c.*, p.name as parent_name FROM goods_category c " +
            "LEFT JOIN goods_category p ON c.parent_id = p.id " +
            "WHERE c.id = #{id}")
    GoodsCategory selectWithParent(Long id);
    
    @Select("SELECT * FROM goods_category WHERE deleted = 0 AND status = 1 ORDER BY sort ASC")
    List<GoodsCategory> selectAllEnabled();
    
    @Select("SELECT parent_id, COUNT(*) as count FROM goods_category WHERE deleted = 0 GROUP BY parent_id")
    List<java.util.Map<String, Object>> countByParent();
}
