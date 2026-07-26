package com.kaede.erp.common.converter;


import com.kaede.erp.entity.Supplier;
import com.kaede.erp.vo.SupplierVO;


public final class SupplierConverter {


    private SupplierConverter() {
    }


    public static SupplierVO toVO(Supplier entity) {

        if (entity == null) {
            return null;
        }

        SupplierVO vo = new SupplierVO();

        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setContact(entity.getContact());
        vo.setPhone(entity.getPhone());
        vo.setAddress(entity.getAddress());
        vo.setStatus(entity.getStatus());

        return vo;
    }

}
