<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="profile-card">
          <div class="profile-header">
            <div class="avatar-section">
              <el-upload
                class="avatar-uploader"
                action="/api/upload/avatar"
                :headers="{ Authorization: 'Bearer ' + token }"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <el-avatar :size="120" :src="avatarUrl">
                  {{ userForm.realName?.charAt(0) || 'U' }}
                </el-avatar>
                <div class="avatar-overlay">
                  <el-icon><Camera /></el-icon>
                  <span>更换头像</span>
                </div>
              </el-upload>
            </div>
            <h2 class="user-name">{{ userForm.realName }}</h2>
            <p class="user-role">{{ getRoleName(userForm.role) }}</p>
          </div>
          <div class="profile-info">
            <div class="info-item">
              <el-icon><User /></el-icon>
              <span>{{ userForm.username }}</span>
            </div>
            <div class="info-item">
              <el-icon><Phone /></el-icon>
              <span>{{ userForm.phone || '未设置' }}</span>
            </div>
            <div class="info-item">
              <el-icon><Message /></el-icon>
              <span>{{ userForm.email || '未设置' }}</span>
            </div>
            <div class="info-item">
              <el-icon><OfficeBuilding /></el-icon>
              <span>{{ userForm.departmentName || '未分配部门' }}</span>
            </div>
            <div class="info-item">
              <el-icon><Calendar /></el-icon>
              <span>入职时间：{{ formatDate(userForm.createTime) }}</span>
            </div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="16">
        <el-card class="form-card">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本信息" name="info">
              <el-form ref="infoFormRef" :model="userForm" :rules="infoRules" label-width="80px">
                <el-form-item label="用户名">
                  <el-input v-model="userForm.username" disabled />
                </el-form-item>
                <el-form-item label="真实姓名" prop="realName">
                  <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                  <el-input v-model="userForm.phone" placeholder="请输入手机号" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="userForm.email" placeholder="请输入邮箱" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="updateInfo" :loading="infoLoading">
                    保存修改
                  </el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            
            <el-tab-pane label="修改密码" name="password">
              <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="100px">
                <el-form-item label="当前密码" prop="oldPassword">
                  <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入当前密码" show-password />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
                </el-form-item>
                <el-form-item label="确认新密码" prop="confirmPassword">
                  <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请确认新密码" show-password />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="updatePassword" :loading="passwordLoading">
                    修改密码
                  </el-button>
                  <el-button @click="resetPasswordForm">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { updateProfile, updatePassword as updatePasswordApi, updateAvatar } from '@/api/user'

const userStore = useUserStore()
// 从sessionStorage获取token，用于头像上传认证
const token = sessionStorage.getItem('token')

// 当前选中的标签页
const activeTab = ref('info')
// 基本信息表单引用
const infoFormRef = ref(null)
// 密码表单引用
const passwordFormRef = ref(null)
// 基本信息保存加载状态
const infoLoading = ref(false)
// 密码修改加载状态
const passwordLoading = ref(false)

// 默认头像（SVG占位图）
const defaultAvatar = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMDAgMTAwIj48Y2lyY2xlIGN4PSI1MCIgY3k9IjUwIiByPSI1MCIgZmlsbD0iIzQwOWVmZiIvPjx0ZXh0IHg9IjUwIiB5PSI2NSIgZm9udC1zaXplPSI0MCIgZmlsbD0iI2ZmZiIgdGV4dC1hbmNob3I9Im1pZGRsZSI+VTwvdGV4dD48L3N2Zz4='

// 用户信息表单数据
const userForm = reactive({
  username: '',
  realName: '',
  phone: '',
  email: '',
  avatar: '',
  role: '',
  departmentName: '',
  createTime: ''
})

// 计算属性：头像URL（处理相对路径和绝对路径）
const avatarUrl = computed(() => {
  if (userForm.avatar) {
    if (userForm.avatar.startsWith('http') || userForm.avatar.startsWith('data:')) {
      return userForm.avatar
    }
    return '/api' + userForm.avatar
  }
  return defaultAvatar
})

// 密码表单数据
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 基本信息表单验证规则
const infoRules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

// 确认密码验证器
const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

