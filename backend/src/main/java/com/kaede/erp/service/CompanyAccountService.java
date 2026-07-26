package com.kaede.erp.service;


import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.entity.CompanyAccount;
import com.kaede.erp.entity.AccountTransaction;
import com.kaede.erp.mapper.AccountTransactionMapper;
import com.kaede.erp.mapper.CompanyAccountMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
public class CompanyAccountService {


    private final CompanyAccountMapper accountMapper;

    private final AccountTransactionMapper transactionMapper;

    private final JdbcTemplate jdbc;


    public CompanyAccountService(
            CompanyAccountMapper accountMapper,
            AccountTransactionMapper transactionMapper,
            JdbcTemplate jdbc
    ) {
        this.accountMapper = accountMapper;
        this.transactionMapper = transactionMapper;
        this.jdbc = jdbc;
    }


    public CompanyAccount getAccount() {

        CompanyAccount account = accountMapper.selectById(1L);

        if (account == null) {
            throw new BusinessException(40400, "企业账户不存在");
        }

        return account;
    }


    @Transactional
    public void updateAccount(String accountName, String remark) {

        CompanyAccount account = accountMapper.selectById(1L);

        if (account == null) {
            throw new BusinessException(40400, "企业账户不存在");
        }

        if (accountName != null) account.setAccountName(accountName);
        if (remark != null) account.setRemark(remark);

        accountMapper.updateById(account);

    }


    @Transactional
    public void increase(BigDecimal amount, String businessType, Long businessId, String remark, Long operatorId) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(40000, "入账金额必须大于0");
        }


        CompanyAccount account = accountMapper.selectById(1L);
        BigDecimal before = account.getBalance();
        BigDecimal after = before.add(amount);


        jdbc.update("UPDATE company_account SET balance = balance + ? WHERE id = 1", amount);


        AccountTransaction txn = new AccountTransaction();
        txn.setChangeAmount(amount.toString());
        txn.setBeforeBalance(before.toString());
        txn.setAfterBalance(after.toString());
        txn.setType("INCOME");
        txn.setChangeType("IN");
        txn.setBusinessType(businessType);
        txn.setBusinessId(businessId);
        txn.setRemark(remark);
        txn.setOperatorId(operatorId);

        transactionMapper.insert(txn);

    }


    @Transactional
    public void decrease(BigDecimal amount, String businessType, Long businessId, String remark, Long operatorId) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(40000, "支出金额必须大于0");
        }


        CompanyAccount account = accountMapper.selectById(1L);
        BigDecimal before = account.getBalance();


        int rows = jdbc.update(
                "UPDATE company_account SET balance = balance - ? WHERE id = 1 AND balance >= ?",
                amount, amount
        );

        if (rows == 0) {
            throw new BusinessException(40001,
                    "企业账户余额不足，当前余额: " + before);
        }


        BigDecimal after = before.subtract(amount);


        AccountTransaction txn = new AccountTransaction();
        txn.setChangeAmount(amount.toString());
        txn.setBeforeBalance(before.toString());
        txn.setAfterBalance(after.toString());
        txn.setType("EXPENSE");
        txn.setChangeType("OUT");
        txn.setBusinessType(businessType);
        txn.setBusinessId(businessId);
        txn.setRemark(remark);
        txn.setOperatorId(operatorId);

        transactionMapper.insert(txn);

    }


    public BigDecimal todayIncome() {
        return transactionMapper.todayIncome();
    }


    public BigDecimal todayExpense() {
        return transactionMapper.todayExpense();
    }


    public List<Map<String, Object>> trend() {
        return transactionMapper.selectTrend(
                LocalDateTime.now().minusDays(7).toString().substring(0, 10)
        );
    }


    public Map<String, Object> listTransactions(String type, String businessType, int page, int size) {

        List<AccountTransaction> list = transactionMapper.selectTransactionList(
                type, businessType, size, (page - 1) * size
        );

        long total = transactionMapper.countTransactions(type, businessType);

        return Map.of("records", list, "total", total, "page", page, "size", size);
    }

}
