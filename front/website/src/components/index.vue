<template>
  <div class="mainContainer">
    <header-component></header-component>
  </div>
</template>

<script>
import Header from './header'

export default {
  name: 'index',
  components: {
    headerComponent: Header
  }
}
</script>

<style src="../style/index.less" lang="less" scoped></style>
