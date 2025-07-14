package com.example.serverc.config;

import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 按照月份将表进行分片
 * $tableName_yyyyMM
 */
@Slf4j
public class ShardingByMonthStandardShardingAlgorithm implements StandardShardingAlgorithm<Date> {

    private static final SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");
    private static final DateTimeFormatter yyyyMM_ = DateTimeFormatter.ofPattern("yyyyMM");

    private Properties properties;

    /**
     * 单个时间--如果sql中没有此时间参数是不会进入此方法的
     * 查询的时候时间类型一定要是 Date 否则会报错
     * @param availableTargetNames available data sources or table names
     * @param shardingValue sharding value
     * @return
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
        Date value = shardingValue.getValue();
        String format = yyyyMM.format(value);
        return shardingValue.getLogicTableName()+"_"+format;
    }

    /**
     * 时间区间---如果sql没有此区间参数是不会调用此方法的
     * @param availableTargetNames available data sources or table names
     * @param shardingValue sharding value
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Date> shardingValue) {
        List<String> tables=new ArrayList<>();
        Range<Date> valueRange = shardingValue.getValueRange();
        Date start = valueRange.lowerEndpoint();
        Date end = valueRange.upperEndpoint();
        int startMonth=0;
        int endMonth=0;
        if(Objects.isNull(start))
        {
           startMonth = Integer.parseInt(this.properties.getProperty("startMonth"));
        }else{
           startMonth=Integer.parseInt(yyyyMM.format(start));
        }
        if(Objects.isNull(end))
        {
            endMonth = Integer.parseInt(this.properties.getProperty("endMonth"));
        }else{
            endMonth=Integer.parseInt(yyyyMM.format(end));
        }
        List<String> allMonths = getMonthsBetween(YearMonth.parse(startMonth+"",yyyyMM_), YearMonth.parse(endMonth+"",yyyyMM_));
        for (String allMonth : allMonths) {
            tables.add(shardingValue.getLogicTableName()+"_"+allMonth);
        }
        return tables;
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

    public static List<String> getMonthsBetween(YearMonth start, YearMonth end) {
        List<String> months = new ArrayList<>();
        YearMonth current = start;

        while (!current.isAfter(end)) {
            months.add(current.format(yyyyMM_));
            current = current.plusMonths(1);
        }

        return months;
    }
}
