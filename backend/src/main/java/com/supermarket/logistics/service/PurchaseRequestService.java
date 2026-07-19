package com.supermarket.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supermarket.logistics.entity.PurchaseRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 采购请求服务接口
 */
public interface PurchaseRequestService extends IService<PurchaseRequest> {
    
    PurchaseRequest getWithDetails(Long id);
    
    List<PurchaseRequest> getAllWithDetails();
    
    /**
     * 按条件查询采购请求（支持商品名称模糊查询）
     */
    List<PurchaseRequest> getPageWithDetails(String requestNo, String goodsName, Integer approvalStatus, Long applicantId);
    
    List<Map<String, Object>> countByStatus();
    
    BigDecimal calculateApprovedTotal();
    
    List<Map<String, Object>> monthlyStatistics();
    
    String generateRequestNo();
}
