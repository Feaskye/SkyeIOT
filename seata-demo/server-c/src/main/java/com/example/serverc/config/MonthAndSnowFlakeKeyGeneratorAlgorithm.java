package com.example.serverc.config;

import com.example.common.web.utils.IDWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.spi.KeyGenerateAlgorithm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Slf4j
public class MonthAndSnowFlakeKeyGeneratorAlgorithm implements KeyGenerateAlgorithm {
    private Properties properties;

    private final static IDWorker idWorker = new IDWorker(10, 14);

    private final static SimpleDateFormat yyyyMM=new SimpleDateFormat("yyyyMM");

    @Override
    public Comparable<String> generateKey() {
        return yyyyMM.format(new Date()+"_"+idWorker.nextId());
    }

    @Override
    public Properties getProps() {
        return this.properties;
    }

    @Override
    public void init(Properties properties) {
        log.info("init properties ... {}",properties);
       this.properties=properties;
    }

    @Override
    public String getType() {
        return "CLASS_BASED";
    }

    @Override
    public boolean isDefault() {
        return true;
    }
}
