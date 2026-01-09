package com.example.UtioyV1.utio.model.microService;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataHttpModel {
    private String publicKey; //公钥
    private String t1; //公钥
    public Map<String, List<String>> roleList;//权限

}
