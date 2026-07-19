<template>
  <el-container class="employee-layout">
    <el-header class="employee-header">
      <div class="header-logo">
        <el-icon :size="28"><Shop /></el-icon>
        <span class="logo-text">商超后勤系统</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        mode="horizontal"
        class="employee-menu"
        router
      >
      <!-- 通过路由跳转不同的页面，动态生成菜单，实际上就是把路径push到路由栈中 -->
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
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
        
        <el-menu-item index="/my-schedule">
          <el-icon><Calendar /></el-icon>
          <span>我的排班</span>
        </el-menu-item>
        
        <el-menu-item index="/profile">
          <el-icon><UserFilled /></el-icon>
          <span>个人中心</span>
        </el-menu-item>
      </el-menu>
      
      <div class="header-right">
        <el-dropdown trigger="click" @command="handleCommand">
          <div class="user-info">
            <el-avatar :size="36" :src="avatarUrl">
              {{ userStore.user.realName?.charAt(0) || 'U' }}
            </el-avatar>
            <div class="user-detail">
              <span class="user-name">{{ userStore.user.realName }}</span>
              <span class="user-role">员工</span>
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
    
    <el-main class="employee-content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </el-main>
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

const defaultAvatar = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMDAgMTAwIj48Y2lyY2xlIGN4PSI1MCIgY3k9IjUwIiByPSI1MCIgZmlsbD0iIzY3YzIzYSIvPjx0ZXh0IHg9IjUwIiB5PSI2NSIgZm9udC1zaXplPSI0MCIgZmlsbD0iI2ZmZiIgdGV4dC1hbmNob3I9Im1pZGRsZSI+RTwvdGV4dD48L3N2Zz4='

const activeMenu = computed(() => route.path)

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
.employee-layout {
  height: 100vh;
  background: #f0f2f5;
}

.employee-header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  height: 60px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  
  .header-logo {
    display: flex;
    align-items: center;
    gap: 10px;
    color: #67c23a;
    
    .logo-text {
      font-size: 18px;
      font-weight: 600;
      white-space: nowrap;
    }
  }
  
  .employee-menu {
    flex: 1;
    display: flex;
    justify-content: center;
    border: none;
    background: transparent;
    height: 60px;
    
    .el-menu-item {
      height: 60px;
      line-height: 60px;
      font-size: 14px;
      padding: 0 20px;
      margin: 0 5px;
      border-bottom: 3px solid transparent;
      
      &:hover {
        background: rgba(103, 194, 58, 0.1);
        border-bottom-color: #67c23a;
      }
      
      &.is-active {
        color: #67c23a;
        background: rgba(103, 194, 58, 0.1);
        border-bottom-color: #67c23a;
      }
      
      .el-icon {
        margin-right: 5px;
      }
    }
  }
  
  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      gap: 10px;
      cursor: pointer;
      padding: 5px 15px;
      border-radius: 20px;
      
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
          color: #67c23a;
        }
      }
    }
  }
}

.employee-content {
  margin-top: 60px;
  padding: 20px;
  min-height: calc(100vh - 60px);
  overflow-y: auto;
}
</style>
