package com.supermarket.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermarket.logistics.entity.FinanceRecord;
import com.supermarket.logistics.mapper.FinanceRecordMapper;
import com.supermarket.logistics.service.FinanceRecordService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 财务记录服务实现类
 */
@Service
public class FinanceRecordServiceImpl extends ServiceImpl<FinanceRecordMapper, FinanceRecord> implements FinanceRecordService {
    
    @Override
    public FinanceRecord getWithOperator(Long id) {
        return baseMapper.selectWithOperator(id);
    }
    
    @Override
    public List<FinanceRecord> getAllWithOperator() {
        return baseMapper.selectAllWithOperator();
    }
    
    @Override
    public List<Map<String, Object>> sumByType() {
        return baseMapper.sumByType();
    }
    
    @Override
    public List<Map<String, Object>> sumByCategory() {
        return baseMapper.sumByCategory();
    }
    
    @Override
    public List<Map<String, Object>> monthlySumByType() {
        return baseMapper.monthlySumByType();
    }
    
    @Override
    public BigDecimal calculateTotalIncome() {
        BigDecimal total = baseMapper.calculateTotalIncome();
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal calculateTotalExpense() {
        BigDecimal total = baseMapper.calculateTotalExpense();
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Override
    public String generateRecordNo() {
        // 通过时间生成流水号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefix = "FR" + sdf.format(new Date());
        int random = (int) (Math.random() * 1000);
        return prefix + String.format("%03d", random);
    }
}
