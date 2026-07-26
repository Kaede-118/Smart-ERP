package com.kaede.erp.service;


import com.kaede.erp.vo.InventoryRecordVO;
import com.kaede.erp.vo.InventoryVO;

import java.util.List;


public interface InventoryService {


    List<InventoryVO> list(String keyword, Long categoryId);


    InventoryVO getByProductId(Long productId);


    void adjust(Long productId, Integer changeQty, String remark, Long operatorId);


    List<InventoryRecordVO> getRecords(Long productId, String type);

}
