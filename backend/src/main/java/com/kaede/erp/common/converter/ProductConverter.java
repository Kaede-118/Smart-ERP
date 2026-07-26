package com.kaede.erp.common.converter;


import com.kaede.erp.entity.Product;
import com.kaede.erp.vo.ProductVO;


public final class ProductConverter {


    private ProductConverter() {
    }


    public static ProductVO toVO(Product entity) {

        if (entity == null) {
            return null;
        }

        ProductVO vo = new ProductVO();

        vo.setId(entity.getId());
        vo.setCategoryId(entity.getCategoryId());
        vo.setName(entity.getName());
        vo.setCode(entity.getCode());
        vo.setCoverUrl(entity.getCoverUrl());
        vo.setCostPrice(entity.getCostPrice());
        vo.setSalePrice(entity.getSalePrice());
        vo.setUnit(entity.getUnit());
        vo.setStatus(entity.getStatus());
        vo.setDescription(entity.getDescription());
        vo.setCreateTime(entity.getCreateTime());

        return vo;
    }

}
