package com.example.liteflow.service;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LiteflowComponent("b")
public class BCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        log.info("B executed!");
    }
}
