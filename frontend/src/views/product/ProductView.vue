<template>
  <div class="page">
    <div class="page-header">
      <h3>商品管理</h3>
      <div class="page-actions">
        <el-select v-model="query.categoryId" placeholder="全部分类" clearable @change="loadData" style="width:150px">
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
        <el-input v-model="query.keyword" placeholder="搜索商品名称/编码" clearable style="width:220px" @clear="loadData" @keyup.enter="loadData" />
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="primary" @click="loadData">搜索</el-button>
        <el-button type="primary" @click="openCreate">新增商品</el-button>
      </div>
    </div>

    <div v-if="list.length === 0" class="empty-state">
      <div class="empty-icon">📦</div>
      <p class="empty-text">暂无商品</p>
      <el-button type="primary" @click="openCreate">立即新增商品</el-button>
    </div>

    <div v-else class="product-grid">
      <el-card v-for="p in list" :key="p.id" shadow="never" class="product-card">
        <div class="card-img" @click="openDetail(p)">
          <el-image
            v-if="p.coverUrl"
            :src="p.coverUrl"
            fit="cover"
            class="product-img"
            loading="lazy"
          >
            <template #error><div class="img-placeholder">📷</div></template>
          </el-image>
          <div v-else class="img-placeholder">📷</div>
        </div>
        <div class="card-body" @click="openDetail(p)">
          <div class="card-name">{{ p.name }}</div>
          <div class="card-code">{{ p.code }}</div>
          <div class="card-price-row">
            <span class="card-sale-price">¥{{ p.salePrice }}</span>
            <span class="card-cost-price">成本 ¥{{ p.costPrice }}</span>
          </div>
          <div class="card-meta-row">
            <span class="card-stock">库存 {{ inventoryMap[p.id] ?? '--' }}</span>
            <el-tag :type="p.status === 1 ? 'success' : 'danger'" size="small">
              {{ p.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </div>
        </div>
        <div class="card-actions">
          <el-button size="small" text type="primary" @click.stop="openEdit(p)">编辑</el-button>
          <el-button size="small" text type="danger" @click.stop="confirmDelete(p)">删除</el-button>
        </div>
      </el-card>
    </div>

    <el-pagination
      v-if="total > 0"
      background layout="prev,pager,next"
      :total="total" v-model:page="page" :page-size="size"
      @change="loadData"
    />

    <!-- 新增 Dialog -->
    <el-dialog v-model="showCreate" title="新增商品" width="520px" destroy-on-close>
      <el-form :model="createForm" label-width="80px">
        <el-form-item label="商品名称"><el-input v-model="createForm.name" /></el-form-item>
        <el-form-item label="商品编码"><el-input v-model="createForm.code" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="createForm.categoryId" style="width:100%">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="售价"><el-input-number v-model="createForm.salePrice" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="成本价"><el-input-number v-model="createForm.costPrice" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="单位"><el-input v-model="createForm.unit" /></el-form-item>
        <el-form-item label="商品图片">
          <el-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="(r) => { createForm.coverUrl = r.data }"
            :show-file-list="false"
            accept="image/*"
          >
            <el-button size="small">上传图片</el-button>
            <template #tip>
              <el-image v-if="createForm.coverUrl" :src="createForm.coverUrl" style="width:80px;height:80px;margin-top:8px;border-radius:6px" fit="cover" />
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreate = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑 Dialog -->
    <el-dialog v-model="showEdit" title="编辑商品" width="520px" destroy-on-close>
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="商品名称"><el-input v-model="editForm.name" /></el-form-item>
        <el-form-item label="商品编码">
          <el-input :model-value="editForm.code" disabled>
            <template #suffix><el-tag size="small" type="info">不可修改</el-tag></template>
          </el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="editForm.categoryId" style="width:100%">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="售价"><el-input-number v-model="editForm.salePrice" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="成本价"><el-input-number v-model="editForm.costPrice" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="单位"><el-input v-model="editForm.unit" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="editForm.status">
            <el-radio :value="1">上架</el-radio>
            <el-radio :value="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="商品图片">
          <el-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="(r) => { editForm.coverUrl = r.data }"
            :show-file-list="false"
            accept="image/*"
          >
            <el-button size="small">上传图片</el-button>
            <template #tip>
              <el-image v-if="editForm.coverUrl" :src="editForm.coverUrl" style="width:80px;height:80px;margin-top:8px;border-radius:6px" fit="cover" />
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEdit = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate">保存</el-button>
      </template>
    </el-dialog>

    <!-- 详情 Dialog -->
    <el-dialog v-model="showDetail" :title="detail.name" width="520px" destroy-on-close>
      <div v-if="detail.id" class="detail-layout">
        <div class="detail-img">
          <div class="detail-img-placeholder">📷</div>
        </div>
        <el-descriptions :column="2" border class="detail-info">
          <el-descriptions-item label="名称">{{ detail.name }}</el-descriptions-item>
          <el-descriptions-item label="编码">{{ detail.code }}</el-descriptions-item>
          <el-descriptions-item label="分类">{{ categoryName(detail.categoryId) }}</el-descriptions-item>
          <el-descriptions-item label="售价">¥{{ detail.salePrice }}</el-descriptions-item>
          <el-descriptions-item label="成本价">¥{{ detail.costPrice }}</el-descriptions-item>
          <el-descriptions-item label="库存">{{ inventoryMap[detail.id] ?? '--' }}</el-descriptions-item>
          <el-descriptions-item label="单位">{{ detail.unit || '个' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="detail.status === 1 ? 'success' : 'danger'" size="small">
              {{ detail.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ detail.createTime }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ detail.updateTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button type="primary" @click="editFromDetail">编辑</el-button>
        <el-button type="danger" @click="confirmDelete(detail)">删除</el-button>
        <el-button @click="showDetail = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { productApi, inventoryApi } from '../../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref<any[]>([])
const categories = ref<any[]>([])
const inventoryMap = ref<Record<number, number>>({})
const total = ref(0)
const page = ref(1)
const size = ref(12)
const query = reactive({ keyword: '', categoryId: undefined as number | undefined })

const showCreate = ref(false)
const showEdit = ref(false)
const showDetail = ref(false)
const uploadUrl = '/api/files/upload'
const uploadHeaders = { Authorization: 'Bearer ' + localStorage.getItem('token') }
const createForm = reactive({ name: '', code: '', categoryId: undefined as number | undefined, salePrice: 0, costPrice: 0, unit: '个', coverUrl: '' })
const editForm = reactive({ id: 0, name: '', code: '', categoryId: undefined as number | undefined, salePrice: 0, costPrice: 0, unit: '个', status: 1, coverUrl: '' })
const detail = ref<any>({})

function categoryName(id: number) {
  return categories.value.find(c => c.id === id)?.name || '--'
}

function resetSearch() {
  query.keyword = ''
  query.categoryId = undefined
  loadData()
}

async function loadData() {
  const res = await productApi.list({ page: page.value, size: size.value, keyword: query.keyword, categoryId: query.categoryId })
  list.value = res.data.records
  total.value = res.data.total
  await loadInventoryBatch()
}

async function loadInventoryBatch() {
  const ids = list.value.map(p => p.id)
  inventoryMap.value = {}
  const res = await inventoryApi.list()
  for (const item of res.data) {
    inventoryMap.value[item.productId] = item.quantity
  }
}

function openCreate() {
  createForm.name = ''; createForm.code = ''; createForm.categoryId = undefined
  createForm.salePrice = 0; createForm.costPrice = 0; createForm.unit = '个'; createForm.coverUrl = ''
  showCreate.value = true
}

function openEdit(p: any) {
  editForm.id = p.id; editForm.name = p.name; editForm.code = p.code
  editForm.categoryId = p.categoryId; editForm.salePrice = p.salePrice
  editForm.costPrice = p.costPrice; editForm.unit = p.unit; editForm.status = p.status ?? 1; editForm.coverUrl = p.coverUrl || ''
  showEdit.value = true
}

function openDetail(p: any) {
  detail.value = p
  showDetail.value = true
}

function editFromDetail() {
  showDetail.value = false
  openEdit(detail.value)
}

async function handleCreate() {
  await productApi.create({
    name: createForm.name, code: createForm.code,
    categoryId: createForm.categoryId, salePrice: createForm.salePrice,
    costPrice: createForm.costPrice, unit: createForm.unit,
    coverUrl: createForm.coverUrl || undefined
  })
  ElMessage.success('新增成功')
  showCreate.value = false
  loadData()
}

async function handleUpdate() {
  await productApi.update(editForm.id, {
    name: editForm.name, categoryId: editForm.categoryId,
    salePrice: editForm.salePrice, costPrice: editForm.costPrice,
    unit: editForm.unit, status: editForm.status,
    coverUrl: editForm.coverUrl || undefined
  })
  ElMessage.success('修改成功')
  showEdit.value = false
  loadData()
}

function confirmDelete(p: any) {
  ElMessageBox.confirm(`确定删除商品「${p.name}」？`, '删除确认', {
    confirmButtonText: '确认删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    await productApi.delete(p.id)
    ElMessage.success('删除成功')
    showDetail.value = false
    loadData()
  }).catch(() => {})
}

onMounted(async () => {
  const res = await productApi.categories.list()
  categories.value = res.data
  loadData()
})
</script>

<style scoped>
.page { max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 12px; }
.page-header h3 { margin: 0; font-size: 18px; }
.page-actions { display: flex; gap: 8px; flex-wrap: wrap; }

/* Empty */
.empty-state { text-align: center; padding: 80px 0; background: #fff; border-radius: 12px; }
.empty-icon { font-size: 48px; margin-bottom: 12px; }
.empty-text { font-size: 15px; color: #999; margin: 0 0 20px; }

/* Grid */
.product-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 20px; }

/* Card */
.product-card {
  border-radius: 12px; overflow: hidden;
  transition: transform .15s ease, box-shadow .15s ease;
  position: relative;
}
.product-card:hover { transform: translateY(-3px); box-shadow: 0 8px 24px rgba(0,0,0,.08); }
.product-card:hover .card-actions { opacity: 1; }

.card-img { height: 120px; background: #f5f7fa; display: flex; align-items: center; justify-content: center; cursor: pointer; overflow: hidden; }
.product-img { width: 100%; height: 100%; object-fit: cover; }
.img-placeholder { font-size: 32px; opacity: .4; }

.card-body { padding: 12px 16px; cursor: pointer; }
.card-name { font-size: 15px; font-weight: 600; margin-bottom: 2px; }
.card-code { font-size: 12px; color: #bbb; margin-bottom: 8px; }
.card-price-row { display: flex; align-items: center; gap: 8px; margin-bottom: 6px; }
.card-sale-price { font-size: 18px; font-weight: 700; color: #1677ff; }
.card-cost-price { font-size: 12px; color: #999; text-decoration: line-through; }
.card-meta-row { display: flex; align-items: center; justify-content: space-between; }
.card-stock { font-size: 12px; color: #666; }

.card-actions {
  position: absolute; top: 8px; right: 8px; display: flex; gap: 4px;
  opacity: 0; transition: opacity .15s ease;
}
.card-actions .el-button { background: rgba(255,255,255,.9); border-radius: 6px; }

/* Detail */
.detail-layout { display: flex; gap: 20px; }
.detail-img { flex-shrink: 0; }
.detail-img-placeholder { width: 120px; height: 120px; background: #f5f7fa; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 32px; }
.detail-info { flex: 1; }
</style>
