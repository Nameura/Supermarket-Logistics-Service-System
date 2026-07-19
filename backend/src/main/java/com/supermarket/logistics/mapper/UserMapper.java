package com.supermarket.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.logistics.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT u.*, d.name as department_name FROM sys_user u " +
            "LEFT JOIN sys_department d ON u.department_id = d.id " +
            "WHERE u.id = #{id}")
    User selectUserWithDepartment(Long id);
    
    @Select("SELECT u.*, d.name as department_name FROM sys_user u " +
            "LEFT JOIN sys_department d ON u.department_id = d.id " +
            "WHERE u.deleted = 0 ORDER BY u.create_time DESC")
    List<User> selectAllWithDepartment();
    
    @Select("SELECT role, COUNT(*) as count FROM sys_user WHERE deleted = 0 GROUP BY role")
    List<Map<String, Object>> countByRole();
    
    @Select("SELECT department_id, COUNT(*) as count FROM sys_user WHERE deleted = 0 AND department_id IS NOT NULL GROUP BY department_id")
    List<Map<String, Object>> countByDepartment();
}
