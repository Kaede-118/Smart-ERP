package com.kaede.erp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaede.erp.common.enums.SalesStatus;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.dto.CreateSalesOrderDTO;
import com.kaede.erp.entity.Inventory;
import com.kaede.erp.entity.InventoryRecord;
import com.kaede.erp.entity.SalesItem;
import com.kaede.erp.entity.SalesOrder;
import com.kaede.erp.mapper.InventoryMapper;
import com.kaede.erp.mapper.InventoryRecordMapper;
import com.kaede.erp.mapper.SalesItemMapper;
import com.kaede.erp.mapper.SalesOrderMapper;
import com.kaede.erp.service.SalesOrderService;
import com.kaede.erp.vo.SalesItemVO;
import com.kaede.erp.vo.SalesOrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class SalesOrderServiceImpl implements SalesOrderService {


    private final SalesOrderMapper orderMapper;

    private final SalesItemMapper itemMapper;

    private final InventoryMapper inventoryMapper;

    private final InventoryRecordMapper recordMapper;


    public SalesOrderServiceImpl(
            SalesOrderMapper orderMapper,
            SalesItemMapper itemMapper,
            InventoryMapper inventoryMapper,
            InventoryRecordMapper recordMapper
    ) {
        this.orderMapper = orderMapper;
        this.itemMapper = itemMapper;
        this.inventoryMapper = inventoryMapper;
        this.recordMapper = recordMapper;
    }


    @Override
    @Transactional
    public SalesOrderVO create(CreateSalesOrderDTO dto, Long creatorId) {

        String orderNo = "SO" + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        ) + (System.currentTimeMillis() % 1000);


        BigDecimal total = dto.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        SalesOrder order = new SalesOrder();
        order.setOrderNo(orderNo);
        order.setCustomerId(dto.getCustomerId());
        order.setTotalAmount(total);
        order.setStatus(SalesStatus.PENDING.name());
        order.setCreatorId(creatorId);

        orderMapper.insert(order);


        for (CreateSalesOrderDTO.SalesItemDTO itemDTO : dto.getItems()) {

            BigDecimal amount = itemDTO.getPrice()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantity()));

            SalesItem item = new SalesItem();
            item.setOrderId(order.getId());
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setAmount(amount);

            itemMapper.insert(item);
        }


        return getOrder(order.getId());
    }


    @Override
    @Transactional
    public void complete(Long orderId, Long operatorId) {

        SalesOrder order = orderMapper.selectById(orderId);

        if (order == null) {
            throw new BusinessException(40000, "销售单不存在");
        }

        if (!SalesStatus.PENDING.name().equals(order.getStatus())) {
            throw new BusinessException(40000, "当前状态不允许出库");
        }


        List<SalesItem> items = itemMapper.selectList(
                new LambdaQueryWrapper<SalesItem>()
                        .eq(SalesItem::getOrderId, orderId)
        );


        for (SalesItem item : items) {

            LambdaQueryWrapper<Inventory> wrapper =
                    new LambdaQueryWrapper<>();
            wrapper.eq(Inventory::getProductId, item.getProductId());

            Inventory inv = inventoryMapper.selectOne(wrapper);

            if (inv == null || inv.getQuantity() < item.getQuantity()) {
                throw new BusinessException(40000,
                        "商品库存不足: " + item.getProductId() +
                                ", 当前库存: " + (inv != null ? inv.getQuantity() : 0) +
                                ", 需求: " + item.getQuantity());
            }

            int before = inv.getQuantity();
            int after = before - item.getQuantity();

            inv.setQuantity(after);
            inventoryMapper.updateById(inv);


            InventoryRecord record = new InventoryRecord();
            record.setProductId(item.getProductId());
            record.setChangeQty(-item.getQuantity());
            record.setBeforeQty(before);
            record.setAfterQty(after);
            record.setType("OUTBOUND");
            record.setBusinessType("SALES");
            record.setBusinessId(orderId);
            record.setRemark("销售出库");
            record.setOperatorId(operatorId);

            recordMapper.insert(record);
        }


        order.setStatus(SalesStatus.COMPLETED.name());
        orderMapper.updateById(order);

    }


    @Override
    public List<SalesOrderVO> list() {

        return orderMapper.selectOrderList()
                .stream()
                .map(SalesOrderVO::fromMap)
                .toList();
    }


    @Override
    public SalesOrderVO getOrder(Long id) {

        SalesOrderVO vo = SalesOrderVO.fromMap(
                orderMapper.selectOrderDetail(id)
        );

        if (vo == null) {
            throw new BusinessException(40000, "销售单不存在");
        }

        List<SalesItemVO> items = itemMapper.selectItemsByOrderId(id)
                .stream()
                .map(SalesItemVO::fromMap)
                .toList();

        vo.setItems(items);

        return vo;
    }

}
