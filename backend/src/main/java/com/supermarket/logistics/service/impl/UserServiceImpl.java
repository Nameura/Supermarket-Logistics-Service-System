package com.supermarket.logistics.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermarket.logistics.entity.User;
import com.supermarket.logistics.mapper.UserMapper;
import com.supermarket.logistics.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Override
    public User login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        wrapper.eq(User::getPassword, DigestUtil.md5Hex(password));
        wrapper.eq(User::getDeleted, 0);
        User user = this.getOne(wrapper);
        if (user != null && user.getStatus() == 1) {
            return user;
        }
        return null;
    }
    
    @Override
    public User register(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        wrapper.eq(User::getDeleted, 0);
        if (this.count(wrapper) > 0) {
            return null;
        }
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        user.setRole(3);
        user.setStatus(1);
        this.save(user);
        return user;
    }
    
    @Override
    public User getUserWithDepartment(Long id) {
        return baseMapper.selectUserWithDepartment(id);
    }
    
    @Override
    public List<User> getAllWithDepartment() {
        return baseMapper.selectAllWithDepartment();
    }
    
    @Override
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        User user = this.getById(id);
        if (user == null) {
            return false;
        }
        if (!user.getPassword().equals(DigestUtil.md5Hex(oldPassword))) {
            return false;
        }
        user.setPassword(DigestUtil.md5Hex(newPassword));
        return this.updateById(user);
    }
    
    @Override
    public boolean updateAvatar(Long id, String avatar) {
        User user = this.getById(id);
        if (user == null) {
            return false;
        }
        user.setAvatar(avatar);
        return this.updateById(user);
    }
    
    @Override
    public List<Map<String, Object>> countByRole() {
        return baseMapper.countByRole();
    }
    
    @Override
    public List<Map<String, Object>> countByDepartment() {
        return baseMapper.countByDepartment();
    }
}
