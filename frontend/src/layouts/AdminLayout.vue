<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <div class="logo">
        <el-icon :size="28"><Shop /></el-icon>
        <span v-show="!isCollapse" class="logo-text">商超后勤系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        class="layout-menu"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        
        <el-menu-item index="/category">
          <el-icon><Grid /></el-icon>
          <span>商品分类</span>
        </el-menu-item>
        
        <el-menu-item index="/goods">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
        
        <el-menu-item index="/inventory">
          <el-icon><Box /></el-icon>
          <span>库存管理</span>
        </el-menu-item>
        
        <el-menu-item index="/purchase">
          <el-icon><ShoppingCart /></el-icon>
          <span>采购管理</span>
        </el-menu-item>
        
        <el-menu-item index="/supplier" v-if="isAdmin || isManager">
          <el-icon><OfficeBuilding /></el-icon>
          <span>供应商管理</span>
        </el-menu-item>
        
        <el-menu-item index="/finance" v-if="isAdmin || isManager">
          <el-icon><Wallet /></el-icon>
          <span>财务管理</span>
        </el-menu-item>
        
        <el-menu-item index="/schedule" v-if="isAdmin || isManager">
          <el-icon><Calendar /></el-icon>
          <span>排班管理</span>
        </el-menu-item>
        
        <el-menu-item index="/my-schedule" v-if="isEmployee">
          <el-icon><Calendar /></el-icon>
          <span>我的排班</span>
        </el-menu-item>
        
        <!-- 部门管理暂时隐藏 -->
        <!-- <el-menu-item index="/department" v-if="isAdmin || isManager">
          <el-icon><OfficeBuilding /></el-icon>
          <span>部门管理</span>
        </el-menu-item> -->
        
        <el-menu-item index="/profile">
          <el-icon><UserFilled /></el-icon>
          <span>个人中心</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container class="layout-main">
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="toggleCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="currentRoute.meta.title">
              {{ currentRoute.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown trigger="click" @command="handleCommand">
            <div class="user-info">
              <el-avatar :size="36" :src="avatarUrl">
                {{ userStore.user.realName?.charAt(0) || 'U' }}
              </el-avatar>
              <div class="user-detail">
                <span class="user-name">{{ userStore.user.realName }}</span>
                <span class="user-role">{{ getRoleName(userStore.role) }}</span>
              </div>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main class="layout-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)
const defaultAvatar = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMDAgMTAwIj48Y2lyY2xlIGN4PSI1MCIgY3k9IjUwIiByPSI1MCIgZmlsbD0iIzQwOWVmZiIvPjx0ZXh0IHg9IjUwIiB5PSI2NSIgZm9udC1zaXplPSI0MCIgZmlsbD0iI2ZmZiIgdGV4dC1hbmNob3I9Im1pZGRsZSI+VTwvdGV4dD48L3N2Zz4='

const activeMenu = computed(() => route.path)
const currentRoute = computed(() => route)

const isAdmin = computed(() => userStore.role === 1)
const isManager = computed(() => userStore.role === 2)
const isEmployee = computed(() => userStore.role === 3)

const avatarUrl = computed(() => {
  const avatar = userStore.user.avatar
  if (avatar) {
    if (avatar.startsWith('http') || avatar.startsWith('data:')) {
      return avatar
    }
    return '/api' + avatar
  }
  return defaultAvatar
})

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const getRoleName = (role) => {
  const roles = {
    1: '超级管理员',
    2: '经理',
    3: '员工'
  }
  return roles[role] || '未知'
}

const handleCommand = async (command) => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      await userStore.logoutAction()
      ElMessage.success('退出成功')
      router.push('/login')
    } catch (e) {
      if (e !== 'cancel') {
        console.error(e)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
}

.layout-aside {
  background: linear-gradient(180deg, #1e3a5f 0%, #0d2137 100%);
  transition: width 0.3s;
  overflow: hidden;
  
  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    color: #fff;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    
    .logo-text {
      font-size: 16px;
      font-weight: 600;
      white-space: nowrap;
    }
  }
  
  .layout-menu {
    border: none;
    background: transparent;
    
    &:not(.el-menu--collapse) {
      width: 220px;
    }
    
    .el-menu-item {
      color: rgba(255, 255, 255, 0.75);
      height: 50px;
      line-height: 50px;
      margin: 2px 8px;
      border-radius: 6px;
      
      &:hover {
        background: rgba(255, 255, 255, 0.1);
        color: #fff;
      }
      
      &.is-active {
        background: linear-gradient(90deg, #409eff 0%, #337ecc 100%);
        color: #fff;
        box-shadow: 0 2px 8px rgba(64, 158, 255, 0.4);
      }
    }
    
    :deep(.el-sub-menu) {
      .el-sub-menu__title {
        color: rgba(255, 255, 255, 0.75);
        height: 50px;
        line-height: 50px;
        margin: 2px 8px;
        border-radius: 6px;
        
        &:hover {
          background: rgba(255, 255, 255, 0.1);
          color: #fff;
        }
      }
      
      &.is-active {
        .el-sub-menu__title {
          color: #fff;
        }
      }
      
      .el-menu-item {
        background: rgba(0, 0, 0, 0.2);
        margin: 2px 12px;
        padding-left: 50px !important;
        
        &:hover {
          background: rgba(255, 255, 255, 0.15);
        }
        
        &.is-active {
          background: linear-gradient(90deg, #409eff 0%, #337ecc 100%);
        }
      }
    }
  }
}

.layout-main {
  background: #f0f2f5;
}

.layout-header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  height: 60px;
  
  .header-left {
    display: flex;
    align-items: center;
    gap: 15px;
    
    .collapse-btn {
      font-size: 20px;
      cursor: pointer;
      color: #606266;
      
      &:hover {
        color: #409eff;
      }
    }
  }
  
  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      gap: 10px;
      cursor: pointer;
      padding: 5px 10px;
      border-radius: 6px;
      
      &:hover {
        background: #f5f7fa;
      }
      
      .user-detail {
        display: flex;
        flex-direction: column;
        
        .user-name {
          font-size: 14px;
          color: #303133;
          font-weight: 500;
        }
        
        .user-role {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }
}

.layout-content {
  padding: 20px;
  overflow-y: auto;
}
</style>
