// import { title } from 'process'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'welcome',
      component: () => import('../views/WelcomeView.vue'),
      children: [
        {
          path: '',
          name: 'welcome-login',
          component: () => import('../views/LoginView.vue')
        },
        {
          path: 'register',
          name: 'welcome-register',
          component: () => import('../views/RegisterView.vue')
        }
      ]
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('@/views/HomeView.vue'),
      children: [
        {
          path: 'summary',
          name: 'home-summary',
          meta: {
            homeList: true,
            title: '报告',
            index: 1
          },
          component: () => import('@/views/SummaryView.vue')
        },
        {
          path: '',
          name: 'home-record',
          meta: {
            homeList: true,
            title: '记账',
            index:2
          },
          component: () => import('@/views/RecordView.vue')
        },
        
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if ((to.name as string).startsWith('welcome') && token) {
    next('/home')
  } else if (to.fullPath.startsWith('/home') && !token) {
    next('/')
  } else {
    next()
  }
})
export default router
