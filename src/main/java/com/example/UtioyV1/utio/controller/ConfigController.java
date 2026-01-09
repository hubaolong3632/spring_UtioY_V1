package com.example.UtioyV1.utio.controller;


import com.example.UtioyV1.utio.Code.Config;
import com.example.UtioyV1.utio.Code.Result;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.LogInfo.LogEntryModel;
import com.example.UtioyV1.utio.config.InitConfig;
import com.example.UtioyV1.utio.mapper.UtioMapper;
import com.example.UtioyV1.utio.model.ConfigKeyModel;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * 配置文件
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    private UtioMapper utioMapper;


    @Resource
    private InitConfig init;

    /**
     *  重新初始化配置文件
     * @return
     */
    @RequestMapping("/init")
    public Result init(){

        return  Result.utio(init.initConfig(),"更新配置文件成功","数据库为空/微服务错误");
    }

    /**
     * 查询最新的日志
     * @param level
     * @param type
     * @param sum
     * @return
     */
    @RequestMapping("/log")
    public Result log(String level, String type, @RequestParam(value = "sum", defaultValue = "20") Integer sum ){
        List<LogEntryModel> logEntryModels = utioMapper.from_log(level, type, sum);

        return Result.success(logEntryModels);
    }


}
