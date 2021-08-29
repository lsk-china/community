<template>
  <div class="mainContainer">
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
      <button class="loginButton" ref="loginButton" @click="login">登录</button>
    </div>
  </div>
</template>

<script>
import auth from '../api/auth'
import gateway from '../gateway'

export default {
  name: 'login',
  data () {
    return {
      identity: '',
      password: '',
      captcha: {
        codeID: '',
        codeText: ''
      }
    }
  },
  methods: {
    login: function () {
      gateway.generateCaptcha().then(resp => {
      })
    }
  }
}
</script>

<style src="../style/login.less" lang="less" scoped></style>
