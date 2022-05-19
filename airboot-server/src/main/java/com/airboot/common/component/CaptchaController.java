package com.airboot.common.component;

import com.airboot.common.core.config.ProjectConfig;
import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.redis.RedisCache;
import com.airboot.common.core.utils.IdUtils;
import com.airboot.common.core.utils.VerifyCodeUtils;
import com.airboot.common.core.utils.sign.Base64;
import com.airboot.common.model.vo.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 验证码操作处理
 *
 * @author airboot
 */
@RestController
public class CaptchaController {
    
    @Resource
    private ProjectConfig projectConfig;
    
    @Resource
    private RedisCache redisCache;
    
    /**
     * 是否开启验证码
     */
    @GetMapping("/needCaptcha")
    public AjaxResult needCaptcha() {
        return AjaxResult.success(projectConfig.isCaptchaEnabled());
    }
    
    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException {
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        // 唯一标识
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.REDIS_CAPTCHA_CODE_KEY + uuid;
        
        redisCache.setCacheObject(verifyKey, verifyCode, Constants.CAPTCHA_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        // 生成图片
        int w = 111, h = 36;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(w, h, stream, verifyCode);
        try {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("uuid" , uuid);
            dataMap.put("img" , Base64.encode(stream.toByteArray()));
            return AjaxResult.success(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        } finally {
            stream.close();
        }
    }
}
