<template>
  <el-container class="layout">
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <span class="logo-text">ERP Enterprise</span>
      </div>
      <el-menu
        :router="true"
        :default-active="route.path"
        class="menu"
        background-color="#fff"
        text-color="#333"
        active-text-color="#1677ff"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>Dashboard</span>
        </el-menu-item>
        <el-menu-item index="/products">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/inventory">
          <el-icon><TrendCharts /></el-icon>
          <span>库存管理</span>
        </el-menu-item>
        <el-menu-item index="/purchase">
          <el-icon><ShoppingCart /></el-icon>
          <span>采购管理</span>
        </el-menu-item>
        <el-menu-item index="/sales">
          <el-icon><Sell /></el-icon>
          <span>销售管理</span>
        </el-menu-item>
        <el-menu-item index="/ai">
          <el-icon><MagicStick /></el-icon>
          <span>AI 助手</span>
        </el-menu-item>
        <el-menu-item index="/rbac">
          <el-icon><Setting /></el-icon>
          <span>系统设置</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <span class="greeting">👋 上午好，{{ auth.user?.nickname }}</span>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-avatar :size="32" icon="UserFilled" />
              <span class="username">{{ auth.user?.nickname }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-item @click="logout">
                <el-icon><SwitchButton /></el-icon> 退出登录
              </el-dropdown-item>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import {
  Odometer, Goods, TrendCharts, ShoppingCart, Sell,
  MagicStick, Setting, SwitchButton, UserFilled
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

function logout() {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout { height: 100vh; background: #f5f7fa; }
.sidebar { background: #fff; border-right: 1px solid #eee; display: flex; flex-direction: column; }
.logo { height: 64px; display: flex; align-items: center; padding: 0 20px; border-bottom: 1px solid #f0f0f0; }
.logo-text { font-size: 18px; font-weight: 700; color: #1677ff; letter-spacing: 1px; }
.menu { border-right: none; flex: 1; }
.header {
  height: 64px; background: #fff; display: flex; align-items: center;
  justify-content: space-between; padding: 0 24px; border-bottom: 1px solid #f0f0f0;
}
.greeting { font-size: 15px; color: #333; }
.header-right { display: flex; align-items: center; gap: 16px; }
.user-info { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.username { font-size: 14px; color: #333; }
.main-content { padding: 24px; background: #f5f7fa; overflow-y: auto; }
</style>
