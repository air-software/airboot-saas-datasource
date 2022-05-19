package com.airboot.project.system.service.impl;

import com.airboot.common.core.aspectj.lang.annotation.DataScope;
import com.airboot.common.core.exception.CustomException;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.model.entity.TreeSelect;
import com.airboot.common.model.enums.StatusEnum;
import com.airboot.project.system.mapper.SysDeptMapper;
import com.airboot.project.system.model.entity.SysDept;
import com.airboot.project.system.model.vo.SearchSysDeptVO;
import com.airboot.project.system.service.ISysDeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 *
 * @author airboot
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {
    
    @Resource
    private SysDeptMapper deptMapper;
    
    /**
     * 查询部门管理数据
     *
     * @param search 查询条件
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> getList(SearchSysDeptVO search) {
        return deptMapper.findList(search);
    }
    
    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<>();
        for (SysDept dept : depts) {
            tempList.add(dept.getId());
        }
        for (SysDept dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }
    
    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }
    
    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> getListByRoleId(Long roleId) {
        return deptMapper.findIdListByRoleId(roleId);
    }
    
    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept getById(Long deptId) {
        return deptMapper.selectById(deptId);
    }
    
    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int getNormalChildrenDeptById(Long deptId) {
        return deptMapper.findNormalChildrenDeptById(deptId);
    }
    
    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        return deptMapper.hasChildByDeptId(deptId) > 0;
    }
    
    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        return deptMapper.checkDeptExistUser(deptId) > 0;
    }
    
    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean checkDeptNameUnique(SysDept dept) {
        if (StringUtils.isBlank(dept.getDeptName())) {
            return false;
        }
        Long deptId = StringUtils.isNull(dept.getId()) ? -1L : dept.getId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        return StringUtils.isNull(info) || info.getId().equals(deptId);
    }
    
    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int save(SysDept dept) {
        // 如果是新增根部门
        if (dept.getParentId() == 0) {
            dept.setAncestors("0");
        } else {
            SysDept info = deptMapper.selectById(dept.getParentId());
            // 如果父节点不为正常状态,则不允许新增子节点
            if (!StatusEnum.正常.equals(info.getStatus())) {
                throw new CustomException("部门停用，不允许新增");
            }
            dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        }
        
        return deptMapper.insert(dept);
    }
    
    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int update(SysDept dept) {
        SysDept newParentDept = deptMapper.selectById(dept.getParentId());
        SysDept oldDept = deptMapper.selectById(dept.getId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateById(dept);
        if (StatusEnum.正常.equals(dept.getStatus())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatus(dept);
        }
        return result;
    }
    
    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatus(SysDept dept) {
        Long updaterId = dept.getUpdaterId();
        String updaterInfo = dept.getUpdaterInfo();
        Date updateTime = dept.getUpdateTime();
        dept = deptMapper.selectById(dept.getId());
        dept.setUpdaterId(updaterId);
        dept.setUpdaterInfo(updaterInfo);
        dept.setUpdateTime(updateTime);
        deptMapper.updateDeptStatus(dept);
    }
    
    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.findChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            deptMapper.updateDeptChildren(children);
        }
    }
    
    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteById(Long deptId) {
        return deptMapper.deleteDeptById(deptId);
    }
    
    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                for (SysDept n : childList) {
                    recursionFn(list, n);
                }
            }
        }
    }
    
    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<>();
        for (SysDept n : list) {
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }
    
    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0;
    }
}
