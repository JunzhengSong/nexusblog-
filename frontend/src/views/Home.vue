<template>
  <div class="home">
    <header class="header">
      <div class="container">
        <h1 class="logo">NexusBlog</h1>
        <el-link type="primary" href="/admin/login" v-if="!isAuthenticated">管理后台</el-link>
        <el-button type="primary" @click="goToAdmin" v-else>管理后台</el-button>
      </div>
    </header>

    <main class="main">
      <div class="container">
        <el-empty v-if="articles.length === 0 && !loading" description="暂无文章" />

        <div class="article-list" v-else>
          <el-skeleton :rows="3" animated v-if="loading" v-for="i in 3" :key="i" />

          <article-card
            v-for="article in articles"
            :key="article.id"
            :article="article"
            @click="viewArticle(article.id)"
          />
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { articleApi } from '@/api'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const articles = ref([])
const loading = ref(false)

const isAuthenticated = computed(() => userStore.checkIsAuthenticated)

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

const viewArticle = (id) => {
  router.push(`/article/${id}`)
}

const goToAdmin = () => {
  router.push('/admin')
}

onMounted(() => {
  userStore.loadFromStorage()
  fetchArticles()
})
</script>

<style scoped>
.home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background: #ffffff;
  border-bottom: 1px solid #e5e7eb;
  padding: 1rem 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.main {
  flex: 1;
  padding: 2rem 0;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}
</style>
