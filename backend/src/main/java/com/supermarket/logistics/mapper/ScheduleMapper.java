package com.supermarket.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.logistics.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 排班Mapper接口
 */
@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {
    
    @Select("SELECT s.*, u.real_name, u.username, d.name as department_name FROM schedule s " +
            "LEFT JOIN sys_user u ON s.user_id = u.id " +
            "LEFT JOIN sys_department d ON u.department_id = d.id " +
            "WHERE s.id = #{id}")
    Schedule selectWithUser(Long id);
    
    @Select("SELECT s.*, u.real_name, u.username, d.name as department_name FROM schedule s " +
            "LEFT JOIN sys_user u ON s.user_id = u.id " +
            "LEFT JOIN sys_department d ON u.department_id = d.id " +
            "WHERE s.deleted = 0 ORDER BY s.work_date ASC, s.start_time ASC")
    List<Schedule> selectAllWithUser();
    
    @Select("SELECT s.*, u.real_name, u.username, d.name as department_name FROM schedule s " +
            "LEFT JOIN sys_user u ON s.user_id = u.id " +
            "LEFT JOIN sys_department d ON u.department_id = d.id " +
            "WHERE s.deleted = 0 AND s.user_id = #{userId} ORDER BY s.work_date ASC")
    List<Schedule> selectByUserId(Long userId);
    
    @Select("SELECT shift, COUNT(*) as count FROM schedule WHERE deleted = 0 GROUP BY shift")
    List<Map<String, Object>> countByShift();
    
    @Select("SELECT u.real_name, COUNT(*) as count FROM schedule s " +
            "LEFT JOIN sys_user u ON s.user_id = u.id " +
            "WHERE s.deleted = 0 GROUP BY s.user_id ORDER BY count DESC LIMIT 10")
    List<Map<String, Object>> countByUser();
}
