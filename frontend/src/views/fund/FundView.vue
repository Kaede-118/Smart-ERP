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
      <template #header>
        <div class="chart-header">
          <span>余额变化趋势（近7天）</span>
          <div class="chart-legend">
            <span class="legend-item"><span class="legend-dot" style="background:#52c41a" />收入</span>
            <span class="legend-item"><span class="legend-dot" style="background:#ff4d4f" />支出</span>
            <span class="legend-item"><span class="legend-dot" style="background:#1677ff" />余额</span>
          </div>
        </div>
      </template>
      <v-chart :option="trendOption" style="height:280px" autoresize />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { companyAccountApi } from '../../api'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'

use([CanvasRenderer, BarChart, LineChart, GridComponent, TooltipComponent, LegendComponent])

const account = ref<any>({ balance: 0, accountName: '', remark: '' })
const todayIncome = ref(0)
const todayExpense = ref(0)
const trend = ref<any[]>([])

const cashflow = computed(() => todayIncome.value - todayExpense.value)

const trendOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { left: 70, right: 60, bottom: 30, top: 10 },
  xAxis: { type: 'category', data: trend.value.map(t => t.date?.slice(5) || '') },
  yAxis: [
    { type: 'value', name: '收支' },
    { type: 'value', name: '余额', position: 'right' }
  ],
  series: [
    {
      name: '收入', type: 'bar',
      data: trend.value.map(t => Number(t.income)),
      color: '#52c41a', barWidth: 12, yAxisIndex: 0,
      label: { show: true, position: 'top', color: '#52c41a', fontSize: 11, formatter: (p: any) => p.value > 0 ? '¥' + p.value : '' }
    },
    {
      name: '支出', type: 'bar',
      data: trend.value.map(t => Number(t.expense)),
      color: '#ff4d4f', barWidth: 12, yAxisIndex: 0,
      label: { show: true, position: 'top', color: '#ff4d4f', fontSize: 11, formatter: (p: any) => p.value > 0 ? '¥' + p.value : '' }
    },
    {
      name: '余额', type: 'line',
      data: trend.value.map(t => Number(t.balance)),
      color: '#1677ff', smooth: true, yAxisIndex: 1,
      label: { show: true, position: 'top', color: '#1677ff', fontSize: 11, formatter: (p: any) => '¥' + p.value }
    }
  ]
}))

async function loadData() {
  const [a, inc, exp, tr] = await Promise.all([
    companyAccountApi.get(), companyAccountApi.todayIncome(),
    companyAccountApi.todayExpense(), companyAccountApi.trend()
  ])
  account.value = a.data
  todayIncome.value = Number(inc.data)
  todayExpense.value = Number(exp.data)
  trend.value = tr.data
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
.chart-header { display: flex; align-items: center; justify-content: space-between; }
.chart-legend { display: flex; gap: 16px; }
.legend-item { font-size: 13px; color: #666; display: flex; align-items: center; gap: 6px; }
.legend-dot { width: 10px; height: 10px; border-radius: 50%; display: inline-block; }
</style>
