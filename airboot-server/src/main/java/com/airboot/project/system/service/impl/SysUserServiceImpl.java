package com.airboot.project.system.service.impl;

import com.airboot.common.core.aspectj.lang.annotation.DataScope;
import com.airboot.common.core.exception.CustomException;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.core.utils.security.Md5Utils;
import com.airboot.common.model.enums.StatusEnum;
import com.airboot.common.security.LoginUser;
import com.airboot.common.security.LoginUserContextHolder;
import com.airboot.project.system.mapper.SysPostMapper;
import com.airboot.project.system.mapper.SysRoleMapper;
import com.airboot.project.system.mapper.SysUserMapper;
import com.airboot.project.system.mapper.relation.SysUserPostMapper;
import com.airboot.project.system.mapper.relation.SysUserRoleMapper;
import com.airboot.project.system.model.entity.SysPost;
import com.airboot.project.system.model.entity.SysRole;
import com.airboot.project.system.model.entity.SysUser;
import com.airboot.project.system.model.entity.relation.SysUserPost;
import com.airboot.project.system.model.entity.relation.SysUserRole;
import com.airboot.project.system.model.vo.SearchSysUserVO;
import com.airboot.project.system.service.ISysConfigService;
import com.airboot.project.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户 业务层处理
 *
 * @author airboot
 */
@Slf4j
@Service
public class SysUserServiceImpl implements ISysUserService {
    
    @Resource
    private SysUserMapper userMapper;
    
    @Resource
    private SysRoleMapper roleMapper;
    
    @Resource
    private SysPostMapper postMapper;
    
    @Resource
    private SysUserRoleMapper userRoleMapper;
    
    @Resource
    private SysUserPostMapper userPostMapper;
    
    @Resource
    private ISysConfigService configService;
    
    /**
     * 查询用户分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public IPage<SysUser> getPage(SearchSysUserVO search) {
        return userMapper.findPage(search);
    }
    
    /**
     * 查询用户列表
     *
     * @param search 查询条件
     * @return 用户列表
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> getList(SearchSysUserVO search) {
        return userMapper.findList(search);
    }
    
    /**
     * 通过登录账号查询用户
     *
     * @param account 用户登录账号
     * @return 用户对象信息
     */
    @Override
    public SysUser getByAccount(String account) {
        return userMapper.findByAccount(account);
    }
    
