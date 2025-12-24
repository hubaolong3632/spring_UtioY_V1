package com.example.UtioyV1.utio.Filter;

import com.example.UtioyV1.utio.Code.Result;
import com.example.UtioyV1.utio.Code.ResultCode;
import com.example.UtioyV1.utio.Code.Role;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.UtioY;
import com.example.UtioyV1.utio.config.FilterTool;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IP限流过滤器
 * 规则：每个IP每分钟最多访问50次，超过后阶梯式封禁
 * 第一次超限：封禁10分钟
 * 第二次超限：封禁1小时
 * 第三次及以上：封禁1天
 */
@Component
@Order(1)
public class IpFilter implements Filter {

    // 存储IP的访问计数和时间窗口
    private final Map<String, IpAccessInfo> ipAccessMap = new ConcurrentHashMap<>();
    // 存储IP的封禁信息（封禁结束时间 + 累计封禁次数）
    private final Map<String, IpBlockInfo> ipBlockMap = new ConcurrentHashMap<>();

    // 限流规则常量（测试用：每分钟250次，正式环境改回50）
    private static final int MAX_REQUEST_PER_MINUTE = 250;
    private static final long TIME_WINDOW = 60 * 1000; // 1分钟时间窗口

    // 封禁时长（毫秒，恢复正常测试值）
    private static final long FIRST_BLOCK_TIME = 10 * 60 * 100; // 第一次：1分钟
    private static final long SECOND_BLOCK_TIME = 60 * 60 * 1000; // 第二次：1小时
    private static final long THIRD_PLUS_BLOCK_TIME = 24 * 60 * 60 * 1000; // 第三次：1天

    @Resource
    private FilterTool filterTool;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        long startA = System.nanoTime(); // 纳秒级起点

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        CustomRequestWrapper wrappedRequest = new CustomRequestWrapper(httpRequest);

        // 获取客户端真实IP
        String userIp = UtioY.getClient_IP(httpRequest);
        wrappedRequest.addParameter(Role.user_ip, userIp);
//        Log.debug("处理IP "+userIp+" 的请求" );

        // 1. 检查IP是否处于封禁状态
        IpBlockInfo blockInfo = checkIpBlockStatus(userIp);
        if (blockInfo != null && System.currentTimeMillis() < blockInfo.getBlockEndTime()) {
            long remainTime = (blockInfo.getBlockEndTime() - System.currentTimeMillis()) / 1000;
            String message = String.format("IP %s 已被封禁，剩余解封时间：%d秒，封禁原因：访问频率超限（第%d次违规）",
                    userIp, remainTime, blockInfo.getBlockCount());
            filterTool.send(httpResponse, Result.failure(ResultCode.IP_NO,message));
            return;
        }

        // 2. 检查并更新访问计数
        boolean isOverLimit = checkAndUpdateAccessCount(userIp);

        // 3. 如果超限，执行封禁逻辑
        if (isOverLimit) {
            blockIp(userIp, blockInfo); // 传入历史封禁记录，保留累计次数
            IpBlockInfo newBlockInfo = ipBlockMap.get(userIp);
            long blockTime = getBlockTimeByCount(newBlockInfo.getBlockCount());
            String message = String.format("IP %s 访问频率超限（每分钟最多%d次），已被封禁%.1f分钟，第%d次违规",
                    userIp, MAX_REQUEST_PER_MINUTE, blockTime / 60000.0, newBlockInfo.getBlockCount());
//            returnForbidden(httpResponse, message);


            filterTool.send(httpResponse, Result.failure(ResultCode.IP_NO,message));
            return;
        }

        // 4. 正常放行
        chain.doFilter(wrappedRequest, response);

        double time = (System.nanoTime()-startA)/1_000_000.0; // 获取程序耗时 毫秒

