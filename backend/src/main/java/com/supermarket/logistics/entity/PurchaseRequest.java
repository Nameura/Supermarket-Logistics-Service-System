package com.supermarket.logistics.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购请求实体类
 */
@Data
@TableName("purchase_request")
public class PurchaseRequest implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String requestNo;
    
    private Long goodsId;
    
    private Long supplierId;
    
    private Integer quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal totalPrice;
    
    private Integer approvalStatus;
    
    private Long applicantId;
    
    private Long approverId;
    
    private String approvalRemark;
    
    private LocalDateTime approvalTime;
    
    private String remark;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(exist = false)
    private String goodsName;
    
    @TableField(exist = false)
    private String goodsImage;
    
    @TableField(exist = false)
    private String supplierName;
    
    @TableField(exist = false)
    private String applicantName;
    
    @TableField(exist = false)
    private String approverName;
}
