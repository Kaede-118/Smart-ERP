package com.kaede.erp.service;


import com.kaede.erp.dto.CreateCustomerDTO;
import com.kaede.erp.dto.UpdateCustomerDTO;
import com.kaede.erp.vo.CustomerVO;

import java.util.List;


public interface CustomerService {


    List<CustomerVO> list();


    CustomerVO create(CreateCustomerDTO dto);


    CustomerVO update(UpdateCustomerDTO dto);


    void delete(Long id);

}
