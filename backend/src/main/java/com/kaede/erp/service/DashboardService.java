package com.kaede.erp.service;


import com.kaede.erp.dto.DashboardSummaryDTO;
import com.kaede.erp.dto.DashboardTrendDTO;
import com.kaede.erp.vo.DashboardWarningVO;
import com.kaede.erp.vo.TopProductVO;

import java.util.List;


public interface DashboardService {


    DashboardSummaryDTO summary();


    List<DashboardTrendDTO> trend();


    List<DashboardWarningVO> warnings();


    List<TopProductVO> topProducts();

}
