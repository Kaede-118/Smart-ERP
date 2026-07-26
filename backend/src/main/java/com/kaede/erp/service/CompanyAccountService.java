package com.kaede.erp.service;


import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.entity.AccountRecord;
import com.kaede.erp.entity.CompanyAccount;
import com.kaede.erp.mapper.AccountRecordMapper;
import com.kaede.erp.mapper.CompanyAccountMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service
public class CompanyAccountService {


    private final CompanyAccountMapper accountMapper;

    private final AccountRecordMapper recordMapper;


    public CompanyAccountService(CompanyAccountMapper accountMapper, AccountRecordMapper recordMapper) {
        this.accountMapper = accountMapper;
        this.recordMapper = recordMapper;
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

        if (accountName != null) {
            account.setAccountName(accountName);
        }

        if (remark != null) {
            account.setRemark(remark);
        }

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


        accountMapper.updateBalance(amount);


        AccountRecord record = new AccountRecord();
        record.setChangeAmount(amount.toString());
        record.setBeforeBalance(before.toString());
        record.setAfterBalance(after.toString());
        record.setType("INCOME");
        record.setBusinessType(businessType);
        record.setBusinessId(businessId);
        record.setRemark(remark);
        record.setOperatorId(operatorId);

        recordMapper.insert(record);

    }


    @Transactional
    public void decrease(BigDecimal amount, String businessType, Long businessId, String remark, Long operatorId) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(40000, "支出金额必须大于0");
        }


        CompanyAccount account = accountMapper.selectById(1L);

        BigDecimal before = account.getBalance();


        int rows = accountMapper.deductBalance(amount);

        if (rows == 0) {
            throw new BusinessException(40001, "企业账户余额不足，当前余额: " + before);
        }


        BigDecimal after = before.subtract(amount);


        AccountRecord record = new AccountRecord();
        record.setChangeAmount(amount.toString());
        record.setBeforeBalance(before.toString());
        record.setAfterBalance(after.toString());
        record.setType("EXPENSE");
        record.setBusinessType(businessType);
        record.setBusinessId(businessId);
        record.setRemark(remark);
        record.setOperatorId(operatorId);

        recordMapper.insert(record);

    }


    public BigDecimal todayIncome() {
        return recordMapper.todayIncome();
    }


    public BigDecimal todayExpense() {
        return recordMapper.todayExpense();
    }


    public List<Map<String, Object>> trend() {
        return recordMapper.selectTrend(java.time.LocalDate.now().minusDays(7).toString());
    }

}
