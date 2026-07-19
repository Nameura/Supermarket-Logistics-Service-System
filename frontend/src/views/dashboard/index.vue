<template>
  <div class="dashboard-container">
    <div class="welcome-section">
      <div class="welcome-text">
        <h2>欢迎回来，{{ userStore.user.realName }}</h2>
        <p>{{ getGreeting() }}，祝您工作愉快！</p>
      </div>
      <div class="welcome-date">
        <p>{{ currentDate }}</p>
        <p>{{ currentWeek }}</p>
      </div>
    </div>
    
    <div class="stats-section" v-if="userStore.isAdmin || userStore.isManager">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card stat-card-1">
            <div class="stat-content">
              <div class="stat-title">商品总数</div>
              <div class="stat-value">{{ statistics.goodsCount || 0 }}</div>
            </div>
            <el-icon class="stat-icon"><Goods /></el-icon>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card stat-card-2">
            <div class="stat-content">
              <div class="stat-title">库存总量</div>
              <div class="stat-value">{{ statistics.inventoryTotal || 0 }}</div>
            </div>
            <el-icon class="stat-icon"><Box /></el-icon>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card stat-card-3">
            <div class="stat-content">
              <div class="stat-title">库存预警</div>
              <div class="stat-value">{{ statistics.inventoryWarning || 0 }}</div>
            </div>
            <el-icon class="stat-icon"><Warning /></el-icon>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card stat-card-4">
            <div class="stat-content">
              <div class="stat-title">员工总数</div>
              <div class="stat-value">{{ statistics.userCount || 0 }}</div>
            </div>
            <el-icon class="stat-icon"><UserFilled /></el-icon>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <div class="stats-section" v-else>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="stat-card stat-card-1">
            <div class="stat-content">
              <div class="stat-title">商品总数</div>
              <div class="stat-value">{{ employeeStats.goodsCount || 0 }}</div>
            </div>
            <el-icon class="stat-icon"><Goods /></el-icon>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card stat-card-2">
            <div class="stat-content">
              <div class="stat-title">库存总量</div>
              <div class="stat-value">{{ employeeStats.inventoryTotal || 0 }}</div>
            </div>
            <el-icon class="stat-icon"><Box /></el-icon>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card stat-card-3">
            <div class="stat-content">
              <div class="stat-title">库存预警</div>
              <div class="stat-value">{{ employeeStats.inventoryWarning || 0 }}</div>
            </div>
            <el-icon class="stat-icon"><Warning /></el-icon>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <div class="charts-section" v-if="userStore.isAdmin || userStore.isManager">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">收入支出趋势</span>
            </div>
            <div ref="financeChartRef" class="chart-content"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">商品分类分布</span>
            </div>
            <div ref="categoryChartRef" class="chart-content"></div>
          </div>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="8">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">采购审批状态</span>
            </div>
            <div ref="purchaseChartRef" class="chart-content"></div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">仓库库存分布</span>
            </div>
            <div ref="warehouseChartRef" class="chart-content"></div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">员工角色分布</span>
            </div>
            <div ref="roleChartRef" class="chart-content"></div>
          </div>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">采购月度统计</span>
            </div>
            <div ref="purchaseMonthlyChartRef" class="chart-content"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">排班班次分布</span>
            </div>
            <div ref="scheduleChartRef" class="chart-content"></div>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <div class="quick-actions" v-if="userStore.isEmployee">
      <h3>快捷操作</h3>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="action-card" @click="$router.push('/goods')">
            <el-icon :size="40"><Goods /></el-icon>
            <span>商品管理</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card" @click="$router.push('/inventory')">
            <el-icon :size="40"><Box /></el-icon>
            <span>库存管理</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card" @click="$router.push('/purchase')">
            <el-icon :size="40"><ShoppingCart /></el-icon>
            <span>采购申请</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card" @click="$router.push('/my-schedule')">
            <el-icon :size="40"><Calendar /></el-icon>
            <span>我的排班</span>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <div class="warning-section" v-if="warningList.length > 0">
      <h3>库存预警提醒</h3>
      <el-table :data="warningList" stripe style="width: 100%">
        <el-table-column prop="goodsName" label="商品名称" />
        <el-table-column prop="categoryName" label="分类" />
        <el-table-column prop="quantity" label="当前库存">
          <template #default="{ row }">
            <el-tag type="danger">{{ row.quantity }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warningQuantity" label="预警值" />
        <el-table-column prop="warehouse" label="仓库" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="goToPurchase(row)">
              申请采购
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { useUserStore } from '@/store/user'
import { getStatistics, getEmployeeStatistics } from '@/api/dashboard'
import { getInventoryWarning } from '@/api/inventory'

const router = useRouter()
const userStore = useUserStore()

// 管理端统计数据
const statistics = ref({})
// 员工端统计数据
const employeeStats = ref({})
// 库存预警列表
const warningList = ref([])

// 图表引用
const financeChartRef = ref(null)
const categoryChartRef = ref(null)
const purchaseChartRef = ref(null)
const warehouseChartRef = ref(null)
const roleChartRef = ref(null)
const purchaseMonthlyChartRef = ref(null)
const scheduleChartRef = ref(null)

// 当前日期
const currentDate = ref('')
// 当前星期
const currentWeek = ref('')

// 根据当前时间返回问候语
const getGreeting = () => {
  const hour = new Date().getHours()
  if (hour < 6) return '凌晨好'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 17) return '下午好'
  if (hour < 19) return '傍晚好'
  if (hour < 22) return '晚上好'
  return '夜深了'
}

