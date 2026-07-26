package com.kaede.erp.service;


import com.kaede.erp.dto.CreateSupplierDTO;
import com.kaede.erp.dto.UpdateSupplierDTO;
import com.kaede.erp.vo.SupplierVO;

import java.util.List;


public interface SupplierService {


    List<SupplierVO> list();


    SupplierVO create(CreateSupplierDTO dto);


    SupplierVO update(UpdateSupplierDTO dto);


    void delete(Long id);

}
