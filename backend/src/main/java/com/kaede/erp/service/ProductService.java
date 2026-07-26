package com.kaede.erp.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaede.erp.dto.CreateProductDTO;
import com.kaede.erp.dto.ProductQueryDTO;
import com.kaede.erp.dto.UpdateProductDTO;
import com.kaede.erp.vo.ProductVO;


public interface ProductService {


    Page<ProductVO> list(ProductQueryDTO dto);


    ProductVO getProduct(Long id);


    ProductVO create(CreateProductDTO dto);


    ProductVO update(UpdateProductDTO dto);


    void delete(Long id);

}
