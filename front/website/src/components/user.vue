<template>
    <div class="mainContainer">
        <el-container>
            <el-aside width="20%">
                <div class="left">
                    <div class="leftTop">
                        <el-avatar :src="avatar" class="avatar" :size="100"></el-avatar>
                        <span class="leftUsername" v-text="userinfo.username"></span>
                    </div>
                </div>
            </el-aside>
            <el-main>
                <el-container class="relative"> 
                    <el-header></el-header>
                    <el-main class="content" style="background: #EEE;">
                        <div class="full">
                            <component class="component" v-bind:is="rightComponent"></component>
                        </div>
                    </el-main>
                </el-container>
            </el-main>
        </el-container>
    </div>
</template>

<script>
import storage from 'good-storage'
import auth from '../api/auth_test'
import info from './user/info'

export default {
    name: 'user',
    data () {
        return {
            userinfo: null,
            avatar: 'http://localhost:10002/avatar/',
            rightComponent: info 
        }
    },
    created () {
        if (!storage.session.has('userinfo')) {
            auth.userinfo().then(resp => {
                storage.session.set('userinfo', resp)
            }).catch(code => {
                switch (code) {
                    case 403:
                        this.$router.push('/login')
                        break
                    case 500:
                        this.$message.error('服务器错误')
                        break
                    default:
                        this.$message.error('未知错误')
                        console.log(code)
                        break
                }
            })
        }
        this.userinfo = storage.session.get('userinfo')
        this.avatar += this.userinfo.id
        this.avatar += '.'
        this.avatar += this.userinfo.avatar
    }
}
</script>

<style src="../style/user.less" lang="less" scoped></style>
