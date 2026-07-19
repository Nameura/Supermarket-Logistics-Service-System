import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    // 登录界面
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    // 注册界面
    path: '/register',
    name: 'Register',
    component: () => import('@/views/login/register.vue'),
    meta: { title: '注册', requiresAuth: false }
  },
  {
    // 
    path: '/',
    component: () => import('@/layouts/DynamicLayout.vue'),
    redirect: '/dashboard', // 默认重定向到首页
    meta: { requiresAuth: true },
    children: [// 动态子路由
      {
        // 首页，也即仪表盘
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页' }
      },
      {
        // 个人中心
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心' }
      },
      // 部门管理路由暂时隐藏，因为目前用不上了
      // {
      //   path: 'department',
      //   name: 'Department',
      //   component: () => import('@/views/user/department.vue'),
      //   meta: { title: '部门管理', roles: [1, 2] }
      // },
      {
        // 商品分类页面
        path: 'category',
        name: 'Category',
        component: () => import('@/views/category/index.vue'),
        meta: { title: '商品分类' }
      },
      {
        // 商品管理页面
        path: 'goods',
        name: 'Goods',
        component: () => import('@/views/goods/index.vue'),
        meta: { title: '商品管理' }
      },
      {
        // 库存管理页面
        path: 'inventory',
        name: 'Inventory',
        component: () => import('@/views/inventory/index.vue'),
        meta: { title: '库存管理' }
      },
      {
        // 采购管理页面
        path: 'purchase',
        name: 'Purchase',
        component: () => import('@/views/purchase/index.vue'),
        meta: { title: '采购管理' }
      },
      {
        // 供应商管理页面，roles是角色限制，只有1和2的角色才能访问
        path: 'supplier',
        name: 'Supplier',
        component: () => import('@/views/supplier/index.vue'),
        meta: { title: '供应商管理', roles: [1, 2] }
      },
      {
        // 财务管理页面
        path: 'finance',
        name: 'Finance',
        component: () => import('@/views/finance/index.vue'),
        meta: { title: '财务管理', roles: [1, 2] }
      },
      {
        // 排班管理页面
        path: 'schedule',
        name: 'Schedule',
        component: () => import('@/views/schedule/index.vue'),
        meta: { title: '排班管理', roles: [1, 2] }
      },
      {
        // 我的排班页面（只有员工端会用到）
        path: 'my-schedule',
        name: 'MySchedule',
        component: () => import('@/views/schedule/my.vue'),
        meta: { title: '我的排班', roles: [3] }
      }
    ]
  }
]

// 创建路由实例，使用HTML5 History模式
const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  // 根据路由的meta信息动态设置浏览器标题
  // 例如：访问商品管理页面，标题显示"商品管理 - 商超后勤服务系统"
  document.title = to.meta.title ? `${to.meta.title} - 商超后勤服务系统` : '商超后勤服务系统'
  
  // 获取用户状态，判断是否已登录
  const userStore = useUserStore()
  const isLoggedIn = userStore.isLoggedIn
  
  // 校验，跳转对应界面
  if (to.meta.requiresAuth !== false && !isLoggedIn) {
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && isLoggedIn) {
    next('/dashboard')
  } else if (to.meta.roles && to.meta.roles.length > 0) {
    const userRole = userStore.role
    if (to.meta.roles.includes(userRole)) {
      next()
    } else {
      next('/dashboard')
    }
  } else {
    next()
  }
})

export default router
