import http from './http'
import type { ApiResponse, LoginResult, PageResult, UserInfo } from '../types/api'

export const authApi = {
  login(data: { username: string; password: string }) {
    return http.post<any, ApiResponse<LoginResult>>('/auth/login', data)
  },
  me() {
    return http.get<any, ApiResponse<UserInfo>>('/auth/me')
  }
}

export const userApi = {
  list(params: { page: number; size: number; username?: string; status?: number }) {
    return http.post<any, ApiResponse<PageResult<UserInfo>>>('/users/list', params)
  },
  detail(id: number) {
    return http.get<any, ApiResponse<UserInfo>>(`/users/detail/${id}`)
  },
  create(data: { username: string; password: string; nickname: string; status?: number }) {
    return http.post<any, ApiResponse<UserInfo>>('/users', data)
  },
  update(id: number, data: { nickname: string; status: number }) {
    return http.put<any, ApiResponse<UserInfo>>(`/users/${id}`, data)
  },
  delete(id: number) {
    return http.delete<any, ApiResponse<null>>(`/users/${id}`)
  },
  resetPassword(id: number, data: { newPassword?: string }) {
    return http.put<any, ApiResponse<null>>(`/users/${id}/reset-password`, data)
  },
  assignRoles(id: number, data: { roleIds: number[] }) {
    return http.put<any, ApiResponse<null>>(`/users/${id}/roles`, data)
  },
  getRoles(id: number) {
    return http.get<any, ApiResponse<any[]>>(`/users/${id}/roles`)
  }
}

export const dashboardApi = {
  summary() {
    return http.get<any, ApiResponse<any>>('/dashboard/summary')
  },
  trend() {
    return http.get<any, ApiResponse<any[]>>('/dashboard/trend')
  },
  warnings() {
    return http.get<any, ApiResponse<any[]>>('/dashboard/warnings')
  },
  topProducts() {
    return http.get<any, ApiResponse<any[]>>('/dashboard/top-products')
  }
}

export const productApi = {
  categories: {
    list() { return http.get<any, ApiResponse<any[]>>('/product-categories') },
    create(data: { name: string }) { return http.post<any, ApiResponse<any>>('/product-categories', data) },
    update(id: number, data: any) { return http.put<any, ApiResponse<any>>(`/product-categories/${id}`, data) },
    delete(id: number) { return http.delete<any, ApiResponse<null>>(`/product-categories/${id}`) }
  },
  list(params: { page?: number; size?: number; categoryId?: number; keyword?: string }) {
    return http.post<any, ApiResponse<PageResult<any>>>('/products/list', params)
  },
  detail(id: number) { return http.get<any, ApiResponse<any>>(`/products/${id}`) },
  create(data: any) { return http.post<any, ApiResponse<any>>('/products', data) },
  update(id: number, data: any) { return http.put<any, ApiResponse<any>>(`/products/${id}`, data) },
  delete(id: number) { return http.delete<any, ApiResponse<null>>(`/products/${id}`) }
}

export const inventoryApi = {
  list(params?: { keyword?: string; categoryId?: number }) {
    return http.get<any, ApiResponse<any[]>>('/inventory/list', { params })
  },
  detail(productId: number) { return http.get<any, ApiResponse<any>>(`/inventory/${productId}`) },
  adjust(data: { productId: number; changeQty: number; remark?: string }) {
    return http.post<any, ApiResponse<null>>('/inventory/adjust', data)
  },
  records(params?: { productId?: number; type?: string }) {
    return http.get<any, ApiResponse<any[]>>('/inventory/records', { params })
  },
  exportExcel() {
    const token = localStorage.getItem('token')
    fetch('/api/excel/inventory/export', { headers: { Authorization: 'Bearer ' + token } })
      .then(r => r.blob())
      .then(blob => {
        const url = URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = '库存数据.xlsx'
        a.click()
        URL.revokeObjectURL(url)
      })
  }
}

export const purchaseApi = {
  suppliers: {
    list() { return http.get<any, ApiResponse<any[]>>('/suppliers') },
    create(data: any) { return http.post<any, ApiResponse<any>>('/suppliers', data) },
    update(id: number, data: any) { return http.put<any, ApiResponse<any>>(`/suppliers/${id}`, data) },
    delete(id: number) { return http.delete<any, ApiResponse<null>>(`/suppliers/${id}`) }
  },
  orders: {
    list() { return http.get<any, ApiResponse<any[]>>('/purchase/orders') },
    detail(id: number) { return http.get<any, ApiResponse<any>>(`/purchase/orders/${id}`) },
    create(data: any) { return http.post<any, ApiResponse<any>>('/purchase/orders', data) },
    receive(id: number) { return http.post<any, ApiResponse<null>>(`/purchase/orders/${id}/receive`) },
    exportExcel() {
      const token = localStorage.getItem('token')
      fetch('/api/excel/purchase/export', { headers: { Authorization: 'Bearer ' + token } })
        .then(r => r.blob())
        .then(blob => {
          const url = URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = '采购数据.xlsx'
          a.click()
          URL.revokeObjectURL(url)
        })
    }
  }
}

