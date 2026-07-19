<template>
  <div class="login-container">
    <div class="login-left">
      <div class="login-left-content">
        <div class="logo-section">
          <div class="logo-icon">
            <el-icon :size="60"><Shop /></el-icon>
          </div>
          <h1 class="logo-title">商超后勤服务系统</h1>
          <p class="logo-subtitle">Supermarket Logistics Service System</p>
        </div>
        <div class="feature-section">
          <div class="feature-item">
            <el-icon :size="32"><Box /></el-icon>
            <div class="feature-text">
              <h3>商品管理</h3>
              <p>全面的商品信息管理</p>
            </div>
          </div>
          <div class="feature-item">
            <el-icon :size="32"><DataAnalysis /></el-icon>
            <div class="feature-text">
              <h3>库存监控</h3>
              <p>实时库存预警提醒</p>
            </div>
          </div>
          <div class="feature-item">
            <el-icon :size="32"><TrendCharts /></el-icon>
            <div class="feature-text">
              <h3>数据分析</h3>
              <p>可视化数据统计报表</p>
            </div>
          </div>
          <div class="feature-item">
            <el-icon :size="32"><UserFilled /></el-icon>
            <div class="feature-text">
              <h3>人员管理</h3>
              <p>智能排班与考勤系统</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="login-right">
      <div class="login-form-container">
        <div class="login-header">
          <h2>欢迎登录</h2>
          <p>请输入您的账号信息</p>
        </div>
        <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
          <el-form-item prop="username">
            <el-input 
              v-model="loginForm.username" 
              placeholder="请输入用户名"
              prefix-icon="User"
              size="large"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input 
              v-model="loginForm.password" 
              type="password" 
              placeholder="请输入密码"
              prefix-icon="Lock"
              size="large"
              show-password
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              size="large" 
              class="login-btn"
              :loading="loading"
              @click="handleLogin"
            >
              登 录
            </el-button>
          </el-form-item>
          <div class="login-footer">
            <span>还没有账号？</span>
            <router-link to="/register">立即注册</router-link>
          </div>
        </el-form>
        <div class="demo-accounts">
          <p class="demo-title">演示账号</p>
          <div class="demo-list">
            <!-- 管理员的暂时没必要展示 -->
            <!-- <div class="demo-item" @click="fillAdmin">
              <el-tag type="danger">超级管理员</el-tag>
              <span>admin / 123456</span>
            </div> -->
            <div class="demo-item" @click="fillManager">
              <el-tag type="warning">经理</el-tag>
              <span>manager1 / 123456</span>
            </div>
            <div class="demo-item" @click="fillEmployee">
              <el-tag type="success">员工</el-tag>
              <span>employee1 / 123456</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

// 登录表单校验规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 处理登录函数
const handleLogin = async () => {
  // 表单校验
  if (!loginFormRef.value) return
  // 表单校验通过，把数据提交给后端，路由跳转
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.loginAction(loginForm)
        ElMessage.success('登录成功')
        router.push('/dashboard')
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}

const fillAdmin = () => {
  loginForm.username = 'admin'
  loginForm.password = '123456'
}

const fillManager = () => {
  loginForm.username = 'manager1'
  loginForm.password = '123456'
}

const fillEmployee = () => {
  loginForm.username = 'employee1'
  loginForm.password = '123456'
}
</script>

<style lang="scss" scoped>
.login-container {
  display: flex;
  width: 100%;
  height: 100vh;
  background: #f0f2f5;
}

.login-left {
  width: 65%;
  position: relative;
  overflow: hidden;
  background: url('@/assets/img/login_bg.jpg') no-repeat center center;
  background-size: cover;
}

.login-left-content {
  position: relative;
  z-index: 1;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 60px;
  color: #fff;
}

.logo-section {
  margin-bottom: 60px;
  
  .logo-icon {
    width: 100px;
    height: 100px;
    background: rgba(255, 255, 255, 0.15);
    border-radius: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 20px;
    backdrop-filter: blur(10px);
  }
  
  .logo-title {
    font-size: 42px;
    font-weight: 700;
    margin-bottom: 10px;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
  }
  
  .logo-subtitle {
    font-size: 16px;
    opacity: 0.8;
    letter-spacing: 2px;
  }
}

.feature-section {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  backdrop-filter: blur(5px);
  transition: all 0.3s ease;
  
  &:hover {
    background: rgba(255, 255, 255, 0.2);
    transform: translateY(-2px);
  }
  
  .el-icon {
    color: #fff;
  }
  
  .feature-text {
    h3 {
      font-size: 16px;
      font-weight: 600;
      margin-bottom: 5px;
    }
    
    p {
      font-size: 13px;
      opacity: 0.8;
    }
  }
}

.decoration-circles {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  
  .circle {
    position: absolute;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.05);
  }
  
  .circle-1 {
    width: 300px;
    height: 300px;
    top: -100px;
    right: -50px;
  }
  
  .circle-2 {
    width: 200px;
    height: 200px;
    bottom: 50px;
    left: -50px;
  }
  
  .circle-3 {
    width: 150px;
    height: 150px;
    bottom: -50px;
    right: 20%;
  }
}

.login-right {
  width: 35%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
}

.login-form-container {
  width: 80%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
  
  h2 {
    font-size: 28px;
    color: #303133;
    margin-bottom: 10px;
  }
  
  p {
    color: #909399;
    font-size: 14px;
  }
}

.login-form {
  .el-form-item {
    margin-bottom: 25px;
  }
  
  .el-input {
    --el-input-border-radius: 8px;
  }
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
  background: linear-gradient(135deg, #409eff, #337ecc);
  border: none;
  
  &:hover {
    background: linear-gradient(135deg, #66b1ff, #409eff);
  }
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  color: #909399;
  
  a {
    color: #409eff;
    font-weight: 500;
    
    &:hover {
      text-decoration: underline;
    }
  }
}

.demo-accounts {
  margin-top: 40px;
  padding-top: 30px;
  border-top: 1px solid #ebeef5;
  
  .demo-title {
    text-align: center;
    color: #909399;
    font-size: 13px;
    margin-bottom: 15px;
  }
  
  .demo-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  .demo-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 10px 15px;
    background: #f5f7fa;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.3s;
    
    &:hover {
      background: #ecf5ff;
    }
    
    span {
      color: #606266;
      font-size: 13px;
    }
  }
}
</style>
