package com.kaede.erp.service.impl;


import com.kaede.erp.dto.DashboardSummaryDTO;
import com.kaede.erp.dto.DashboardTrendDTO;
import com.kaede.erp.mapper.DashboardMapper;
import com.kaede.erp.service.DashboardService;
import com.kaede.erp.vo.DashboardWarningVO;
import com.kaede.erp.vo.TopProductVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class DashboardServiceImpl implements DashboardService {


    private final DashboardMapper dashboardMapper;


    public DashboardServiceImpl(DashboardMapper dashboardMapper) {
        this.dashboardMapper = dashboardMapper;
    }


    @Override
    public DashboardSummaryDTO summary() {

        DashboardSummaryDTO dto = new DashboardSummaryDTO();

        dto.setProductCount(dashboardMapper.countProduct());
        dto.setCustomerCount(dashboardMapper.countCustomer());
        dto.setSupplierCount(dashboardMapper.countSupplier());
        dto.setInventoryQuantity(dashboardMapper.sumInventoryQuantity());
        dto.setTodayPurchaseAmount(dashboardMapper.todayPurchaseAmount());
        dto.setTodaySaleAmount(dashboardMapper.todaySaleAmount());
        dto.setLowStockCount(dashboardMapper.countLowStock());

        return dto;
    }


    @Override
    public List<DashboardTrendDTO> trend() {

        String since = LocalDate.now().minusDays(7).toString();


        List<DashboardTrendDTO> purchase =
                dashboardMapper.selectPurchaseTrend(since);

        List<DashboardTrendDTO> sales =
                dashboardMapper.selectSalesTrend(since);


        Map<LocalDate, DashboardTrendDTO> merged =
                purchase.stream()
                        .collect(Collectors.toMap(
                                DashboardTrendDTO::getDate,
                                t -> t
                        ));

        for (DashboardTrendDTO s : sales) {

            DashboardTrendDTO exist = merged.get(s.getDate());

            if (exist != null) {
                exist.setSaleAmount(s.getSaleAmount());
            } else {
                merged.put(s.getDate(), s);
            }
        }


        return merged.values().stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .toList();
    }


    @Override
    public List<DashboardWarningVO> warnings() {

        return dashboardMapper.selectLowStockList();
    }


    @Override
    public List<TopProductVO> topProducts() {

        return dashboardMapper.selectTopProducts();
    }

}
