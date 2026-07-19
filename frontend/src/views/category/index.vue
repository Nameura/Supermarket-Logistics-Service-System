<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">商品分类管理</span>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        添加分类
      </el-button>
    </div>
    
    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="分类名称">
          <el-input v-model="searchForm.name" placeholder="请输入分类名称" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select class="categorySelect" v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序方式">
          <el-select class="categorySelect" v-model="searchForm.orderBy" placeholder="请选择排序方式" @change="handleSearch">
            <el-option label="按排序号排序" value="sort" />
            <el-option label="按ID排序" value="id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <el-table :data="tableData" stripe v-loading="loading" row-key="id">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="parentName" label="父级分类">
        <template #default="{ row }">
          {{ row.parentName || '顶级分类' }}
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="100">
        <template #default="{ row }">
          <el-tag type="info">{{ row.sort }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template #default="{ row }">
          {{ formatDateTime(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
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
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="父级分类" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="categoryTreeData"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="请选择父级分类（不选则为顶级分类）"
            clearable
            check-strictly
            :render-after-expand="false"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" style="width: 100%" />
          <div class="form-tip">数字越小，排序越靠前</div>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCategoryPage, getCategoryList, addCategory, updateCategory, deleteCategory } from '@/api/category'

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
  name: '',
  status: null,
  orderBy: 'sort'
})

// 分页参数
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 分类表单数据
const form = reactive({
  id: null,
  name: '',
  parentId: null,
  sort: 0,
  status: 1,
  description: ''
})

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

// 所有分类列表（用于构建树形选择器）
const allCategories = ref([])

// 计算属性：将扁平分类列表转换为树形结构，用于父级分类选择器
const categoryTreeData = computed(() => {
  // 递归构建树形结构
  const buildTree = (data, parentId = null, excludeId = null) => {
    return data
      .filter(item => {
        // 统一处理parentId，将0转为null
        const itemParentId = item.parentId == null || item.parentId === 0 ? null : item.parentId
        // 过滤出当前层级的子节点，并排除当前编辑的分类（防止选择自己作为父级）
        return itemParentId === parentId && item.id !== excludeId
      })
      .map(item => ({
        id: item.id,
        name: item.name,
        // 递归构建子节点
        children: buildTree(data, item.id, excludeId)
      }))
  }
  
  // 编辑时排除当前分类，防止选择自己作为父级
  const excludeId = form.id
  return buildTree(allCategories.value, null, excludeId)
})

// 计算属性：对话框标题
const dialogTitle = computed(() => form.id ? '编辑分类' : '添加分类')

// 格式化日期时间
const formatDateTime = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

// 加载分类列表数据（分页查询）
const loadData = async () => {
  loading.value = true
  try {
    // 构建请求参数
    const params = {
      current: pagination.current,
      size: pagination.size,
      orderBy: searchForm.orderBy
    }
    // 条件参数：分类名称
    if (searchForm.name) {
      params.name = searchForm.name
    }
    // 条件参数：状态
    if (searchForm.status !== null && searchForm.status !== undefined) {
      params.status = searchForm.status
    }
    
    const res = await getCategoryPage(params)
    if (res.data && res.data.records) {
      tableData.value = res.data.records
      pagination.total = res.data.total
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 加载所有分类列表（用于树形选择器）
const loadAllCategories = async () => {
  try {
    const res = await getCategoryList()
    allCategories.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

// 搜索按钮点击事件
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置搜索条件
const resetSearch = () => {
  searchForm.name = ''
  searchForm.status = null
  searchForm.orderBy = 'sort'
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
  form.id = null
  form.name = ''
  form.parentId = null
  form.sort = 0
  form.status = 1
  form.description = ''
}

// 添加分类按钮点击事件
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑分类按钮点击事件
const handleEdit = (row) => {
  Object.assign(form, {
    id: row.id,
    name: row.name,
    // 将0转为null，便于树形选择器显示
    parentId: row.parentId == null || row.parentId === 0 ? null : row.parentId,
    sort: row.sort,
    status: row.status,
    description: row.description
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
        // 构建提交数据
        const data = {
          name: form.name,
          // parentId为空时设为0，表示顶级分类
          parentId: form.parentId || 0,
          sort: form.sort || 0,
          status: form.status,
          description: form.description || ''
        }
        
        // 根据是否有id判断新增还是编辑
        if (form.id) {
          data.id = form.id
          await updateCategory(data)
          ElMessage.success('修改成功')
        } else {
          await addCategory(data)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        // 刷新列表和分类树
        loadData()
        loadAllCategories()
      } catch (error) {
        console.error(error)
        ElMessage.error('操作失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 删除分类
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCategory(row.id)
      ElMessage.success('删除成功')
      // 刷新列表和分类树
      loadData()
      loadAllCategories()
    } catch (error) {
      console.error(error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 页面挂载时加载数据
onMounted(() => {
  loadData()
  loadAllCategories()
})
</script>

<style lang="scss" scoped>
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.categorySelect {
  width: 10vw;
}
</style>
