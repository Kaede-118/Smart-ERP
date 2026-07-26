package com.kaede.erp.controller;


import com.kaede.erp.common.result.Result;
import com.kaede.erp.entity.Product;
import com.kaede.erp.mapper.ProductMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


@RestController
@RequestMapping("/api/excel")
public class ExcelController {


    private final ProductMapper productMapper;


    public ExcelController(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }


    @GetMapping("/products/export")
    public void exportProducts(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        String filename = URLEncoder.encode("商品数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + filename + ".xlsx");


        List<Product> products = productMapper.selectList(null);


        List<ProductExcelRow> rows = products.stream().map(p -> {

            ProductExcelRow row = new ProductExcelRow();
            row.setName(p.getName());
            row.setCode(p.getCode());
            row.setCostPrice(p.getCostPrice());
            row.setSalePrice(p.getSalePrice());
            row.setUnit(p.getUnit());
            row.setStatus(p.getStatus());
            return row;

        }).toList();

        com.alibaba.excel.EasyExcel.write(response.getOutputStream(), ProductExcelRow.class)
                .sheet("商品数据")
                .doWrite(rows);

    }


    @PostMapping("/products/import")
    public Result<Integer> importProducts(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        List<ProductExcelRow> rows =
                com.alibaba.excel.EasyExcel.read(file.getInputStream())
                        .head(ProductExcelRow.class)
                        .sheet()
                        .doReadSync();


        int count = 0;

        for (ProductExcelRow row : rows) {

            Product product = new Product();
            product.setName(row.getName());
            product.setCode(row.getCode());
            product.setCostPrice(row.getCostPrice());
            product.setSalePrice(row.getSalePrice());
            product.setUnit(row.getUnit());
            product.setStatus(row.getStatus() != null ? row.getStatus() : 1);

            productMapper.insert(product);
            count++;
        }

        return Result.success(count);

    }

}
