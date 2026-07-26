<template>
  <div class="page">
    <div class="page-header">
      <h3>系统设置</h3>
    </div>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="角色管理" name="roles">
        <div style="margin-bottom:16px">
          <el-button type="primary" @click="showRoleDialog = true">新增角色</el-button>
        </div>
        <el-table :data="roles">
          <el-table-column prop="roleName" label="角色名称" />
          <el-table-column prop="roleCode" label="编码" />
          <el-table-column prop="description" label="描述" />
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button size="small" type="danger" @click="deleteRole(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="权限管理" name="permissions">
        <div style="margin-bottom:16px">
          <el-button type="primary" @click="showPermDialog = true">新增权限</el-button>
        </div>
        <el-table :data="permissions">
          <el-table-column prop="name" label="权限名称" />
          <el-table-column prop="code" label="编码" />
          <el-table-column prop="type" label="类型" width="100" />
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="用户角色分配" name="users">
        <el-table :data="users">
          <el-table-column prop="username" label="用户名" />
          <el-table-column prop="nickname" label="昵称" />
          <el-table-column label="角色">
            <template #default="{ row }">
              <el-tag v-for="r in row.roles" :key="r" style="margin-right:4px">{{ r.roleCode }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button size="small" @click="assignRole(row)">分配角色</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="showRoleDialog" title="新增角色" width="400px">
      <el-form :model="roleForm" label-width="80px">
        <el-form-item label="名称"><el-input v-model="roleForm.roleName" /></el-form-item>
        <el-form-item label="编码"><el-input v-model="roleForm.roleCode" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="roleForm.description" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showRoleDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreateRole">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showPermDialog" title="新增权限" width="400px">
      <el-form :model="permForm" label-width="80px">
        <el-form-item label="名称"><el-input v-model="permForm.name" /></el-form-item>
        <el-form-item label="编码"><el-input v-model="permForm.code" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPermDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreatePerm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { rbacApi, userApi } from '../../api'
import { ElMessage } from 'element-plus'

const activeTab = ref('roles')
const roles = ref<any[]>([])
const permissions = ref<any[]>([])
const users = ref<any[]>([])
const showRoleDialog = ref(false)
const showPermDialog = ref(false)
const roleForm = reactive({ roleName: '', roleCode: '', description: '' })
const permForm = reactive({ name: '', code: '' })

async function loadRoles() {
  const res = await rbacApi.roles.list({ page: 1, size: 50 })
  roles.value = res.data.records
}

async function loadPerms() {
  const res = await rbacApi.permissions.list()
  permissions.value = res.data
}

async function loadUsers() {
  const res = await userApi.list({ page: 1, size: 50 })
  users.value = res.data.records
  for (const u of users.value) {
    try {
      const r = await userApi.getRoles(u.id)
      u.roles = r.data || []
    } catch {
      u.roles = []
    }
  }
}

async function handleCreateRole() {
  await rbacApi.roles.create(roleForm)
  ElMessage.success('创建成功')
  showRoleDialog.value = false
  loadRoles()
}

async function handleCreatePerm() {
  await rbacApi.permissions.create(permForm)
  ElMessage.success('创建成功')
  showPermDialog.value = false
  loadPerms()
}

async function deleteRole(id: number) {
  try {
    await rbacApi.roles.delete(id)
    ElMessage.success('删除成功')
    loadRoles()
  } catch {
    // error already handled by http interceptor
  }
  ElMessage.success('删除成功')
  loadRoles()
}

onMounted(() => { loadRoles(); loadPerms(); loadUsers() })
</script>

<style scoped>
.page { max-width: 1200px; margin: 0 auto; }
.page-header { margin-bottom: 20px; }
.page-header h3 { margin: 0; font-size: 18px; }
</style>
