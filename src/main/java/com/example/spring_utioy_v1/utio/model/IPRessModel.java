package com.example.spring_utioy_v1.utio.model;

import lombok.Data;

@Data
public class IPRessModel {
   private String ip; //IP
   private String province; //省份
   private String provinceCode;//省份代码
   private String city;//城市
   private String cityCode; //城市代码
   private String address;//地址

    public IPRessModel() {
    }

    public IPRessModel(String ip, String province, String provinceCode, String city, String cityCode, String address) {
        this.ip = ip;
        this.province = province;
        this.provinceCode = provinceCode;
        this.city = city;
        this.cityCode = cityCode;
        this.address = address;
    }
}
