<template>
  <div class="sync-articles-page">
    <div class="page-header">
      <h1>同步文章管理</h1>
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

    <el-table :data="articleList" v-loading="loading" border>
      <el-table-column prop="articleTitle" label="文章标题" min-width="200" />
      <el-table-column prop="repoName" label="来源仓库" width="150" />
      <el-table-column prop="fileName" label="文件名" width="200" />
      <el-table-column prop="filePath" label="文件路径" min-width="250" />
      <el-table-column prop="fileHash" label="文件哈希" width="200" show-overflow-tooltip />
      <el-table-column prop="lastCommitSha" label="最后提交" width="200" show-overflow-tooltip />
      <el-table-column prop="createdAt" label="同步时间" width="180" />
      <el-table-column prop="updatedAt" label="更新时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="handleEditArticle(row)">
            编辑文章
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import { getSyncArticles, getRepoConfigs, deleteSyncArticle } from '@/api/github'

const router = useRouter()
const loading = ref(false)
const articleList = ref([])
const repoList = ref([])
const filterRepoId = ref()

// 加载仓库列表
const loadRepoList = async () => {
  try {
    const res = await getRepoConfigs()
    repoList.value = res.data
  } catch (error) {
    ElMessage.error('加载仓库列表失败：' + error.message)
  }
}

// 加载同步文章
const loadData = async () => {
  loading.value = true
  try {
    let res
    if (filterRepoId.value) {
      res = await getSyncArticles({ repoId: filterRepoId.value })
    } else {
      res = await getSyncArticles()
    }
    // 补充仓库名称和文章标题
    articleList.value = res.data.content.map(item => {
      const repo = repoList.value.find(r => r.id === item.repoConfigId)
      return {
        ...item,
        repoName: repo ? repo.name : `仓库${item.repoConfigId}`,
        articleTitle: item.article ? item.article.title : `文章${item.articleId}`
      }
    })
  } catch (error) {
    ElMessage.error('加载失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 编辑文章
const handleEditArticle = (row) => {
  router.push(`/admin/articles/edit/${row.articleId}`)
}

// 删除同步文章
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除同步文章"${row.articleTitle}"吗？这会同时删除对应的文章记录。`,
      '提示',
      { type: 'warning' }
    )

    await deleteSyncArticle(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error.action !== 'cancel') {
      ElMessage.error('删除失败：' + error.message)
    }
  }
}

onMounted(() => {
  loadRepoList().then(() => {
    loadData()
  })
})
</script>

<style scoped>
.sync-articles-page {
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
</style>
