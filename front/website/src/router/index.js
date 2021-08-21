import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: () => import('../components/index')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../components/login')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../components/register')
    }
  ]
})
