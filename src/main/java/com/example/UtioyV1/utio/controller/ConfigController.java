package com.example.UtioyV1.utio.controller;


import com.example.UtioyV1.utio.Code.Config;
import com.example.UtioyV1.utio.Code.Result;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.LogInfo.LogEntryModel;
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

    /**
     *  重新初始化配置文件
     * @return
     */
    @RequestMapping("/init")
    public Result init(){
        //配置从数据库导入的
        List<ConfigKeyModel> configKeyModels = utioMapper.from_config();
        if(configKeyModels!=null){
            Config.DAO_VALUE.clear(); //清空原来的表

            for (ConfigKeyModel c1 : configKeyModels) {
                Config.DAO_VALUE.put(c1.config_key,c1.config_value);
            }
            Log.info("重新更新配置文件");
            return Result.success("更新配置文件成功");
        }


      return Result.failure("数据库为空");
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
