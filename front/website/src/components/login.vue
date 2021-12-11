<template>
  <div class="mainContainer">
    <el-dialog
      :visible.sync="showCaptchaDialog"
      title="请完成验证"
      width="50%"
      v-on:open="captchaDialogOpen"
    >
      <div class="captchaImageWrapper">
        <img :src="captcha.codeImage" class="codeImage"/>
      </div>
      <div class="captchaInputs">
        <el-input class="captchaText" v-model="captcha.codeText"></el-input>
        <el-button type="primary" class="submitCaptchaButton" @click="login">提交</el-button>
      </div>
    </el-dialog>
    <div class="loginPanel">
      <h1>请登录</h1>
      <div class="inputs">
        <div>
          <span class="label">用户名:</span>
          <input class="input" type="text" v-model="identity"/>
        </div>
        <div>
          <span class="label" style="">密码:</span>
          <input class="input" style="margin-left: 35px;" type="password" v-model="password"/>
        </div>
      </div>
      <button class="loginButton" ref="loginButton" @click="showCaptchaDialog = true">登录</button>
    </div>
  </div>
</template>

<script>
// import auth from '../api/auth'
import gateway from '../api/gateway'
import auth from '../api/auth'
import axios from 'axios'
import Cookies from 'js-cookie'

export default {
  name: 'login',
  data () {
    return {
      identity: '',
      password: '',
      showCaptchaDialog: false,
      captcha: {
        codeID: '',
        codeText: '',
        codeImage: ''
      }
    }
  },
  methods: {
    login: function () {
      this.showCaptchaDialog = false
      gateway.checkCaptcha(this.captcha.codeID, this.captcha.codeText, '/login').then(resp => {
        console.log(resp)
        if (resp.message !== 'Success') {
          this.$message.error('验证码错误！')
          return
        }
        auth.login(this.identity, this.password).then(resp => {
          console.log(resp)
          if (resp.message !== 'Success') {
            switch (resp.message) {
              case 'User not found':
                this.$message.error('用户未找到，请检查用户名是否有误！')
                break
              case 'Incorrect password':
                this.$message.error('密码错误！')
                break
            }
            return
          }
          this.$router.push('/')
        })
      })
    },
    captchaDialogOpen: function () {
      gateway.generateCaptcha().then(resp => {
        this.captcha.codeID = resp.data.codeID
        this.captcha.codeImage = resp.data.codeImage
      })
    }
  }
}
</script>

<style src="../style/login.less" lang="less" scoped></style>
