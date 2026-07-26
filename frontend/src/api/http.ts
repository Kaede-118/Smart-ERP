import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const http = axios.create({
  baseURL: '/api',
  timeout: 30000
})

http.interceptors.request.use(config => {
  console.log(`[API] ${config.method?.toUpperCase()} ${config.url}`, config.params || config.data || '')
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

http.interceptors.response.use(
  response => {
    const res = response.data
    console.log(`[API] ${response.config.method?.toUpperCase()} ${response.config.url} → ${res.code}`, res.data ? (typeof res.data === 'object' ? Object.keys(res.data).length + ' fields' : res.data) : '')
    if (res.code === 40100) {
      console.warn('[API] Token expired, redirecting to login')
      localStorage.removeItem('token')
      router.push('/login')
      return Promise.reject(res)
    }
    if (res.code !== 200) {
      console.warn(`[API] Business error: ${res.code} ${res.message}`)
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(res)
    }
    return res
  },
  error => {
    if (error.response) {
      console.error(`[API] HTTP ${error.response.status} ${error.config?.url}`, error.response.data)
      if (error.response.status === 401) {
        localStorage.removeItem('token')
        router.push('/login')
      }
      ElMessage.error(error.response.data?.message || `请求失败 (${error.response.status})`)
    } else if (error.request) {
      console.error('[API] No response received', error.message)
      ElMessage.error('后端服务未响应，请确认后端已启动')
    } else {
      console.error('[API] Request error', error.message)
      ElMessage.error(error.message)
    }
    return Promise.reject(error)
  }
)

export default http
