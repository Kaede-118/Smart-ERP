package com.kaede.erp.service.impl;


import com.kaede.erp.common.converter.SupplierConverter;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.dto.CreateSupplierDTO;
import com.kaede.erp.dto.UpdateSupplierDTO;
import com.kaede.erp.entity.Supplier;
import com.kaede.erp.mapper.SupplierMapper;
import com.kaede.erp.service.SupplierService;
import com.kaede.erp.vo.SupplierVO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SupplierServiceImpl implements SupplierService {


    private final SupplierMapper mapper;


    public SupplierServiceImpl(SupplierMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public List<SupplierVO> list() {

        return mapper.selectList(null)
                .stream()
                .map(SupplierConverter::toVO)
                .toList();
    }


    @Override
    public SupplierVO create(CreateSupplierDTO dto) {

        Supplier entity = new Supplier();
        entity.setName(dto.getName());
        entity.setContact(dto.getContact());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setStatus(dto.getStatus());

        mapper.insert(entity);

        return SupplierConverter.toVO(entity);
    }


    @Override
    public SupplierVO update(UpdateSupplierDTO dto) {

        Supplier entity = mapper.selectById(dto.getId());

        if (entity == null) {
            throw new BusinessException(40000, "供应商不存在");
        }

        entity.setName(dto.getName());
        entity.setContact(dto.getContact());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setStatus(dto.getStatus());

        mapper.updateById(entity);

        return SupplierConverter.toVO(entity);
    }


    @Override
    public void delete(Long id) {

        Supplier entity = mapper.selectById(id);

        if (entity == null) {
            throw new BusinessException(40000, "供应商不存在");
        }

        mapper.deleteById(id);

    }

}
