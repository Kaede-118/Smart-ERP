<template>
  <div class="page">
    <div class="page-header">
      <h3>商品管理</h3>
      <div class="page-actions">
        <el-select v-model="categoryId" placeholder="分类" clearable @change="loadData" style="width:150px">
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
        <el-input v-model="keyword" placeholder="搜索商品" clearable style="width:200px" @clear="loadData" @keyup.enter="loadData" />
        <el-button type="primary" @click="loadData">搜索</el-button>
        <el-button type="primary" @click="showDialog = true">新增商品</el-button>
      </div>
    </div>

    <div class="product-grid">
      <el-card v-for="p in list" :key="p.id" shadow="never" class="product-card">
        <div class="product-info">
          <div class="product-name">{{ p.name }}</div>
          <div class="product-code">{{ p.code }}</div>
          <div class="product-price">¥{{ p.salePrice }}</div>
          <el-tag :type="p.status === 1 ? 'success' : 'danger'" size="small">
            {{ p.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </div>
      </el-card>
    </div>

    <el-pagination background layout="prev,pager,next" :total="total" v-model:page="page" :page-size="size" @change="loadData" />

    <el-dialog v-model="showDialog" title="新增商品" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="商品名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="商品编码"><el-input v-model="form.code" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" style="width:100%">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="售价"><el-input-number v-model="form.salePrice" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="成本价"><el-input-number v-model="form.costPrice" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="单位"><el-input v-model="form.unit" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { productApi } from '../../api'
import { ElMessage } from 'element-plus'

const list = ref<any[]>([])
const categories = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(12)
const keyword = ref('')
const categoryId = ref<number>()
const showDialog = ref(false)
const form = reactive({ name: '', code: '', categoryId: undefined as number|undefined, salePrice: 0, costPrice: 0, unit: '个' })

async function loadData() {
  const res = await productApi.list({ page: page.value, size: size.value, keyword: keyword.value, categoryId: categoryId.value })
  list.value = res.data.records
  total.value = res.data.total
}

async function handleCreate() {
  await productApi.create(form)
  ElMessage.success('新增成功')
  showDialog.value = false
  loadData()
}

onMounted(async () => {
  const res = await productApi.categories.list()
  categories.value = res.data
  loadData()
})
</script>

<style scoped>
.page { max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h3 { margin: 0; font-size: 18px; }
.page-actions { display: flex; gap: 8px; }
.product-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 20px; }
.product-card { border-radius: 12px; cursor: pointer; transition: transform .2s; }
.product-card:hover { transform: translateY(-2px); }
.product-info { text-align: center; padding: 12px 0; }
.product-name { font-size: 15px; font-weight: 600; margin-bottom: 4px; }
.product-code { font-size: 12px; color: #999; margin-bottom: 8px; }
.product-price { font-size: 20px; font-weight: 700; color: #1677ff; margin-bottom: 8px; }
</style>
