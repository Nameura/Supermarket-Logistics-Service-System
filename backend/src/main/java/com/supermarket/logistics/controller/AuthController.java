package com.supermarket.logistics.controller;

import com.supermarket.logistics.common.Result;
import com.supermarket.logistics.entity.User;
import com.supermarket.logistics.service.TokenService;
import com.supermarket.logistics.service.UserService;
import com.supermarket.logistics.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 处理用户登录、注册、获取用户信息、退出登录等认证相关请求
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private TokenService tokenService;
    
    /**
     * 用户登录
     * 接收用户名和密码，验证成功后返回Token和用户信息
     * @param params 包含username和password的请求参数
     * @return 登录结果，包含Token和用户信息
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        // 从请求参数中获取用户名和密码
        String username = params.get("username");
        String password = params.get("password");
        
        // 校验用户名和密码是否为空
        if (username == null || password == null) {
            return Result.error("用户名或密码不能为空");
        }

        // 调用Service层进行登录验证
        User user = userService.login(username, password);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        // 通过JWT工具类生成Token
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
        // 存进map集合，方便后续使用
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        // *发现有个问题，我直接返回的整个user对象*
        // 这里应该只返回必要的字段，比如id、username、role
        // 不过因为有密码字段方便演示MD5加密，所以就直接返回了
        data.put("user", user);
        
        return Result.success("登录成功", data);
    }
    
    /**
     * 用户注册
     * 接收用户信息，创建新用户账号
     * @param user 用户信息对象
     * @return 注册结果，包含注册成功的用户信息
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        // 校验用户名和密码是否为空
        if (user.getUsername() == null || user.getPassword() == null) {
            return Result.error("用户名或密码不能为空");
        }
        
        // 调用Service层进行用户注册
        User registeredUser = userService.register(user);
        if (registeredUser == null) {
            return Result.error("用户名已存在");
        }
        
        return Result.success("注册成功", registeredUser);
    }
    
    /**
     * 获取当前用户信息
     * 根据请求头中的Token获取当前登录用户的详细信息
     * 用于前端页面刷新时恢复用户登录状态
     * @param token 请求头中的Authorization字段，格式为"Bearer {token}"
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestHeader("Authorization") String token) {
        // 去掉Token的"Bearer "前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // 从Token中解析用户ID
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("无效的Token");
        }
        
        // 查询用户信息（包含部门信息）
        User user = userService.getUserWithDepartment(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        return Result.success(user);
    }
    
    /**
     * 退出登录
     * 将Token加入黑名单，使其立即失效
     * 这样即使用户退出后，Token在过期前也无法再次使用
     * @param authHeader 请求头中的Authorization字段
     * @return 退出结果
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 提取Token
            String token = authHeader.substring(7);
            // 将Token加入黑名单，使其失效
            tokenService.addToBlacklist(token);
        }
        return Result.success("退出成功");
    }
}
