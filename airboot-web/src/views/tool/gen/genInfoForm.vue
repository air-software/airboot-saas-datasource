<template>
  <el-form ref="genInfoForm" :model="info" :rules="rules" label-width="170px">
    <el-row>
      <el-col :span="12">
        <el-form-item prop="tplCategory">
          <span slot="label">生成模板</span>
          <el-select v-model="info.tplCategory">
            <el-option label="单表（增删改查）" value="单表" />
            <el-option label="树表（增删改查）" value="树表"/>
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="packageName">
          <span slot="label">
            生成包路径
            <el-tooltip content="生成在哪个java包下，例如 com.airboot.system" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.packageName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="moduleName">
          <span slot="label">
            生成模块名
            <el-tooltip content="可理解为子系统名，例如 system" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.moduleName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="businessName">
          <span slot="label">
            生成业务名
            <el-tooltip content="可理解为功能英文名，例如 user，请使用驼峰命名法" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.businessName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="functionName">
          <span slot="label">
            生成功能名
            <el-tooltip content="用作类描述，例如 用户" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.functionName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            上级菜单
            <el-tooltip content="分配到指定菜单下，例如 系统管理" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <treeselect
            :append-to-body="true"
            v-model="info.parentMenuId"
            :options="menus"
            :normalizer="normalizer"
            :show-count="true"
            placeholder="请选择系统菜单"
          />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="autoResultMap">
          <span slot="label">
            生成ResultMap
            <el-tooltip content="是否自动生成Mybatis的ResultMap" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-radio-group v-model="info.autoResultMap">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="interfaceService">
          <span slot="label">
            生成Service Interface
            <el-tooltip content="是否需要生成Service的Interface，如果业务不复杂且确定未来不会用到多态，则可以不生成Interface，直接使用Service，方便应对需求变更" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-radio-group v-model="info.interfaceService">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="globalEnums">
          <span slot="label">
            使用前端全局枚举变量
            <el-tooltip content="是否使用前端全局$enums枚举变量来配置下拉菜单等组件，如开启使用，请确保你在系统中已建好枚举，并配置到了Constants.FRONT_SHOW_ENUMS中" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-radio-group v-model="info.globalEnums">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row v-show="info.tplCategory === '树表'">
      <h4 class="form-header">其他信息</h4>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            树编码字段
            <el-tooltip content="树显示的编码字段名， 如：id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeCode" placeholder="请选择">
            <el-option
              v-for="column in info.columns"
              :key="column.columnName"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            树父编码字段
            <el-tooltip content="树显示的父编码字段名， 如：parent_id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeParentCode" placeholder="请选择">
            <el-option
              v-for="column in info.columns"
              :key="column.columnName"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            树名称字段
            <el-tooltip content="树节点的显示名称字段名， 如：dept_name" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeName" placeholder="请选择">
            <el-option
              v-for="column in info.columns"
              :key="column.columnName"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'BasicInfoForm',
  components: { Treeselect },
  props: {
    info: {
      type: Object,
      default: null
    },
    menus: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      rules: {
        tplCategory: [
          { required: true, message: '请选择生成模板', trigger: 'blur' }
        ],
        packageName: [
          { required: true, message: '请输入生成包路径', trigger: 'blur' }
        ],
        moduleName: [
          { required: true, message: '请输入生成模块名', trigger: 'blur' }
        ],
        businessName: [
          { required: true, message: '请输入生成业务名', trigger: 'blur' }
        ],
        functionName: [
          { required: true, message: '请输入生成功能名', trigger: 'blur' }
        ]
      }
    }
  },
  created() {},
  methods: {
    // 转换菜单数据结构
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.menuName,
        children: node.children
      }
    }
  }
}
</script>
