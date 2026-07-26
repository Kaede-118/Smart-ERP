<template>
  <div class="page">
    <div class="page-header">
      <h3>💰 资金管理</h3>
    </div>

    <el-row :gutter="16" style="margin-bottom:20px">
      <el-col :span="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-label">企业余额</div>
          <div class="stat-value">¥{{ account.balance }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-label">今日收入</div>
          <div class="stat-value income">¥{{ todayIncome }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-label">今日支出</div>
          <div class="stat-value expense">¥{{ todayExpense }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-label">现金流（今日）</div>
          <div class="stat-value" :class="cashflow >= 0 ? 'income' : 'expense'">¥{{ cashflow }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="chart-card">
      <template #header><span>余额变化趋势（近7天）</span></template>
      <v-chart :option="trendOption" style="height:280px" autoresize />
    </el-card>

    <el-card shadow="never" class="info-card" style="margin-top:16px">
      <template #header><span>账户信息</span></template>
      <el-form :model="form" label-width="100px">
        <el-form-item label="账户名称"><el-input v-model="form.accountName" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="2" /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUpdate">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { companyAccountApi } from '../../api'
import { ElMessage } from 'element-plus'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'

use([CanvasRenderer, BarChart, GridComponent, TooltipComponent, LegendComponent])

const account = ref<any>({ balance: 0, accountName: '', remark: '' })
const todayIncome = ref(0)
const todayExpense = ref(0)
const trend = ref<any[]>([])
const form = reactive({ accountName: '', remark: '' })

const cashflow = computed(() => todayIncome.value - todayExpense.value)

const trendOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { left: 60, right: 20, bottom: 30, top: 10 },
  xAxis: { type: 'category', data: trend.value.map(t => t.date?.slice(5) || '') },
  yAxis: { type: 'value' },
  series: [
    { name: '收入', type: 'bar', data: trend.value.map(t => Number(t.income)), color: '#52c41a', barWidth: 12 },
    { name: '支出', type: 'bar', data: trend.value.map(t => Number(t.expense)), color: '#ff4d4f', barWidth: 12 }
  ],
  legend: { right: 10, top: 5 }
}))

async function loadData() {
  const [a, inc, exp, tr] = await Promise.all([
    companyAccountApi.get(), companyAccountApi.todayIncome(),
    companyAccountApi.todayExpense(), companyAccountApi.trend()
  ])
  account.value = a.data
  form.accountName = a.data.accountName
  form.remark = a.data.remark || ''
  todayIncome.value = Number(inc.data)
  todayExpense.value = Number(exp.data)
  trend.value = tr.data
}

async function handleUpdate() {
  await companyAccountApi.update({ accountName: form.accountName, remark: form.remark })
  ElMessage.success('修改成功')
  loadData()
}

onMounted(() => loadData())
</script>

<style scoped>
.page { max-width: 1200px; margin: 0 auto; }
.page-header { margin-bottom: 20px; }
.page-header h3 { margin: 0; font-size: 18px; }
.stat-card { text-align: center; border-radius: 12px; padding: 8px 0; }
.stat-label { font-size: 13px; color: #666; margin-bottom: 8px; }
.stat-value { font-size: 24px; font-weight: 700; color: #1677ff; }
.stat-value.income { color: #52c41a; }
.stat-value.expense { color: #ff4d4f; }
.chart-card { border-radius: 12px; }
.info-card { border-radius: 12px; }
</style>
