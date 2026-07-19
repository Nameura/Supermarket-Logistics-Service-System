<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">库存管理</span>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        添加库存
      </el-button>
    </div>
    
    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.goodsName" placeholder="请输入商品名称" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="商品分类">
          <el-tree-select 
            style="width: 150px;" 
            v-model="searchForm.categoryId" 
            :data="categoryTree"
            :props="{ label: 'name', value: 'id' }" 
            placeholder="请选择分类" 
            clearable 
            check-strictly 
          />
        </el-form-item>
        <el-form-item label="仓库">
          <el-select style="width: 10vw;" v-model="searchForm.warehouse" placeholder="请选择仓库" clearable>
            <el-option label="一号仓库" value="一号仓库" />
            <el-option label="二号仓库" value="二号仓库" />
            <el-option label="三号仓库" value="三号仓库" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="warning" @click="showWarning">库存预警</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <el-table :data="tableData" stripe v-loading="loading" @sort-change="handleSortChange">
      <el-table-column prop="id" label="ID" width="80" sortable="custom" />
      <el-table-column prop="goodsImage" label="商品图片" width="100">
        <template #default="{ row }">
          <img 
            :src="getImageUrl(row.goodsImage)" 
            class="goods-image"
            @click.stop="handlePreviewImage(getImageUrl(row.goodsImage))"
            @error="handleImageError"
          />
        </template>
      </el-table-column>
      <el-table-column prop="goodsName" label="商品名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="categoryName" label="分类" width="100" />
      <el-table-column prop="barcode" label="条码" width="130" />
      <el-table-column prop="quantity" label="库存数量" width="100">
        <template #default="{ row }">
          <el-tag :type="row.quantity <= row.warningQuantity ? 'danger' : 'success'">
            {{ row.quantity }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="warningQuantity" label="预警值" width="80" />
      <el-table-column prop="unit" label="单位" width="60" />
      <el-table-column prop="salePrice" label="销售价" width="90">
        <template #default="{ row }">
          ¥{{ row.salePrice?.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="warehouse" label="仓库" width="100" />
      <el-table-column prop="location" label="库位" width="100" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="warning" link size="small" @click="handlePurchase(row)">采购</el-button>
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
    
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑库存' : '添加库存'" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="商品" prop="goodsId" v-if="!form.id">
          <el-select v-model="form.goodsId" placeholder="请选择商品" filterable style="width: 100%">
            <el-option v-for="item in goodsList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="库存数量" prop="quantity">
          <el-input-number v-model="form.quantity" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="预警数量" prop="warningQuantity">
          <el-input-number v-model="form.warningQuantity" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="仓库" prop="warehouse">
          <el-select v-model="form.warehouse" placeholder="请选择仓库" style="width: 100%">
            <el-option label="一号仓库" value="一号仓库" />
            <el-option label="二号仓库" value="二号仓库" />
            <el-option label="三号仓库" value="三号仓库" />
          </el-select>
        </el-form-item>
        <el-form-item label="库位" prop="location">
          <el-input v-model="form.location" placeholder="请输入库位" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
    
    <el-dialog v-model="warningVisible" title="库存预警列表" width="900px">
      <el-table :data="warningList" stripe>
        <el-table-column prop="goodsName" label="商品名称" />
        <el-table-column prop="categoryName" label="分类" />
        <el-table-column prop="quantity" label="当前库存">
          <template #default="{ row }">
            <el-tag type="danger">{{ row.quantity }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warningQuantity" label="预警值" />
        <el-table-column prop="warehouse" label="仓库" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handlePurchase(row)">采购</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    
    <!-- 图片预览对话框 -->
    <el-dialog 
      v-model="imagePreviewVisible" 
      title="图片预览" 
      width="800px" 
      center
      destroy-on-close
    >
      <img :src="previewImageUrl" class="preview-image" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getInventoryPage, getInventoryWarning, addInventory, updateInventory, deleteInventory } from '@/api/inventory'
import { getGoodsList } from '@/api/goods'
import { getCategoryTree } from '@/api/category'

const router = useRouter()
const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const goodsList = ref([])
const categoryTree = ref([])
const dialogVisible = ref(false)
const warningVisible = ref(false)
const warningList = ref([])
const formRef = ref(null)

// 图片预览
const imagePreviewVisible = ref(false)
const previewImageUrl = ref('')

const searchForm = reactive({
  goodsName: '',
  categoryId: null,
  warehouse: ''
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const sortInfo = reactive({
  field: 'id',
  order: 'asc'
})

const form = reactive({
  id: null,
  goodsId: null,
  quantity: 0,
  warningQuantity: 10,
  warehouse: '',
  location: '',
  status: 1
})

const rules = {
  goodsId: [{ required: true, message: '请选择商品', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入库存数量', trigger: 'blur' }]
}

const getImageUrl = (image) => {
  if (!image) return ''
  if (image.startsWith('http') || image.startsWith('data:')) {
    return image
  }
  return '/api' + image
}

// 图片预览
const handlePreviewImage = (url) => {
  if (!url) {
    ElMessage.warning('图片地址为空')
    return
  }
  previewImageUrl.value = url
  imagePreviewVisible.value = true
}

const handleImageError = (e) => {
  e.target.src = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMDAgMTAwIj48Y2lyY2xlIGN4PSI1MCIgY3k9IjUwIiByPSI1MCIgZmlsbD0iI2Y1ZjdmYSIvPjx0ZXh0IHg9IjUwIiB5PSI1NSIgZm9udC1zaXplPSIxMiIgZmlsbD0iIzkwOTM5OSIgdGV4dC1hbmNob3I9Im1pZGRsZSI+5aSx5pWwPC90ZXh0Pjwvc3ZnPg=='
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getInventoryPage({
      current: pagination.current,
      size: pagination.size,
      ...searchForm,
      field: sortInfo.field,
      order: sortInfo.order
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const loadGoods = async () => {
  try {
    const res = await getGoodsList()
    goodsList.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const loadCategories = async () => {
  try {
    const res = await getCategoryTree()
    categoryTree.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const handleSearch = () => {
  pagination.current = 1
  loadData()
}

const resetSearch = () => {
  searchForm.goodsName = ''
  searchForm.categoryId = null
  searchForm.warehouse = ''
  handleSearch()
}

const handleSizeChange = () => {
  pagination.current = 1
  loadData()
}

const handleCurrentChange = () => {
  loadData()
}

// 处理排序
const handleSortChange = ({ prop, order }) => {
  if (prop && order) { // 排序
    sortInfo.field = prop
    sortInfo.order = order === 'ascending' ? 'asc' : 'desc'
  } else { // 重置排序
    sortInfo.field = 'id'
    sortInfo.order = 'asc'
  }
  loadData()
}

// 处理库存预警
const showWarning = async () => { 
  try { 
    // 获取库存预警列表
    const res = await getInventoryWarning()
    warningList.value = res.data
    warningVisible.value = true
  } catch (error) {
    console.error(error)
  }
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    goodsId: null,
    quantity: 0,
    warningQuantity: 10,
    warehouse: '',
    location: '',
    status: 1
  })
}

const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, {
    id: row.id,
    goodsId: row.goodsId,
    quantity: row.quantity,
    warningQuantity: row.warningQuantity,
    warehouse: row.warehouse,
    location: row.location,
    status: row.status
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.id) {
          await updateInventory(form)
          ElMessage.success('修改成功')
        } else {
          await addInventory(form)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error(error)
        ElMessage.error('操作失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该库存记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteInventory(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handlePurchase = (row) => {
  router.push({
    path: '/purchase',
    query: { goodsId: row.goodsId }
  })
}

onMounted(() => {
  loadData()
  loadGoods()
  loadCategories()
})
</script>

<style lang="scss" scoped>
.goods-image {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  cursor: pointer;
  object-fit: cover;
  
  &:hover {
    opacity: 0.8;
  }
}

.preview-image {
  width: 100%;
  max-height: 70vh;
  object-fit: contain;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
