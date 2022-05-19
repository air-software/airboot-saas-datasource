package com.airboot.common.security.service;

import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.constant.HttpStatus;
import com.airboot.common.core.exception.CustomException;
import com.airboot.common.core.redis.RedisCache;
import com.airboot.common.core.utils.IdUtils;
import com.airboot.common.core.utils.ServletUtils;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.core.utils.ip.IpUtils;
import com.airboot.common.core.utils.security.Md5Utils;
import com.airboot.common.security.LoginUser;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author airboot
 */
@Slf4j
@Service
public class TokenService {
    
    /**
     * 令牌自定义标识
     */
    @Value("${token.header}")
    private String header;
    
    /**
     * 令牌秘钥
     */
    @Value("${token.secret}")
    private String secret;
    
    /**
     * 令牌有效期
     */
    @Value("${token.expireTime}")
    private int expireTime;
    
    @Resource
    private RedisCache redisCache;
    
    protected static final long MILLIS_SECOND = 1000;
    
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    
    /**
     * 过期阈值 - 1小时，用于刷新token
     */
    private static final Long EXPIRE_THRESHOLD = 60 * 60 * 1000L;
    
    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的token
        String token = getToken(request);
        if (StringUtils.isBlank(token)) {
            throw new CustomException("用户未登录", HttpStatus.UNAUTHORIZED);
        }
        try {
            // 解析token获取rediskey
            Claims claims = parseToken(token);
            String uuid = (String) claims.get(Constants.JWT_UUID);
            String userKey = (String) claims.get(Constants.JWT_USER_KEY);
            String redisKey = getRedisKey(userKey, uuid);
            // 从redis中获取已登录的用户信息
            LoginUser user = redisCache.getCacheObject(redisKey);
            if (user == null) {
                throw new Exception("缓存中没有用户信息");
            }
            return user;
        } catch (Exception e) {
            log.error("---解析token或获取用户信息异常, token={}---", token, e);
            throw new CustomException("无法获取用户信息", HttpStatus.UNAUTHORIZED);
        }
    }
    
    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getUuid())) {
            refreshToken(loginUser);
        }
    }
    
    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String userKey, String uuid) {
        // 检测userKey和uuid中不能含有*星号，否则可能被人恶意将登录用户全部踢掉
        if (StringUtils.isNotBlank(userKey)
            && StringUtils.isNotBlank(uuid)
            && StringUtils.containsNone(userKey, "*")
            && StringUtils.containsNone(uuid, "*")) {
            String redisKey = getRedisKey(userKey, uuid);
            redisCache.deleteObject(redisKey);
        }
    }
    
    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        if (StringUtils.isBlank(loginUser.getUuid())) {
            // 如果没有预设uuid，则随机生成一个
            loginUser.setUuid(IdUtils.fastUUID());
        }
        setUserAgent(loginUser);
        refreshToken(loginUser);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.JWT_UUID, loginUser.getUuid());
        claims.put(Constants.JWT_USER_KEY, loginUser.getUserKey());
        return createToken(claims);
    }
    
    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= EXPIRE_THRESHOLD) {
            refreshToken(loginUser);
        }
    }
    
    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 将loginUser缓存
        String redisKey = getRedisKey(loginUser.getUserKey(), loginUser.getUuid());
        redisCache.setCacheObject(redisKey, loginUser, expireTime, TimeUnit.MINUTES);
    }
    
    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        // 由于查询真实地址需要远程调用其他网站，为保障用户登录时响应速度的稳定，不建议在此处同步调用。如确实需要同步调用获取真实地址，打开下方代码的注释即可。
        // loginUser.setLoginLocation(AddressUtils.getRealAddressByIp(ip));
        loginUser.setUserAgent(userAgent);
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }
    
    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }
    
    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
    }
    
    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }
    
    /**
     * 【密钥+userId+设备类型】再Md5
     */
    public String genUserKey(LoginUser loginUser) {
        return Md5Utils.md5Hex(secret + loginUser.getUserId() + loginUser.getDevice().name());
    }
    
    private String getRedisKey(String userKey, String uuid) {
        return Constants.REDIS_LOGIN_KEY + userKey + "," + uuid;
    }
}
