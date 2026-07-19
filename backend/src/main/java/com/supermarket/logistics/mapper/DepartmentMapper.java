package com.supermarket.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.logistics.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 部门Mapper接口
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    
    @Select("SELECT d.*, p.name as parent_name FROM sys_department d " +
            "LEFT JOIN sys_department p ON d.parent_id = p.id " +
            "WHERE d.id = #{id}")
    Department selectWithParent(Long id);
    
    @Select("SELECT * FROM sys_department WHERE deleted = 0 AND status = 1 ORDER BY sort ASC")
    List<Department> selectAllEnabled();
}
