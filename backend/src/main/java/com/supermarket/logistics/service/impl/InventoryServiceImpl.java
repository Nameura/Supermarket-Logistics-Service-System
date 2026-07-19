package com.supermarket.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermarket.logistics.entity.Inventory;
import com.supermarket.logistics.mapper.InventoryMapper;
import com.supermarket.logistics.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 库存服务实现类
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {
    
    @Override
    public Inventory getWithGoods(Long id) {
        return baseMapper.selectWithGoods(id);
    }
    
    @Override
    public List<Inventory> getAllWithGoods() {
        return baseMapper.selectAllWithGoods();
    }
    
    @Override
    public List<Inventory> getWarningInventory() {
        return baseMapper.selectWarningInventory();
    }
    
    @Override
    public Integer getTotalQuantity() {
        Integer total = baseMapper.getTotalQuantity();
        return total != null ? total : 0;
    }
    
    @Override
    public Integer getWarningCount() {
        return baseMapper.getWarningCount();
    }
    
    @Override
    public List<Map<String, Object>> countByWarehouse() {
        return baseMapper.countByWarehouse();
    }
    
    @Override
    public List<Inventory> getPageWithGoods(String goodsName, List<Long> categoryIds, String warehouse, Integer status, 
                                             String orderField, String orderDir) {
        return baseMapper.selectPageWithGoods(goodsName, categoryIds, warehouse, status, orderField, orderDir);
    }
    
    /**
     * 根据商品ID查询库存
     * @param goodsId 商品ID
     * @return 库存记录
     */
    @Override
    public Inventory getByGoodsId(Long goodsId) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getGoodsId, goodsId);
        wrapper.eq(Inventory::getDeleted, 0);
        return baseMapper.selectOne(wrapper);
    }
    
    /**
     * 增加库存数量（采购入库）
     * 如果商品库存不存在则创建新记录，存在则增加数量
     * @param goodsId 商品ID
     * @param quantity 增加的数量
     * @param warehouse 仓库名称
     */
    @Override
    public void increaseStock(Long goodsId, Integer quantity, String warehouse) {
        Inventory inventory = getByGoodsId(goodsId);
        
        if (inventory == null) {
            // 库存记录不存在，创建新记录
            inventory = new Inventory();
            inventory.setGoodsId(goodsId);
            inventory.setQuantity(quantity);
            inventory.setWarningQuantity(10);  // 默认预警数量
            inventory.setWarehouse(warehouse != null ? warehouse : "一号仓库");
            inventory.setLocation("");
            inventory.setStatus(1);
            baseMapper.insert(inventory);
        } else {
            // 库存记录存在，增加数量
            inventory.setQuantity(inventory.getQuantity() + quantity);
            baseMapper.updateById(inventory);
        }
    }
}
