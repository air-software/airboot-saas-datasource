<template>
  <div class="app-container">
    <el-form :model="queryParams" class="query-form" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="租户名称" prop="tenantName">
        <el-input
          v-model="queryParams.tenantName"
          placeholder="请输入租户名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="负责人姓名" prop="personName" label-width="85px">
        <el-input
          v-model="queryParams.personName"
          placeholder="请输入负责人姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="负责人手机号码" prop="mobile" label-width="110px">
        <el-input
          v-model="queryParams.mobile"
          placeholder="请输入负责人手机号码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="租户类型" prop="tenantType">
        <el-select v-model="queryParams.tenantType" placeholder="请选择租户类型" clearable size="small">
          <el-option value="管理平台" />
          <el-option value="普通商家" />
        </el-select>
      </el-form-item>
      <el-form-item label="租户状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择租户状态" clearable size="small">
          <el-option value="正常" />
          <el-option value="停用" />
        </el-select>
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
          v-hasPermi="['system:tenant:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="!ids.length"
          @click="handleDelete"
          v-hasPermi="['system:tenant:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:tenant:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-lollipop"
          size="mini"
          @click="handleSql"
          v-hasPermi="['system:tenant:exesql']"
        >执行SQL</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="租户id" align="center" prop="id" />
      <el-table-column label="租户名称" align="center" prop="tenantName" />
      <el-table-column label="数据源" align="center" prop="dataSourceId" :formatter="rowFormat" />
      <el-table-column label="Schema" align="center" prop="schemaName" width="200" />
      <el-table-column label="负责人姓名" align="center" prop="personName" />
      <el-table-column label="负责人手机号码" align="center" prop="mobile" />
      <el-table-column label="租户类型" align="center" prop="tenantType" />
      <el-table-column label="租户状态" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:tenant:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tenant:remove']"
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

    <!-- 添加或修改租户对话框 -->
    <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="租户名称" prop="tenantName">
          <el-input v-model="form.tenantName" placeholder="请输入租户名称" />
        </el-form-item>
        <el-form-item label="数据源" prop="dataSourceId">
          <el-select v-model="form.dataSourceId" placeholder="请选择数据源" filterable>
            <el-option
              v-for="(item, index) in dataSourceList"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Schema" prop="schemaName">
          <el-input v-model="form.schemaName" placeholder="非必填，如不填系统会自动生成" />
        </el-form-item>
        <el-form-item label="负责人姓名" prop="personName">
          <el-input v-model="form.personName" placeholder="请输入负责人姓名" />
        </el-form-item>
        <el-form-item label="手机号码" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入负责人手机号码" />
        </el-form-item>
        <el-form-item label="电子邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入负责人邮箱" />
        </el-form-item>
        <el-form-item label="租户类型">
          <el-select v-model="form.tenantType" placeholder="请选择租户类型">
            <el-option value="普通商家" />
          </el-select>
        </el-form-item>
        <el-form-item label="租户状态">
          <el-radio-group v-model="form.status">
            <el-radio label="正常" />
            <el-radio label="停用" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="submitLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 执行SQL对话框 -->
    <el-dialog title="执行SQL" :visible.sync="sqlOpen" :close-on-click-modal="false" width="500px" append-to-body>
      <el-form ref="sqlForm" :model="sqlForm" :rules="sqlRules" label-width="100px">
        <el-form-item label="数据源" prop="dsKey">
          <el-select v-model="sqlForm.dsKey" placeholder="请选择数据源" filterable>
            <el-option label="全部（不含common）" value="all"></el-option>
            <el-option label="common" value="common"></el-option>
            <el-option
              v-for="(item, index) in tenantList"
              :key="index"
              :label="item.tenantName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="密钥" prop="secret">
          <el-input v-model="sqlForm.secret" placeholder="默认airboot，正式使用时请改为你自己设定的密钥。" />
        </el-form-item>
        <el-row>
          <el-col :span="24">
            <el-form-item label="SQL" prop="sqlStr">
              <el-input v-model="sqlForm.sqlStr" type="textarea" placeholder="请输入SQL语句，支持多行批量。为安全起见，请勿一次性执行过多SQL语句。" maxlength="5000" rows="10"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="submitLoading" type="primary" @click="submitSqlForm">确 定</el-button>
        <el-button @click="cancelSqlForm">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { pageTenant, listTenant, getTenant, delTenant, addTenant, updateTenant, exportTenant, executeSql } from '@/api/system/tenant'
