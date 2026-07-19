<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">我的排班</span>
      <div class="header-actions">
        <el-date-picker
          v-model="selectedYear"
          type="year"
          placeholder="选择年份"
          format="YYYY年"
          value-format="YYYY"
          @change="handleYearChange"
        />
      </div>
    </div>
    
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-info">
              <div class="stat-value">{{ stats.total }}</div>
              <div class="stat-label">总排班数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-info">
              <div class="stat-value">{{ stats.morning }}</div>
              <div class="stat-label">早班次数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-info">
              <div class="stat-value">{{ stats.afternoon }}</div>
              <div class="stat-label">中班次数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-info">
              <div class="stat-value">{{ stats.night }}</div>
              <div class="stat-label">晚班次数</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <el-card class="calendar-card">
      <template #header>
        <div class="card-header">
          <span>{{ selectedYear }}年排班日历</span>
          <div class="legend">
            <span class="legend-item">
              <span class="legend-color morning"></span>
              早班
            </span>
            <span class="legend-item">
              <span class="legend-color afternoon"></span>
              中班
            </span>
            <span class="legend-item">
              <span class="legend-color night"></span>
              晚班
            </span>
          </div>
        </div>
      </template>
      <div ref="calendarChart" class="calendar-chart"></div>
    </el-card>
    
    <el-card class="detail-card">
      <template #header>
        <div class="card-header">
          <span>排班详情</span>
          <el-radio-group v-model="filterShift" size="small">
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button label="早班">早班</el-radio-button>
            <el-radio-button label="中班">中班</el-radio-button>
            <el-radio-button label="晚班">晚班</el-radio-button>
          </el-radio-group>
        </div>
      </template>
      
      <el-timeline v-if="filteredSchedules.length > 0">
        <el-timeline-item
          v-for="item in filteredSchedules"
          :key="item.id"
          :type="getShiftType(item.shift)"
          :timestamp="formatDate(item.workDate)"
          placement="top"
        >
          <el-card class="schedule-item" :body-style="{ padding: '15px' }">
            <div class="schedule-content">
              <div class="shift-info">
                <el-tag :type="getShiftType(item.shift)" size="large">
                  {{ item.shift }}
                </el-tag>
                <span class="time-range">
                  <el-icon><Clock /></el-icon>
                  {{ formatTime(item.startTime) }} - {{ formatTime(item.endTime) }}
                </span>
              </div>
              <div class="schedule-status">
                <el-tag :type="item.status === 1 ? 'success' : 'info'" size="small">
                  {{ item.status === 1 ? '上班' : '休息' }}
                </el-tag>
              </div>
              <div class="schedule-remark" v-if="item.remark">
                <el-icon><Document /></el-icon>
                {{ item.remark }}
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      
      <el-empty v-else description="暂无排班记录" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { getMySchedule } from '@/api/schedule'
import * as echarts from 'echarts'

const loading = ref(false)
const schedules = ref([])
const filterShift = ref('')
const selectedYear = ref(new Date().getFullYear().toString())
const calendarChart = ref(null)
let chartInstance = null

const stats = reactive({
  total: 0,
  morning: 0,
  afternoon: 0,
  night: 0
})

const filteredSchedules = computed(() => {
  if (!filterShift.value) {
    return schedules.value
  }
  return schedules.value.filter(s => s.shift === filterShift.value)
})

const formatDate = (date) => {
  if (!date) return '-'
  if (typeof date === 'string') return date.split('T')[0]
  return new Date(date).toLocaleDateString('zh-CN')
}

