import { createRouter, createWebHistory } from 'vue-router'
import NotFound from '@/components/NotFound.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/Home.vue')
    },
    {
      path: '/article/:id',
      name: 'ArticleDetail',
      component: () => import('@/views/ArticleDetail.vue')
    },
    {
      path: '/admin/login',
      name: 'AdminLogin',
      component: () => import('@/views/admin/Login.vue')
    },
    {
      path: '/admin',
      name: 'Admin',
      component: () => import('@/views/admin/Dashboard.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'AdminHome',
          component: () => import('@/views/admin/Home.vue')
        },
        {
          path: 'articles',
          name: 'AdminArticles',
          component: () => import('@/views/admin/Articles.vue')
        },
        {
          path: 'articles/create',
          name: 'AdminArticleCreate',
          component: () => import('@/views/admin/ArticleForm.vue')
        },
        {
          path: 'articles/edit/:id',
          name: 'AdminArticleEdit',
          component: () => import('@/views/admin/ArticleForm.vue')
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      component: NotFound
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (to.meta && to.meta.requiresAuth) {
    const isAuthenticated = localStorage.getItem('authenticated') === 'true'
    if (!isAuthenticated) {
      next({ name: 'AdminLogin', query: { redirect: to.fullPath } })
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
