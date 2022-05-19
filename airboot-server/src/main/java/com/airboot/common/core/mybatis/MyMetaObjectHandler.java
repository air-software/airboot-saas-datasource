package com.airboot.common.core.mybatis;

import com.airboot.common.security.LoginUser;
import com.airboot.common.security.LoginUserContextHolder;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * Mybatis-Plus自动填充处理器
 *
 * @author airboot
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        // 如果能获取到登录用户，则设置创建者/更新者信息，否则（如定时任务场景）按手动设置处理，如手动未设置，则设为0和“系统”。
        if (loginUser != null) {
            this.setFieldValByName("creatorId", loginUser.getUserId(), metaObject);
            this.setFieldValByName("creatorInfo", loginUser.getPersonName() + "_" + loginUser.getAccount(), metaObject);
            
            this.setFieldValByName("updaterId", loginUser.getUserId(), metaObject);
            this.setFieldValByName("updaterInfo", loginUser.getPersonName() + "_" + loginUser.getAccount(), metaObject);
        } else {
            Long creatorId = Optional.ofNullable((Long) metaObject.getValue("creatorId")).orElse(0L);
            String creatorInfo = Optional.ofNullable((String) metaObject.getValue("creatorInfo")).orElse("系统");
            this.setFieldValByName("creatorId", creatorId, metaObject);
            this.setFieldValByName("creatorInfo", creatorInfo, metaObject);
    
            Long updaterId = Optional.ofNullable((Long) metaObject.getValue("updaterId")).orElse(0L);
            String updaterInfo = Optional.ofNullable((String) metaObject.getValue("updaterInfo")).orElse("系统");
            this.setFieldValByName("updaterId", updaterId, metaObject);
            this.setFieldValByName("updaterInfo", updaterInfo, metaObject);
        }
    }
    
    @Override
    public void updateFill(MetaObject metaObject) {
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        if (loginUser != null) {
            this.setFieldValByName("updaterId", loginUser.getUserId(), metaObject);
            this.setFieldValByName("updaterInfo", loginUser.getPersonName() + "_" + loginUser.getAccount(), metaObject);
            this.setFieldValByName("updateTime", new Date(), metaObject);
        } else {
            Long updaterId = Optional.ofNullable((Long) metaObject.getValue("updaterId")).orElse(0L);
            String updaterInfo = Optional.ofNullable((String) metaObject.getValue("updaterInfo")).orElse("系统");
            Date updateTime = Optional.ofNullable((Date) metaObject.getValue("updateTime")).orElse(new Date());
            this.setFieldValByName("updaterId", updaterId, metaObject);
            this.setFieldValByName("updaterInfo", updaterInfo, metaObject);
            this.setFieldValByName("updateTime", updateTime, metaObject);
        }
    }
}
