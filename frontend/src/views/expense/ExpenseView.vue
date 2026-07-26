<template>
  <div class="page">
    <div class="page-header">
      <h3>费用管理</h3>
      <div class="page-actions">
        <el-select v-model="query.type" placeholder="类型" clearable style="width:120px" @change="loadData">
          <el-option v-for="t in types" :key="t" :label="t" :value="t" />
        </el-select>
        <el-select v-model="query.status" placeholder="状态" clearable style="width:120px" @change="loadData">
          <el-option label="待审批" value="PENDING" />
          <el-option label="已审批" value="APPROVED" />
          <el-option label="已付款" value="PAID" />
          <el-option label="已驳回" value="REJECTED" />
        </el-select>
        <el-input v-model="query.keyword" placeholder="搜索员工/编号" clearable style="width:200px" @clear="loadData" @keyup.enter="loadData" />
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="primary" @click="loadData">搜索</el-button>
        <el-button type="primary" @click="openCreate">新增费用</el-button>
      </div>
    </div>

    <el-table :data="list" style="width:100%">
      <el-table-column prop="expense_no" label="编号" width="180" />
      <el-table-column prop="employee_name" label="员工" width="100" />
      <el-table-column prop="department" label="部门" width="100" />
      <el-table-column prop="type" label="类型" width="100" />
      <el-table-column prop="amount" label="金额" width="120">
        <template #default="{ row }">¥{{ row.amount }}</template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusColor(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="create_time" label="创建时间" width="160" />
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openDetail(row)">查看</el-button>
          <el-button v-if="row.status === 'PENDING'" size="small" @click="openEdit(row)">编辑</el-button>
          <el-button v-if="row.status === 'PENDING'" size="small" type="success" @click="handleApprove(row.id)">审批</el-button>
          <el-button v-if="row.status === 'PENDING'" size="small" type="warning" @click="handleReject(row.id)">驳回</el-button>
          <el-button v-if="row.status === 'APPROVED'" size="small" type="primary" @click="handlePay(row.id)">付款</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Create / Edit Dialog -->
    <el-dialog v-model="showForm" :title="isEdit ? '编辑费用' : '新增费用'" width="520px" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="员工姓名"><el-input v-model="form.employeeName" /></el-form-item>
        <el-form-item label="部门"><el-input v-model="form.department" /></el-form-item>
        <el-form-item label="费用类型">
          <el-select v-model="form.type" style="width:100%">
            <el-option v-for="t in types" :key="t" :label="t" :value="t" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额"><el-input-number v-model="form.amount" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="说明"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showForm = false">取消</el-button>
        <el-button type="primary" @click="isEdit ? handleUpdate() : handleCreate()">
          {{ isEdit ? '保存' : '创建' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- Detail Drawer -->
    <el-drawer v-model="showDetail" :title="'费用详情 - ' + (detail.expense_no || '')" size="500px" destroy-on-close>
      <div v-if="detail.id" class="detail-wrap">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="编号">{{ detail.expense_no }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="statusColor(detail.status)" size="small">{{ statusLabel(detail.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="员工">{{ detail.employee_name }}</el-descriptions-item>
          <el-descriptions-item label="部门">{{ detail.department }}</el-descriptions-item>
          <el-descriptions-item label="类型">{{ detail.type }}</el-descriptions-item>
          <el-descriptions-item label="金额">¥{{ detail.amount }}</el-descriptions-item>
          <el-descriptions-item label="说明" :span="2">{{ detail.description || '--' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ detail.create_time }}</el-descriptions-item>
          <el-descriptions-item label="审批时间">{{ detail.approve_time || '--' }}</el-descriptions-item>
          <el-descriptions-item label="付款时间" :span="2">{{ detail.pay_time || '--' }}</el-descriptions-item>
        </el-descriptions>
        <div class="detail-actions" v-if="detail.status === 'PENDING'">
          <el-button type="success" @click="handleApprove(detail.id)">审批通过</el-button>
          <el-button type="warning" @click="handleReject(detail.id)">驳回</el-button>
        </div>
        <div class="detail-actions" v-if="detail.status === 'APPROVED'">
          <el-button type="primary" @click="handlePay(detail.id)">确认付款</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { expenseApi } from '../../api'
import { ElMessage } from 'element-plus'

const types = ['TRAVEL', 'OFFICE', 'TRANSPORT', 'ENTERTAINMENT', 'TRAINING', 'MAINTENANCE', 'OTHER']
const typeLabels: Record<string, string> = {
  TRAVEL: '差旅', OFFICE: '办公', TRANSPORT: '交通',
  ENTERTAINMENT: '招待', TRAINING: '培训', MAINTENANCE: '维修', OTHER: '其他'
}

const list = ref<any[]>([])
const query = reactive({ keyword: '', type: '', status: '', page: 1, size: 20 })
const showForm = ref(false)
const isEdit = ref(false)
const showDetail = ref(false)
const detail = ref<any>({})
const editId = ref(0)
const form = reactive({ employeeName: '', department: '', type: 'OTHER', amount: 0, description: '', remark: '' })

function statusColor(s: string) {
  return { PENDING: 'warning', APPROVED: 'primary', PAID: 'success', REJECTED: 'danger' }[s] || 'info'
}
function statusLabel(s: string) {
  return { PENDING: '待审批', APPROVED: '已审批', PAID: '已付款', REJECTED: '已驳回' }[s] || s
}

function resetSearch() { query.keyword = ''; query.type = ''; query.status = ''; loadData() }

async function loadData() {
  const res = await expenseApi.list({ keyword: query.keyword, type: query.type || undefined, status: query.status || undefined })
  list.value = res.data
}

function openCreate() {
  isEdit.value = false; editId.value = 0
  form.employeeName = ''; form.department = ''; form.type = 'OTHER'
  form.amount = 0; form.description = ''; form.remark = ''
  showForm.value = true
}

function openEdit(row: any) {
  isEdit.value = true; editId.value = row.id
  form.employeeName = row.employee_name || ''; form.department = row.department || ''
  form.type = row.type || 'OTHER'; form.amount = row.amount || 0
  form.description = row.description || ''; form.remark = row.remark || ''
  showForm.value = true
}

async function openDetail(row: any) {
  const res = await expenseApi.detail(row.id)
  detail.value = res.data
  showDetail.value = true
}

async function handleCreate() {
  await expenseApi.create(form)
  ElMessage.success('新增成功'); showForm.value = false; loadData()
}

async function handleUpdate() {
  await expenseApi.update(editId.value, form)
  ElMessage.success('修改成功'); showForm.value = false; loadData()
}

async function handleApprove(id: number) {
  await expenseApi.approve(id)
  ElMessage.success('审批通过'); showDetail.value = false; loadData()
}

async function handleReject(id: number) {
  await expenseApi.reject(id)
  ElMessage.success('已驳回'); showDetail.value = false; loadData()
}

async function handlePay(id: number) {
  await expenseApi.pay(id)
  ElMessage.success('付款成功'); showDetail.value = false; loadData()
}

onMounted(() => loadData())
</script>

<style scoped>
.page { max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 12px; }
.page-header h3 { margin: 0; font-size: 18px; }
.page-actions { display: flex; gap: 8px; flex-wrap: wrap; }
.detail-wrap { padding: 0 4px; }
.detail-actions { margin-top: 20px; display: flex; gap: 8px; }
</style>
