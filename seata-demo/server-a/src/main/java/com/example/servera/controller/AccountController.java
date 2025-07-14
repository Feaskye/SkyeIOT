package com.example.servera.controller;


import com.example.common.domain.Account;
import com.example.common.serverb.AccountFeign;
import com.example.servera.service.IAccountService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 
 * @since 2025-04-28
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Resource
    private IAccountService accountService;

    @Resource
    private AccountFeign accountFeignB;

    @Resource
    private com.example.common.serverc.AccountFeign accountFeignC;

    @GlobalTransactional
    @GetMapping("/list")
    public List<Account> list(){
        return accountService.list();
    }

    @GlobalTransactional
    @PostMapping("/transfer")
    public Boolean transfer(){
        Map<String, BigDecimal> params = new HashMap<>();
        params.put("1",BigDecimal.valueOf(12L));
        params.put("2",BigDecimal.valueOf(18L));
        params.put("3",BigDecimal.valueOf(19L));
        accountFeignB.transfer(params);
        accountFeignC.transfer(params);
//        throw new RuntimeException("分布式");
        return false;
    }
}
