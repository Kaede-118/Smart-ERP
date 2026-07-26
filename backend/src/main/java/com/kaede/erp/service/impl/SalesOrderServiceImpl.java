package com.kaede.erp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaede.erp.common.constant.ResultCode;
import com.kaede.erp.common.enums.BusinessType;
import com.kaede.erp.common.enums.SalesStatus;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.dto.CreateSalesOrderDTO;
import com.kaede.erp.entity.SalesItem;
import com.kaede.erp.entity.SalesOrder;
import com.kaede.erp.mapper.SalesItemMapper;
import com.kaede.erp.mapper.SalesOrderMapper;
import com.kaede.erp.service.InventoryService;
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


    private static final DateTimeFormatter ORDER_NO_FMT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");


    private final SalesOrderMapper orderMapper;

    private final SalesItemMapper itemMapper;

    private final InventoryService inventoryService;


    public SalesOrderServiceImpl(
            SalesOrderMapper orderMapper,
            SalesItemMapper itemMapper,
            InventoryService inventoryService
    ) {
        this.orderMapper = orderMapper;
        this.itemMapper = itemMapper;
        this.inventoryService = inventoryService;
    }


    @Override
    @Transactional
    public SalesOrderVO create(CreateSalesOrderDTO dto, Long creatorId) {

        String orderNo = "SO" + LocalDateTime.now().format(ORDER_NO_FMT)
                + (System.currentTimeMillis() % 1000);


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
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (!SalesStatus.PENDING.name().equals(order.getStatus())) {
            throw new BusinessException(
                    ResultCode.INVALID_STATUS.getCode(),
                    "当前状态不允许出库"
            );
        }


        List<SalesItem> items = itemMapper.selectList(
                new LambdaQueryWrapper<SalesItem>()
                        .eq(SalesItem::getOrderId, orderId)
        );


        for (SalesItem item : items) {

            inventoryService.decrease(
                    item.getProductId(),
                    item.getQuantity(),
                    BusinessType.SALES.name(),
                    orderId,
                    "销售出库",
                    operatorId
            );
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
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        List<SalesItemVO> items = itemMapper.selectItemsByOrderId(id)
                .stream()
                .map(SalesItemVO::fromMap)
                .toList();

        vo.setItems(items);

        return vo;
    }

}
