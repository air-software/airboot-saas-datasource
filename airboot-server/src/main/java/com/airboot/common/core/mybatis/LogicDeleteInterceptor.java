package com.airboot.common.core.mybatis;

import com.airboot.common.core.utils.DateUtils;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.security.LoginUser;
import com.airboot.common.security.LoginUserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;

/**
 * 逻辑删除拦截器
 * 目的是为手写的XML逻辑删除语句自动插入操作人和操作时间，方便有问题时追责。
 *
 * 之所以不使用Mybatis-Plus自带的【逻辑删除】+【自动填充】功能，是因为目前只有用户、角色等用到了逻辑删除，其余大部分功能都是物理删除。
 * 如果你需要全部改为逻辑删除，可参考Mybatis-Plus的文档进行配置 https://baomidou.com/guide/logic-delete.html
 * 需要注意的是，Mybatis-Plus的逻辑删除功能也只是针对其封装的删除方法，自己写的XML是不生效的，还是得像这样自己写拦截器。
 *
 * @author airboot
 */
@Slf4j
@Component
//拦截StatementHandler类中参数类型为Statement的 prepare 方法
//即拦截 Statement prepare(Connection var1, Integer var2) 方法
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class LogicDeleteInterceptor implements Interceptor {
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        //通过MetaObject优雅访问对象的属性，这里是访问statementHandler的属性;：MetaObject是Mybatis提供的一个用于方便优雅访问对象属性的对象，通过它可以简化代码、不需要try/catch各种reflect异常，同时它支持对JavaBean、Collection、Map三种类型对象的操作。
        MetaObject metaObject = MetaObject
            .forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                new DefaultReflectorFactory());
        //先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        //sql语句类型 select、delete、insert、update
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
   
        BoundSql boundSql = statementHandler.getBoundSql();
        //获取到原始sql语句
        String sql = boundSql.getSql();
        
        // 如果是删除类型，并且sql语句中以update开头，就说明是逻辑删除语句
        if (SqlCommandType.DELETE.equals(sqlCommandType) && StringUtils.startsWithIgnoreCase(sql, "update")) {
            LoginUser loginUser = LoginUserContextHolder.getLoginUser();
            Long userId = loginUser.getUserId();
            String personName = loginUser.getPersonName();
            String account = loginUser.getAccount();
            // 为逻辑删除的数据加上操作人和操作时间
            String tableAlias = "";
            if (sql.toLowerCase().contains(" left join")) {
                // 如果有联表，则需要获取表别名
                String[] tempArray = sql.toLowerCase().split(" left join")[0].split(" ");
                tableAlias = tempArray[tempArray.length - 1] + ".";
            }
            String updaterInfo = personName + "_" + account;
            String replaceSql = ", " + tableAlias + "updater_id = " + userId + ", " + tableAlias + "updater_info = '" + updaterInfo + "', " + tableAlias + "update_time = '" + DateUtils.getTime() + "' where";
            String newSql = StringUtils.replaceIgnoreCase(sql, " where", replaceSql);
    
            //通过反射修改原sql语句
            Field field = boundSql.getClass().getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql, newSql);
        }
        return invocation.proceed();
    }
    
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }
    
    @Override
    public void setProperties(Properties properties) {}
}
