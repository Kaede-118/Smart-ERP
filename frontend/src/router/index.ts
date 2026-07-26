import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layout/MainLayout.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/login/LoginView.vue')
    },
    {
      path: '/',
      component: MainLayout,
      redirect: '/dashboard',
      children: [
        { path: 'dashboard', name: 'Dashboard', component: () => import('../views/dashboard/DashboardView.vue') },
        { path: 'products', name: 'Products', component: () => import('../views/product/ProductView.vue') },
        { path: 'inventory', name: 'Inventory', component: () => import('../views/inventory/InventoryView.vue') },
        { path: 'purchase', name: 'Purchase', component: () => import('../views/purchase/PurchaseView.vue') },
        { path: 'sales', name: 'Sales', component: () => import('../views/sales/SalesView.vue') },
        { path: 'ai', name: 'AI', component: () => import('../views/ai/AIView.vue') },
        { path: 'rbac', name: 'RBAC', component: () => import('../views/rbac/RBACView.vue') }
      ]
    }
  ]
})

router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
