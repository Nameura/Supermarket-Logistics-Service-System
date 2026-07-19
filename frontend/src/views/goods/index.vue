<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">商品管理</span>
      <el-button type="primary" @click="handleAdd">
        <el-icon>
          <Plus />
        </el-icon>
        添加商品
      </el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="商品分类">
          <el-tree-select class="goodSelect" v-model="searchForm.categoryId" :data="categoryTree"
            :props="{ label: 'name', value: 'id' }" placeholder="请选择分类" clearable check-strictly />
        </el-form-item>
        <el-form-item label="状态">
          <el-select class="goodSelect" v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" stripe v-loading="loading" @sort-change="handleSortChange">
      <el-table-column prop="id" label="ID" width="80" sortable="custom" />
      <el-table-column prop="image" label="图片" width="100">
        <template #default="{ row }">
          <img :src="getImageUrl(row.image)" class="goods-image"
            @click.stop="handlePreviewImage(getImageUrl(row.image))" @error="handleImageError" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="categoryName" label="分类" width="120" />
      <el-table-column prop="barcode" label="条码" width="130" />
      <el-table-column prop="specification" label="规格" width="100" />
      <el-table-column prop="unit" label="单位" width="60" />
      <el-table-column prop="purchasePrice" label="进货价" width="90">
        <template #default="{ row }">
          ¥{{ row.purchasePrice?.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="salePrice" label="销售价" width="90">
        <template #default="{ row }">
          ¥{{ row.salePrice?.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="brand" label="品牌" width="100" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="row.status === 0" type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页条 -->
    <div class="pagination-container">
      <el-pagination v-model:current-page="pagination.current" v-model:page-size="pagination.size"
        :total="pagination.total" :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

    <!-- 图片预览对话框 -->
    <el-dialog v-model="imagePreviewVisible" title="图片预览" width="800px" center destroy-on-close>
      <img :src="previewImageUrl" class="preview-image" />
    </el-dialog>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑商品' : '添加商品'" width="700px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="商品名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入商品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品分类" prop="categoryId">
              <el-tree-select v-model="form.categoryId" :data="categoryTree" :props="{ label: 'name', value: 'id' }"
                placeholder="请选择分类" check-strictly />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="商品条码" prop="barcode">
              <el-input v-model="form.barcode" placeholder="请输入商品条码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格" prop="specification">
              <el-input v-model="form.specification" placeholder="请输入规格" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="请输入单位" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="品牌" prop="brand">
              <el-input v-model="form.brand" placeholder="请输入品牌" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="进货价" prop="purchasePrice">
              <el-input-number v-model="form.purchasePrice" :precision="2" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="销售价" prop="salePrice">
              <el-input-number v-model="form.salePrice" :precision="2" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplier">
              <el-select v-model="form.supplier" placeholder="请选择供应商" clearable filterable style="width: 100%">
                <el-option v-for="item in supplierList" :key="item.id" :label="item.name" :value="item.name" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产地" prop="origin">
              <el-input v-model="form.origin" placeholder="请输入产地" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="商品图片" prop="image">
          <el-upload class="image-uploader" action="/api/upload/goods" :headers="{ Authorization: 'Bearer ' + token }"
            :show-file-list="false" :on-success="handleImageSuccess" :before-upload="beforeImageUpload">
            <el-image v-if="form.image" :src="getImageUrl(form.image)" fit="cover" class="uploaded-image">
              <template #error>
                <div class="image-placeholder">
                  <el-icon>
                    <Picture />
                  </el-icon>
                </div>
              </template>
            </el-image>
            <div v-else class="upload-placeholder">
              <el-icon class="upload-icon">
                <Plus />
              </el-icon>
              <span>点击上传图片</span>
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入商品描述" />
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
import { getGoodsPage, addGoods, updateGoods, deleteGoods } from '@/api/goods'
import { getCategoryList } from '@/api/category'
import { getSupplierList } from '@/api/supplier'

// 从sessionStorage获取token，用于图片上传认证
const token = sessionStorage.getItem('token')
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
// 所有分类列表
const allCategories = ref([])
// 供应商列表
const supplierList = ref([])

// 图片预览相关
const imagePreviewVisible = ref(false)
const previewImageUrl = ref('')

// 搜索表单
const searchForm = reactive({
  name: '',
  categoryId: null,
  status: null
})

// 分页参数
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 排序信息
const sortInfo = reactive({
  field: 'id',
  order: 'asc'
})

// 商品表单数据
const form = reactive({
  id: null,
  name: '',
  categoryId: null,
  barcode: '',
  specification: '',
  unit: '',
  purchasePrice: 0,
  salePrice: 0,
  image: '',
  description: '',
  status: 1,
  supplier: '',
  brand: '',
  origin: ''
})

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  salePrice: [{ required: true, message: '请输入销售价', trigger: 'blur' }]
}

// 计算属性：将扁平分类列表转换为树形结构
const categoryTree = computed(() => {
  const buildTree = (data, parentId = 0) => {
    return data
      .filter(item => (item.parentId || 0) === parentId)
      .map(item => ({
        id: item.id,
        name: item.name,
        children: buildTree(data, item.id)
      }))
  }
  return buildTree(allCategories.value)
})

// 获取图片完整URL，处理相对路径和绝对路径
const getImageUrl = (image) => {
  if (!image) return ''
  if (image.startsWith('http') || image.startsWith('data:')) {
    return image
  }
  return '/api' + image
}

// 图片预览处理
const handlePreviewImage = (url) => {
  if (!url) {
    ElMessage.warning('图片地址为空')
    return
  }
  previewImageUrl.value = url
  imagePreviewVisible.value = true
}

// 图片加载失败时显示默认占位图
const handleImageError = (e) => {
  e.target.src = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMDAgMTAwIj48Y2lyY2xlIGN4PSI1MCIgY3k9IjUwIiByPSI1MCIgZmlsbD0iI2Y1ZjdmYSIvPjx0ZXh0IHg9IjUwIiB5PSI1NSIgZm9udC1zaXplPSIxMiIgZmlsbD0iIzkwOTM5OSIgdGV4dC1hbmNob3I9Im1pZGRsZSI+5aSx5pWwPC90ZXh0Pjwvc3ZnPg=='
}

// 加载商品列表数据（分页查询）
const loadData = async () => {
  loading.value = true
  try {
    const res = await getGoodsPage({
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

// 加载商品分类列表
const loadCategories = async () => {
  try {
    const res = await getCategoryList()
    allCategories.value = res.data
  } catch (error) {
    console.error(error)
  }
}

// 加载供应商列表
const loadSuppliers = async () => {
  try {
    const res = await getSupplierList()
    supplierList.value = res.data || []
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
  searchForm.categoryId = null
  searchForm.status = null
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

// 表格排序改变事件
const handleSortChange = ({ prop, order }) => {
  if (prop && order) {
    sortInfo.field = prop
    sortInfo.order = order === 'ascending' ? 'asc' : 'desc'
  } else {
    sortInfo.field = 'id'
    sortInfo.order = 'asc'
  }
  loadData()
}

// 重置表单数据
const resetForm = () => {
  Object.assign(form, {
    id: null,
    name: '',
    categoryId: null,
    barcode: '',
    specification: '',
    unit: '',
    purchasePrice: 0,
    salePrice: 0,
    image: '',
    description: '',
    status: 1,
    supplier: '',
    brand: '',
    origin: ''
  })
}

// 添加商品按钮点击事件
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑商品按钮点击事件
const handleEdit = (row) => {
  Object.assign(form, {
    id: row.id,
    name: row.name,
    categoryId: row.categoryId,
    barcode: row.barcode,
    specification: row.specification,
    unit: row.unit,
    purchasePrice: row.purchasePrice,
    salePrice: row.salePrice,
    image: row.image,
    description: row.description,
    status: row.status,
    supplier: row.supplier,
    brand: row.brand,
    origin: row.origin
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
        if (form.id) {
          await updateGoods(form)
          ElMessage.success('修改成功')
        } else {
          await addGoods(form)
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

// 删除商品
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteGoods(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
      ElMessage.error('删除失败')
    }
  }).catch(() => { })
}

// 图片上传前校验（文件类型和大小）
const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 图片上传成功回调
const handleImageSuccess = (response) => {
  if (response.code === 200) {
    form.image = response.data
    ElMessage.success('上传成功')
  } else {
    ElMessage.error('上传失败')
  }
}

// 页面挂载时加载数据
onMounted(() => {
  loadData()
  loadCategories()
  loadSuppliers()
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

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;
  font-size: 24px;
}

.preview-image {
  width: 100%;
  max-height: 70vh;
  object-fit: contain;
}

.image-preview-dialog {
  :deep(.el-dialog__body) {
    padding: 10px;
    text-align: center;
  }
}

.image-uploader {
  :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: border-color 0.3s;

    &:hover {
      border-color: #409eff;
    }
  }

  .upload-placeholder {
    width: 120px;
    height: 120px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: #8c939d;

    .upload-icon {
      font-size: 28px;
      margin-bottom: 8px;
    }

    span {
      font-size: 12px;
    }
  }

  .uploaded-image {
    width: 120px;
    height: 120px;
    display: block;
  }
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.goodSelect {
  width: 10vw;
}
</style>
