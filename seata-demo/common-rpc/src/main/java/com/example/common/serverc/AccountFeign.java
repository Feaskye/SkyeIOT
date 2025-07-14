package com.example.common.serverc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient(value = "server-c",contextId = "server-c-account")
public interface AccountFeign {
    @PostMapping("/api/account/transfer")
    void transfer(@SpringQueryMap Map<String, BigDecimal> params);
}
