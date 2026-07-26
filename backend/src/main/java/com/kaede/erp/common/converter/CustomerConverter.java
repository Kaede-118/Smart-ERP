package com.kaede.erp.common.converter;


import com.kaede.erp.entity.Customer;
import com.kaede.erp.vo.CustomerVO;


public final class CustomerConverter {


    private CustomerConverter() {
    }


    public static CustomerVO toVO(Customer entity) {

        if (entity == null) {
            return null;
        }

        CustomerVO vo = new CustomerVO();

        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setPhone(entity.getPhone());
        vo.setAddress(entity.getAddress());
        vo.setLevel(entity.getLevel());

        return vo;
    }

}
