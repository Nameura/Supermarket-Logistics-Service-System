<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">排班管理</span>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        添加排班
      </el-button>
    </div>
    
    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="员工">
          <el-select style="width: 10vw;" v-model="searchForm.userId" placeholder="请选择员工" filterable clearable>
            <el-option v-for="item in userList" :key="item.id" :label="item.realName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="班次">
          <el-select style="width: 10vw;" v-model="searchForm.shift" placeholder="请选择班次" clearable>
            <el-option label="早班" value="早班" />
            <el-option label="中班" value="中班" />
            <el-option label="晚班" value="晚班" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <el-table :data="tableData" stripe v-loading="loading">
      <el-table-column prop="realName" label="员工姓名" width="100" />
      <el-table-column prop="departmentName" label="部门" width="120" />
      <el-table-column prop="workDate" label="工作日期" width="120">
        <template #default="{ row }">
          {{ formatDate(row.workDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="shift" label="班次" width="80">
        <template #default="{ row }">
          <el-tag>{{ row.shift }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="100">
        <template #default="{ row }">
          {{ formatTime(row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="endTime" label="结束时间" width="100">
        <template #default="{ row }">
          {{ formatTime(row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '上班' : '休息' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑排班' : '添加排班'" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="员工" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择员工" filterable style="width: 100%">
            <el-option v-for="item in userList" :key="item.id" :label="item.realName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="工作日期" prop="workDate">
          <el-date-picker v-model="form.workDate" type="date" placeholder="请选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="班次" prop="shift">
          <el-select v-model="form.shift" placeholder="请选择班次" style="width: 100%">
            <el-option label="早班" value="早班" />
            <el-option label="中班" value="中班" />
            <el-option label="晚班" value="晚班" />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-time-picker v-model="form.startTime" placeholder="请选择开始时间" style="width: 100%" format="HH:mm" value-format="HH:mm:ss" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-time-picker v-model="form.endTime" placeholder="请选择结束时间" style="width: 100%" format="HH:mm" value-format="HH:mm:ss" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">上班</el-radio>
            <el-radio :label="0">休息</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSchedulePage, addSchedule, updateSchedule, deleteSchedule } from '@/api/schedule'
import { getUserList } from '@/api/user'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const userList = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  userId: null,
  shift: ''
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: null,
  userId: null,
  workDate: null,
  shift: '',
  startTime: null,
  endTime: null,
  status: 1,
  remark: ''
})

const rules = {
  userId: [{ required: true, message: '请选择员工', trigger: 'change' }],
  workDate: [{ required: true, message: '请选择工作日期', trigger: 'change' }],
  shift: [{ required: true, message: '请选择班次', trigger: 'change' }]
}

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

const extractTime = (datetime) => {
  if (!datetime) return null
  if (typeof datetime === 'string') {
    if (datetime.includes('T')) {
      return datetime.substring(11, 19)
    }
    return datetime
  }
  const date = new Date(datetime)
  return date.toTimeString().substring(0, 8)
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size
    }
    if (searchForm.userId) {
      params.userId = searchForm.userId
    }
    if (searchForm.shift) {
      params.shift = searchForm.shift
    }
    
    const res = await getSchedulePage(params)
    if (res.data && res.data.records) {
      tableData.value = res.data.records
      pagination.total = res.data.total || 0
    }
  } catch (error) {
    console.error('Load schedule error:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadUsers = async () => {
  try {
    const res = await getUserList()
    userList.value = res.data || []
  } catch (error) {
    console.error('Load users error:', error)
  }
}

const handleSearch = () => {
  pagination.current = 1
  loadData()
}

const resetSearch = () => {
  searchForm.userId = null
  searchForm.shift = ''
  handleSearch()
}

const handleSizeChange = () => {
  pagination.current = 1
  loadData()
}

const handleCurrentChange = () => {
  loadData()
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    userId: null,
    workDate: null,
    shift: '',
    startTime: null,
    endTime: null,
    status: 1,
    remark: ''
  })
}

const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, {
    id: row.id,
    userId: row.userId,
    workDate: row.workDate ? (typeof row.workDate === 'string' ? row.workDate.split('T')[0] : new Date(row.workDate)) : null,
    shift: row.shift,
    startTime: extractTime(row.startTime),
    endTime: extractTime(row.endTime),
    status: row.status,
    remark: row.remark
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const workDateStr = form.workDate || new Date().toISOString().split('T')[0]
        
        const data = {
          userId: form.userId,
          shift: form.shift,
          status: form.status,
          remark: form.remark || '',
          workDate: workDateStr + 'T00:00:00',
          startTime: workDateStr + 'T' + (form.startTime || '08:00:00'),
          endTime: workDateStr + 'T' + (form.endTime || '17:00:00')
        }
        
        if (form.id) {
          data.id = form.id
          await updateSchedule(data)
          ElMessage.success('修改成功')
        } else {
          await addSchedule(data)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error('Submit error:', error)
        ElMessage.error('操作失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该排班记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSchedule(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('Delete error:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

onMounted(() => {
  loadData()
  loadUsers()
})
</script>

<style lang="scss" scoped>
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
