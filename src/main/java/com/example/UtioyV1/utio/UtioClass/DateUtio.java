package com.example.UtioyV1.utio.UtioClass;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtio {
 private  static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 时间格式式
    public static String dateDay_String(){ //获取当前在线时间
        String customDateStr = dateFormat.format(new Date());
        return customDateStr; //返回时间
    }
    public static LocalDateTime dateDay_Date(){ //获取当前在线时间
        LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime; //返回时间
    }

    public static Date dateDay_Date(Long day){ //获取增加指定时长后的时间
        Date now = new Date();
        Instant instant = now.toInstant();
        Instant nextWeekInstant = instant.plus(day, ChronoUnit.DAYS); // 加一天
        Date nextWeekWithJava8 = Date.from(nextWeekInstant);
        return nextWeekWithJava8;
    }

}
