package com.kaede.erp.service;


import com.kaede.erp.dto.CreateProductCategoryDTO;
import com.kaede.erp.dto.UpdateProductCategoryDTO;
import com.kaede.erp.vo.ProductCategoryVO;

import java.util.List;


public interface ProductCategoryService {


    List<ProductCategoryVO> list();


    ProductCategoryVO create(CreateProductCategoryDTO dto);


    ProductCategoryVO update(UpdateProductCategoryDTO dto);


    void delete(Long id);

}
