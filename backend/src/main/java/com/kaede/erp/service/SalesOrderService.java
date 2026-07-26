package com.kaede.erp.service;


import com.kaede.erp.dto.CreateSalesOrderDTO;
import com.kaede.erp.vo.SalesOrderVO;

import java.util.List;


public interface SalesOrderService {


    SalesOrderVO create(CreateSalesOrderDTO dto, Long creatorId);


    void complete(Long orderId, Long operatorId);


    List<SalesOrderVO> list();


    SalesOrderVO getOrder(Long id);

}
