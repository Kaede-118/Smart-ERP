import { defineStore } from 'pinia'
import { ref } from 'vue'
import { authApi } from '../api'
import type { LoginResult } from '../types/api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const user = ref<LoginResult['user'] | null>(null)
  const roles = ref<string[]>([])
  const permissions = ref<string[]>([])

  async function login(username: string, password: string) {
    const res = await authApi.login({ username, password })
    token.value = res.data.token
    user.value = res.data.user
    roles.value = res.data.roles
    permissions.value = res.data.permissions
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('user', JSON.stringify(res.data.user))
    return res
  }

  function logout() {
    token.value = ''
    user.value = null
    roles.value = []
    permissions.value = []
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  function hasPermission(code: string) {
    return permissions.value.includes(code)
  }

  function init() {
    const saved = localStorage.getItem('user')
    if (saved) {
      user.value = JSON.parse(saved)
    }
  }

  return { token, user, roles, permissions, login, logout, hasPermission, init }
})
