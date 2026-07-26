<template>
  <div class="page">
    <div class="page-header">
      <h3>采购管理</h3>
      <el-button type="primary" @click="showCreateDialog = true">新建采购单</el-button>
    </div>

    <el-table :data="orders">
      <el-table-column prop="orderNo" label="采购单号" width="200" />
      <el-table-column prop="supplierName" label="供应商" />
      <el-table-column label="内容" min-width="200">
        <template #default="{ row }">{{ formatItems(row.itemNames, row.itemCount) }}</template>
      </el-table-column>
      <el-table-column prop="totalAmount" label="金额" width="120">
        <template #default="{ row }">¥{{ row.totalAmount }}</template>
      </el-table-column>
      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="row.status === 'RECEIVED' ? 'success' : 'warning'">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="creatorName" label="创建人" width="100" />
      <el-table-column prop="createTime" label="时间" width="160" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button v-if="row.status === 'DRAFT'" size="small" type="primary" @click="handleReceive(row.id)">入库</el-button>
          <el-button size="small" @click="showDetail(row.id)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showCreateDialog" title="新建采购单" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="供应商">
          <el-select v-model="form.supplierId" style="width:100%">
            <el-option v-for="s in suppliers" :key="s.id" :label="s.name" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="商品">
          <el-select v-model="form.productId" style="width:100%" filterable>
            <el-option v-for="p in products" :key="p.id" :label="`${p.name}(${p.code})`" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="数量"><el-input-number v-model="form.quantity" :min="1" style="width:100%" /></el-form-item>
        <el-form-item label="单价"><el-input-number v-model="form.price" :min="0" :precision="2" style="width:100%" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">创建</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showDetailDialog" title="采购单详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="单号">{{ detail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="供应商">{{ detail.supplierName }}</el-descriptions-item>
        <el-descriptions-item label="金额">¥{{ detail.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detail.status }}</el-descriptions-item>
      </el-descriptions>
      <el-table :data="detail.items" size="small" style="margin-top:16px">
        <el-table-column prop="productName" label="商品" />
        <el-table-column prop="quantity" label="数量" />
        <el-table-column prop="price" label="单价" />
        <el-table-column prop="amount" label="小计" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { purchaseApi, productApi } from '../../api'
import { ElMessage } from 'element-plus'

const orders = ref<any[]>([])
const suppliers = ref<any[]>([])
const products = ref<any[]>([])
const detail = ref<any>({})
const showCreateDialog = ref(false)
const showDetailDialog = ref(false)
const form = reactive({ supplierId: undefined as number|undefined, productId: undefined as number|undefined, quantity: 1, price: 0 })

function formatItems(names: string | null, count: number) {
  if (!names) return '--'
  const items = names.split('|')
  if (count === 1 && items[0]) return items[0]
  const firstThree = items.slice(0, 3)
  return items.length > 3 ? firstThree.join(', ') + '...' : firstThree.join(', ')
}

async function loadData() {
  const res = await purchaseApi.orders.list()
  orders.value = res.data
}

async function showDetail(id: number) {
  const res = await purchaseApi.orders.detail(id)
  detail.value = res.data
  showDetailDialog.value = true
}

async function handleReceive(id: number) {
  await purchaseApi.orders.receive(id)
  ElMessage.success('入库成功')
  loadData()
}

async function handleCreate() {
  await purchaseApi.orders.create({
    supplierId: form.supplierId,
    items: [{ productId: form.productId, quantity: form.quantity, price: form.price }]
  })
  ElMessage.success('创建成功')
  showCreateDialog.value = false
  loadData()
}

onMounted(async () => {
  const [s, p] = await Promise.all([purchaseApi.suppliers.list(), productApi.list({ page: 1, size: 100 })])
  suppliers.value = s.data
  products.value = p.data.records
  loadData()
})
</script>

<style scoped>
.page { max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h3 { margin: 0; font-size: 18px; }
</style>
