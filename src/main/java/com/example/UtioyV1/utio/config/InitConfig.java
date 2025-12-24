package com.example.UtioyV1.utio.config;

import com.example.UtioyV1.utio.Code.Config;
import com.example.UtioyV1.utio.Code.Role;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.mapper.UtioMapper;
import com.example.UtioyV1.utio.model.ConfigKeyModel;
import com.example.UtioyV1.utio.model.JurisdictionModel;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InitConfig {

    @Resource
   private   UtioMapper utioMapper;

    /**
     * 这个项目启动起来的时候需要做的事情
     * @return
     */
    public boolean initConfig(){
        //导入配置文件的配置
        List<ConfigKeyModel> configKeyModels = utioMapper.from_config();
        for (ConfigKeyModel c1 : configKeyModels) {
            Config.DAO_VALUE.put(c1.config_key,c1.config_value);
        }
        Config.IS_DAO=true; //表示加载完成配置文件


        Role.jurisdiction_map = utioMapper.from_jurisdiction();// 用于判断是否有权限
         //拿到集合
//        Role.jurisdiction_set=

//        导入权限配置 - 按照name分组，存储对应的jurisdiction_name列表
        for (JurisdictionModel model :   Role.jurisdiction_map.values()) {
            String name = model.getName();
            String jurisdictionName = model.getJurisdiction_name();
            
            // 如果该name还没有对应的List，创建一个新的ArrayList
            Role.role_list.computeIfAbsent(name, k -> new ArrayList<>()).add(jurisdictionName);
            
            // 调试输出
//            System.out.println("key:   "+model.getMap_key()+"     value: "+model);
        }


        System.out.println(Role.role_list);

        Log.info("数据库加载完毕");

        return true;
    }

}
