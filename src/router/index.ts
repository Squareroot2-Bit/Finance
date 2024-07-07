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
      component: () => import('@/views/HomeView.vue')
    }
  ]
})

router.beforeEach((to, from, next) => {})
export default router
