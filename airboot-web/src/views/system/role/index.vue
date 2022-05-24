<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="角色名称" prop="roleName">
        <el-input
          v-model="queryParams.roleName"
          placeholder="请输入角色名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="角色类型" prop="status">
        <el-select
          v-model="queryParams.roleType"
          placeholder="角色类型"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="item in roleTypeOptions"
            :key="item"
            :value="item"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="角色状态"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option value="正常" />
          <el-option value="停用" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:role:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="!ids.length"
          @click="handleDelete"
          v-hasPermi="['system:role:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:role:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :selectable="selectable" />
      <el-table-column label="角色名称" prop="roleName" show-overflow-tooltip />
      <el-table-column label="角色类型" align="center" prop="roleType" width="200" />
      <el-table-column label="显示顺序" prop="roleSort" width="100" />
      <el-table-column label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="正常"
            inactive-value="停用"
            @change="handleStatusChange(scope.row)"
            :disabled="!selectable(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="230">
        <template slot-scope="scope">
          <el-button
            v-if="$isTenantAdmin || scope.row.roleType !== '管理员'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:role:edit']"
          >修改</el-button>
          <el-button
            v-if="$isTenantAdmin || scope.row.roleType !== '管理员'"
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleDataScope(scope.row)"
            v-hasPermi="['system:role:edit']"
          >数据权限</el-button>
          <el-button
            v-if="$isTenantAdmin || scope.row.roleType !== '管理员'"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:role:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.current"
      :limit.sync="queryParams.size"
      @pagination="getPage"
    />

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" :disabled="!$isTenantAdmin && form.roleType === '管理员'" />
        </el-form-item>
        <el-form-item v-if="$isTenantAdmin || form.roleType !== '管理员'" label="角色类型" prop="roleType">
          <el-select v-model="form.roleType" placeholder="请选择角色类型">
            <el-option
              v-for="item in roleTypeOptions"
              :key="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="角色顺序" prop="roleSort">
          <el-input-number v-model="form.roleSort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item v-if="$isTenantAdmin || form.roleType !== '管理员'" label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="正常" />
            <el-radio label="停用" />
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="$isTenantAdmin || form.roleType !== '管理员'" label="菜单权限">
          <el-tree
            :data="menuOptions"
            show-checkbox
            ref="menu"
            node-key="id"
            empty-text="加载中，请稍后"
            :props="defaultProps"
          ></el-tree>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="submitLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 分配角色数据权限对话框 -->
    <el-dialog :title="title" :visible.sync="openDataScope" :close-on-click-modal="false" width="500px" append-to-body>
      <el-form :model="form" label-width="80px">
        <el-form-item label="角色名称">
          <el-input v-model="form.roleName" :disabled="true" />
        </el-form-item>
        <el-form-item label="权限范围">
          <el-select v-model="form.dataScope" :disabled="!$isTenantAdmin && form.roleType === '管理员'">
            <el-option
              v-for="item in dataScopeOptions"
              :key="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数据权限" v-show="form.dataScope === '自定义数据权限'">
          <el-tree
            :data="deptOptions"
            show-checkbox
            default-expand-all
            ref="dept"
            node-key="id"
            empty-text="加载中，请稍后"
            :props="defaultProps"
          ></el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="submitLoading" type="primary" @click="submitDataScope">确 定</el-button>
        <el-button @click="cancelDataScope">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { pageRole, getRole, delRole, addRole, updateRole, exportRole, dataScope, changeRoleStatus } from '@/api/system/role'
import { treeselect as menuTreeselect, roleMenuTreeselect } from '@/api/system/menu'
import { treeselect as deptTreeselect, roleDeptTreeselect } from '@/api/system/dept'