// 更新当前日期和星期
const updateDate = () => {
  const now = new Date()
  const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  currentDate.value = now.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
  currentWeek.value = weekDays[now.getDay()]
}

// 加载统计数据（根据角色加载不同数据）
const loadStatistics = async () => {
  try {
    if (userStore.isAdmin || userStore.isManager) {
      // 管理端：加载完整统计数据
      const res = await getStatistics()
      statistics.value = res.data
      
      // 加载库存预警列表
      const warningRes = await getInventoryWarning()
      warningList.value = warningRes.data || []
      
      // 等待DOM更新后初始化图表
      await nextTick()
      initCharts()
    } else {
      // 员工端：加载简化统计数据
      const res = await getEmployeeStatistics()
      employeeStats.value = res.data
      
      // 加载库存预警列表
      const warningRes = await getInventoryWarning()
      warningList.value = warningRes.data || []
    }
  } catch (error) {
    console.error(error)
  }
}

// 初始化所有图表
const initCharts = () => {
  initFinanceChart()
  initCategoryChart()
  initPurchaseChart()
  initWarehouseChart()
  initRoleChart()
  initPurchaseMonthlyChart()
  initScheduleChart()
}

// 初始化收入支出趋势图（折线图）
const initFinanceChart = () => {
  if (!financeChartRef.value) return
  const chart = echarts.init(financeChartRef.value)
  
  const monthlyData = statistics.value.financeMonthly || []
  const months = monthlyData.map(item => item.month).reverse()
  const incomeData = []
  const expenseData = []
  
  months.forEach(month => {
    const monthItem = monthlyData.find(item => item.month === month)
    if (monthItem) {
      incomeData.push(monthItem.type === 1 ? monthItem.total : 0)
      expenseData.push(monthItem.type === 2 ? monthItem.total : 0)
    }
  })
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['收入', '支出']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: months
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '收入',
        type: 'line',
        smooth: true,
        data: incomeData,
        itemStyle: { color: '#67c23a' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
            { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
          ])
        }
      },
      {
        name: '支出',
        type: 'line',
        smooth: true,
        data: expenseData,
        itemStyle: { color: '#f56c6c' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(245, 108, 108, 0.3)' },
            { offset: 1, color: 'rgba(245, 108, 108, 0.05)' }
          ])
        }
      }
    ]
  }
  
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 初始化商品分类分布图（饼图）
const initCategoryChart = () => {
  if (!categoryChartRef.value) return
  const chart = echarts.init(categoryChartRef.value)
  
  const categoryData = statistics.value.goodsByCategory || []
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: categoryData.map((item, index) => ({
          name: item.category_name || '未分类',
          value: item.count
        }))
      }
    ]
  }
  
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 初始化采购审批状态图（饼图）
const initPurchaseChart = () => {
  if (!purchaseChartRef.value) return
  const chart = echarts.init(purchaseChartRef.value)
  
  const statusData = statistics.value.purchaseByStatus || []
  const statusNames = ['待审批', '已通过', '已拒绝']
  const colors = ['#e6a23c', '#67c23a', '#f56c6c']
  
  const option = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        type: 'pie',
        radius: '70%',
        data: statusData.map((item, index) => ({
          name: statusNames[item.approval_status] || '未知',
          value: item.count,
          itemStyle: { color: colors[item.approval_status] }
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 初始化仓库库存分布图（柱状图）
const initWarehouseChart = () => {
  if (!warehouseChartRef.value) return
  const chart = echarts.init(warehouseChartRef.value)
  
  const warehouseData = statistics.value.inventoryByWarehouse || []
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: warehouseData.map(item => item.warehouse)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        type: 'bar',
        data: warehouseData.map(item => item.total),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#67c23a' },
            { offset: 1, color: '#95d475' }
          ])
        }
      }
    ]
  }
  
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 初始化员工角色分布图（饼图）
const initRoleChart = () => {
  if (!roleChartRef.value) return
  const chart = echarts.init(roleChartRef.value)
  
  const roleData = statistics.value.userByRole || []
  const roleNames = { 1: '超级管理员', 2: '经理', 3: '员工' }
  const colors = { 1: '#f56c6c', 2: '#e6a23c', 3: '#67c23a' }
  
  const option = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        type: 'pie',
        radius: '70%',
        data: roleData.map(item => ({
          name: roleNames[item.role] || '未知',
          value: item.count,
          itemStyle: { color: colors[item.role] }
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 初始化采购月度统计图（柱状图）
const initPurchaseMonthlyChart = () => {
  if (!purchaseMonthlyChartRef.value) return
  const chart = echarts.init(purchaseMonthlyChartRef.value)
  
  const monthlyData = statistics.value.purchaseMonthly || []
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: monthlyData.map(item => item.month).reverse()
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        type: 'bar',
        data: monthlyData.map(item => item.count).reverse(),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#409eff' },
            { offset: 1, color: '#79bbff' }
          ])
        }
      }
    ]
  }
  
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 初始化排班班次分布图（柱状图）
const initScheduleChart = () => {
  if (!scheduleChartRef.value) return
  const chart = echarts.init(scheduleChartRef.value)
  
  const shiftData = statistics.value.scheduleByShift || []
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: shiftData.map(item => item.shift)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        type: 'bar',
        data: shiftData.map(item => item.count),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#e6a23c' },
            { offset: 1, color: '#f3d19e' }
          ])
        }
      }
    ]
  }
  
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 跳转到采购页面（带商品ID参数）
const goToPurchase = (row) => {
  router.push({
    path: '/purchase',
    query: { goodsId: row.goodsId }
  })
}

