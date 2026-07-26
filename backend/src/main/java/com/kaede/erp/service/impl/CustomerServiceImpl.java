package com.kaede.erp.service.impl;


import com.kaede.erp.common.converter.CustomerConverter;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.dto.CreateCustomerDTO;
import com.kaede.erp.dto.UpdateCustomerDTO;
import com.kaede.erp.entity.Customer;
import com.kaede.erp.mapper.CustomerMapper;
import com.kaede.erp.service.CustomerService;
import com.kaede.erp.vo.CustomerVO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {


    private final CustomerMapper mapper;


    public CustomerServiceImpl(CustomerMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public List<CustomerVO> list() {

        return mapper.selectList(null)
                .stream()
                .map(CustomerConverter::toVO)
                .toList();
    }


    @Override
    public CustomerVO create(CreateCustomerDTO dto) {

        Customer entity = new Customer();
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setLevel(dto.getLevel());

        mapper.insert(entity);

        return CustomerConverter.toVO(entity);
    }


    @Override
    public CustomerVO update(UpdateCustomerDTO dto) {

        Customer entity = mapper.selectById(dto.getId());
        if (entity == null) {
            throw new BusinessException(40000, "客户不存在");
        }

        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setLevel(dto.getLevel());

        mapper.updateById(entity);

        return CustomerConverter.toVO(entity);
    }


    @Override
    public void delete(Long id) {

        Customer entity = mapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(40000, "客户不存在");
        }

        mapper.deleteById(id);

    }

}
