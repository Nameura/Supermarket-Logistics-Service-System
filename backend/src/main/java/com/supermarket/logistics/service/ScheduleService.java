package com.supermarket.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supermarket.logistics.entity.Schedule;

import java.util.List;
import java.util.Map;

/**
 * 排班服务接口
 */
public interface ScheduleService extends IService<Schedule> {
    
    Schedule getWithUser(Long id);
    
    List<Schedule> getAllWithUser();
    
    List<Schedule> getByUserId(Long userId);
    
    List<Map<String, Object>> countByShift();
    
    List<Map<String, Object>> countByUser();
}
