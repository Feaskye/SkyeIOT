package com.example.serverb.service.impl;

import com.example.common.TccService;
import com.example.common.domain.Account;
import com.example.serverb.service.IAccountService;
import com.yomahub.tlog.core.thread.TLogInheritableTask;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TccServiceImpl implements TccService {
    @Resource
    private IAccountService accountService;

    @Resource(name = "exec00")
    private AsyncTaskExecutor asyncTaskExecutor;

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 3,
            30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000),
            r -> {
                Thread thread = new Thread(new TLogInheritableTask() {
                    @Override
                    public void runTask() {
                        r.run();
                    }
                });
                thread.setName("exec-01");
                thread.setUncaughtExceptionHandler((t, e) -> log.error(t.getName(), e));
                return thread;
            }, new ThreadPoolExecutor.DiscardOldestPolicy());

    @Override
    public boolean transfer(Map<String, BigDecimal> params) {
        log.info("this is a test {}",Thread.currentThread().getId());
        executor.execute(() -> log.info("this is a test {}",Thread.currentThread().getId()));
        asyncTaskExecutor.execute(() -> log.info("this is a test {}",Thread.currentThread().getId()));
        return false;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        log.info("commit-params={}", actionContext.getActionContext("params"));
        Account account = new Account();
        account.setId(2L);
        account.setMoney(BigDecimal.valueOf(47.6));
        accountService.updateById(account);
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        log.info("rollback-params={}", actionContext.getActionContext("params"));
        Account account = new Account();
        account.setId(2L);
        account.setMoney(BigDecimal.valueOf(15.5));
        accountService.updateById(account);
        return true;
    }
}