const formatTime = (time) => {
  if (!time) return '-'
  if (typeof time === 'string') {
    return time.substring(11, 16)
  }
  const date = new Date(time)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

const getShiftType = (shift) => {
  const types = {
    '早班': 'success',
    '中班': 'warning',
    '晚班': 'info'
  }
  return types[shift] || ''
}

const getShiftValue = (shift) => {
  const values = {
    '早班': 1,
    '中班': 2,
    '晚班': 3
  }
  return values[shift] || 0
}

const initChart = () => {
  if (!calendarChart.value) return
  
  if (chartInstance) {
    chartInstance.dispose()
  }
  
  chartInstance = echarts.init(calendarChart.value)
  
  const year = parseInt(selectedYear.value)
  const startDate = `${year}-01-01`
  const endDate = `${year}-12-31`
  
  const scheduleMap = new Map()
  schedules.value.forEach(s => {
    const date = formatDate(s.workDate)
    scheduleMap.set(date, s.shift)
  })
  
  const data = []
  const current = new Date(startDate)
  const end = new Date(endDate)
  
  while (current <= end) {
    const dateStr = current.toISOString().split('T')[0]
    const shift = scheduleMap.get(dateStr)
    if (shift) {
      data.push([dateStr, getShiftValue(shift)])
    } else {
      data.push([dateStr, 0])
    }
    current.setDate(current.getDate() + 1)
  }
  
  const option = {
    tooltip: {
      formatter: function(params) {
        const date = params.value[0]
        const value = params.value[1]
        const shiftMap = { 0: '休息', 1: '早班', 2: '中班', 3: '晚班' }
        return `${date}<br/>${shiftMap[value] || '无排班'}`
      }
    },
    visualMap: {
      min: 0,
      max: 3,
      calculable: false,
      orient: 'horizontal',
      left: 'center',
      top: 0,
      show: false,
      inRange: {
        color: ['#f5f7fa', '#67c23a', '#e6a23c', '#909399']
      }
    },
    calendar: {
      top: 60,
      left: 30,
      right: 30,
      cellSize: ['auto', 30],
      range: year,
      itemStyle: {
        borderWidth: 3,
        borderColor: '#fff',
        borderRadius: 4
      },
      yearLabel: { show: false },
      monthLabel: {
        show: true,
        nameMap: 'ZH',
        fontSize: 14,
        color: '#606266'
      },
      dayLabel: {
        firstDay: 1,
        nameMap: 'ZH',
        fontSize: 12,
        color: '#909399'
      },
      splitLine: {
        show: true,
        lineStyle: {
          color: '#e4e7ed',
          width: 1
        }
      }
    },
    series: [{
      type: 'heatmap',
      coordinateSystem: 'calendar',
      data: data,
      itemStyle: {
        borderRadius: 4
      }
    }]
  }
  
  chartInstance.setOption(option)
  
  chartInstance.on('click', function(params) {
    if (params.value) {
      const date = params.value[0]
      const schedule = schedules.value.find(s => formatDate(s.workDate) === date)
      if (schedule) {
        ElMessage.info(`${date} - ${schedule.shift} (${formatTime(schedule.startTime)} - ${formatTime(schedule.endTime)})`)
      }
    }
  })
}

const handleYearChange = () => {
  initChart()
}

// 加载我的排班数据
const loadMySchedule = async () => {
  loading.value = true
  try {
    const res = await getMySchedule()
    schedules.value = res.data || []
    // 更新统计数据
    stats.total = schedules.value.length
    stats.morning = schedules.value.filter(s => s.shift === '早班').length
    stats.afternoon = schedules.value.filter(s => s.shift === '中班').length
    stats.night = schedules.value.filter(s => s.shift === '晚班').length
    // 初始化图表
    initChart()
  } catch (error) {
    console.error('Load my schedule error:', error)
    ElMessage.error('加载排班数据失败')
  } finally {
    loading.value = false
  }
}

const handleResize = () => {
  if (chartInstance) {
    chartInstance.resize()
  }
}

onMounted(() => {
  loadMySchedule()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  if (chartInstance) {
    chartInstance.dispose()
  }
})

watch(filterShift, () => {
})
</script>

<style lang="scss" scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  .page-title {
    font-size: 20px;
    font-weight: 600;
    color: #303133;
  }
  
  .header-actions {
    display: flex;
    gap: 10px;
  }
}

.stats-cards {
  margin-bottom: 20px;
  
  .stat-card {
    background: #fff;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
    transition: transform 0.3s, box-shadow 0.3s;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    }
    
    .stat-info {
      text-align: center;
      
      .stat-value {
        font-size: 32px;
        font-weight: 600;
        color: #303133;
      }
      
      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-top: 8px;
      }
    }
  }
}

.calendar-card {
  margin-bottom: 20px;
  border-radius: 12px;
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .legend {
      display: flex;
      gap: 20px;
      
      .legend-item {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 13px;
        color: #606266;
        
        .legend-color {
          width: 16px;
          height: 16px;
          border-radius: 4px;
          
          &.morning {
            background: #67c23a;
          }
          
          &.afternoon {
            background: #e6a23c;
          }
          
          &.night {
            background: #909399;
          }
        }
      }
    }
  }
  
  .calendar-chart {
    height: 350px;
  }
}

.detail-card {
  border-radius: 12px;
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .schedule-item {
    margin-top: 0;
    
    .schedule-content {
      .shift-info {
        display: flex;
        align-items: center;
        gap: 15px;
        margin-bottom: 10px;
        
        .time-range {
          display: flex;
          align-items: center;
          gap: 5px;
          color: #606266;
          font-size: 14px;
        }
      }
      
      .schedule-status {
        margin-bottom: 10px;
      }
      
      .schedule-remark {
        display: flex;
        align-items: center;
        gap: 5px;
        color: #909399;
        font-size: 13px;
        padding: 8px 12px;
        background: #f5f7fa;
        border-radius: 6px;
      }
    }
  }
}
</style>