// 密码表单验证规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 根据角色ID获取角色名称
const getRoleName = (role) => {
  const roles = {
    1: '超级管理员',
    2: '经理',
    3: '员工'
  }
  return roles[role] || '未知'
}

// 格式化日期显示
const formatDate = (date) => {
  if (!date) return '未知'
  return new Date(date).toLocaleDateString('zh-CN')
}

// 从Pinia加载用户信息到表单
const loadUserInfo = () => {
  const user = userStore.user
  Object.assign(userForm, {
    username: user.username,
    realName: user.realName,
    phone: user.phone,
    email: user.email,
    avatar: user.avatar,
    role: user.role,
    departmentName: user.departmentName,
    createTime: user.createTime
  })
}

// 更新基本信息
const updateInfo = async () => {
  if (!infoFormRef.value) return
  
  await infoFormRef.value.validate(async (valid) => {
    if (valid) {
      infoLoading.value = true
      try {
        await updateProfile({
          realName: userForm.realName,
          phone: userForm.phone,
          email: userForm.email
        })
        // 同步更新Pinia中的用户信息
        userStore.updateUser({ ...userStore.user, ...userForm })
        ElMessage.success('修改成功')
      } catch (error) {
        console.error(error)
      } finally {
        infoLoading.value = false
      }
    }
  })
}

// 修改密码
const updatePassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      passwordLoading.value = true
      try {
        await updatePasswordApi({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })
        ElMessage.success('密码修改成功')
        resetPasswordForm()
      } catch (error) {
        console.error(error)
      } finally {
        passwordLoading.value = false
      }
    }
  })
}

// 重置密码表单
const resetPasswordForm = () => {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordFormRef.value?.resetFields()
}

// 头像上传前校验（文件类型和大小）
const beforeAvatarUpload = (file) => {
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

// 头像上传成功回调
const handleAvatarSuccess = async (response) => {
  if (response.code === 200) {
    const avatarPath = response.data
    try {
      // 更新后端头像路径
      await updateAvatar({ avatar: avatarPath })
      // 更新本地表单数据
      userForm.avatar = avatarPath
      // 同步更新Pinia中的用户信息
      userStore.updateUser({ ...userStore.user, avatar: avatarPath })
      ElMessage.success('头像更新成功')
    } catch (error) {
      console.error(error)
      ElMessage.error('头像保存失败')
    }
  } else {
    ElMessage.error('头像上传失败')
  }
}

// 页面挂载时加载用户信息
onMounted(() => {
  loadUserInfo()
})
</script>

<style lang="scss" scoped>
.profile-container {
  padding: 0;
}

.profile-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  
  .profile-header {
    background: linear-gradient(135deg, #409eff, #337ecc);
    padding: 40px 20px;
    text-align: center;
    color: #fff;
    
    .avatar-section {
      position: relative;
      display: inline-block;
      margin-bottom: 15px;
      
      .avatar-uploader {
        position: relative;
        cursor: pointer;
        
        &:hover .avatar-overlay {
          opacity: 1;
        }
      }
      
      .avatar-overlay {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.5);
        border-radius: 50%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        opacity: 0;
        transition: opacity 0.3s;
        
        .el-icon {
          font-size: 24px;
          margin-bottom: 5px;
        }
        
        span {
          font-size: 12px;
        }
      }
    }
    
    .user-name {
      font-size: 20px;
      margin-bottom: 5px;
    }
    
    .user-role {
      font-size: 14px;
      opacity: 0.9;
    }
  }
  
  .profile-info {
    padding: 20px;
    
    .info-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 12px 0;
      border-bottom: 1px solid #f0f0f0;
      
      &:last-child {
        border-bottom: none;
      }
      
      .el-icon {
        color: #409eff;
        font-size: 18px;
      }
      
      span {
        color: #606266;
        font-size: 14px;
      }
    }
  }
}

.form-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  
  :deep(.el-card__body) {
    padding: 20px 30px;
  }
  
  :deep(.el-tabs__item.is-active) {
    color: #409eff;
  }
  
  :deep(.el-tabs__active-bar) {
    background-color: #409eff;
  }
  
  .el-form {
    max-width: 500px;
    margin-top: 20px;
  }
}
</style>
