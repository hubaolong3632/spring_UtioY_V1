package com.example.UtioyV1.utio.config;

import com.example.UtioyV1.utio.Code.Config;
import com.example.UtioyV1.utio.Code.Role;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.UtioClass.ApiUtio;
import com.example.UtioyV1.utio.mapper.UtioMapper;
import com.example.UtioyV1.utio.model.ConfigKeyModel;
import com.example.UtioyV1.utio.model.JurisdictionModel;
import com.example.UtioyV1.utio.model.microService.MicroServiceHttp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class InitConfig {

    @Resource
   private   UtioMapper utioMapper;

    /**
     * 这个项目启动起来的时候需要做的事情
     */
    public boolean initConfig(){
        //导入配置文件的配置
        List<ConfigKeyModel> configKeyModels = utioMapper.from_config();
        for (ConfigKeyModel c1 : configKeyModels) {
            Config.DAO_VALUE.put(c1.config_key,c1.config_value);
        }
        Config.IS_DAO=true; //表示加载完成配置文件



        //加载权限列表 如果是本地服务，如果是微服务就加载下面那个权限配置（从微服务的板子里面加载）
        if(Config.MicroService==false) { //判断是否开启微服务调用

            Role.jurisdiction_map = utioMapper.from_jurisdiction();// 用于判断是否有权限
//        导入权限配置 - 按照name分组，存储对应的jurisdiction_name列表
            for (JurisdictionModel model : Role.jurisdiction_map.values()) {
                String name = model.getName();
                String jurisdictionName = model.getJurisdiction_name();
                // 如果该name还没有对应的List，创建一个新的ArrayList
                Role.role_list.computeIfAbsent(name, k -> new ArrayList<>()).add(jurisdictionName);
            }

        }
        else {
//            如果为微服务调用的话就走这边
            try{

                MicroServiceHttp microService = ApiUtio.API_get(Config.service_url + "/oauth2/serviceData",MicroServiceHttp.class);
                Role.JwtKeyConfig=microService.getData().getPublicKey();

                Log.debug("密钥："+ Role.JwtKeyConfig);

                String JwtKeyConfig= Role.JwtKeyConfig; //这个是公钥
                byte[] publicKeyBytes = Base64.getDecoder().decode(JwtKeyConfig);
                X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);

//                配置公钥
                Role.publicKey= KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
                Log.info("微服务公钥配置完成");
            }catch (Exception e){
               Log.error("请配置正确的公钥/服务器错误"+e.getMessage());
                e.printStackTrace();
                return false;
            }

        }


        Log.info("数据库加载完毕");

        return true;
    }

}
