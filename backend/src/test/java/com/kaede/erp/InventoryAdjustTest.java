package com.kaede.erp;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.entity.Inventory;
import com.kaede.erp.mapper.InventoryMapper;
import com.kaede.erp.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class InventoryAdjustTest {


    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryMapper inventoryMapper;


    private Inventory getInventory(Long productId) {
        return inventoryMapper.selectOne(
                new LambdaQueryWrapper<Inventory>()
                        .eq(Inventory::getProductId, productId)
        );
    }


    @Test
    void adjust_byLargePositiveQuantity_shouldSucceed() {

        Long productId = 99999901L;
        int quantity = 5000;


        inventoryService.adjust(productId, quantity, "测试批量入库", 1L);


        Inventory inv = getInventory(productId);
        assertNotNull(inv);
        assertEquals(5000, inv.getQuantity());

    }


    @Test
    void adjust_byLargeNegativeQuantity_shouldSucceed() {

        Long productId = 99999902L;
        int initial = 10000;
        int decrease = 9999;


        inventoryService.adjust(productId, initial, "测试初始化", 1L);
        inventoryService.adjust(productId, -decrease, "测试批量出库", 1L);


        Inventory inv = getInventory(productId);
        assertNotNull(inv);
        assertEquals(initial - decrease, inv.getQuantity());

    }


    @Test
    void adjust_toExactZero_shouldSucceed() {

        Long productId = 99999903L;
        int initial = 500;


        inventoryService.adjust(productId, initial, "测试初始化", 1L);
        inventoryService.adjust(productId, -initial, "测试归零", 1L);


        Inventory inv = getInventory(productId);
        assertNotNull(inv);
        assertEquals(0, inv.getQuantity());

    }


    @Test
    void adjust_whenInsufficientStock_shouldThrow() {

        Long productId = 99999904L;


        inventoryService.adjust(productId, 10, "测试初始化", 1L);


        assertThrows(BusinessException.class,
                () -> inventoryService.adjust(productId, -20, "测试超额出库", 1L));

    }


    @Test
    void adjust_withZeroChange_shouldThrow() {

        Long productId = 99999905L;


        assertThrows(BusinessException.class,
                () -> inventoryService.adjust(productId, 0, "测试零变动", 1L));

    }


    @Test
    void adjust_negativeToExactBoundary_shouldSucceed() {

        Long productId = 99999906L;
        int initial = 1;


        inventoryService.adjust(productId, initial, "测试初始化", 1L);
        inventoryService.adjust(productId, -1, "测试刚好出完", 1L);


        Inventory inv = getInventory(productId);
        assertNotNull(inv);
        assertEquals(0, inv.getQuantity());

    }

}
