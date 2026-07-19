package com.supermarket.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.logistics.entity.PurchaseRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 采购请求Mapper接口
 */
@Mapper
public interface PurchaseRequestMapper extends BaseMapper<PurchaseRequest> {
    
    @Select("SELECT p.*, g.name as goods_name, g.image as goods_image, s.name as supplier_name, " +
            "u1.real_name as applicant_name, u2.real_name as approver_name " +
            "FROM purchase_request p " +
            "LEFT JOIN goods g ON p.goods_id = g.id " +
            "LEFT JOIN supplier s ON p.supplier_id = s.id " +
            "LEFT JOIN sys_user u1 ON p.applicant_id = u1.id " +
            "LEFT JOIN sys_user u2 ON p.approver_id = u2.id " +
            "WHERE p.id = #{id}")
    PurchaseRequest selectWithDetails(Long id);
    
    @Select("SELECT p.*, g.name as goods_name, g.image as goods_image, s.name as supplier_name, " +
            "u1.real_name as applicant_name, u2.real_name as approver_name " +
            "FROM purchase_request p " +
            "LEFT JOIN goods g ON p.goods_id = g.id " +
            "LEFT JOIN supplier s ON p.supplier_id = s.id " +
            "LEFT JOIN sys_user u1 ON p.applicant_id = u1.id " +
            "LEFT JOIN sys_user u2 ON p.approver_id = u2.id " +
            "WHERE p.deleted = 0 ORDER BY p.create_time DESC")
    List<PurchaseRequest> selectAllWithDetails();
    
    /**
     * 按条件查询采购请求（支持商品名称模糊查询）
     */
    @Select("<script>" +
            "SELECT p.*, g.name as goods_name, g.image as goods_image, s.name as supplier_name, " +
            "u1.real_name as applicant_name, u2.real_name as approver_name " +
            "FROM purchase_request p " +
            "LEFT JOIN goods g ON p.goods_id = g.id " +
            "LEFT JOIN supplier s ON p.supplier_id = s.id " +
            "LEFT JOIN sys_user u1 ON p.applicant_id = u1.id " +
            "LEFT JOIN sys_user u2 ON p.approver_id = u2.id " +
            "WHERE p.deleted = 0 " +
            "<if test='requestNo != null and requestNo != \"\"'>" +
            "AND p.request_no LIKE CONCAT('%', #{requestNo}, '%') " +
            "</if>" +
            "<if test='goodsName != null and goodsName != \"\"'>" +
            "AND g.name LIKE CONCAT('%', #{goodsName}, '%') " +
            "</if>" +
            "<if test='approvalStatus != null'>" +
            "AND p.approval_status = #{approvalStatus} " +
            "</if>" +
            "<if test='applicantId != null'>" +
            "AND p.applicant_id = #{applicantId} " +
            "</if>" +
            "ORDER BY p.create_time DESC" +
            "</script>")
    List<PurchaseRequest> selectPageWithDetails(String requestNo, String goodsName, Integer approvalStatus, Long applicantId);
    
    @Select("SELECT approval_status, COUNT(*) as count FROM purchase_request WHERE deleted = 0 GROUP BY approval_status")
    List<Map<String, Object>> countByStatus();
    
    @Select("SELECT SUM(total_price) FROM purchase_request WHERE deleted = 0 AND approval_status = 1")
    BigDecimal calculateApprovedTotal();
    
    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m') as month, COUNT(*) as count, SUM(total_price) as total " +
            "FROM purchase_request WHERE deleted = 0 GROUP BY DATE_FORMAT(create_time, '%Y-%m') ORDER BY month DESC LIMIT 12")
    List<Map<String, Object>> monthlyStatistics();
}
