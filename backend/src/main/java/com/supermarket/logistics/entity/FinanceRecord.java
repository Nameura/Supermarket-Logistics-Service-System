package com.supermarket.logistics.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 财务记录实体类
 */
@Data
@TableName("finance_record")
public class FinanceRecord implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String recordNo;
    
    private Integer type;
    
    private String category;
    
    private BigDecimal amount;
    
    private String description;
    
    private Long operatorId;
    
    private Integer status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime recordDate;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(exist = false)
    private String operatorName;
    
    @TableField(exist = false)
    private String typeName;
}
