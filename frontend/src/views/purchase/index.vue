<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">采购管理</span>
      <div class="header-actions">
        <el-button 
          type="success" 
          @click="handleBatchApprove"
          :disabled="selectedRows.length === 0"
          v-if="userStore.isAdmin || userStore.isManager"
        >
          <el-icon><Select /></el-icon>
          批量审批 ({{ selectedRows.length }})
        </el-button>
        <el-button type="primary" @click="handleAdd" v-if="userStore.isEmployee || userStore.isAdmin">
          <el-icon><Plus /></el-icon>
          发起采购
        </el-button>
      </div>
    </div>
    
    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="采购单号">
          <el-input v-model="searchForm.requestNo" placeholder="请输入采购单号" clearable />
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.goodsName" placeholder="请输入商品名称" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="审批状态">
          <el-select style="width: 10vw;" v-model="searchForm.approvalStatus" placeholder="请选择状态" clearable>
            <el-option label="待审批" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已拒绝" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <el-table 
      :data="tableData" 
      stripe 
      v-loading="loading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column 
        type="selection" 
        width="55"
        :selectable="checkSelectable"
        v-if="userStore.isAdmin || userStore.isManager"
      />
      <el-table-column prop="requestNo" label="采购单号" width="180" />
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
      <el-table-column prop="goodsName" label="商品名称" min-width="150" />
      <el-table-column prop="supplierName" label="供应商" width="150" />
      <el-table-column prop="quantity" label="采购数量" width="100" />
      <el-table-column prop="unitPrice" label="单价" width="100">
        <template #default="{ row }">
          ¥{{ row.unitPrice?.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="totalPrice" label="总价" width="120">
        <template #default="{ row }">
          ¥{{ row.totalPrice?.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="applicantName" label="申请人" width="100" />
      <el-table-column prop="approvalStatus" label="审批状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.approvalStatus)">
            {{ getStatusText(row.approvalStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="approverName" label="审批人" width="100" />
      <el-table-column prop="createTime" label="申请时间" width="160">
        <template #default="{ row }">
          {{ formatDateTime(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="handleView(row)">查看</el-button>
          <el-button 
            v-if="row.approvalStatus === 0 && (userStore.isAdmin || userStore.isManager)" 
            type="success" 
            link 
            size="small" 
            @click="handleApprove(row)"
          >
            审批
          </el-button>
          <el-button 
            v-if="row.approvalStatus === 0 && (row.applicantId === userStore.user.id || userStore.isAdmin)" 
            type="danger" 
            link 
            size="small" 
            @click="handleDelete(row)"
          >
            删除
          </el-button>
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
    
    <el-dialog v-model="dialogVisible" title="发起采购申请" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="商品" prop="goodsId">
          <el-select v-model="form.goodsId" placeholder="请选择商品" filterable style="width: 100%" @change="handleGoodsChange">
            <el-option v-for="item in goodsList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="供应商" prop="supplierId">
          <el-select v-model="form.supplierId" placeholder="请选择供应商" filterable style="width: 100%">
            <el-option v-for="item in supplierList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="form.quantity" :min="1" style="width: 100%" @change="calculateTotal" />
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input-number v-model="form.unitPrice" :precision="2" :min="0" style="width: 100%" @change="calculateTotal" />
        </el-form-item>
        <el-form-item label="总价">
          <span class="total-price">¥{{ form.totalPrice?.toFixed(2) }}</span>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">提交</el-button>
      </template>
    </el-dialog>
    
    <el-dialog v-model="viewVisible" title="采购详情" width="500px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="采购单号">{{ viewData.requestNo }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ viewData.goodsName }}</el-descriptions-item>
        <el-descriptions-item label="供应商">{{ viewData.supplierName }}</el-descriptions-item>
        <el-descriptions-item label="采购数量">{{ viewData.quantity }}</el-descriptions-item>
        <el-descriptions-item label="单价">¥{{ viewData.unitPrice?.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="总价">¥{{ viewData.totalPrice?.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ viewData.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="审批状态">
          <el-tag :type="getStatusType(viewData.approvalStatus)">{{ getStatusText(viewData.approvalStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审批人">{{ viewData.approverName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批时间">{{ viewData.approvalTime ? formatDateTime(viewData.approvalTime) : '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批备注" :span="2">{{ viewData.approvalRemark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
    
    <el-dialog v-model="approveVisible" title="审批采购申请" width="450px">
      <el-form :model="approveForm" label-width="80px">
        <el-form-item label="审批结果">
          <el-radio-group v-model="approveForm.approvalStatus">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批备注">
          <el-input v-model="approveForm.approvalRemark" type="textarea" :rows="3" placeholder="请输入审批备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApprove" :loading="approveLoading">确定</el-button>
      </template>
    </el-dialog>
    
    <!-- 批量审批弹窗 -->
    <el-dialog v-model="batchApproveVisible" title="批量审批采购申请" width="500px">
      <div class="batch-info">
        <el-alert 
          :title="`已选择 ${selectedRows.length} 条待审批的采购申请`" 
          type="info" 
          show-icon
          :closable="false"
          style="margin-bottom: 20px;"
        />
        <div class="selected-items">
          <div v-for="item in selectedRows" :key="item.id" class="selected-item">
            <span class="item-name">{{ item.goodsName }}</span>
            <span class="item-info">x {{ item.quantity }} | ¥{{ item.totalPrice?.toFixed(2) }}</span>
          </div>
        </div>
      </div>
      <el-form :model="batchApproveForm" label-width="80px" style="margin-top: 20px;">
        <el-form-item label="订单总价">
          <span class="batch-total-price">¥{{ batchTotalPrice.toFixed(2) }}</span>
          <span class="batch-count">（共 {{ selectedRows.length }} 条采购申请）</span>
        </el-form-item>
        <el-form-item label="审批结果">
          <el-radio-group v-model="batchApproveForm.approvalStatus">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批备注">
          <el-input 
            v-model="batchApproveForm.approvalRemark" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入审批备注（将自动添加【批量审批 - 】前缀）" 
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="batchApproveVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBatchApprove" :loading="batchApproveLoading">确定</el-button>
      </template>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'
import { getPurchasePage, addPurchase, approvePurchase, deletePurchase, batchApprovePurchase } from '@/api/purchase'
import { getGoodsList } from '@/api/goods'
import { getSupplierList } from '@/api/supplier'

const route = useRoute()
const userStore = useUserStore()

// 页面加载状态
const loading = ref(false)
// 表单提交加载状态
const submitLoading = ref(false)
// 审批加载状态
const approveLoading = ref(false)
// 批量审批加载状态
const batchApproveLoading = ref(false)
// 表格数据
const tableData = ref([])
// 商品列表
const goodsList = ref([])
// 供应商列表
const supplierList = ref([])
// 发起采购弹窗显示状态
const dialogVisible = ref(false)
// 查看详情弹窗显示状态
const viewVisible = ref(false)
// 审批弹窗显示状态
const approveVisible = ref(false)
// 批量审批弹窗显示状态
const batchApproveVisible = ref(false)
// 查看详情数据
const viewData = ref({})
// 表单引用
const formRef = ref(null)
// 已选择的行数据
const selectedRows = ref([])

// 计算属性：批量审批订单总价
const batchTotalPrice = computed(() => {
  return selectedRows.value.reduce((sum, row) => sum + (row.totalPrice || 0), 0)
})

// 图片预览
const imagePreviewVisible = ref(false)
const previewImageUrl = ref('')

// 搜索表单
const searchForm = reactive({
  requestNo: '',
  goodsName: '',
  approvalStatus: null
})

// 分页参数
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 发起采购表单数据
const form = reactive({
  goodsId: null,
  supplierId: null,
  quantity: 1,
  unitPrice: 0,
  totalPrice: 0,
  remark: ''
})

// 审批表单数据
const approveForm = reactive({
  id: null,
  approvalStatus: 1,
  approvalRemark: ''
})

// 批量审批表单数据
const batchApproveForm = reactive({
  approvalStatus: 1,
  approvalRemark: ''
})

// 表单验证规则
const rules = {
  goodsId: [{ required: true, message: '请选择商品', trigger: 'change' }],
  supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
  unitPrice: [{ required: true, message: '请输入单价', trigger: 'blur' }]
}

// 获取图片完整URL
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

// 图片加载失败处理
const handleImageError = (e) => {
  e.target.src = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMDAgMTAwIj48Y2lyY2xlIGN4PSI1MCIgY3k9IjUwIiByPSI1MCIgZmlsbD0iI2Y1ZjdmYSIvPjx0ZXh0IHg9IjUwIiB5PSI1NSIgZm9udC1zaXplPSIxMiIgZmlsbD0iIzkwOTM5OSIgdGV4dC1hbmNob3I9Im1pZGRsZSI+5aSx5pWwPC90ZXh0Pjwvc3ZnPg=='
}

// 获取状态标签类型
const getStatusType = (status) => {
  const types = { 0: 'warning', 1: 'success', 2: 'danger' }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = { 0: '待审批', 1: '已通过', 2: '已拒绝' }
  return texts[status] || '未知'
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  }).replace(/\//g, '-')
}

// 判断行是否可选择（只有待审批的可选）
const checkSelectable = (row) => {
  return row.approvalStatus === 0
}

// 表格选择变化事件
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 加载采购列表数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getPurchasePage({
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

// 加载商品列表
const loadGoods = async () => {
  try {
    const res = await getGoodsList()
    goodsList.value = res.data
  } catch (error) {
    console.error(error)
  }
}

// 加载供应商列表
const loadSuppliers = async () => {
  try {
    const res = await getSupplierList()
    supplierList.value = res.data
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
  searchForm.requestNo = ''
  searchForm.goodsName = ''
  searchForm.approvalStatus = null
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

// 商品选择变化事件
const handleGoodsChange = (goodsId) => {
  const goods = goodsList.value.find(g => g.id === goodsId)
  if (goods) {
    form.unitPrice = goods.purchasePrice || 0
    calculateTotal()
  }
}

// 计算总价
const calculateTotal = () => {
  form.totalPrice = form.quantity * form.unitPrice
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    goodsId: null,
    supplierId: null,
    quantity: 1,
    unitPrice: 0,
    totalPrice: 0,
    remark: ''
  })
}

// 发起采购按钮点击事件
const handleAdd = () => {
  resetForm()
  if (route.query.goodsId) {
    form.goodsId = parseInt(route.query.goodsId)
    handleGoodsChange(form.goodsId)
  }
  dialogVisible.value = true
}

// 查看详情按钮点击事件
const handleView = (row) => {
  viewData.value = row
  viewVisible.value = true
}

// 审批按钮点击事件
const handleApprove = (row) => {
  approveForm.id = row.id
  approveForm.approvalStatus = 1
  approveForm.approvalRemark = ''
  approveVisible.value = true
}

// 批量审批按钮点击事件
const handleBatchApprove = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要审批的采购申请')
    return
  }
  batchApproveForm.approvalStatus = 1
  batchApproveForm.approvalRemark = ''
  batchApproveVisible.value = true
}

// 提交单个审批
const submitApprove = async () => {
  approveLoading.value = true
  try { 
    await approvePurchase(approveForm.id, {
      approvalStatus: approveForm.approvalStatus,
      approvalRemark: approveForm.approvalRemark
    })
    ElMessage.success('审批成功')
    approveVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  } finally {
    approveLoading.value = false 
  }
}

// 提交批量审批
const submitBatchApprove = async () => {
  batchApproveLoading.value = true
  try {
    const ids = selectedRows.value.map(row => row.id)
    await batchApprovePurchase({
      ids: ids,
      approvalStatus: batchApproveForm.approvalStatus,
      approvalRemark: batchApproveForm.approvalRemark
    })
    ElMessage.success('批量审批成功')
    batchApproveVisible.value = false
    selectedRows.value = []
    loadData()
  } catch (error) {
    console.error(error)
  } finally {
    batchApproveLoading.value = false
  }
}

// 提交采购申请
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        await addPurchase(form)
        ElMessage.success('提交成功')
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

// 删除采购申请
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该采购申请吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deletePurchase(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
}

// 页面挂载时初始化
onMounted(() => {
  loadData()
  loadGoods()
  loadSuppliers()
})
</script>

<style lang="scss" scoped>
.header-actions {
  display: flex;
  gap: 10px;
}

.total-price {
  font-size: 20px;
  font-weight: 600;
  color: #f56c6c;
}

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

.batch-info {
  .selected-items {
    max-height: 200px;
    overflow-y: auto;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    padding: 10px;
    
    .selected-item {
      display: flex;
      justify-content: space-between;
      padding: 8px 0;
      border-bottom: 1px dashed #ebeef5;
      
      &:last-child {
        border-bottom: none;
      }
      
      .item-name {
        font-weight: 500;
        color: #303133;
      }
      
      .item-info {
        color: #909399;
        font-size: 13px;
      }
    }
  }
}

.batch-total-price {
  font-size: 24px;
  font-weight: 600;
  color: #f56c6c;
}

.batch-count {
  color: #909399;
  font-size: 14px;
  margin-left: 10px;
}
</style>
