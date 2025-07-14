package com.example.common.web.interceptor;

import com.example.common.web.RpcAndLogCons;
import com.example.common.web.utils.IDWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private IDWorker idWorker = new IDWorker(19L,23L);

    private String applicationName;

    private String contextPath;

    public LogInterceptor(String applicationName, String contextPath){
        this.applicationName = applicationName;
        this.contextPath=contextPath;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionId;
        if(StringUtils.isNotBlank(request.getHeader(RpcAndLogCons.SESSION_ID)))
        {
            sessionId=request.getHeader(RpcAndLogCons.SESSION_ID);
        }else {
            sessionId = "SESSION::"+idWorker.nextId();
        }
        long requestTime = System.currentTimeMillis();
        String requestId = "REQUEST::"+idWorker.nextId();
        request.setAttribute(RpcAndLogCons.SESSION_ID,sessionId);
        request.setAttribute(RpcAndLogCons.REQUEST_ID,requestId);
        request.setAttribute(RpcAndLogCons.REQUEST_TIME,requestTime);
        MDC.put("sessionId",sessionId);
        log.info("request-url {},request-id {},session-id {}", request.getRequestURI(), requestId,sessionId);
        response.setHeader(RpcAndLogCons.SESSION_ID,sessionId);
        response.setHeader(RpcAndLogCons.REQUEST_ID,requestId);
        response.setHeader(RpcAndLogCons.REQUEST_TIME,requestTime+"");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String sessionId=(String)request.getAttribute(RpcAndLogCons.SESSION_ID);
        String requestId = (String)request.getAttribute(RpcAndLogCons.REQUEST_ID);
        long requestTime = (long)request.getAttribute(RpcAndLogCons.REQUEST_TIME);
        long cost=System.currentTimeMillis()-requestTime;
        log.info("request-url {},request-id {},session-id {},#cost {}ms", request.getRequestURI(), requestId,sessionId,cost);
    }
}