    /**
     * 查询所有正常用户
     */
    @Override
    public List<SysUser> getAllNormalList() {
        return userMapper.selectList(new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getStatus, StatusEnum.正常)
            .eq(SysUser::getDeleted, false));
    }
    
    /**
     * 查询正常用户
     *
     * @param id
     */
    @Override
    public SysUser getNormalOne(Long id) {
        return userMapper.findNormalById(id);
    }
    
    /**
     * 根据手机号查询正常用户
     *
     * @param mobile
     */
    @Override
    public SysUser getNormalOneByMobile(String mobile) {
        return userMapper.findNormalByMobile(mobile);
    }
    
    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser getById(Long userId) {
        return userMapper.findById(userId);
    }
    
    /**
     * 通过用户ID查询用户，并携带密码
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser getByIdWithPwd(Long userId) {
        return userMapper.findById(userId);
    }
    
    /**
     * 查询用户所属角色组
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String getUserRoleGroup(Long userId) {
        List<SysRole> list = roleMapper.findListByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append("、");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }
    
    /**
     * 查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String getUserPostGroup(Long userId) {
        List<SysPost> list = postMapper.findListByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list) {
            idsStr.append(post.getPostName()).append("、");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }
    
    /**
     * 校验用户
     *
     * @param user 用户信息
     * @param isUpdate 是否更新
     */
    @Override
    public void validateUser(SysUser user, boolean isUpdate) {
        String operType = isUpdate ? "修改" : "新增";
        if (StringUtils.isNotBlank(user.getUsername()) && !this.checkUsernameUnique(user)) {
            throw new CustomException(operType + "用户失败，用户名【" + user.getUsername() + "】已存在");
        } else if (!this.checkMobileUnique(user)) {
            throw new CustomException(operType + "用户失败，手机号码【" + user.getMobile() + "】已存在");
        } else if (StringUtils.isNotBlank(user.getEmail()) && !this.checkEmailUnique(user)) {
            throw new CustomException(operType + "用户失败，邮箱【" + user.getEmail() + "】已存在");
        }
        
        // 正则校验
        if (StringUtils.isNotBlank(user.getUsername()) && !StringUtils.isUsername(user.getUsername())) {
            throw new CustomException(operType + "用户失败，用户名【" + user.getUsername() + "】不符合格式规范");
        } else if (!StringUtils.isMobile(user.getMobile())) {
            throw new CustomException(operType + "用户失败，手机号码【" + user.getMobile() + "】不符合格式规范");
        } else if (StringUtils.isNotBlank(user.getEmail()) && !StringUtils.isEmail(user.getEmail())) {
            throw new CustomException(operType + "用户失败，邮箱【" + user.getEmail() + "】不符合格式规范");
        }
    }
    
    /**
     * 校验用户名是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUsernameUnique(SysUser user) {
        if (StringUtils.isBlank(user.getUsername())) {
            return false;
        }
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = userMapper.checkUsernameUnique(user.getUsername());
        return info == null || info.getId().equals(userId);
    }
    
    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkMobileUnique(SysUser user) {
        if (StringUtils.isBlank(user.getMobile())) {
            return false;
        }
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = userMapper.checkMobileUnique(user.getMobile());
        return info == null || info.getId().equals(userId);
    }
    
    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUser user) {
        if (StringUtils.isBlank(user.getEmail())) {
            return false;
        }
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        return info == null || info.getId().equals(userId);
    }
    
    /**
     * 校验用户是否允许操作
     *
     * @param user 要操作的用户对象
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (user.getId() == null) {
            throw new CustomException("未找到要操作的用户");
        }
        // 如果操作人本身是超级租户管理员，则可以操作所有租户的所有用户
        // 如果操作人是租户管理员，则可以操作租户内的所有用户
        // 如果操作人是其他角色，则可以操作除管理员外的所有用户
        SysUser loginUser = LoginUserContextHolder.getLoginUser().getUser();
        // 从数据库里查出要操作的带角色信息的用户数据
        SysUser editUser = this.getById(user.getId());
        
        // 如果不是超级租户管理员，却要操作超级租户管理员，则返回异常
        if (!loginUser.isTenantAdmin() && editUser.isTenantAdmin()) {
            throw new CustomException("不允许操作超级租户管理员用户");
        }
        // 如果不是管理员，却要操作管理员，则返回异常
        if (!loginUser.isAdmin() && editUser.isAdmin()) {
            throw new CustomException("不允许操作管理员，如需修改请联系贵司管理员");
        }
    }
    
    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int save(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insert(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }
    
    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public void update(SysUser user) {
        Long userId = user.getId();
        // 删除用户与角色关联
        userRoleMapper.deleteByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        userMapper.saveOrUpdate(user);
    }
    
    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public void updateUserProfile(SysUser user) {
        userMapper.saveOrUpdate(user);
    }
    
    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<>();
            for (Long roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchInsert(list);
            }
        }
    }
    
    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<>();
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchInsert(list);
            }
        }
    }
    
    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteByUserId(userId);
        return userMapper.deleteById(userId);
    }
    
    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteByIds(Long[] userIds) {
        for (Long userId : userIds) {
            if (userId == null) {
                throw new CustomException("未找到要操作的用户");
            }
            LoginUser loginUser = LoginUserContextHolder.getLoginUser();
            if (loginUser.getUserId().equals(userId)) {
                throw new CustomException("不允许自己删除自己");
            }
            SysUser existUser = this.getById(userId);
            if (existUser.isAdmin()) {
                throw new CustomException("不允许删除管理员用户");
            }
        }
        return userMapper.deleteUserByIds(userIds);
    }
    
    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param updateSupport   是否支持更新，如果已存在，则进行更新数据
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, boolean updateSupport) {
        if (CollectionUtils.isEmpty(userList)) {
            throw new CustomException("导入失败！导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.getByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                checkRequiredUserField(user);
                
                if (updateSupport) {
                    // 查找是否已存在这个用户（以手机号为依据）
                    SysUser existUser = userMapper.getOne(new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getMobile, user.getMobile())
                        .eq(SysUser::getDeleted, false), false);
                    
                    if (existUser != null) {
                        user.setId(existUser.getId());
                    } else {
                        user.setPassword(Md5Utils.encryptPassword(password));
                    }
                } else {
                    user.setPassword(Md5Utils.encryptPassword(password));
                }
                
                // 校验用户
                validateUser(user, updateSupport);
                
                // 新增/更新用户
                userMapper.saveOrUpdate(user);
                successNum++;
                String operType = updateSupport ? "更新" : "导入";
                successMsg.append("<br/>" + successNum + "、账号 " + user.getMobile() + "——" + operType + "成功");
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getMobile() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "存在导入失败的数据！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
    
    /**
     * 检查必填项
     * @param user
     */
    private void checkRequiredUserField(SysUser user) {
        Assert.notNull(user.getDeptId(), "部门编号不能为空");
        Assert.hasText(user.getUsername(), "用户名不能为空");
        Assert.hasText(user.getPersonName(), "姓名不能为空");
        Assert.hasText(user.getMobile(), "手机号码不能位空");
        Assert.notNull(user.getGender(), "性别不能为空");
    }
    
}