export const salesApi = {
  customers: {
    list() { return http.get<any, ApiResponse<any[]>>('/customers') },
    create(data: any) { return http.post<any, ApiResponse<any>>('/customers', data) },
    update(id: number, data: any) { return http.put<any, ApiResponse<any>>(`/customers/${id}`, data) },
    delete(id: number) { return http.delete<any, ApiResponse<null>>(`/customers/${id}`) }
  },
  orders: {
    list() { return http.get<any, ApiResponse<any[]>>('/sales/orders') },
    detail(id: number) { return http.get<any, ApiResponse<any>>(`/sales/orders/${id}`) },
    create(data: any) { return http.post<any, ApiResponse<any>>('/sales/orders', data) },
    complete(id: number) { return http.post<any, ApiResponse<null>>(`/sales/orders/${id}/complete`) },
    exportExcel() {
      const token = localStorage.getItem('token')
      fetch('/api/excel/sales/export', { headers: { Authorization: 'Bearer ' + token } })
        .then(r => r.blob())
        .then(blob => {
          const url = URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = '销售数据.xlsx'
          a.click()
          URL.revokeObjectURL(url)
        })
    }
  }
}

export const aiApi = {
  generate(data: { type: string; range: string }) {
    return http.post<any, ApiResponse<any>>('/ai/reports', data)
  },
  list() { return http.get<any, ApiResponse<any[]>>('/ai/reports') },
  detail(id: number) { return http.get<any, ApiResponse<any>>(`/ai/reports/${id}`) }
}

export const rbacApi = {
  roles: {
    list(params?: { keyword?: string; page?: number; size?: number }) {
      return http.get<any, ApiResponse<PageResult<any>>>('/roles', { params })
    },
    detail(id: number) { return http.get<any, ApiResponse<any>>(`/roles/${id}`) },
    create(data: any) { return http.post<any, ApiResponse<any>>('/roles', data) },
    update(id: number, data: any) { return http.put<any, ApiResponse<any>>(`/roles/${id}`, data) },
    delete(id: number) { return http.delete<any, ApiResponse<null>>(`/roles/${id}`) },
    permissions: {
      list(roleId: number) { return http.get<any, ApiResponse<any[]>>(`/roles/${roleId}/permissions`) },
      assign(roleId: number, data: { permissionIds: number[] }) {
        return http.put<any, ApiResponse<null>>(`/roles/${roleId}/permissions`, data)
      }
    }
  },
  permissions: {
    list() { return http.get<any, ApiResponse<any[]>>('/permissions') },
    create(data: any) { return http.post<any, ApiResponse<any>>('/permissions', data) },
    update(id: number, data: any) { return http.put<any, ApiResponse<any>>(`/permissions/${id}`, data) },
    delete(id: number) { return http.delete<any, ApiResponse<null>>(`/permissions/${id}`) }
  }
}

export const expenseApi = {
  list(params?: { keyword?: string; type?: string; status?: string; page?: number; size?: number }) {
    return http.get<any, ApiResponse<any[]>>('/expenses', { params })
  },
  detail(id: number) { return http.get<any, ApiResponse<any>>(`/expenses/${id}`) },
  create(data: any) { return http.post<any, ApiResponse<null>>('/expenses', data) },
  update(id: number, data: any) { return http.put<any, ApiResponse<null>>(`/expenses/${id}`, data) },
  delete(id: number) { return http.delete<any, ApiResponse<null>>(`/expenses/${id}`) },
  approve(id: number) { return http.post<any, ApiResponse<null>>(`/expenses/${id}/approve`) },
  reject(id: number) { return http.post<any, ApiResponse<null>>(`/expenses/${id}/reject`) },
  pay(id: number) { return http.post<any, ApiResponse<null>>(`/expenses/${id}/pay`) }
}

export const companyAccountApi = {
  get() { return http.get<any, ApiResponse<any>>('/company-account') },
  update(data: any) { return http.put<any, ApiResponse<null>>('/company-account', data) },
  todayIncome() { return http.get<any, ApiResponse<any>>('/company-account/today-income') },
  todayExpense() { return http.get<any, ApiResponse<any>>('/company-account/today-expense') },
  trend() { return http.get<any, ApiResponse<any[]>>('/company-account/trend') }
}
