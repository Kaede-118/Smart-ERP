<template>
  <div class="page">
    <div class="page-header">
      <h3>库存管理</h3>
      <div class="page-actions">
        <el-input v-model="keyword" placeholder="搜索商品" clearable @keyup.enter="loadData" style="width:200px" />
        <el-button type="primary" @click="loadData">搜索</el-button>
        <el-button @click="inventoryApi.exportExcel()">导出 Excel</el-button>
      </div>
    </div>

    <el-table :data="list" @row-click="showDetail" style="cursor:pointer">
      <el-table-column prop="productName" label="商品" />
      <el-table-column prop="productCode" label="编码" width="120" />
      <el-table-column prop="categoryName" label="分类" width="120" />
      <el-table-column label="库存" width="250">
        <template #default="{ row }">
          <div class="stock-bar">
            <el-progress
              :percentage="Math.min(100, row.quantity / (row.warningValue * 2) * 100)"
              :color="row.quantity < row.warningValue ? '#ff4d4f' : row.quantity < row.warningValue * 3 ? '#faad14' : '#52c41a'"
              :stroke-width="16"
            >
              <span class="stock-text">{{ row.quantity }}</span>
            </el-progress>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="warningValue" label="预警值" width="80" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.quantity < row.warningValue ? 'danger' : 'success'" size="small">
            {{ row.quantity < row.warningValue ? '偏低' : '正常' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showDetailDialog" :title="detail.productName" width="600px">
      <div class="detail-stock">
        <span class="stock-big">{{ detail.quantity }}</span>
        <span class="stock-unit">当前库存</span>
      </div>
      <div class="detail-actions">
        <el-input-number v-model="changeQty" style="width:160px" />
        <el-button type="primary" :disabled="changeQty === 0" @click="handleAdjust">调整库存</el-button>
      </div>
      <el-divider />
      <h4>库存流水</h4>
      <el-table :data="records" size="small">
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }"><el-tag :type="row.type === 'INBOUND' ? 'success' : 'danger'" size="small">{{ row.type }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="changeQty" label="变动" width="80" />
        <el-table-column prop="beforeQty" label="变动前" width="80" />
        <el-table-column prop="afterQty" label="变动后" width="80" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column prop="createTime" label="时间" width="160" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { inventoryApi } from '../../api'
import { ElMessage } from 'element-plus'

const list = ref<any[]>([])
const keyword = ref('')
const detail = ref<any>({})
const records = ref<any[]>([])
const showDetailDialog = ref(false)
const changeQty = ref(0)
const currentProductId = ref(0)

async function loadData() {
  const res = await inventoryApi.list({ keyword: keyword.value })
  list.value = res.data
}

async function showDetail(row: any) {
  currentProductId.value = row.productId
  detail.value = row
  changeQty.value = 0
  const res = await inventoryApi.records({ productId: row.productId })
  records.value = res.data
  showDetailDialog.value = true
}

async function handleAdjust() {
  await inventoryApi.adjust({ productId: currentProductId.value, changeQty: changeQty.value, remark: '手动调整' })
  ElMessage.success('调整成功')
  showDetailDialog.value = false
  loadData()
}

onMounted(() => loadData())
</script>

<style scoped>
.page { max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h3 { margin: 0; font-size: 18px; }
.page-actions { display: flex; gap: 8px; }
.stock-bar { padding: 4px 0; }
.stock-text { font-size: 12px; font-weight: 600; }
.detail-stock { text-align: center; padding: 24px; }
.stock-big { font-size: 48px; font-weight: 700; color: #1677ff; display: block; }
.stock-unit { font-size: 13px; color: #666; }
.detail-actions { display: flex; gap: 8px; justify-content: center; margin: 16px 0; }
</style>
