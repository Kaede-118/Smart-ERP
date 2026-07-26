package com.kaede.erp.service;


import com.kaede.erp.dto.CreatePurchaseOrderDTO;
import com.kaede.erp.vo.PurchaseOrderVO;

import java.util.List;


public interface PurchaseOrderService {


    PurchaseOrderVO create(CreatePurchaseOrderDTO dto, Long creatorId);


    void receive(Long orderId, Long operatorId);


    List<PurchaseOrderVO> list();


    PurchaseOrderVO getOrder(Long id);

}
