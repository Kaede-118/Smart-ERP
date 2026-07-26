<template>
  <div class="ai-page">
    <div class="page-header">
      <h3>🤖 AI 经营分析</h3>
      <div class="page-actions">
        <el-select v-model="reportType" style="width:150px">
          <el-option label="经营概览" value="OVERVIEW" />
          <el-option label="销售分析" value="SALES" />
          <el-option label="库存分析" value="INVENTORY" />
        </el-select>
        <el-button type="primary" :loading="generating" @click="generateReport">生成报告</el-button>
      </div>
    </div>

    <div class="ai-layout">
      <div class="history-panel">
        <h4>历史报告</h4>
        <div v-for="r in reports" :key="r.id" class="history-item" :class="{ active: currentId === r.id }" @click="loadReport(r.id)">
          <div class="history-title">{{ r.title }}</div>
          <div class="history-meta">{{ r.type }} · {{ r.createdTime }}</div>
        </div>
        <el-empty v-if="reports.length === 0" description="暂无报告" :image-size="60" />
      </div>
      <div class="report-panel">
        <div v-if="currentReport" class="report-content">
          <h2>{{ currentReport.title }}</h2>
          <p class="report-summary">{{ currentReport.summary }}</p>
          <el-divider />
          <div class="report-body" v-html="renderedContent"></div>
          <el-divider />
          <div class="report-meta">
            <span>模型：{{ currentReport.model }}</span>
            <span>耗时：{{ currentReport.elapsedMs }}ms</span>
          </div>
        </div>
        <div v-else class="report-empty">
          <p>选择历史报告或点击"生成报告"获取分析</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { aiApi } from '../../api'
import { ElMessage } from 'element-plus'

const reports = ref<any[]>([])
const currentReport = ref<any>(null)
const currentId = ref<number>()
const generating = ref(false)
const reportType = ref('OVERVIEW')

function renderMarkdown(text: string) {
  if (!text) return ''
  return text
    .replace(/### (.+)/g, '<h3>$1</h3>')
    .replace(/## (.+)/g, '<h2>$1</h2>')
    .replace(/# (.+)/g, '<h1>$1</h1>')
    .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
    .replace(/\n-/g, '<br>-')
    .replace(/\n/g, '<br>')
}

const renderedContent = computed(() => renderMarkdown(currentReport.value?.content || ''))

async function loadReports() {
  const res = await aiApi.list()
  reports.value = res.data
}

async function loadReport(id: number) {
  currentId.value = id
  const res = await aiApi.detail(id)
  currentReport.value = res.data
}

async function generateReport() {
  generating.value = true
  try {
    const res = await aiApi.generate({ type: reportType.value, range: 'MONTH' })
    ElMessage.success('报告生成完成')
    await loadReports()
    currentReport.value = res.data
    currentId.value = res.data.id
  } finally { generating.value = false }
}

onMounted(() => loadReports())
</script>

<style scoped>
.ai-page { max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h3 { margin: 0; font-size: 18px; }
.page-actions { display: flex; gap: 8px; }
.ai-layout { display: grid; grid-template-columns: 280px 1fr; gap: 20px; min-height: 600px; }
.history-panel { background: #fff; border-radius: 12px; padding: 20px; }
.history-panel h4 { margin: 0 0 16px; font-size: 15px; }
.history-item { padding: 12px; border-radius: 8px; cursor: pointer; margin-bottom: 8px; transition: background .2s; }
.history-item:hover, .history-item.active { background: #f0f5ff; }
.history-title { font-size: 13px; font-weight: 600; margin-bottom: 4px; }
.history-meta { font-size: 11px; color: #999; }
.report-panel { background: #fff; border-radius: 12px; padding: 32px; min-height: 600px; }
.report-content h2 { margin: 0 0 16px; }
.report-summary { color: #666; font-size: 14px; line-height: 1.6; }
.report-body { line-height: 1.8; color: #333; font-size: 14px; }
.report-meta { display: flex; gap: 16px; color: #999; font-size: 12px; }
.report-empty { display: flex; align-items: center; justify-content: center; height: 400px; color: #999; }
</style>
