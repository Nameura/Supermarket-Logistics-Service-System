// 导入pinia，进行状态管理
import { defineStore } from 'pinia'
import { login, logout, getUserInfo } from '@/api/auth'

/**
 * 用户状态管理
 * 
 * 使用sessionStorage存储用户信息，实现多标签页独立登录
 * - localStorage: 跨标签页共享，后登录会覆盖前一个用户
 * - sessionStorage: 每个标签页独立，互不干扰
 * 
 * 当前数据持久化流程：
 * 1. 页面加载时，从sessionStorage读取数据恢复状态
 * 2. 用户登录时，更新Pinia状态并同步写入sessionStorage
 * 3. 用户退出时，清空Pinia状态并清除sessionStorage
 * 4. 刷新页面时，状态从sessionStorage恢复
 */
export const useUserStore = defineStore('user', {

  // 状态定义
  // 初始化时从sessionStorage读取数据，实现页面刷新后状态恢复
  state: () => ({
    // 用户Token，用于接口认证
    token: sessionStorage.getItem('token') || '',
    // 用户信息对象，包含id、username、realName等
    user: JSON.parse(sessionStorage.getItem('user') || '{}'),
    // 用户角色：1-管理员，2-经理，3-员工
    role: parseInt(sessionStorage.getItem('role')) || 0
  }),
  
  getters: {
    // 判断用户是否已登录（token存在即为已登录）
    isLoggedIn: (state) => !!state.token,
    // 判断用户是否为管理员
    isAdmin: (state) => state.role === 1 || state.user.role === 1,
    // 判断用户是否为经理
    isManager: (state) => state.role === 2 || state.user.role === 2,
    // 判断用户是否为员工
    isEmployee: (state) => state.role === 3 || state.user.role === 3
  },
  

  actions: {

    // 用户登录登录成功后更新Pinia状态并同步写入sessionStorage

    async loginAction(loginForm) {
      const res = await login(loginForm)
      // 更新Pinia状态
      this.token = res.data.token
      this.user = res.data.user
      this.role = res.data.user.role
      // 同步写入sessionStorage，实现数据持久化
      sessionStorage.setItem('token', res.data.token)
      sessionStorage.setItem('user', JSON.stringify(res.data.user))
      sessionStorage.setItem('role', res.data.user.role)
      return res
    },
    
    // 用户退出登录清空Pinia状态并清除sessionStorage
    async logoutAction() {
      await logout()
      // 清空Pinia状态
      this.token = ''
      this.user = {}
      this.role = 0
      // 清除sessionStorage中的用户信息
      sessionStorage.removeItem('token')
      sessionStorage.removeItem('user')
      sessionStorage.removeItem('role')
    },
    
    // 从后端API获取用户信息，然后同步写入sessionStorage。
    async getUserInfoAction() {
      const res = await getUserInfo()
      // 更新Pinia状态
      this.user = res.data
      this.role = res.data.role
      // 同步更新sessionStorage
      sessionStorage.setItem('user', JSON.stringify(res.data))
      sessionStorage.setItem('role', res.data.role)
      return res
    },
    

    updateUser(user) {
      // 合并更新用户信息
      this.user = { ...this.user, ...user }
      if (user.role !== undefined) {
        this.role = user.role
        sessionStorage.setItem('role', user.role)
      }
      // 同步更新sessionStorage
      sessionStorage.setItem('user', JSON.stringify(this.user))
    }
  }
})
