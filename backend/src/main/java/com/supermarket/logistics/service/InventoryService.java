package com.supermarket.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supermarket.logistics.entity.Inventory;

import java.util.List;
import java.util.Map;

/**
 * 库存服务接口
 */
public interface InventoryService extends IService<Inventory> {
    
    Inventory getWithGoods(Long id);
    
    List<Inventory> getAllWithGoods();
    
    List<Inventory> getWarningInventory();
    
    Integer getTotalQuantity();
    
    Integer getWarningCount();
    
    List<Map<String, Object>> countByWarehouse();
    
    List<Inventory> getPageWithGoods(String goodsName, List<Long> categoryIds, String warehouse, Integer status, 
                                      String orderField, String orderDir);
    
    /**
     * 根据商品ID查询库存
     * @param goodsId 商品ID
     * @return 库存记录
     */
    Inventory getByGoodsId(Long goodsId);
    
    /**
     * 增加库存数量（采购入库）
     * @param goodsId 商品ID
     * @param quantity 增加的数量
     * @param warehouse 仓库名称
     */
    void increaseStock(Long goodsId, Integer quantity, String warehouse);
}
