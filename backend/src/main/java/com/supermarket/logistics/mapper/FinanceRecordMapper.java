package com.supermarket.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.logistics.entity.FinanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 财务记录Mapper接口
 */
@Mapper
public interface FinanceRecordMapper extends BaseMapper<FinanceRecord> {
    
    @Select("SELECT f.*, u.real_name as operator_name FROM finance_record f " +
            "LEFT JOIN sys_user u ON f.operator_id = u.id " +
            "WHERE f.id = #{id}")
    FinanceRecord selectWithOperator(Long id);
    
    @Select("SELECT f.*, u.real_name as operator_name FROM finance_record f " +
            "LEFT JOIN sys_user u ON f.operator_id = u.id " +
            "WHERE f.deleted = 0 ORDER BY f.record_date DESC, f.create_time DESC")
    List<FinanceRecord> selectAllWithOperator();
    
    @Select("SELECT type, SUM(amount) as total FROM finance_record WHERE deleted = 0 GROUP BY type")
    List<Map<String, Object>> sumByType();
    
    @Select("SELECT category, SUM(amount) as total FROM finance_record WHERE deleted = 0 GROUP BY category ORDER BY total DESC LIMIT 10")
    List<Map<String, Object>> sumByCategory();
    
    @Select("SELECT DATE_FORMAT(record_date, '%Y-%m') as month, type, SUM(amount) as total " +
            "FROM finance_record WHERE deleted = 0 GROUP BY DATE_FORMAT(record_date, '%Y-%m'), type " +
            "ORDER BY month DESC LIMIT 24")
    List<Map<String, Object>> monthlySumByType();
    
    @Select("SELECT SUM(amount) FROM finance_record WHERE deleted = 0 AND type = 1")
    BigDecimal calculateTotalIncome();
    
    @Select("SELECT SUM(amount) FROM finance_record WHERE deleted = 0 AND type = 2")
    BigDecimal calculateTotalExpense();
}
