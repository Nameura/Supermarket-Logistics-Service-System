package com.supermarket.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermarket.logistics.entity.PurchaseRequest;
import com.supermarket.logistics.mapper.PurchaseRequestMapper;
import com.supermarket.logistics.service.PurchaseRequestService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 采购请求服务实现类
 */
@Service
public class PurchaseRequestServiceImpl extends ServiceImpl<PurchaseRequestMapper, PurchaseRequest> implements PurchaseRequestService {
    
    @Override
    public PurchaseRequest getWithDetails(Long id) {
        return baseMapper.selectWithDetails(id);
    }
    
    @Override
    public List<PurchaseRequest> getAllWithDetails() {
        return baseMapper.selectAllWithDetails();
    }
    
    /**
     * 按条件查询采购请求（支持商品名称模糊查询）
     */
    @Override
    public List<PurchaseRequest> getPageWithDetails(String requestNo, String goodsName, Integer approvalStatus, Long applicantId) {
        return baseMapper.selectPageWithDetails(requestNo, goodsName, approvalStatus, applicantId);
    }
    
    @Override
    public List<Map<String, Object>> countByStatus() {
        return baseMapper.countByStatus();
    }
    
    @Override
    public BigDecimal calculateApprovedTotal() {
        BigDecimal total = baseMapper.calculateApprovedTotal();
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Override
    public List<Map<String, Object>> monthlyStatistics() {
        return baseMapper.monthlyStatistics();
    }
    
    @Override
    public String generateRequestNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefix = "PR" + sdf.format(new Date());
        int random = (int) (Math.random() * 1000);
        return prefix + String.format("%03d", random);
    }
}
