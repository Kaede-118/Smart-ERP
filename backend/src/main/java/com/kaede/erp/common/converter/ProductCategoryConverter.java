package com.kaede.erp.common.converter;


import com.kaede.erp.entity.ProductCategory;
import com.kaede.erp.vo.ProductCategoryVO;


public final class ProductCategoryConverter {


    private ProductCategoryConverter() {
    }


    public static ProductCategoryVO toVO(ProductCategory entity) {

        if (entity == null) {
            return null;
        }

        ProductCategoryVO vo = new ProductCategoryVO();

        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setParentId(entity.getParentId());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());

        return vo;
    }

}