// 页面挂载时初始化
onMounted(() => {
  updateDate()
  loadStatistics()
})
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 0;
}

.welcome-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #67c23a, #529b2e);
  padding: 30px;
  border-radius: 12px;
  color: #fff;
  margin-bottom: 20px;
  
  .welcome-text {
    h2 {
      font-size: 24px;
      margin-bottom: 8px;
    }
    
    p {
      font-size: 14px;
      opacity: 0.9;
    }
  }
  
  .welcome-date {
    text-align: right;
    
    p {
      margin: 5px 0;
      font-size: 14px;
      opacity: 0.9;
    }
  }
}

.stats-section {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 25px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  position: relative;
  transition: transform 0.3s, box-shadow 0.3s;
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  }
  
  .stat-content {
    .stat-title {
      font-size: 14px;
      color: #909399;
      margin-bottom: 10px;
    }
    
    .stat-value {
      font-size: 32px;
      font-weight: 600;
      color: #303133;
    }
  }
  
  .stat-icon {
    font-size: 50px;
    opacity: 0.8;
    position: absolute;
    right: 25px;
    top: 50%;
    transform: translateY(-50%);
  }
  
  &.stat-card-1 .stat-icon { color: #67c23a; }
  &.stat-card-2 .stat-icon { color: #409eff; }
  &.stat-card-3 .stat-icon { color: #e6a23c; }
  &.stat-card-4 .stat-icon { color: #f56c6c; }
}

.chart-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  
  .chart-header {
    padding-bottom: 15px;
    border-bottom: 1px solid #ebeef5;
    margin-bottom: 15px;
    
    .chart-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }
  
  .chart-content {
    height: 280px;
  }
}

.quick-actions {
  margin-top: 20px;
  
  h3 {
    font-size: 16px;
    color: #303133;
    margin-bottom: 15px;
  }
  
  .action-card {
    background: #fff;
    border-radius: 12px;
    padding: 30px;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 15px;
    cursor: pointer;
    transition: all 0.3s;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
    
    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      
      .el-icon {
        color: #67c23a;
      }
    }
    
    .el-icon {
      color: #606266;
      transition: color 0.3s;
    }
    
    span {
      font-size: 14px;
      color: #606266;
    }
  }
}

.warning-section {
  margin-top: 20px;
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  
  h3 {
    font-size: 16px;
    color: #303133;
    margin-bottom: 15px;
  }
}
</style>
