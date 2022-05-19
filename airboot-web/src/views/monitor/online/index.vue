<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="登录地址" prop="ipaddr">
        <el-input
          v-model="queryParams.ipaddr"
          placeholder="请输入登录地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录账号" prop="account">
        <el-input
          v-model="queryParams.account"
          placeholder="请输入登录账号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="tableData.slice((current-1) * size, current * size)"
      class="online-table"
      style="width: 100%;"
    >
      <el-table-column label="序号" type="index" align="center">
        <template slot-scope="scope">
          <span>{{(current - 1) * size + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="会话编号" align="center" prop="uuid" show-overflow-tooltip />
      <el-table-column label="登录账号" align="center" prop="account" width="110" show-overflow-tooltip />
      <el-table-column label="部门名称" align="center" prop="deptName" />
      <el-table-column label="登录地点" align="center" prop="ipaddr" width="240" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-button
            title="点击IP查询真实地址"
            :loading="scope.row.loginLocation === '获取中'"
            type="text"
            @click="getIpLocation(scope.row)"
          >{{ scope.row.ipaddr }}</el-button>
          <span v-if="scope.row.loginLocation && scope.row.loginLocation !== '获取中'">/ {{ scope.row.loginLocation }}</span>
        </template>
      </el-table-column>
      <el-table-column label="登录设备" align="center" prop="device" show-overflow-tooltip />
      <el-table-column label="浏览器" align="center" prop="browser" />
      <el-table-column label="操作系统" align="center" prop="os" />
      <el-table-column label="登录时间" align="center" prop="loginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleForceLogout(scope.row)"
            v-hasPermi="['monitor:online:forceLogout']"
          >强退</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="current" :limit.sync="size" />
  </div>
</template>

<script>
import { pageOnline, forceLogout } from '@/api/monitor/online'
import { getIpLocation } from '@/api/common'

export default {
  name: 'Online',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      tableData: [],
      current: 1,
      size: 10,
      // 查询参数
      queryParams: {
        ipaddr: undefined,
        account: undefined
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
      pageOnline(this.queryParams).then(data => {
        this.tableData = data.records
        this.total = data.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.current = 1
      this.getPage()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 强退按钮操作 */
    handleForceLogout(row) {
      this.$confirm('是否确认强退账号为【' + row.account + '】的用户?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return forceLogout(row.userKey, row.uuid)
        }).then(() => {
          this.getPage()
          this.msgSuccess('强退成功')
        }).catch(function() {})
    },
    /** 获取IP真实地址 **/
    getIpLocation(row) {
      if (row.loginLocation) return
      row.loginLocation = '获取中'
      setTimeout(() => {
        getIpLocation(row.ipaddr).then(data => {
          row.loginLocation = data || this.msgInfo('未开启地址查询功能')
        })
      }, 500)
    }
  }
}
</script>

<style scoped>
.online-table .el-button.is-loading:before {
  background-color: transparent;
}
</style>
