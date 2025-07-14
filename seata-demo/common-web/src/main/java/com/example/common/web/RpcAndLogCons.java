package com.example.common.web;

public interface RpcAndLogCons {
    /**
     * 会话ID---会话链路
     */
    String SESSION_ID="JoywareSessionId";
    /**
     * 请求ID--代表当前会话
     */
    String REQUEST_ID="JoywareRequestId";
    /**
     * 请求时间
     */
    String REQUEST_TIME="JoywareRequestTime";

    String REQUEST_URL="JoywareRequestUrl";
}
