package com.example.serverb.config;

import com.yomahub.tlog.core.thread.TLogInheritableTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.scheduling.concurrent.ExecutorConfigurationSupport;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class ExecutorBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof ExecutorConfigurationSupport){
            log.info("beanName={},bean={}",beanName,bean);
            ((ExecutorConfigurationSupport) bean).setThreadFactory(r->{
                AtomicInteger atomicInteger = new AtomicInteger(1);
                Thread thread = new Thread(new TLogInheritableTask() {
                    @Override
                    public void runTask() {
                        r.run();
                    }
                });
                thread.setDaemon(true);
                thread.setPriority(Thread.NORM_PRIORITY);
                thread.setName(((ExecutorConfigurationSupport) bean).getThreadNamePrefix()+"-thread-"+atomicInteger.getAndIncrement());
                return thread;
            });
        }
        return bean;
    }
}