export default {
  name: 'Role',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 总条数
      total: 0,
      // 角色表格数据
      tableData: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层（数据权限）
      openDataScope: false,
      // 日期范围
      dateRange: [],
      // 角色类型选项
      roleTypeOptions: [
        '自定义'
      ],
      // 数据范围选项
      dataScopeOptions: [
        '全部数据权限',
        '自定义数据权限',
        '本部门数据权限',
        '本部门及以下数据权限',
        '仅本人数据权限'
      ],
      // 菜单列表
      menuOptions: [],
      // 部门列表
      deptOptions: [],
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        roleName: undefined,
        roleType: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 表单校验
      rules: {
        roleName: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' }
        ],
        roleType: [
          { required: true, message: '角色类型不能为空', trigger: 'blur' }
        ],
        roleSort: [
          { required: true, message: '角色顺序不能为空', trigger: 'blur' }
        ]
      },
      submitLoading: false
    }
  },
  created() {
    if (this.$isTenantAdmin) this.roleTypeOptions.unshift('管理员')
    this.getPage()
  },
  methods: {
    /** 查询角色列表 */
    getPage() {
      this.loading = true
      pageRole(this.addDateRange(this.queryParams, this.dateRange)).then(
        data => {
          this.tableData = data.records
          this.total = data.total
          this.loading = false
        }
      )
    },
    selectable(row) {
      return this.$isTenantAdmin || row.roleType !== '管理员'
    },
    // 参数系统内置字典翻译
    builtInFormat(row, column) {
      return row.roleType === '自定义' ? '否' : '是'
    },
    /** 查询菜单树结构 */
    getMenuTreeselect() {
      menuTreeselect().then(data => {
        this.menuOptions = data
      })
    },
    /** 查询部门树结构 */
    getDeptTreeselect() {
      deptTreeselect().then(data => {
        this.deptOptions = data
      })
    },
    // 所有菜单节点数据
    getMenuAllCheckedKeys() {
      // 半选中的菜单节点
      let checkedKeys = this.$refs.menu.getHalfCheckedKeys()
      // 目前被选中的菜单节点
      let halfCheckedKeys = this.$refs.menu.getCheckedKeys()
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
      return checkedKeys
    },
    // 所有部门节点数据
    getDeptAllCheckedKeys() {
      /* 如果想要自定义权限时，不包含父节点部门的权限，则使用如下代码 */
      return this.$refs.dept.getCheckedKeys()

      /* 如果想要自定义权限时，包含父节点部门的权限，则使用如下代码 */
      // 半选中的父部门节点
      // let halfCheckedKeys = this.$refs.dept.getHalfCheckedKeys()
      // 选中的部门节点
      // let checkedKeys = this.$refs.dept.getCheckedKeys()
      // checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
      // return checkedKeys
    },
    /** 根据角色ID查询菜单树结构 */
    getRoleMenuTreeselect(id) {
      roleMenuTreeselect(id).then(data => {
        this.menuOptions = data.menus
        this.$refs.menu.setCheckedKeys(data.checkedKeys)
      })
    },
    /** 根据角色ID查询部门树结构 */
    getRoleDeptTreeselect(id) {
      roleDeptTreeselect(id).then(data => {
        this.deptOptions = data.depts
        this.$refs.dept.setCheckedKeys(data.checkedKeys)
      })
    },
    // 角色状态修改
    handleStatusChange(row) {
      let text = row.status === '正常' ? '启用' : '停用'
      this.$confirm('确认要“' + text + '”【' + row.roleName + '】角色吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return changeRoleStatus(row.id, row.status)
      }).then(() => {
        this.msgSuccess(text + '成功')
      }).catch(function() {
        row.status = row.status === '正常' ? '停用' : '正常'
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 取消按钮（数据权限）
    cancelDataScope() {
      this.openDataScope = false
      this.reset()
    },
    // 表单重置
    reset() {
      if (this.$refs.menu !== undefined) {
        this.$refs.menu.setCheckedKeys([])
      }
      this.form = {
        id: undefined,
        roleName: undefined,
        roleSort: 0,
        status: '正常',
        roleType: undefined,
        menuIds: [],
        deptIds: [],
        remark: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.current = 1
      this.getPage()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.getMenuTreeselect()
      this.open = true
      this.title = '添加角色'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      this.$nextTick(() => {
        this.getRoleMenuTreeselect(id)
      })
      getRole(id).then(data => {
        this.$parseFormExtJson(data, 'remark')
        this.form = data
        this.open = true
        this.title = '修改角色'
      })
    },
    /** 分配数据权限操作 */
    handleDataScope(row) {
      this.reset()
      this.$nextTick(() => {
        this.getRoleDeptTreeselect(row.id)
      })
      getRole(row.id).then(data => {
        this.form = data
        this.openDataScope = true
        this.title = '分配数据权限'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.submitLoading = true
          this.$seriFormExtJson(this.form, 'remark')
          if (this.form.id !== undefined) {
            this.form.menuIds = this.getMenuAllCheckedKeys()
            updateRole(this.form).then(data => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getPage()
            }).finally(() => this.submitLoading = false)
          } else {
            this.form.menuIds = this.getMenuAllCheckedKeys()
            addRole(this.form).then(data => {
              this.msgSuccess('新增成功')
              this.open = false
              this.getPage()
            }).finally(() => this.submitLoading = false)
          }
        }
      })
    },
    /** 提交按钮（数据权限） */
    submitDataScope: function() {
      if (this.form.id !== undefined) {
        this.submitLoading = true
        this.form.deptIds = this.getDeptAllCheckedKeys()
        dataScope(this.form).then(data => {
          this.msgSuccess('修改成功')
          this.openDataScope = false
          this.getPage()
        }).finally(() => this.submitLoading = false)
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$confirm('是否确认删除角色？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delRole(ids)
      }).then(() => {
        this.getPage()
        this.msgSuccess('删除成功')
      }).catch(function() {})
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.addDateRange(this.queryParams, this.dateRange)
      this.$confirm('是否确认导出所有角色数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return exportRole(queryParams)
      }).then(data => {
        this.download(data)
      }).catch(function() {})
    }
  }
}
</script>
