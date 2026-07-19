<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">财务管理</span>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        添加记录
      </el-button>
    </div>
    
    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="记录编号">
          <el-input v-model="searchForm.recordNo" placeholder="请输入记录编号" clearable />
        </el-form-item>
        <el-form-item label="类型">
          <el-select style="width: 10vw;"  v-model="searchForm.type" placeholder="请选择类型" clearable>
            <el-option label="收入" :value="1" />
            <el-option label="支出" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="searchForm.category" placeholder="请输入分类" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <el-table :data="tableData" stripe v-loading="loading">
      <el-table-column prop="recordNo" label="记录编号" width="180" />
      <el-table-column prop="typeName" label="类型" width="80">
        <template #default="{ row }">
          <el-tag :type="row.type === 1 ? 'success' : 'danger'">
            {{ row.typeName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="分类" width="120" />
      <el-table-column prop="amount" label="金额" width="120">
        <template #default="{ row }">
          <span :style="{ color: row.type === 1 ? '#67c23a' : '#f56c6c' }">
            ¥{{ row.amount?.toFixed(2) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="operatorName" label="操作人" width="100" />
      <el-table-column prop="recordDate" label="记录日期" width="120">
        <template #default="{ row }">
          {{ formatDate(row.recordDate) }}
        </template>
      </el-table-column>
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
    
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑记录' : '添加记录'" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio :label="1">收入</el-radio>
            <el-radio :label="2">支出</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-input v-model="form.category" placeholder="请输入分类" />
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="form.amount" :precision="2" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="记录日期" prop="recordDate">
          <el-date-picker v-model="form.recordDate" type="date" placeholder="请选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
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
import { getFinancePage, addFinance, updateFinance, deleteFinance } from '@/api/finance'

// 页面加载状态
const loading = ref(false)
// 表单提交加载状态
const submitLoading = ref(false)
// 表格数据
const tableData = ref([])
// 对话框显示状态
const dialogVisible = ref(false)
// 表单引用
const formRef = ref(null)

// 搜索表单
const searchForm = reactive({
  recordNo: '',
  type: null,
  category: ''
})

// 分页参数
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 财务记录表单数据
const form = reactive({
  id: null,
  type: 1,
  category: '',
  amount: 0,
  description: '',
  recordDate: new Date().toISOString().split('T')[0]
})

// 表单验证规则
const rules = {
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  category: [{ required: true, message: '请输入分类', trigger: 'blur' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  recordDate: [{ required: true, message: '请选择日期', trigger: 'change' }]
}

// 格式化日期显示
const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

// 加载财务记录列表（分页查询）
const loadData = async () => {
  loading.value = true
  try {
    const res = await getFinancePage({
      current: pagination.current,
      size: pagination.size,
      ...searchForm
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 搜索按钮点击事件
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置搜索条件
const resetSearch = () => {
  searchForm.recordNo = ''
  searchForm.type = null
  searchForm.category = ''
  handleSearch()
}

// 分页大小改变事件
const handleSizeChange = () => {
  pagination.current = 1
  loadData()
}

// 当前页码改变事件
const handleCurrentChange = () => {
  loadData()
}

// 重置表单数据
const resetForm = () => {
  Object.assign(form, {
    id: null,
    type: 1,
    category: '',
    amount: 0,
    description: '',
    recordDate: new Date().toISOString().split('T')[0]
  })
}

// 添加记录按钮点击事件
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑记录按钮点击事件
const handleEdit = (row) => {
  // 处理日期格式，兼容字符串和Date对象
  let recordDate = row.recordDate
  if (recordDate) {
    if (typeof recordDate === 'string') {
      recordDate = recordDate.split(' ')[0].split('T')[0]
    } else {
      recordDate = new Date(recordDate).toISOString().split('T')[0]
    }
  }
  Object.assign(form, {
    id: row.id,
    type: row.type,
    category: row.category,
    amount: row.amount,
    description: row.description,
    recordDate: recordDate
  })
  dialogVisible.value = true
}

// 提交表单（新增/编辑）
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        // 构建提交数据，日期添加时间部分
        const submitData = {
          ...form,
          recordDate: form.recordDate + ' 00:00:00'
        }
        // 根据是否有id判断新增还是编辑
        if (form.id) {
          await updateFinance(submitData)
          ElMessage.success('修改成功')
        } else {
          await addFinance(submitData)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 删除财务记录
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteFinance(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
}

// 页面挂载时加载数据
onMounted(() => {
  loadData()
})
</script>
