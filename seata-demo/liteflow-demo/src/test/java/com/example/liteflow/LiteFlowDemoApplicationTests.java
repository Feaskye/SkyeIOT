package com.example.liteflow;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.slot.DefaultContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest(classes = LiteFlowDemoApplication.class)
public class LiteFlowDemoApplicationTests {

    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void test() throws Exception {
        flowExecutor.execute2Resp("chain1","arg", DefaultContext.class);
    }

}
