package com.kaede.erp.controller;


import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.CreateCustomerDTO;
import com.kaede.erp.dto.UpdateCustomerDTO;
import com.kaede.erp.service.CustomerService;
import com.kaede.erp.vo.CustomerVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/customers")
public class CustomerController {


    private final CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping
    public Result<CustomerVO> create(
            @Valid @RequestBody CreateCustomerDTO dto
    ) {

        return Result.success(
                customerService.create(dto)
        );

    }


    @GetMapping
    public Result<List<CustomerVO>> list() {

        return Result.success(
                customerService.list()
        );

    }


    @PutMapping("/{id}")
    public Result<CustomerVO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerDTO dto
    ) {

        dto.setId(id);

        return Result.success(
                customerService.update(dto)
        );

    }


    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @PathVariable Long id
    ) {

        customerService.delete(id);

        return Result.success();

    }

}
