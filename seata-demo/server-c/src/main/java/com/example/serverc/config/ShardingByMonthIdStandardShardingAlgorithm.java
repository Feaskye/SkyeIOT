package com.example.serverc.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

/**
 * 按照月份将表进行分片
 * $tableName_yyyyMM
 */
@Slf4j
public class ShardingByMonthIdStandardShardingAlgorithm implements StandardShardingAlgorithm<String> {

    private Properties properties;

    private static final SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");

    /**
     * 单个时间--如果sql中没有此时间参数是不会进入此方法的
     * 查询的时候时间类型一定要是 Date 否则会报错
     * @param availableTargetNames available data sources or table names
     * @param shardingValue sharding value
     * @return
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        String[] split = shardingValue.getValue().split("_");
        if(split.length>=2)
        {
            return shardingValue.getLogicTableName()+"_"+split[0];
        }else{
            log.warn("old data {},sharding by date",shardingValue.getValue());
            return shardingValue.getLogicTableName()+"_"+yyyyMM.format(new Date());
        }
    }

    /**
     * 不支持区间分片
     * */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<String> shardingValue) {

        return null;
    }

    @Override
    public Properties getProps() {
        log.info("get properties ...");
        return this.properties;
    }

    @Override
    public void init(Properties properties) {
        log.info("init properties ... {}",properties);
        this.properties = properties;
    }
}
