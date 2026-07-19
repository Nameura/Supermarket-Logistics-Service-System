package com.supermarket.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supermarket.logistics.entity.FinanceRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 财务记录服务接口
 */
public interface FinanceRecordService extends IService<FinanceRecord> {
    
    FinanceRecord getWithOperator(Long id);
    
    List<FinanceRecord> getAllWithOperator();
    
    List<Map<String, Object>> sumByType();
    
    List<Map<String, Object>> sumByCategory();
    
    List<Map<String, Object>> monthlySumByType();
    
    BigDecimal calculateTotalIncome();
    
    BigDecimal calculateTotalExpense();
    
    String generateRecordNo();
}
