package com.kaede.erp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.common.enums.PurchaseStatus;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.dto.CreatePurchaseOrderDTO;
import com.kaede.erp.entity.Inventory;
import com.kaede.erp.entity.InventoryRecord;
import com.kaede.erp.entity.PurchaseItem;
import com.kaede.erp.entity.PurchaseOrder;
import com.kaede.erp.mapper.InventoryMapper;
import com.kaede.erp.mapper.InventoryRecordMapper;
import com.kaede.erp.mapper.PurchaseItemMapper;
import com.kaede.erp.mapper.PurchaseOrderMapper;
import com.kaede.erp.service.PurchaseOrderService;
import com.kaede.erp.vo.PurchaseItemVO;
import com.kaede.erp.vo.PurchaseOrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {


    private final PurchaseOrderMapper orderMapper;

    private final PurchaseItemMapper itemMapper;

    private final InventoryMapper inventoryMapper;

    private final InventoryRecordMapper recordMapper;


    public PurchaseOrderServiceImpl(
            PurchaseOrderMapper orderMapper,
            PurchaseItemMapper itemMapper,
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
    public PurchaseOrderVO create(CreatePurchaseOrderDTO dto, Long creatorId) {

        String orderNo = "PO" + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        ) + (System.currentTimeMillis() % 1000);


        BigDecimal total = dto.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNo(orderNo);
        order.setSupplierId(dto.getSupplierId());
        order.setTotalAmount(total);
        order.setStatus(PurchaseStatus.DRAFT.name());
        order.setCreatorId(creatorId);

        orderMapper.insert(order);


        for (CreatePurchaseOrderDTO.PurchaseItemDTO itemDTO : dto.getItems()) {

            BigDecimal amount = itemDTO.getPrice()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantity()));

            PurchaseItem item = new PurchaseItem();
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
    public void receive(Long orderId, Long operatorId) {

        PurchaseOrder order = orderMapper.selectById(orderId);

        if (order == null) {
            throw new BusinessException(40000, "采购单不存在");
        }

        if (!PurchaseStatus.DRAFT.name().equals(order.getStatus())) {
            throw new BusinessException(40000, "当前状态不允许入库");
        }


        List<PurchaseItem> items = itemMapper.selectList(
                new LambdaQueryWrapper<PurchaseItem>()
                        .eq(PurchaseItem::getOrderId, orderId)
        );


        for (PurchaseItem item : items) {

            LambdaQueryWrapper<Inventory> wrapper =
                    new LambdaQueryWrapper<>();
            wrapper.eq(Inventory::getProductId, item.getProductId());

            Inventory inv = inventoryMapper.selectOne(wrapper);

            if (inv == null) {
                inv = new Inventory();
                inv.setProductId(item.getProductId());
                inv.setQuantity(0);
                inv.setWarningValue(10);
                inventoryMapper.insert(inv);
            }

            int before = inv.getQuantity();
            int after = before + item.getQuantity();

            inv.setQuantity(after);
            inventoryMapper.updateById(inv);


            InventoryRecord record = new InventoryRecord();
            record.setProductId(item.getProductId());
            record.setChangeQty(item.getQuantity());
            record.setBeforeQty(before);
            record.setAfterQty(after);
            record.setType("INBOUND");
            record.setBusinessType("PURCHASE");
            record.setBusinessId(orderId);
            record.setRemark("采购入库");
            record.setOperatorId(operatorId);

            recordMapper.insert(record);
        }


        order.setStatus(PurchaseStatus.RECEIVED.name());
        orderMapper.updateById(order);

    }


    @Override
    public List<PurchaseOrderVO> list() {

        return orderMapper.selectOrderList()
                .stream()
                .map(PurchaseOrderVO::fromMap)
                .toList();
    }


    @Override
    public PurchaseOrderVO getOrder(Long id) {

        PurchaseOrderVO vo = PurchaseOrderVO.fromMap(
                orderMapper.selectOrderDetail(id)
        );

        if (vo == null) {
            throw new BusinessException(40000, "采购单不存在");
        }


        List<PurchaseItemVO> items = itemMapper.selectItemsByOrderId(id)
                .stream()
                .map(PurchaseItemVO::fromMap)
                .toList();

        vo.setItems(items);

        return vo;
    }

}
