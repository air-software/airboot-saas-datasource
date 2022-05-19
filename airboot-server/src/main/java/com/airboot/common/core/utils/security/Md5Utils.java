package com.airboot.common.core.utils.security;

import com.airboot.common.core.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.util.Random;

/**
 * Md5加密方法
 *
 * @author airboot
 */
@Slf4j
public class Md5Utils {
    
    /**
     * 生成含有随机盐的密码
     * 推荐采用此方法生成密码，避免彩虹表
     * 但如果非要使用固定MD5生成密码，可使用下方{@link #md5Hex(String)}方法
     */
    public static String encryptPassword(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }
    
    /**
     * 校验随机盐密码是否正确
     */
    public static boolean verifyPassword(String password, String md5) {
        if (null == password || "".equals(password) || null == md5 || "".equals(md5)) {
            return false;
        }
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }
    
    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            log.error("---MD5异常, 字符串={}---", src, e);
            throw new CustomException("MD5异常", e);
        }
    }
    
    /**
     * 可在这里运行main方法测试随机盐密码是否生效
     */
    public static void main(String[] args) throws Exception {
        System.out.println(md5Hex("123456"));
        System.out.println(md5Hex("123456"));
        String password = encryptPassword("123456");
        String password1 = encryptPassword("123456");
        System.out.println(password);
        System.out.println(password1);
        System.out.println(verifyPassword("123456", password));
        System.out.println(verifyPassword("123456", password1));
    }
    
}
