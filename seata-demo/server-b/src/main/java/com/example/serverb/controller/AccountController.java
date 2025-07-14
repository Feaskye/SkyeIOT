package com.example.serverb.controller;


import com.example.common.domain.Account;
import com.example.serverb.service.IAccountService;
import com.example.serverb.service.impl.TccServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    private TccServiceImpl tccService;

    @GlobalTransactional
    @GetMapping("/list")
    public List<Account> list(){
        return accountService.list();
    }


    @PostMapping("/transfer")
    public void transfer(@RequestParam Map<String, BigDecimal> params){
        tccService.transfer(params);
    }
}
