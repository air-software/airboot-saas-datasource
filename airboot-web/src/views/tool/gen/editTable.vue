<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane label="基本信息" name="basic">
        <basic-info-form ref="basicInfo" :info="info" />
      </el-tab-pane>
      <el-tab-pane label="字段信息" name="cloum">
        <el-table
          ref="dragTable"
          :data="columns"
          row-key="id"
          :max-height="tableHeight">
          <el-table-column
            fixed
            label="序号"
            type="index"
            min-width="50"
            class-name="allowDrag"
          />
          <el-table-column
            fixed
            label="字段列名"
            prop="columnName"
            min-width="100"
            class-name="allowDrag"
            show-overflow-tooltip
          />
          <el-table-column label="字段描述" min-width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.columnComment"></el-input>
            </template>
          </el-table-column>
          <el-table-column
            label="物理类型"
            prop="columnType"
            min-width="100"
            show-overflow-tooltip
          />
          <el-table-column label="Java类型" min-width="110">
            <template slot-scope="scope">
              <el-select v-model="scope.row.javaType">
                <el-option label="Long" value="Long" />
                <el-option label="String" value="String" />
                <el-option label="Integer" value="Integer" />
                <el-option label="Double" value="Double" />
                <el-option label="BigDecimal" value="BigDecimal" />
                <el-option label="Date" value="Date" />
                <el-option label="Enum" value="Enum" />
                <el-option label="Boolean" value="Boolean" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="java属性" min-width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.javaField"></el-input>
            </template>
          </el-table-column>

          <el-table-column label="编辑" min-width="50">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.edit"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="列表" min-width="50">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.list"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="查询" min-width="50">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.query"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="查询方式" min-width="100">
            <template slot-scope="scope">
              <el-select v-model="scope.row.queryType">
                <el-option label="=" value="等于" />
                <el-option label="!=" value="不等于" />
                <el-option label=">" value="大于" />
                <el-option label=">=" value="大于等于" />
                <el-option label="<" value="小于" />
                <el-option label="<=" value="小于等于" />
                <el-option label="LIKE" value="模糊" />
                <el-option label="BETWEEN" value="范围" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="必填" min-width="50">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.required"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="导出" min-width="50">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.excelExport"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="导入" min-width="50">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.excelImport"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="显示类型" min-width="130">
            <template slot-scope="scope">
              <el-select v-model="scope.row.htmlType">
                <el-option value="文本框" />
                <el-option value="文本域" />
                <el-option value="下拉框" />
                <el-option value="单选框" />
                <!--<el-option value="复选框" />-->
                <el-option value="日期控件" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="枚举类全限定名" min-width="500">
            <template slot-scope="scope">
              <el-input v-model="scope.row.enumFullName" :disabled="scope.row.javaType !== 'Enum'"></el-input>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="生成信息" name="genInfo">
        <gen-info-form ref="genInfo" :info="info" :menus="menus" />
      </el-tab-pane>
    </el-tabs>
    <el-form label-width="100px">
      <el-form-item style="text-align: center;margin-left:-100px;margin-top:10px;">
        <el-button :loading="submitLoading" type="primary" @click="submitForm()">提交</el-button>
        <el-button @click="close()">返回</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import { getGenTable, updateGenTable } from '@/api/tool/gen'
import { listMenu as getMenuTreeselect } from "@/api/system/menu";
import basicInfoForm from './basicInfoForm'
import genInfoForm from './genInfoForm'
import Sortable from 'sortablejs'
export default {
  name: 'GenEdit',
  components: {
    basicInfoForm,
    genInfoForm
  },
  data() {
    return {
      // 选中选项卡的 name
      activeName: 'cloum',
      // 表格的高度
      tableHeight: document.documentElement.scrollHeight - 245 + 'px',
      // 表列信息
      columns: [],
      // 菜单信息
      menus: [],
      // 表详细信息
      info: {},
      submitLoading: false
    }
  },
  beforeCreate() {
    const { tableId } = this.$route.query
    if (tableId) {
      // 获取表详细信息
      getGenTable(tableId).then(data => {
        this.columns = data.columns
        this.info = data.info
      })
      // 查询菜单下拉列表
      getMenuTreeselect().then(data => {
        this.menus = this.handleTree(data)
      })
    }
  },
  methods: {
    /** 提交按钮 */
    submitForm() {
      // 校验表单提交
      if (!this.validateColumns()) return false
      const basicForm = this.$refs.basicInfo.$refs.basicInfoForm
      const genForm = this.$refs.genInfo.$refs.genInfoForm
      Promise.all([basicForm, genForm].map(this.getFormPromise)).then(res => {
        // res是另外两个表单的校验结果数组，形式为[boolean, boolean]
        const validateResult = res.every(item => !!item)
        if (validateResult) {
          const genTable = Object.assign({}, basicForm.model, genForm.model)
          genTable.columns = this.columns
          genTable.params = {
            treeCode: genTable.treeCode,
            treeName: genTable.treeName,
            treeParentCode: genTable.treeParentCode,
            parentMenuId: genTable.parentMenuId,
            autoResultMap: genTable.autoResultMap
          }
          this.submitLoading = true
          updateGenTable(genTable).then(data => {
            this.msgSuccess(data)
            this.close()
          }).finally(() => this.submitLoading = false)
        } else {
          this.msgError('表单校验未通过，请重新检查提交内容')
        }
      })
    },
    getFormPromise(form) {
      return new Promise(resolve => {
        form.validate(res => {
          resolve(res)
        })
      })
    },
    validateColumns() {
      const result = this.columns.some(column => column.javaType === 'Enum' && !column.enumFullName)
      if (result) this.msgError('请为枚举类型字段填写枚举类全限定名')
      return !result
    },
    /** 关闭按钮 */
    close() {
      this.$store.dispatch('tagsView/delView', this.$route)
      this.$router.push({ path: '/tool/gen', query: { t: Date.now()}})
    },
    /** 拖拽功能，在需要支持拖拽的列上添加 class-name="allowDrag" */
    initDrag(el) {
      const sortable = Sortable.create(el, {
        handle: '.allowDrag',
        onEnd: evt => {
          const targetRow = this.columns.splice(evt.oldIndex, 1)[0]
          this.columns.splice(evt.newIndex, 0, targetRow)
          for (let index in this.columns) {
            this.columns[index].sort = parseInt(index) + 1
          }
        }
      })
    }
  },
  mounted() {
    // 支持行拖拽
    // 如果存在固定列fixed，则使用如下代码
    this.$nextTick(() => {
      const el = this.$refs.dragTable.$el.querySelectorAll('.el-table__fixed > .el-table__fixed-body-wrapper > table > tbody')[0]
      this.initDrag(el)
    })

    // 如果是正常表格，则使用如下代码
    // const el = this.$refs.dragTable.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
    // this.initDrag(el)
  }
}
</script>
