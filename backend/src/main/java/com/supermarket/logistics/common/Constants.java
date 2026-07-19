package com.supermarket.logistics.common;

/**
 * 系统常量类，刚开始数据库里表中没添加role字段的时候，
 * 准备用着个常量类管理各种数据，后面暂时停用了
 */
public class Constants {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    
    public static final Integer ROLE_ADMIN = 1;
    public static final Integer ROLE_MANAGER = 2;
    public static final Integer ROLE_EMPLOYEE = 3;
    
    public static final Integer STATUS_ENABLE = 1;
    public static final Integer STATUS_DISABLE = 0;
    
    public static final Integer APPROVAL_PENDING = 0;
    public static final Integer APPROVAL_APPROVED = 1;
    public static final Integer APPROVAL_REJECTED = 2;
}
