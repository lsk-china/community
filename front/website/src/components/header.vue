<template>
  <div class="mainContainer">
    <div class="part">
      <h2>论坛</h2>
    </div>
    <div class="part">
      <div class="userinfo">
        <div v-if="isLogin">
          <el-avatar :src="avatar" class="avatar"></el-avatar>
        </div>
        <div v-else>
          <router-link to="/login" class="spanButton">登录</router-link>
          <router-link to="/register" class="spanButton">注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import storage from 'good-storage'

export default {
  name: 'header',
  data () {
    return {
      isLogin: false,
      avatar: ''
    }
  },
  created () {
    if (storage.has('user')) {
      let user = storage.get('user')
      this.isLogin = true
      this.avatar = 'http://localhost:10002/avatar/' + user.id + '.jpg'
    }
  }
}
</script>

<style src="../style/header.less" lang="less" scoped></style>
