package com.kaede.erp.service.impl;


import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.common.enums.InventoryType;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.entity.Inventory;
import com.kaede.erp.entity.InventoryRecord;
import com.kaede.erp.mapper.InventoryMapper;
import com.kaede.erp.mapper.InventoryRecordMapper;
import com.kaede.erp.service.InventoryService;
import com.kaede.erp.vo.InventoryRecordVO;
import com.kaede.erp.vo.InventoryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class InventoryServiceImpl implements InventoryService {


    private final InventoryMapper inventoryMapper;

    private final InventoryRecordMapper recordMapper;


    public InventoryServiceImpl(
            InventoryMapper inventoryMapper,
            InventoryRecordMapper recordMapper
    ) {
        this.inventoryMapper = inventoryMapper;
        this.recordMapper = recordMapper;
    }


    @Override
    public List<InventoryVO> list(String keyword, Long categoryId) {

        return inventoryMapper.selectInventoryList(keyword, categoryId)
                .stream()
                .map(InventoryVO::fromMap)
                .toList();
    }


    @Override
    public InventoryVO getByProductId(Long productId) {

        return InventoryVO.fromMap(
                inventoryMapper.selectInventoryDetail(productId)
        );
    }


    @Override
    @Transactional
    public void adjust(Long productId, Integer changeQty, String remark, Long operatorId) {


        if (changeQty == 0) {
            throw new BusinessException(40000, "变更数量不能为0");
        }


        Inventory inv = inventoryMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Inventory>()
                        .eq(Inventory::getProductId, productId)
        );


        if (inv == null) {
            throw new BusinessException(40000, "该商品暂无库存记录");
        }


        int before = inv.getQuantity();
        int after = before + changeQty;

        if (after < 0) {
            throw new BusinessException(40000, "库存不足，当前库存: " + before + ", 需求: " + (-changeQty));
        }


        inv.setQuantity(after);
        inventoryMapper.updateById(inv);


        InventoryRecord record = new InventoryRecord();
        record.setProductId(productId);
        record.setChangeQty(changeQty);
        record.setBeforeQty(before);
        record.setAfterQty(after);
        record.setType(changeQty > 0 ? InventoryType.INBOUND.name() : InventoryType.OUTBOUND.name());
        record.setRemark(remark);
        record.setOperatorId(operatorId);

        recordMapper.insert(record);

    }


    @Override
    public List<InventoryRecordVO> getRecords(Long productId, String type) {

        return recordMapper.selectRecordList(productId, type)
                .stream()
                .map(InventoryRecordVO::fromMap)
                .toList();
    }

}
