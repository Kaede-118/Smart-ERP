package com.kaede.erp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaede.erp.common.converter.ProductConverter;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.dto.CreateProductDTO;
import com.kaede.erp.dto.ProductQueryDTO;
import com.kaede.erp.dto.UpdateProductDTO;
import com.kaede.erp.entity.Product;
import com.kaede.erp.mapper.ProductMapper;
import com.kaede.erp.service.ProductService;
import com.kaede.erp.vo.ProductVO;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {


    private final ProductMapper mapper;


    public ProductServiceImpl(ProductMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public Page<ProductVO> list(ProductQueryDTO dto) {

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        if (dto.getCategoryId() != null) {
            wrapper.eq(Product::getCategoryId, dto.getCategoryId());
        }

        if (dto.getKeyword() != null && !dto.getKeyword().isBlank()) {
            wrapper.and(w -> w
                    .like(Product::getName, dto.getKeyword())
                    .or()
                    .like(Product::getCode, dto.getKeyword())
            );
        }

        Page<Product> page = new Page<>(dto.getPage(), dto.getSize());

        Page<Product> result = mapper.selectPage(page, wrapper);

        Page<ProductVO> voPage = new Page<>();
        voPage.setCurrent(result.getCurrent());
        voPage.setSize(result.getSize());
        voPage.setTotal(result.getTotal());
        voPage.setRecords(
                result.getRecords().stream()
                        .map(ProductConverter::toVO)
                        .toList()
        );

        return voPage;
    }


    @Override
    public ProductVO getProduct(Long id) {

        Product entity = mapper.selectById(id);

        if (entity == null) {
            throw new BusinessException(40000, "商品不存在");
        }

        return ProductConverter.toVO(entity);
    }


    @Override
    public ProductVO create(CreateProductDTO dto) {

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCode, dto.getCode());

        if (mapper.selectOne(wrapper) != null) {
            throw new BusinessException(40000, "商品编码已存在");
        }

        Product entity = new Product();
        entity.setCategoryId(dto.getCategoryId());
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setCoverUrl(dto.getCoverUrl());
        entity.setCostPrice(dto.getCostPrice());
        entity.setSalePrice(dto.getSalePrice());
        entity.setUnit(dto.getUnit());
        entity.setStatus(dto.getStatus());
        entity.setDescription(dto.getDescription());

        mapper.insert(entity);

        return ProductConverter.toVO(entity);
    }


    @Override
    public ProductVO update(UpdateProductDTO dto) {

        Product entity = mapper.selectById(dto.getId());

        if (entity == null) {
            throw new BusinessException(40000, "商品不存在");
        }

        entity.setCategoryId(dto.getCategoryId());
        entity.setName(dto.getName());
        entity.setCoverUrl(dto.getCoverUrl());
        entity.setCostPrice(dto.getCostPrice());
        entity.setSalePrice(dto.getSalePrice());
        entity.setUnit(dto.getUnit());
        entity.setStatus(dto.getStatus());
        entity.setDescription(dto.getDescription());

        mapper.updateById(entity);

        return ProductConverter.toVO(entity);
    }


    @Override
    public void delete(Long id) {

        Product entity = mapper.selectById(id);

        if (entity == null) {
            throw new BusinessException(40000, "商品不存在");
        }

        mapper.deleteById(id);

    }

}
