package com.supermarket.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermarket.logistics.common.Result;
import com.supermarket.logistics.entity.User;
import com.supermarket.logistics.service.DepartmentService;
import com.supermarket.logistics.service.UserService;
import com.supermarket.logistics.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public Result<Page<User>> page(@RequestParam(defaultValue = "1") Integer current,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(required = false) String username,
                                    @RequestParam(required = false) String realName,
                                    @RequestParam(required = false) Integer role,
                                    @RequestParam(required = false) Integer status) {
        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(username)) {
            wrapper.like(User::getUsername, username);
        }
        if (StringUtils.isNotBlank(realName)) {
            wrapper.like(User::getRealName, realName);
        }
        if (role != null) {
            wrapper.eq(User::getRole, role);
        }
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        wrapper.eq(User::getDeleted, 0);
        wrapper.orderByDesc(User::getCreateTime);
        
        userService.page(page, wrapper);
        
        page.getRecords().forEach(user -> {
            if (user.getDepartmentId() != null) {
                user.setDepartmentName(departmentService.getById(user.getDepartmentId()) != null ? 
                    departmentService.getById(user.getDepartmentId()).getName() : null);
            }
            user.setRoleName(getRoleName(user.getRole()));
        });
        
        return Result.success(page);
    }
    
    /**
     * 获取所有用户
     */
    @GetMapping("/list")
    public Result<List<User>> list() {
        List<User> users = userService.getAllWithDepartment();
        users.forEach(user -> user.setRoleName(getRoleName(user.getRole())));
        return Result.success(users);
    }
    
    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getUserWithDepartment(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setRoleName(getRoleName(user.getRole()));
        return Result.success(user);
    }
    
    /**
     * 添加用户
     */
    @PostMapping
    public Result<User> save(@RequestBody User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        wrapper.eq(User::getDeleted, 0);
        if (userService.count(wrapper) > 0) {
            return Result.error("用户名已存在");
        }
        
        boolean success = userService.save(user);
        if (success) {
            return Result.success("添加成功", user);
        }
        return Result.error("添加失败");
    }
    
    /**
     * 更新用户
     */
    @PutMapping
    public Result<User> update(@RequestBody User user) {
        User existUser = userService.getById(user.getId());
        if (existUser == null) {
            return Result.error("用户不存在");
        }
        
        if (!existUser.getUsername().equals(user.getUsername())) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUsername, user.getUsername());
            wrapper.eq(User::getDeleted, 0);
            wrapper.ne(User::getId, user.getId());
            if (userService.count(wrapper) > 0) {
                return Result.error("用户名已存在");
            }
        }
        
        boolean success = userService.updateById(user);
        if (success) {
            return Result.success("更新成功", user);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = userService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody Map<String, String> params,
                                        @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        
        if (oldPassword == null || newPassword == null) {
            return Result.error("密码不能为空");
        }
        
        boolean success = userService.updatePassword(userId, oldPassword, newPassword);
        if (success) {
            return Result.success("密码修改成功");
        }
        return Result.error("原密码错误");
    }
    
    /**
     * 修改头像
     */
    @PutMapping("/avatar")
    public Result<Void> updateAvatar(@RequestBody Map<String, String> params,
                                      @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        String avatar = params.get("avatar");
        
        if (avatar == null) {
            return Result.error("头像不能为空");
        }
        
        boolean success = userService.updateAvatar(userId, avatar);
        if (success) {
            return Result.success("头像修改成功");
        }
        return Result.error("头像修改失败");
    }
    
    /**
     * 更新个人信息
     */
    @PutMapping("/profile")
    public Result<User> updateProfile(@RequestBody User user,
                                       @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        User existUser = userService.getById(userId);
        if (existUser == null) {
            return Result.error("用户不存在");
        }
        
        existUser.setRealName(user.getRealName());
        existUser.setPhone(user.getPhone());
        existUser.setEmail(user.getEmail());
        
        boolean success = userService.updateById(existUser);
        if (success) {
            return Result.success("更新成功", existUser);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 获取角色名称
     */
    private String getRoleName(Integer role) {
        if (role == null) return "未知";
        switch (role) {
            case 1: return "超级管理员";
            case 2: return "经理";
            case 3: return "员工";
            default: return "未知";
        }
    }
}
