<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">欢迎登录管理系统</h3>
      <el-form-item prop="tenantId">
        <el-select v-model="loginForm.tenantId" class="select-tenant" placeholder="请选择租户" filterable clearable>
          <svg-icon slot="prefix" icon-class="international" class="el-input__icon input-icon" />
          <el-option
            v-for="(item, index) in tenantList"
            :key="index"
            :label="item.tenantName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="account">
        <el-input
          v-model="loginForm.account"
          type="text"
          auto-complete="off"
          placeholder="手机号/用户名/邮箱"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" />
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2020-2022 AIR Software All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import { needCaptcha, getCodeImg } from '@/api/login'
import { listTenant } from '@/api/system/tenant'
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: 'Login',
  data() {
    return {
      tenantList: [],
      codeUrl: '',
      loginForm: {
        tenantId: undefined,
        account: 'admin',
        password: 'admin123',
        rememberMe: false,
        code: '',
        uuid: '',
        device: 'PC端'
      },
      loginRules: {
        tenantId: [
          { required: true, trigger: 'change', message: '所属租户不能为空' }
        ],
        account: [
          { required: true, trigger: 'blur', message: '登录账号不能为空' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '密码不能为空' }
        ],
        code: [
          { required: true, trigger: 'blur', message: '验证码不能为空' }
        ]
      },
      loading: false,
      redirect: undefined,
      captchaEnabled: false
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getTenantList()
    this.isCaptchaEnabled()
    this.getStorage()
  },
  methods: {
    getTenantList() {
      listTenant().then(data => {
        data.forEach(item => {
          item.id = item.id.toString()
        })
        this.tenantList = data
      })
    },
    isCaptchaEnabled() {
      needCaptcha().then(captchaEnabled => {
        this.captchaEnabled = captchaEnabled
        if (captchaEnabled) {
          this.getCode()
        }
      })
    },
    getCode() {
      getCodeImg().then(data => {
        this.codeUrl = 'data:image/gif;base64,' + data.img
        this.loginForm.uuid = data.uuid
      })
    },
    getStorage() {
      localStorage.removeItem('headerTenantId')
      const tenantId = localStorage.getItem('loginTenantId')
      const account = localStorage.getItem('account')
      const password = localStorage.getItem('password')
      const rememberMe = localStorage.getItem('rememberMe')
      this.loginForm = {
        tenantId,
        account: account || this.loginForm.account,
        password: password ? decrypt(password) : this.loginForm.password,
        rememberMe: rememberMe ? Boolean(rememberMe) : false
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            localStorage.setItem('loginTenantId', this.loginForm.tenantId)
            localStorage.setItem('account', this.loginForm.account)
            localStorage.setItem('password', encrypt(this.loginForm.password))
            localStorage.setItem('rememberMe', this.loginForm.rememberMe)
          } else {
            localStorage.removeItem('loginTenantId')
            localStorage.removeItem('account')
            localStorage.removeItem('password')
            localStorage.removeItem('rememberMe')
          }
          this.$store
            .dispatch('Login', this.loginForm)
            .then(() => {
              this.$router.push({ path: this.redirect || '/' })
            })
            .catch(() => {
              this.loading = false
              this.loginForm.code = ''
              if (this.captchaEnabled) {
                this.getCode()
              }
            })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/image/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0 auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;

  .select-tenant {
    width: 100%;
  }
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
</style>
