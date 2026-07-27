package com.kaede.erp.controller;


import com.kaede.erp.common.result.Result;
import com.kaede.erp.entity.Product;
import com.kaede.erp.mapper.ProductMapper;
import com.kaede.erp.service.InventoryService;
import com.kaede.erp.service.PurchaseOrderService;
import com.kaede.erp.service.SalesOrderService;
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

    private final InventoryService inventoryService;

    private final PurchaseOrderService purchaseOrderService;

    private final SalesOrderService salesOrderService;


    public ExcelController(ProductMapper productMapper, InventoryService inventoryService, PurchaseOrderService purchaseOrderService, SalesOrderService salesOrderService) {
        this.productMapper = productMapper;
        this.inventoryService = inventoryService;
        this.purchaseOrderService = purchaseOrderService;
        this.salesOrderService = salesOrderService;
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


    @GetMapping("/inventory/export")
    public void exportInventory(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        String filename = URLEncoder.encode("库存数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + filename + ".xlsx");


        List<InventoryExcelRow> rows = inventoryService.list(null, null).stream().map(vo -> {

            InventoryExcelRow row = new InventoryExcelRow();
            row.setProductName(vo.getProductName());
            row.setProductCode(vo.getProductCode());
            row.setCategoryName(vo.getCategoryName());
            row.setQuantity(vo.getQuantity());
            row.setWarningValue(vo.getWarningValue());
            return row;

        }).toList();

        com.alibaba.excel.EasyExcel.write(response.getOutputStream(), InventoryExcelRow.class)
                .sheet("库存数据")
                .doWrite(rows);

    }


    @GetMapping("/purchase/export")
    public void exportPurchaseOrders(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        String filename = URLEncoder.encode("采购数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + filename + ".xlsx");


        List<PurchaseOrderExcelRow> rows = purchaseOrderService.list().stream().map(vo -> {

            PurchaseOrderExcelRow row = new PurchaseOrderExcelRow();
            row.setOrderNo(vo.getOrderNo());
            row.setSupplierName(vo.getSupplierName());
            row.setItemNames(vo.getItemNames());
            row.setTotalAmount(vo.getTotalAmount());
            row.setStatus(vo.getStatus());
            row.setCreatorName(vo.getCreatorName());
            row.setCreateTime(vo.getCreateTime() != null ? vo.getCreateTime().toString() : null);
            return row;

        }).toList();

        com.alibaba.excel.EasyExcel.write(response.getOutputStream(), PurchaseOrderExcelRow.class)
                .sheet("采购数据")
                .doWrite(rows);

    }


    @GetMapping("/sales/export")
    public void exportSalesOrders(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        String filename = URLEncoder.encode("销售数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + filename + ".xlsx");


        List<SalesOrderExcelRow> rows = salesOrderService.list().stream().map(vo -> {

            SalesOrderExcelRow row = new SalesOrderExcelRow();
            row.setOrderNo(vo.getOrderNo());
            row.setCustomerName(vo.getCustomerName());
            row.setItemNames(vo.getItemNames());
            row.setTotalAmount(vo.getTotalAmount());
            row.setStatus(vo.getStatus());
            row.setCreatorName(vo.getCreatorName());
            row.setCreateTime(vo.getCreateTime() != null ? vo.getCreateTime().toString() : null);
            return row;

        }).toList();

        com.alibaba.excel.EasyExcel.write(response.getOutputStream(), SalesOrderExcelRow.class)
                .sheet("销售数据")
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
