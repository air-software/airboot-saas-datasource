package com.airboot.common.core.utils.ip;

import com.airboot.common.core.config.ProjectConfig;
import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.core.utils.http.HttpUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取地址类
 *
 * @author airboot
 */
@Slf4j
public class AddressUtils {
    
    // IP地址查询URL
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
    
    /**
     * 根据IP远程查询真实地址
     */
    public static String getRealAddressByIp(String ip) {
        // 如果未开启查询
        if (!ProjectConfig.isAddressEnabled()) {
            return "";
        }
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网地址";
        }
        try {
            String jsonStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
            if (StringUtils.isBlank(jsonStr)) {
                log.error("返回地址结果为空，ip：{}", ip);
                return "未知地址";
            }
            JSONObject obj = JSONObject.parseObject(jsonStr);
            String province = obj.getString("pro");
            String city = obj.getString("city");
            if (StringUtils.isBlank(province) && StringUtils.isBlank(city)) {
                log.error("返回地址结果中province和city为空，ip：{}，jsonStr：{}", ip, jsonStr);
                return "未知地址";
            }
            return String.format("%s %s", province, city);
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", ip);
            return "无法获取地址";
        }
    }
}
