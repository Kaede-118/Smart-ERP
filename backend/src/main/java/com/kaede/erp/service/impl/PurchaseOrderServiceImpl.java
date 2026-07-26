package com.kaede.erp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaede.erp.common.constant.ResultCode;
import com.kaede.erp.common.enums.BusinessType;
import com.kaede.erp.common.enums.PurchaseStatus;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.dto.CreatePurchaseOrderDTO;
import com.kaede.erp.entity.PurchaseItem;
import com.kaede.erp.entity.PurchaseOrder;
import com.kaede.erp.mapper.PurchaseItemMapper;
import com.kaede.erp.mapper.PurchaseOrderMapper;
import com.kaede.erp.service.InventoryService;
import com.kaede.erp.service.PurchaseOrderService;
import com.kaede.erp.vo.PurchaseItemVO;
import com.kaede.erp.vo.PurchaseOrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {


    private static final DateTimeFormatter ORDER_NO_FMT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");


    private final PurchaseOrderMapper orderMapper;

    private final PurchaseItemMapper itemMapper;

    private final InventoryService inventoryService;


    public PurchaseOrderServiceImpl(
            PurchaseOrderMapper orderMapper,
            PurchaseItemMapper itemMapper,
            InventoryService inventoryService
    ) {
        this.orderMapper = orderMapper;
        this.itemMapper = itemMapper;
        this.inventoryService = inventoryService;
    }


    @Override
    @Transactional
    public PurchaseOrderVO create(CreatePurchaseOrderDTO dto, Long creatorId) {

        String orderNo = "PO" + LocalDateTime.now().format(ORDER_NO_FMT)
                + (System.currentTimeMillis() % 1000);


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
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (!PurchaseStatus.DRAFT.name().equals(order.getStatus())) {
            throw new BusinessException(
                    ResultCode.INVALID_STATUS.getCode(),
                    "当前状态不允许入库"
            );
        }


        List<PurchaseItem> items = itemMapper.selectList(
                new LambdaQueryWrapper<PurchaseItem>()
                        .eq(PurchaseItem::getOrderId, orderId)
        );


        for (PurchaseItem item : items) {

            inventoryService.increase(
                    item.getProductId(),
                    item.getQuantity(),
                    BusinessType.PURCHASE.name(),
                    orderId,
                    "采购入库",
                    operatorId
            );
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
            throw new BusinessException(ResultCode.NOT_FOUND);
        }


        List<PurchaseItemVO> items = itemMapper.selectItemsByOrderId(id)
                .stream()
                .map(PurchaseItemVO::fromMap)
                .toList();

        vo.setItems(items);

        return vo;
    }

}
