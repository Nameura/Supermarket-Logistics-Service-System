package com.supermarket.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supermarket.logistics.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    User login(String username, String password);
    
    User register(User user);
    
    User getUserWithDepartment(Long id);
    
    List<User> getAllWithDepartment();
    
    boolean updatePassword(Long id, String oldPassword, String newPassword);
    
    boolean updateAvatar(Long id, String avatar);
    
    List<Map<String, Object>> countByRole();
    
    List<Map<String, Object>> countByDepartment();
}
