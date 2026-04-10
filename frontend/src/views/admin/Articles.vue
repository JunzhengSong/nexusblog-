<template>
  <div class="admin-articles">
    <div class="header">
      <h2>文章管理</h2>
      <el-button type="primary" @click="createArticle">
        <el-icon><Plus /></el-icon>
        新建文章
      </el-button>
    </div>

    <el-card class="table-card">
      <el-table
        :data="articles"
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />

        <el-table-column prop="title" label="标题" min-width="200" />

        <el-table-column label="分类" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.category" size="small" type="info">
              {{ row.category.name }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="标签" width="200">
          <template #default="{ row }">
            <el-tag
              v-for="tag in row.tags"
              :key="tag.id"
              size="small"
              style="margin-right: 4px;"
            >
              {{ tag.name }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              link
              @click="editArticle(row.id)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              link
              @click="deleteArticle(row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="articles.length === 0 && !loading" description="暂无文章" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { articleApi } from '@/api'

const router = useRouter()

const articles = ref([])
const loading = ref(false)

const fetchArticles = async () => {
  loading.value = true
  try {
    const response = await articleApi.getAll()
    articles.value = response.data
  } catch (error) {
    console.error('Failed to fetch articles:', error)
  } finally {
    loading.value = false
  }
}

const createArticle = () => {
  router.push('/admin/articles/create')
}

const editArticle = (id) => {
  router.push(`/admin/articles/edit/${id}`)
}

const deleteArticle = async (id) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这篇文章吗？此操作无法撤销。',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await articleApi.delete(id)
    ElMessage.success('删除成功')
    await fetchArticles()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete article:', error)
    }
  }
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  fetchArticles()
})
</script>

<style scoped>
.admin-articles {
  max-width: 1400px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.header h2 {
  margin: 0;
  color: #1f2937;
}

.table-card {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>
