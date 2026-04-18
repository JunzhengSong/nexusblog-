<template>
  <div class="sync-history-page">
    <div class="page-header">
      <h1>同步历史</h1>
      <el-button type="primary" @click="loadData">
        <el-icon><Refresh /></el-icon>
        刷新
      </el-button>
    </div>

    <div class="filter-bar">
      <el-select
        v-model="filterRepoId"
        placeholder="按仓库筛选"
        clearable
        style="width: 250px"
        @change="loadData"
      >
        <el-option
          v-for="repo in repoList"
          :key="repo.id"
          :label="repo.name"
          :value="repo.id"
        />
      </el-select>
    </div>

    <el-table :data="historyList" v-loading="loading" border>
      <el-table-column prop="id" label="任务ID" width="80" />
      <el-table-column prop="repoName" label="仓库名称" width="150" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="totalFiles" label="总文件数" width="100" />
      <el-table-column prop="successCount" label="成功" width="80" align="center">
        <template #default="{ row }">
          <span style="color: #67c23a">{{ row.successCount }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="failCount" label="失败" width="80" align="center">
        <template #default="{ row }">
          <span style="color: #f56c6c">{{ row.failCount }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="180" />
      <el-table-column prop="endTime" label="结束时间" width="180" />
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="handleViewDetail(row)">
            详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="同步详情" width="600px">
      <div v-if="currentDetail" class="detail-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="任务ID">
            {{ currentDetail.id }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentDetail.status)">
              {{ getStatusText(currentDetail.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="仓库ID">
            {{ currentDetail.repoConfigId }}
          </el-descriptions-item>
          <el-descriptions-item label="总文件数">
            {{ currentDetail.totalFiles }}
          </el-descriptions-item>
          <el-descriptions-item label="成功">
            <span style="color: #67c23a">{{ currentDetail.successCount }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="失败">
            <span style="color: #f56c6c">{{ currentDetail.failCount }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="开始时间">
            {{ currentDetail.startTime || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="结束时间">
            {{ currentDetail.endTime || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">
            {{ currentDetail.createdAt }}
          </el-descriptions-item>
          <el-descriptions-item label="错误信息" v-if="currentDetail.errorMessage" :span="2">
            <pre style="white-space: pre-wrap; margin: 0; color: #f56c6c">{{ currentDetail.errorMessage }}</pre>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import { getSyncHistory, getRepoConfigs } from '@/api/github'

const loading = ref(false)
const detailVisible = ref(false)
const historyList = ref([])
const repoList = ref([])
const filterRepoId = ref()
const currentDetail = ref()

const getStatusType = (status) => {
  const typeMap = {
    PENDING: 'info',
    RUNNING: 'warning',
    SUCCESS: 'success',
    FAILED: 'danger'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    PENDING: '等待中',
    RUNNING: '运行中',
    SUCCESS: '成功',
    FAILED: '失败'
  }
  return textMap[status] || status
}

// 加载仓库列表
const loadRepoList = async () => {
  try {
    const res = await getRepoConfigs()
    repoList.value = res
  } catch (error) {
    ElMessage.error('加载仓库列表失败：' + error.message)
  }
}

// 加载同步历史
const loadData = async () => {
  loading.value = true
  try {
    const params = {}
    if (filterRepoId.value) {
      params.repoId = filterRepoId.value
    }
    const res = await getSyncHistory(params)
    // 补充仓库名称
    historyList.value = res.map(item => {
      const repo = repoList.value.find(r => r.id === item.repoConfigId)
      return {
        ...item,
        repoName: repo ? repo.name : `仓库${item.repoConfigId}`
      }
    })
  } catch (error) {
    ElMessage.error('加载失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 查看详情
const handleViewDetail = (row) => {
  currentDetail.value = row
  detailVisible.value = true
}

onMounted(() => {
  loadRepoList().then(() => {
    loadData()
  })
})
</script>

<style scoped>
.sync-history-page {
  height: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.page-header h1 {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
}

.filter-bar {
  margin-bottom: 1rem;
}

.detail-content {
  padding: 1rem 0;
}

pre {
  background: #f5f7fa;
  padding: 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
}
</style>