import { listDataSource } from '@/api/system/datasource'

export default {
  name: 'Tenant',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 总条数
      total: 0,
      // 数据源列表
      dataSourceList: [],
      dataSourceMap: {},
      // 租户表格数据
      tableData: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        tenantName: undefined,
        personName: undefined,
        mobile: undefined,
        status: undefined,
        tenantType: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        tenantName: [
          { required: true, message: '租户名称不能为空', trigger: 'blur' }
        ],
        dataSourceId: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ],
        personName: [
          { required: true, message: '负责人姓名不能为空', trigger: 'blur' }
        ],
        mobile: [
          { required: true, message: '负责人手机号码不能为空', trigger: 'blur' }
        ]
      },
      submitLoading: false,
      tenantList: [],
      sqlOpen: false,
      sqlForm: {},
      sqlRules: {
        dsKey: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ],
        secret: [
          { required: true, message: '密钥不能为空', trigger: 'blur' }
        ],
        sqlStr: [
          { required: true, message: 'SQL不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  async created() {
    await this.getDataSourceList()
    this.getPage()
  },
  methods: {
    /** 查询数据源列表 */
    getDataSourceList() {
      return listDataSource().then(data => {
        this.dataSourceList = data
        this.dataSourceMap = this.$arrayToObject(data, 'id')
      })
    },
    /** 查询租户列表 */
    getPage() {
      this.loading = true
      pageTenant(this.queryParams).then(data => {
        this.tableData = data.records
        this.total = data.total
        this.loading = false
      })
    },
    rowFormat(row, column, cellValue) {
      return this.dataSourceMap[cellValue].name
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.current = 1
      this.getPage()
    },
    /** 重置按钮操作 */
    resetQuery() {
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
      this.open = true
      this.title = '添加租户'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getTenant(id).then(data => {
        this.$parseFormExtJson(data, 'remark')
        this.form = data
        this.open = true
        this.title = '修改租户'
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        tenantName: undefined,
        dataSourceId: undefined,
        schemaName: undefined,
        personName: undefined,
        mobile: undefined,
        email: undefined,
        tenantType: '普通商家',
        status: '正常',
        remark: undefined,
      }
      this.resetForm('form')
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.submitLoading = true
          this.$seriFormExtJson(this.form, 'remark')
          if (this.form.id !== undefined) {
            updateTenant(this.form).then(data => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getPage()
            }).finally(() => this.submitLoading = false)
          } else {
            addTenant(this.form).then(data => {
              this.msgSuccess('新增成功')
              this.open = false
              this.getPage()
            }).finally(() => this.submitLoading = false)
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$confirm('是否确认删除租户编号为【' + ids + '】的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delTenant(ids)
      }).then(() => {
        this.getPage()
        this.msgSuccess('删除成功')
      }).catch(function() {})
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('是否确认导出所有租户数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return exportTenant(queryParams)
      }).then(data => {
        this.download(data)
      }).catch(function() {})
    },
    /** 执行SQL */
    handleSql() {
      listTenant().then(data => {
        this.tenantList = data
        this.sqlOpen = true
      })
    },
    cancelSqlForm() {
      this.sqlOpen = false
      this.resetSqlForm()
    },
    resetSqlForm() {
      this.sqlForm = {
        dsKey: undefined,
        secret: undefined,
        sqlStr: undefined
      }
      this.resetForm('sqlForm')
    },
    submitSqlForm() {
      this.$refs['sqlForm'].validate(valid => {
        if (valid) {
          this.submitLoading = true
          executeSql(this.sqlForm).then(data => {
            this.$alert(`执行完毕，影响行数：${data}`, '执行结果')
            this.cancelSqlForm()
          }).finally(() => this.submitLoading = false)
        }
      })
    }
  }
}
</script>
