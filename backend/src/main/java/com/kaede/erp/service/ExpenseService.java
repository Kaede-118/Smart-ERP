package com.kaede.erp.service;


import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.dto.ExpenseCreateRequest;
import com.kaede.erp.dto.ExpenseQueryRequest;
import com.kaede.erp.dto.ExpenseSummaryDTO;
import com.kaede.erp.dto.ExpenseUpdateRequest;
import com.kaede.erp.entity.Expense;
import com.kaede.erp.mapper.ExpenseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ExpenseService {


    private static final DateTimeFormatter NO_FMT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");


    private final ExpenseMapper expenseMapper;

    private final CompanyAccountService accountService;


    public ExpenseService(ExpenseMapper expenseMapper, CompanyAccountService accountService) {
        this.expenseMapper = expenseMapper;
        this.accountService = accountService;
    }


    public List<Map<String, Object>> list(ExpenseQueryRequest req) {

        return expenseMapper.selectExpenseList()
                .stream()
                .filter(m -> {
                    if (req.getType() != null && !req.getType().isBlank()
                            && !req.getType().equals(m.get("type"))) return false;
                    if (req.getStatus() != null && !req.getStatus().isBlank()
                            && !req.getStatus().equals(m.get("status"))) return false;
                    if (req.getKeyword() != null && !req.getKeyword().isBlank()) {
                        String kw = req.getKeyword().toLowerCase();
                        String v = ((String) m.getOrDefault("employee_name", "")).toLowerCase()
                                + ((String) m.getOrDefault("expense_no", "")).toLowerCase()
                                + ((String) m.getOrDefault("description", "")).toLowerCase();
                        if (!v.contains(kw)) return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }


    public Map<String, Object> detail(Long id) {

        Map<String, Object> m = expenseMapper.selectExpenseDetail(id);

        if (m == null) {
            throw new BusinessException(40400, "费用不存在");
        }

        return m;
    }


    @Transactional
    public void create(ExpenseCreateRequest req) {

        String no = "BX" + LocalDateTime.now().format(NO_FMT)
                + (System.currentTimeMillis() % 1000);


        Expense e = new Expense();
        e.setExpenseNo(no);
        e.setEmployeeId(req.getEmployeeId());
        e.setEmployeeName(req.getEmployeeName());
        e.setDepartment(req.getDepartment());
        e.setType(req.getType());
        e.setAmount(req.getAmount());
        e.setDescription(req.getDescription());
        e.setStatus("PENDING");
        e.setAttachmentUrl(req.getAttachmentUrl());
        e.setRemark(req.getRemark());
        e.setCreateBy(UserContext.getUserId());

        expenseMapper.insert(e);

    }


    @Transactional
    public void update(Long id, ExpenseUpdateRequest req) {

        Expense e = expenseMapper.selectById(id);

        if (e == null) {
            throw new BusinessException(40400, "费用不存在");
        }

        if (!"PENDING".equals(e.getStatus())) {
            throw new BusinessException(40002, "当前状态不允许修改");
        }

        e.setEmployeeName(req.getEmployeeName());
        e.setDepartment(req.getDepartment());
        e.setType(req.getType());
        e.setAmount(req.getAmount());
        e.setDescription(req.getDescription());
        e.setAttachmentUrl(req.getAttachmentUrl());
        e.setRemark(req.getRemark());

        expenseMapper.updateById(e);

    }


    @Transactional
    public void delete(Long id) {

        Expense e = expenseMapper.selectById(id);

        if (e == null) {
            throw new BusinessException(40400, "费用不存在");
        }

        expenseMapper.deleteById(id);

    }


    @Transactional
    public void approve(Long id) {

        Expense e = expenseMapper.selectById(id);

        if (e == null) {
            throw new BusinessException(40400, "费用不存在");
        }

        if (!"PENDING".equals(e.getStatus())) {
            throw new BusinessException(40002, "当前状态不允许审批");
        }

        e.setStatus("APPROVED");
        e.setApproveTime(LocalDateTime.now());

        expenseMapper.updateById(e);

    }


    @Transactional
    public void reject(Long id) {

        Expense e = expenseMapper.selectById(id);

        if (e == null) {
            throw new BusinessException(40400, "费用不存在");
        }

        if (!"PENDING".equals(e.getStatus())) {
            throw new BusinessException(40002, "当前状态不允许驳回");
        }

        e.setStatus("REJECTED");
        e.setApproveTime(LocalDateTime.now());

        expenseMapper.updateById(e);

    }


    @Transactional
    public void pay(Long id) {

        Expense e = expenseMapper.selectById(id);

        if (e == null) {
            throw new BusinessException(40400, "费用不存在");
        }

        if (!"APPROVED".equals(e.getStatus())) {
            throw new BusinessException(40002, "当前状态不允许付款");
        }


        accountService.decrease(
                e.getAmount(),
                "EXPENSE",
                id,
                "费用付款",
                UserContext.getUserId()
        );


        e.setStatus("PAID");
        e.setPayTime(LocalDateTime.now());
        e.setRemark("已通过企业账户付款");

        expenseMapper.updateById(e);

    }


    public ExpenseSummaryDTO summary() {

        var account = accountService.getAccount();

        ExpenseSummaryDTO dto = new ExpenseSummaryDTO();

        dto.setMonthExpense(expenseMapper.monthExpense());
        dto.setPendingCount(expenseMapper.pendingCount());
        dto.setPaidCount(expenseMapper.paidCount());
        dto.setTodayExpense(expenseMapper.todayExpense());
        dto.setCompanyBalance(account != null ? account.getBalance() : BigDecimal.ZERO);

        return dto;
    }

}
