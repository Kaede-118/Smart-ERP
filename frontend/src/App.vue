<template>
  <router-view />
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useAuthStore } from './stores/auth'

onMounted(() => {
  useAuthStore().init()
})
</script>

<style>
body { margin: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; }

.fade-enter-active, .fade-leave-active {
  transition: opacity .15s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

.el-card { transition: box-shadow .15s ease; }
.el-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,.06) !important; }
</style>