        Log.debug("\n\n========耗时:"+String.format("%.4f", (time/1000))+" 秒 |  "+time+" 毫秒 | QPS:"+(int)(1000/time)+"========");

    }

    /**
     * 检查IP封禁状态（保留累计封禁次数，仅移除过期的封禁时间）
     */
    private IpBlockInfo checkIpBlockStatus(String ip) {
        IpBlockInfo blockInfo = ipBlockMap.get(ip);
        if (blockInfo == null) {
            return null;
        }

        // 封禁过期：仅清空封禁结束时间，保留累计次数
        if (System.currentTimeMillis() >= blockInfo.getBlockEndTime()) {
            blockInfo.setBlockEndTime(0); // 标记为未封禁
            ipBlockMap.put(ip, blockInfo);
            Log.debug("IP"+ip+" 封禁过期，保留累计封禁次数："+blockInfo.getBlockCount());
        }
        return blockInfo;
    }

    /**
     * 检查并更新IP访问计数
     */
    private boolean checkAndUpdateAccessCount(String ip) {
        long currentTime = System.currentTimeMillis();
        IpAccessInfo accessInfo = ipAccessMap.get(ip);

        // 首次访问或时间窗口已重置
        if (accessInfo == null || currentTime - accessInfo.getWindowStartTime() > TIME_WINDOW) {
            ipAccessMap.put(ip, new IpAccessInfo(currentTime, 1));
            return false;
        }

        // 时间窗口内计数+1
        int newCount = accessInfo.getRequestCount() + 1;
        accessInfo.setRequestCount(newCount);
        ipAccessMap.put(ip, accessInfo);
        Log.debug("IP "+ip+" 访问计数："+newCount+"次/分钟");

        return newCount > MAX_REQUEST_PER_MINUTE;
    }

    /**
     * 执行IP封禁逻辑（保留累计封禁次数）
     */
    private void blockIp(String ip, IpBlockInfo oldBlockInfo) {
        long currentTime = System.currentTimeMillis();
        int blockCount = 1;

        // 如果有历史记录，复用累计封禁次数
        if (oldBlockInfo != null) {
            blockCount = oldBlockInfo.getBlockCount() + 1;
        }

        // 计算封禁结束时间
        long blockTime = getBlockTimeByCount(blockCount);
        long blockEndTime = currentTime + blockTime;

        // 更新封禁记录（保留累计次数）
        IpBlockInfo newBlockInfo = new IpBlockInfo(blockEndTime, blockCount);
        ipBlockMap.put(ip, newBlockInfo);
        // 重置访问计数
        ipAccessMap.remove(ip);
        Log.debug("IP "+ip+" 封禁记录更新：累计"+blockCount+"次，封禁至"+blockEndTime);
    }

    /**
     * 根据累计封禁次数获取封禁时长
     */
    private long getBlockTimeByCount(int count) {
        return switch (count) {
            case 1 -> FIRST_BLOCK_TIME;
            case 2 -> SECOND_BLOCK_TIME;
            default -> THIRD_PLUS_BLOCK_TIME;
        };
    }

    /**
     * 返回403禁止访问响应
     */
    private void returnForbidden(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(message);
    }

    // IP访问信息封装类
    private static class IpAccessInfo {
        private long windowStartTime;
        private int requestCount;

        public IpAccessInfo(long windowStartTime, int requestCount) {
            this.windowStartTime = windowStartTime;
            this.requestCount = requestCount;
        }

        // getter/setter
        public long getWindowStartTime() {
            return windowStartTime;
        }

        public void setWindowStartTime(long windowStartTime) {
            this.windowStartTime = windowStartTime;
        }

        public int getRequestCount() {
            return requestCount;
        }

        public void setRequestCount(int requestCount) {
            this.requestCount = requestCount;
        }
    }

    // IP封禁信息封装类（新增setter支持更新封禁时间）
    private static class IpBlockInfo {
        private long blockEndTime; // 封禁结束时间（0表示未封禁）
        private int blockCount;    // 累计封禁次数

        public IpBlockInfo(long blockEndTime, int blockCount) {
            this.blockEndTime = blockEndTime;
            this.blockCount = blockCount;
        }

        // getter/setter
        public long getBlockEndTime() {
            return blockEndTime;
        }

        public void setBlockEndTime(long blockEndTime) {
            this.blockEndTime = blockEndTime;
        }

        public int getBlockCount() {
            return blockCount;
        }

        public void setBlockCount(int blockCount) {
            this.blockCount = blockCount;
        }
    }

    // 自定义请求包装器
    static class CustomRequestWrapper extends HttpServletRequestWrapper {
        private final Map<String, String> customParams = new HashMap<>();

        public CustomRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        public void addParameter(String name, String value) {
            customParams.put(name, value);
        }

        @Override
        public String getParameter(String name) {
            if (customParams.containsKey(name)) {
                return customParams.get(name);
            }
            return super.getParameter(name);
        }
    }
}