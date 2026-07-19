package com.supermarket.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.logistics.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 供应商Mapper接口
 */
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {
    
    @Select("SELECT * FROM supplier WHERE deleted = 0 AND status = 1 ORDER BY create_time DESC")
    List<Supplier> selectAllEnabled();
}
