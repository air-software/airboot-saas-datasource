package com.airboot.common.core.aspectj;

import com.airboot.common.core.aspectj.lang.annotation.DataScope;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.model.vo.BaseSearchVO;
import com.airboot.common.security.LoginUserContextHolder;
import com.airboot.project.system.model.entity.SysRole;
import com.airboot.project.system.model.entity.SysUser;
import com.airboot.project.system.model.enums.DataScopeEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据权限过滤处理
 *
 * @author airboot
 */
@Aspect
@Component
public class DataScopeAspect {
    
    // 配置织入点
    @Pointcut("@annotation(com.airboot.common.core.aspectj.lang.annotation.DataScope)")
    public void dataScopePointCut() {
    }
    
    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        handleDataScope(point);
    }
    
    protected void handleDataScope(final JoinPoint joinPoint) {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null) {
            return;
        }
        // 获取当前的用户
        SysUser currentUser = LoginUserContextHolder.getLoginUser().getUser();
        if (currentUser != null) {
            // 如果是超级租户管理员，则不过滤数据
            if (!currentUser.isTenantAdmin()) {
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias());
            }
        }
    }
    
    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user      用户
     * @param deptAlias     部门表别名
     * @param userAlias     用户表别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias, String userAlias) {
        StringBuilder sqlString = new StringBuilder();
        
        // 多个角色采取并集的方式，因此拼接时先用OR，最后再把第一个OR替换为AND
        for (SysRole role : user.getRoles()) {
            DataScopeEnum dataScope = role.getDataScope();
            if (DataScopeEnum.全部数据权限.equals(dataScope)) {
                sqlString = new StringBuilder();
                break;
            } else if (DataScopeEnum.自定义数据权限.equals(dataScope)) {
                sqlString.append(StringUtils.format(
                        " OR {}.id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) " , deptAlias,
                        role.getId()));
            } else if (DataScopeEnum.本部门数据权限.equals(dataScope)) {
                sqlString.append(StringUtils.format(" OR {}.id = {} " , deptAlias, user.getDeptId()));
            } else if (DataScopeEnum.本部门及以下数据权限.equals(dataScope)) {
                sqlString.append(StringUtils.format(
                        " OR {}.id IN ( SELECT id FROM sys_dept WHERE id = {} or find_in_set( {} , ancestors ) )" ,
                        deptAlias, user.getDeptId(), user.getDeptId()));
            } else if (DataScopeEnum.仅本人数据权限.equals(dataScope)) {
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(StringUtils.format(" OR {}.id = {} " , userAlias, user.getId()));
                } else {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(" OR 1=0 ");
                }
            }
        }
        
        if (StringUtils.isNotBlank(sqlString.toString())) {
            BaseSearchVO baseSearchVO = (BaseSearchVO) joinPoint.getArgs()[0];
            baseSearchVO.setDataScopeSql(" AND (" + sqlString.substring(4) + ")");
        }
    }
    
    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        
        if (method != null) {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
