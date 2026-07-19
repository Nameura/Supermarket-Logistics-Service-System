<template>
  <!-- :is 属性指定要渲染哪个组件，可以当作一种语法糖来理解，跟插槽的作用类似 -->
  <component :is="layoutComponent">
    <!-- 使用 <router-view> 渲染当前路由对应的页面组件 -->
    <router-view v-slot="{ Component }">
      <!-- 添加 fade 过渡动画，使页面切换更平滑 -->
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
  </component>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '@/store/user'
import AdminLayout from './AdminLayout.vue' // 管理端布局（含侧边栏菜单）
import EmployeeLayout from './EmployeeLayout.vue' // 员工端布局（简化版菜单）

const userStore = useUserStore()

// 动态组件切换
// 通过计算属性 layoutComponent 根据用户角色返回不同的布局组件
// role === 3 （员工角色）→ 使用 EmployeeLayout （员工端布局）
// 其他角色（管理员、经理）→ 使用 AdminLayout （管理端布局）
const layoutComponent = computed(() => {
  return userStore.role === 3 ? EmployeeLayout : AdminLayout
})
</script>
