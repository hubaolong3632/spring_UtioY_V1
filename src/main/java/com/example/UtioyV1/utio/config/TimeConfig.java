package com.example.UtioyV1.utio.config;

import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.LogInfo.LogEntryModel;
import com.example.UtioyV1.utio.LogInfo.LogQueue;
import com.example.UtioyV1.utio.mapper.UtioMapper;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/**
 * 定时任务执行类（使用配置的周期）
 */
@Configuration
@EnableScheduling //开启定时任务
public class TimeConfig {

    /**
     * 定时任务方法：每隔配置的周期6500执行一次
     * 单位：毫秒（需将配置的秒转换为毫秒）
     */
//    @Scheduled(fixedRateString = "6500000") // 秒 -> 毫秒（拼接 "000"）
//    public void executeTask() {
//
//
//    }


    /**
     * 例：开始启动执行一次后续每30分钟执行一次
     * Cron 语法：秒 分 时 日 月 周 年（年可选）
     */
//    @PostConstruct // 启动后立即执行一次
//    @Scheduled(cron = "0 */30 * * * ?")
//    public void cronTask() {
//        System.out.println("30分钟执行一次");
//    }


//    // 上一次任务执行完成后，延迟30分钟执行下一次（30*60*1000=1800000毫秒）
//    @Scheduled(fixedDelay = 1800000, initialDelay = 0)
//    public void cronTask() {
//        System.out.println("上一次执行完成后30分钟执行");
//    }




//    @PostConstruct // 启动后立即执行一次
//    @Scheduled(cron = "0 */30 * * * ?")


    @Resource
    private UtioMapper utioMapper;

    @Scheduled(cron = "*/5 * * * * ?")
    public void LogTask() {

//        batch.addAll(dao_list)  //list集合拷贝
        //        判断要保存到数据库的内容是否为空

//        System.out.println(!LogQueue.dao_list.isEmpty());
//        System.out.println(LogQueue.dao_list.size());
       if(!LogQueue.dao_list.isEmpty()) {
//

           Log.info("数据库保存");
           List<LogEntryModel> batch = new ArrayList<>(LogQueue.dao_list); //拷贝一份新的
           LogQueue.dao_list.clear();
           utioMapper.save_log(batch); //批量保存数据库
       }

//        Log.debug("保存数据库成功");
    }


}