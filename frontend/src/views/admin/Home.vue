<template>
  <div class="admin-home">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#409eff"><Document /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.articleCount }}</div>
              <div class="stat-label">文章总数</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#67c23a"><Folder /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.categoryCount }}</div>
              <div class="stat-label">分类数量</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#e6a23c"><PriceTag /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.tagCount }}</div>
              <div class="stat-label">标签数量</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#f56c6c"><User /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.userCount }}</div>
              <div class="stat-label">用户数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="welcome-card" style="margin-top: 20px;">
      <h3>欢迎回来，{{ userStore.getUser?.username }}！</h3>
      <p>这里是 NexusBlog 管理后台，您可以在这里管理您的博客内容。</p>
      <el-button type="primary" @click="goToArticles">
        管理文章
      </el-button>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Document, Folder, PriceTag, User } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { articleApi, categoryApi, tagApi } from '@/api'

const router = useRouter()
const userStore = useUserStore()

const stats = ref({
  articleCount: 0,
  categoryCount: 0,
  tagCount: 0,
  userCount: 1
})

const fetchStats = async () => {
  try {
    const [articles, categories, tags] = await Promise.all([
      articleApi.getAll(),
      categoryApi.getAll(),
      tagApi.getAll()
    ])

    stats.value.articleCount = articles.data.length
    stats.value.categoryCount = categories.data.length
    stats.value.tagCount = tags.data.length
  } catch (error) {
    console.error('Failed to fetch stats:', error)
  }
}

const goToArticles = () => {
  router.push('/admin/articles')
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped>
.admin-home {
  max-width: 1200px;
}

.stat-card {
  height: 100%;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.stat-icon {
  font-size: 2.5rem;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 1.75rem;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.875rem;
  color: #6b7280;
}

.welcome-card h3 {
  margin: 0 0 0.5rem 0;
  color: #1f2937;
}

.welcome-card p {
  margin: 0 0 1rem 0;
  color: #6b7280;
}
</style>
