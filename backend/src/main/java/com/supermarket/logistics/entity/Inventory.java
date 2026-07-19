package com.supermarket.logistics.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存实体类
 */
@Data
@TableName("inventory")
public class Inventory implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long goodsId;
    
    private Integer quantity;
    
    private Integer warningQuantity;
    
    private String warehouse;
    
    private String location;
    
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
    private String categoryName;
    
    @TableField(exist = false)
    private String barcode;
    
    @TableField(exist = false)
    private String unit;
    
    @TableField(exist = false)
    private BigDecimal salePrice;
}
