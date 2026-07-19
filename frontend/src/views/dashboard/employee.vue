<template>
  <div class="employee-dashboard">
    <div class="welcome-section">
      <div class="welcome-avatar">
        <!--  -->
        <el-avatar :size="80" :src="avatarUrl">
          {{ userStore.user.realName?.charAt(0) || 'U' }}
        </el-avatar>
        <span class="welcome-text">
          {{ userStore.user.realName }}
        </span>
      </div>
      <div class="welcome-info">
        <div class="info-item">
          <el-icon><Calendar /></el-icon>
          <span>{{ currentDate }}</span>
          <span>{{ currentWeek }}</span>
        </div>
      </div>
    </div>
    
    <div class="quick-actions">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="action-card" @click="$router.push('/inventory')">
            <div class="action-icon">
              <el-icon><Box /></el-icon>
            </div>
            <span>库存预警</span>
            <div class="action-desc">查看库存不足商品</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card" @click="$router.push('/purchase')">
            <div class="action-icon">
              <el-icon><ShoppingCart /></el-icon>
            </div>
            <span>发起采购</span>
            <div class="action-desc">快速补充库存</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card" @click="$router.push('/my-schedule')">
            <div class="action-icon">
              <el-icon><Calendar /></el-icon>
            </div>
            <span>我的排班</span>
            <div class="action-desc">查看本周排班安排</div>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <div class="schedule-section">
      <div class="section-header">
        <el-icon><Calendar /></el-icon>
        <span>本周排班概览</span>
      </div>
      <el-calendar 
        v-model="scheduleMonth" 
        :first-day="1"
        class="schedule-calendar"
      />
      <div class="schedule-list">
        <div 
          v-for="day in scheduleData" 
          :key="day.date"
          class="schedule-day"
          :class="['morning', 'afternoon', 'night'].includes(day.shift) ? day.shift : ''"
        >
          {{ day.shift }}
          {{ day.dateStr }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getScheduleMy } from '@/api/schedule'
import { getInventoryWarning } from '@/api/inventory'
import { ElMessage } from 'element-plus'
import { Calendar, Warning, Box, Goods, UserFilled, ShoppingCart } from '@element-plus/icons-vue'

const userStore = useUserStore()
const router = useRouter()

// 页面加载状态
const loading = ref(false)
// 当前选中的月份
const scheduleMonth = ref(new Date())
// 排班数据列表
const scheduleData = ref([])

// 当前日期
const currentDate = ref('')
// 当前星期
const currentWeek = ref('')

// 计算属性：头像URL
const avatarUrl = computed(() => {
  if (userStore.user.avatar) {
    if (userStore.user.avatar.startsWith('http') || userStore.user.avatar.startsWith('data:')) {
      return userStore.user.avatar
    }
    return '/api' + userStore.user.avatar
  }
  return ''
})

// 根据当前时间返回问候语
const getGreeting = () => {
  const hour = new Date().getHours()
  if (hour < 12) {
    return '早上好'
  } else if (hour >= 12 && hour < 18) {
    return '下午好'
  } else if (hour >= 18 && hour < 22) {
    return '晚上好'
  }
  return '夜深了'
}

// 更新当前日期和星期
const updateDate = () => {
  const now = new Date()
  const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  currentDate.value = now.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
  currentWeek.value = weekDays[now.getDay()]
}

// 加载排班数据
const loadMySchedule = async () => {
  loading.value = true
  try {
    const res = await getScheduleMy()
    scheduleData.value = res.data || []
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 页面挂载时初始化
onMounted(() => {
  updateDate()
  loadMySchedule()
})
</script>

<style lang="scss" scoped>
.employee-dashboard {
  padding: 0;
}

.welcome-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #409eff, #337ecc);
  padding: 30px;
  border-radius: 12px;
  color: #fff;
  margin-bottom: 20px;
  
  .welcome-avatar {
    display: flex;
    align-items: center;
    gap: 15px;
    
    .welcome-text {
      font-size: 20px;
      font-weight: 600;
    }
  }
  
  .welcome-info {
    .info-item {
      display: flex;
      align-items: center;
      gap: 10px;
    }
  }
}

.quick-actions {
  margin-bottom: 20px;
  
  .action-card {
    background: #fff;
    border-radius: 12px;
    padding: 25px;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    cursor: pointer;
    transition: all 0.3s;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
    
    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    }
    
    .action-icon {
      .el-icon {
        font-size: 40px;
        color: #409eff;
      }
    }
    
    span {
      font-size: 14px;
      color: #303133;
      font-weight: 500;
    }
    
    .action-desc {
      font-size: 12px;
      color: #909399;
    }
  }
}

.schedule-section {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  
  .section-header {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 20px;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
  
  .schedule-calendar {
    margin-bottom: 20px;
  }
  
  .schedule-list {
    .schedule-day {
      padding: 10px 15px;
      margin-bottom: 10px;
      border-radius: 8px;
      background: #f5f7fa;
      
      &.morning {
        background: #fdf6ec;
        border-left: 3px solid #e6a23c;
      }
      
      &.afternoon {
        background: #ecf5ff;
        border-left: 3px solid #409eff;
      }
      
      &.night {
        background: #f4f4f5;
        border-left: 3px solid #909399;
      }
    }
  }
}
</style>
