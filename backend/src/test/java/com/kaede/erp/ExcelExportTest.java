package com.kaede.erp;


import com.kaede.erp.common.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class ExcelExportTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String adminToken;


    @BeforeEach
    void setUp() {
        adminToken = jwtTokenProvider.createToken(
                1L, "admin",
                List.of("product:list", "inventory:list", "purchase:list", "sales:list")
        );
    }


    @Test
    void exportProducts_shouldReturnExcelFile() throws Exception {

        mockMvc.perform(get("/api/excel/products/export")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type",
                        Matchers.containsString("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")))
                .andExpect(header().exists("Content-disposition"));

    }


    @Test
    void exportInventory_shouldReturnExcelFile() throws Exception {

        mockMvc.perform(get("/api/excel/inventory/export")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type",
                        Matchers.containsString("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")))
                .andExpect(header().exists("Content-disposition"));

    }


    @Test
    void exportPurchaseOrders_shouldReturnExcelFile() throws Exception {

        mockMvc.perform(get("/api/excel/purchase/export")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type",
                        Matchers.containsString("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")))
                .andExpect(header().exists("Content-disposition"));

    }


    @Test
    void exportSalesOrders_shouldReturnExcelFile() throws Exception {

        mockMvc.perform(get("/api/excel/sales/export")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type",
                        Matchers.containsString("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")))
                .andExpect(header().exists("Content-disposition"));

    }


    @Test
    void exportInventory_withoutAuth_shouldReturn401() throws Exception {

        mockMvc.perform(get("/api/excel/inventory/export"))
                .andExpect(status().isUnauthorized());

    }

}
