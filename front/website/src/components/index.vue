<template>
  <div class="mainContainer">
    <header-component></header-component>
  </div>
</template>

<script>
import Header from './header'
import Cookies from 'js-cookie'
import auth from '../api/auth'

export default {
  name: 'index',
  components: {
    headerComponent: Header
  },
  created () {
    if (Cookies.get('token') === undefined) {
      auth.token()
    }
  }
}
</script>

<style src="../style/index.less" lang="less" scoped></style>
