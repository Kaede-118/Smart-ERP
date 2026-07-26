<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <h1 class="title">ERP Enterprise</h1>
        <p class="subtitle">Welcome Back</p>
      </div>
      <el-form :model="form" @submit.prevent="handleLogin" class="login-form">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" size="large" prefix-icon="User" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" class="login-btn" :loading="loading" native-type="submit">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const form = reactive({ username: 'admin', password: '123456' })

async function handleLogin() {
  loading.value = true
  try {
    await auth.login(form.username, form.password)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch { /* handled by interceptor */ }
  finally { loading.value = false }
}
</script>

<style scoped>
.login-page {
  height: 100vh; display: flex; align-items: center; justify-content: center;
  background: #f5f7fa;
}
.login-card {
  width: 400px; padding: 40px; background: #fff; border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,.06);
}
.login-header { text-align: center; margin-bottom: 32px; }
.title { font-size: 28px; font-weight: 700; color: #1677ff; margin: 0; }
.subtitle { font-size: 14px; color: #666; margin-top: 8px; }
.login-form :deep(.el-input__wrapper) { border-radius: 8px; }
.login-btn { width: 100%; border-radius: 8px; height: 44px; font-size: 16px; }
</style>
