package com.example.liteflow.service;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LiteflowComponent("a")
public class ACmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        log.info("A executed!");
    }
}
