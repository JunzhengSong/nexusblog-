<template>
  <div class="home">
    <header class="header">
      <div class="container">
        <h1 class="logo">NexusBlog</h1>
        <el-link type="primary" href="/admin/login" v-if="!isAuthenticated">管理后台</el-link>
        <el-button type="primary" @click="goToAdmin" v-else>管理后台</el-button>
      </div>
    </header>

    <section class="slogan">
      <div class="container">
        <h2 class="slogan-text">记录技术成长，分享技术心得</h2>
      </div>
    </section>

    <section class="category-tabs" v-if="categories.length > 0">
      <div class="container">
        <el-tabs v-model="activeCategory" @tab-click="handleTabClick">
          <el-tab-pane label="全部" name="all" />
          <el-tab-pane
            v-for="cat in categories"
            :key="cat.id"
            :label="cat.name"
            :name="String(cat.id)"
          />
        </el-tabs>
      </div>
    </section>

    <main class="main">
      <div class="container">
        <el-empty v-if="filteredArticles.length === 0 && !loading" description="暂无文章" />

        <div class="article-list" v-else>
          <el-skeleton :rows="3" animated v-if="loading" v-for="i in 3" :key="i" />

          <article
            v-for="article in filteredArticles"
            :key="article.id"
            class="article-card"
            @click="viewArticle(article.id)"
          >
            <div class="article-header">
              <h2 class="article-title">{{ article.title }}</h2>
              <span class="article-date">{{ formatDate(article.createdAt) }}</span>
            </div>
            <p class="article-excerpt">{{ getExcerpt(article.content) }}</p>
            <div class="article-footer">
              <div class="article-tags">
                <el-tag
                  v-for="tag in article.tags.slice(0, 3)"
                  :key="tag.id"
                  size="small"
                  type="success"
                >
                  {{ tag.name }}
                </el-tag>
              </div>
              <span class="read-more">阅读全文 →</span>
            </div>
          </article>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { articleApi, categoryApi } from '@/api'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const articles = ref([])
const categories = ref([])
const loading = ref(false)
const activeCategory = ref('all')

const isAuthenticated = computed(() => userStore.checkIsAuthenticated)

const filteredArticles = computed(() => {
  if (activeCategory.value === 'all') {
    return articles.value
  }
  return articles.value.filter(article => article.category?.id === Number(activeCategory.value))
})

const getExcerpt = (content) => {
  if (!content) return ''
  const plainText = content.replace(/[#*`\[\]()>-]/g, '').trim()
  return plainText.length > 120 ? plainText.substring(0, 120) + '...' : plainText
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日`
}

const fetchArticles = async () => {
  loading.value = true
  try {
    const response = await articleApi.getAll()
    articles.value = response
  } catch (error) {
    console.error('Failed to fetch articles:', error)
  } finally {
    loading.value = false
  }
}

const fetchCategories = async () => {
  try {
    const response = await categoryApi.getAll()
    categories.value = response
  } catch (error) {
    console.error('Failed to fetch categories:', error)
  }
}

const handleTabClick = () => {
  // Filtering is handled by computed property
}

const viewArticle = (id) => {
  router.push(`/article/${id}`)
}

const goToAdmin = () => {
  router.push('/admin')
}

onMounted(() => {
  userStore.loadFromStorage()
  fetchArticles()
  fetchCategories()
})
</script>

<style scoped>
.home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f9fafb;
}

.header {
  background: #ffffff;
  border-bottom: 1px solid #e5e7eb;
  padding: 1rem 0;
}

.slogan {
  background: #ffffff;
  padding: 2.5rem 0;
  text-align: center;
  border-bottom: 1px solid #e5e7eb;
}

.slogan-text {
  font-size: 1.5rem;
  font-weight: 500;
  color: #374151;
  margin: 0;
}

.category-tabs {
  background: #ffffff;
  padding: 0;
}

.category-tabs :deep(.el-tabs__header) {
  margin: 0;
}

.category-tabs :deep(.el-tabs__nav-wrap::after) {
  display: none;
}

.category-tabs :deep(.el-tabs__item) {
  font-size: 1rem;
  color: #6b7280;
}

.category-tabs :deep(.el-tabs__item.is-active) {
  color: #2563eb;
}

.category-tabs :deep(.el-tabs__active-bar) {
  background-color: #2563eb;
}

.main {
  flex: 1;
  padding: 2rem 0;
}

.container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 1.5rem;
}

.logo {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.article-card {
  background: #ffffff;
  border-radius: 8px;
  padding: 1.5rem 2rem;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid #e5e7eb;
}

.article-card:hover {
  border-color: #2563eb;
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.1);
}

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 0.75rem;
}

.article-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.article-date {
  font-size: 0.875rem;
  color: #9ca3af;
  flex-shrink: 0;
  margin-left: 1rem;
}

.article-excerpt {
  color: #6b7280;
  line-height: 1.6;
  margin: 0 0 1rem 0;
  font-size: 0.95rem;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.article-tags {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.article-tags :deep(.el-tag) {
  background: #ecfdf5;
  border-color: #d1fae5;
  color: #059669;
}

.read-more {
  font-size: 0.875rem;
  color: #2563eb;
  opacity: 0;
  transition: opacity 0.2s;
}

.article-card:hover .read-more {
  opacity: 1;
}

@media (max-width: 768px) {
  .article-card {
    padding: 1.25rem;
  }

  .article-header {
    flex-direction: column;
    gap: 0.5rem;
  }

  .article-date {
    margin-left: 0;
  }

  .read-more {
    opacity: 1;
  }
}
</style>
