<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="登录地址" prop="ipaddr">
        <el-input
          v-model="queryParams.ipaddr"
          placeholder="请输入登录地址"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录账号" prop="account">
        <el-input
          v-model="queryParams.account"
          placeholder="请输入登录账号"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录结果" prop="loginResult">
        <el-select
          v-model="queryParams.loginResult"
          placeholder="登录结果"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option value="登录成功" />
          <el-option value="登录失败" />
          <el-option value="退出成功" />
        </el-select>
      </el-form-item>
      <el-form-item label="登录时间">
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
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="!ids.length"
          @click="handleDelete"
          v-hasPermi="['monitor:logininfor:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          @click="handleClean"
          v-hasPermi="['monitor:logininfor:remove']"
        >清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['monitor:logininfor:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="访问编号" align="center" prop="id" width="100" />
      <el-table-column label="登录账号" align="center" prop="account" width="270" show-overflow-tooltip />
      <el-table-column label="登录IP" align="center" prop="ipaddr" width="130" show-overflow-tooltip />
      <el-table-column label="登录地点" align="center" prop="loginLocation" width="130" show-overflow-tooltip />
      <el-table-column label="登录设备" align="center" prop="device" show-overflow-tooltip />
      <el-table-column label="浏览器" align="center" prop="browser" />
      <el-table-column label="操作系统" align="center" prop="os" width="100" show-overflow-tooltip />
      <el-table-column label="登录结果" align="center" prop="loginResult" />
      <el-table-column label="提示信息" align="center" prop="msg" width="220" />
      <el-table-column label="登录时间" align="center" prop="loginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
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
  </div>
</template>

<script>
import { pageLogininfor, delLogininfor, cleanLogininfor, exportLogininfor } from '@/api/monitor/logininfor'

export default {
  name: 'Logininfor',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 总条数
      total: 0,
      // 表格数据
      tableData: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        ipaddr: undefined,
        account: undefined,
        loginResult: undefined
      }
    }
  },
  created() {
    this.getPage()
  },
  methods: {
    /** 查询登录日志列表 */
    getPage() {
      this.loading = true
      pageLogininfor(this.addDateRange(this.queryParams, this.dateRange)).then(data => {
          this.tableData = data.records
          this.total = data.total
          this.loading = false
        }
      )
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$confirm('是否确认删除访问编号为【' + ids + '】的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return delLogininfor(ids)
        }).then(() => {
          this.getPage()
          this.msgSuccess('删除成功')
        }).catch(function() {})
    },
    /** 清空按钮操作 */
    handleClean() {
        this.$confirm('是否确认清空所有登录日志数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return cleanLogininfor()
        }).then(() => {
          this.getPage()
          this.msgSuccess('清空成功')
        }).catch(function() {})
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.addDateRange(this.queryParams, this.dateRange)
      this.$confirm('是否确认导出所有操作日志数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return exportLogininfor(queryParams)
        }).then(data => {
          this.download(data)
        }).catch(function() {})
    }
  }
}
</script>

