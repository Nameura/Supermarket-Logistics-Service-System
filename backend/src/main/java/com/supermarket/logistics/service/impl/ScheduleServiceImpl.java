package com.supermarket.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermarket.logistics.entity.Schedule;
import com.supermarket.logistics.mapper.ScheduleMapper;
import com.supermarket.logistics.service.ScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 排班服务实现类
 */
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {
    
    @Override
    public Schedule getWithUser(Long id) {
        return baseMapper.selectWithUser(id);
    }
    
    @Override
    public List<Schedule> getAllWithUser() {
        return baseMapper.selectAllWithUser();
    }
    
    @Override
    public List<Schedule> getByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }
    
    @Override
    public List<Map<String, Object>> countByShift() {
        return baseMapper.countByShift();
    }
    
    @Override
    public List<Map<String, Object>> countByUser() {
        return baseMapper.countByUser();
    }
}
