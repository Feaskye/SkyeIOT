package com.example.serverc.service.impl;

import com.example.common.TccService;
import com.example.common.domain.Account;
import com.example.serverc.service.IAccountService;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
public class TccServiceImpl implements TccService {
    @Resource
    private IAccountService accountService;

    @Override
    public boolean transfer(Map<String, BigDecimal> params) {
        return false;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        log.info("commit-params={}", actionContext.getActionContext("params"));
        Account account = new Account();
        account.setId(3L);
        account.setMoney(BigDecimal.valueOf(200));
        accountService.updateById(account);
//        throw new RuntimeException("commit-rollback");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        log.info("rollback-params={}", actionContext.getActionContext("params"));
        Account account = new Account();
        account.setId(3L);
        account.setMoney(BigDecimal.valueOf(45));
        accountService.updateById(account);
        return true;
    }
}
