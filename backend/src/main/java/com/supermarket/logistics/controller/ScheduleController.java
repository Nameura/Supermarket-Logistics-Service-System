package com.supermarket.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermarket.logistics.common.Result;
import com.supermarket.logistics.entity.Schedule;
import com.supermarket.logistics.service.ScheduleService;
import com.supermarket.logistics.service.UserService;
import com.supermarket.logistics.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 排班控制器
 */
@RestController
@RequestMapping("/schedule")
@CrossOrigin
public class ScheduleController {
    
    @Autowired
    private ScheduleService scheduleService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 分页查询排班
     */
    @GetMapping("/page")
    public Result<Page<Schedule>> page(@RequestParam(defaultValue = "1") Integer current,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) Long userId,
                                        @RequestParam(required = false) String shift,
                                        @RequestParam(required = false) Integer status) {
        Page<Schedule> page = new Page<>(current, size);
        LambdaQueryWrapper<Schedule> wrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            wrapper.eq(Schedule::getUserId, userId);
        }
        if (shift != null) {
            wrapper.eq(Schedule::getShift, shift);
        }
        if (status != null) {
            wrapper.eq(Schedule::getStatus, status);
        }
        wrapper.eq(Schedule::getDeleted, 0);
        wrapper.orderByAsc(Schedule::getWorkDate);
        wrapper.orderByAsc(Schedule::getStartTime);
        
        scheduleService.page(page, wrapper);
        
        List<Schedule> allWithUser = scheduleService.getAllWithUser();
        page.getRecords().forEach(schedule -> {
            allWithUser.stream()
                .filter(s -> s.getId().equals(schedule.getId()))
                .findFirst()
                .ifPresent(s -> {
                    schedule.setRealName(s.getRealName());
                    schedule.setUserName(s.getUserName());
                    schedule.setDepartmentName(s.getDepartmentName());
                });
        });
        
        return Result.success(page);
    }
    
    /**
     * 获取所有排班
     */
    @GetMapping("/list")
    public Result<List<Schedule>> list() {
        List<Schedule> schedules = scheduleService.getAllWithUser();
        return Result.success(schedules);
    }
    
    /**
     * 获取当前用户的排班
     */
    @GetMapping("/my")
    public Result<List<Schedule>> mySchedule(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        List<Schedule> schedules = scheduleService.getByUserId(userId);
        return Result.success(schedules);
    }
    
    /**
     * 根据ID获取排班
     */
    @GetMapping("/{id}")
    public Result<Schedule> getById(@PathVariable Long id) {
        Schedule schedule = scheduleService.getWithUser(id);
        if (schedule == null) {
            return Result.error("排班记录不存在");
        }
        return Result.success(schedule);
    }
    
    /**
     * 添加排班
     */
    @PostMapping
    public Result<Schedule> save(@RequestBody Schedule schedule,
                                  @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        schedule.setCreatorId(userId);
        schedule.setStatus(1);
        
        boolean success = scheduleService.save(schedule);
        if (success) {
            return Result.success("添加成功", schedule);
        }
        return Result.error("添加失败");
    }
    
    /**
     * 批量添加排班
     */
    @PostMapping("/batch")
    public Result<Void> saveBatch(@RequestBody List<Schedule> schedules,
                                   @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        schedules.forEach(schedule -> {
            schedule.setCreatorId(userId);
            schedule.setStatus(1);
        });
        
        boolean success = scheduleService.saveBatch(schedules);
        if (success) {
            return Result.success("批量添加成功");
        }
        return Result.error("批量添加失败");
    }
    
    /**
     * 更新排班
     */
    @PutMapping
    public Result<Schedule> update(@RequestBody Schedule schedule) {
        Schedule existSchedule = scheduleService.getById(schedule.getId());
        if (existSchedule == null) {
            return Result.error("排班记录不存在");
        }
        
        boolean success = scheduleService.updateById(schedule);
        if (success) {
            return Result.success("更新成功", schedule);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 删除排班
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = scheduleService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    /**
     * 获取排班统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("countByShift", scheduleService.countByShift());
        data.put("countByUser", scheduleService.countByUser());
        return Result.success(data);
    }
}
