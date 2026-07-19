<template>
  <div class="register-container">
    <div class="register-left">
      <div class="register-left-content">
        <div class="logo-section">
          <div class="logo-icon">
            <el-icon :size="60"><Shop /></el-icon>
          </div>
          <h1 class="logo-title">商超后勤服务系统</h1>
          <p class="logo-subtitle">Supermarket Logistics Service System</p>
        </div>
        <div class="welcome-section">
          <h2>加入我们</h2>
          <p>创建账号，开启高效的后勤管理体验</p>
          <div class="benefits">
            <div class="benefit-item">
              <el-icon><Check /></el-icon>
              <span>快速便捷的商品管理</span>
            </div>
            <div class="benefit-item">
              <el-icon><Check /></el-icon>
              <span>实时库存监控预警</span>
            </div>
            <div class="benefit-item">
              <el-icon><Check /></el-icon>
              <span>智能排班系统</span>
            </div>
            <div class="benefit-item">
              <el-icon><Check /></el-icon>
              <span>数据分析与报表</span>
            </div>
          </div>
        </div>
        <div class="decoration-circles">
          <div class="circle circle-1"></div>
          <div class="circle circle-2"></div>
          <div class="circle circle-3"></div>
        </div>
      </div>
    </div>
    <div class="register-right">
      <div class="register-form-container">
        <div class="register-header">
          <h2>创建账号</h2>
          <p>请填写以下信息完成注册</p>
        </div>
        <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="register-form">
          <el-form-item prop="username">
            <el-input 
              v-model="registerForm.username" 
              placeholder="请输入用户名"
              prefix-icon="User"
              size="large"
            />
          </el-form-item>
          <el-form-item prop="realName">
            <el-input 
              v-model="registerForm.realName" 
              placeholder="请输入真实姓名"
              prefix-icon="UserFilled"
              size="large"
            />
          </el-form-item>
          <el-form-item prop="phone">
            <el-input 
              v-model="registerForm.phone" 
              placeholder="请输入手机号"
              prefix-icon="Phone"
              size="large"
            />
          </el-form-item>
          <el-form-item prop="email">
            <el-input 
              v-model="registerForm.email" 
              placeholder="请输入邮箱"
              prefix-icon="Message"
              size="large"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input 
              v-model="registerForm.password" 
              type="password" 
              placeholder="请输入密码"
              prefix-icon="Lock"
              size="large"
              show-password
            />
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input 
              v-model="registerForm.confirmPassword" 
              type="password" 
              placeholder="请确认密码"
              prefix-icon="Lock"
              size="large"
              show-password
              @keyup.enter="handleRegister"
            />
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              size="large" 
              class="register-btn"
              :loading="loading"
              @click="handleRegister"
            >
              注 册
            </el-button>
          </el-form-item>
          <div class="register-footer">
            <span>已有账号？</span>
            <router-link to="/login">立即登录</router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/auth'

const router = useRouter()

const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  username: '',
  realName: '',
  phone: '',
  email: '',
  password: '',
  confirmPassword: ''
})

// 验证确认密码的函数
const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

// 注册校验规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3到20个字符', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await register({
          username: registerForm.username,
          realName: registerForm.realName,
          phone: registerForm.phone,
          email: registerForm.email,
          password: registerForm.password
        })
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.register-container {
  display: flex;
  width: 100%;
  height: 100vh;
  background: #f0f2f5;
}

.register-left {
  width: 65%;
  position: relative;
  overflow: hidden;
  background: url('@/assets/img/login_bg.jpg') no-repeat center center;
  background-size: cover;
}

.register-left-content {
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
  margin-bottom: 50px;
  
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

.welcome-section {
  h2 {
    font-size: 32px;
    margin-bottom: 15px;
  }
  
  > p {
    font-size: 16px;
    opacity: 0.9;
    margin-bottom: 30px;
  }
}

.benefits {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.benefit-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 15px 20px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  backdrop-filter: blur(5px);
  
  .el-icon {
    color: #85ce61;
    font-size: 20px;
  }
  
  span {
    font-size: 15px;
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

.register-right {
  width: 35%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  overflow-y: auto;
  padding: 40px 0;
}

.register-form-container {
  width: 80%;
  max-width: 400px;
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
  
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

.register-form {
  .el-form-item {
    margin-bottom: 20px;
  }
  
  .el-input {
    --el-input-border-radius: 8px;
  }
}

.register-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
  background: linear-gradient(135deg, #67c23a, #529b2e);
  border: none;
  
  &:hover {
    background: linear-gradient(135deg, #85ce61, #67c23a);
  }
}

.register-footer {
  text-align: center;
  margin-top: 20px;
  color: #909399;
  
  a {
    color: #67c23a;
    font-weight: 500;
    
    &:hover {
      text-decoration: underline;
    }
  }
}
</style>
