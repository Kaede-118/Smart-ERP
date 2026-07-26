package com.kaede.erp.service.impl;


import com.kaede.erp.common.converter.ProductCategoryConverter;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.dto.CreateProductCategoryDTO;
import com.kaede.erp.dto.UpdateProductCategoryDTO;
import com.kaede.erp.entity.ProductCategory;
import com.kaede.erp.mapper.ProductCategoryMapper;
import com.kaede.erp.service.ProductCategoryService;
import com.kaede.erp.vo.ProductCategoryVO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {


    private final ProductCategoryMapper mapper;


    public ProductCategoryServiceImpl(ProductCategoryMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public List<ProductCategoryVO> list() {

        return mapper.selectList(null)
                .stream()
                .map(ProductCategoryConverter::toVO)
                .toList();
    }


    @Override
    public ProductCategoryVO create(CreateProductCategoryDTO dto) {

        ProductCategory entity = new ProductCategory();
        entity.setName(dto.getName());
        entity.setParentId(dto.getParentId());
        entity.setStatus(dto.getStatus());

        mapper.insert(entity);

        return ProductCategoryConverter.toVO(entity);
    }


    @Override
    public ProductCategoryVO update(UpdateProductCategoryDTO dto) {

        ProductCategory entity = mapper.selectById(dto.getId());

        if (entity == null) {
            throw new BusinessException(40000, "分类不存在");
        }

        entity.setName(dto.getName());
        entity.setParentId(dto.getParentId());
        entity.setStatus(dto.getStatus());

        mapper.updateById(entity);

        return ProductCategoryConverter.toVO(entity);
    }


    @Override
    public void delete(Long id) {

        ProductCategory entity = mapper.selectById(id);

        if (entity == null) {
            throw new BusinessException(40000, "分类不存在");
        }

        mapper.deleteById(id);

    }

}
