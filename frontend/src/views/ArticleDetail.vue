<template>
  <div class="article-detail">
    <header class="header">
      <div class="container">
        <el-link @click="goBack">← 返回首页</el-link>
      </div>
    </header>

    <main class="main">
      <div class="container" v-if="article">
        <el-skeleton :rows="10" animated v-if="loading" />

        <article v-else class="article">
          <h1 class="title">{{ article.title }}</h1>

          <div class="meta">
            <el-tag v-if="article.category" size="small" type="info">
              {{ article.category.name }}
            </el-tag>
            <el-tag
              v-for="tag in article.tags"
              :key="tag.id"
              size="small"
            >
              {{ tag.name }}
            </el-tag>
            <span class="date">
              {{ formatDate(article.createdAt) }}
            </span>
          </div>

          <div class="content" v-html="renderedContent"></div>
        </article>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { articleApi } from '@/api'
import { renderMarkdown } from '@/utils/markdown'

const router = useRouter()
const route = useRoute()

const article = ref(null)
const loading = ref(false)

const renderedContent = computed(() => {
  return renderMarkdown(article.value?.content || '')
})

const fetchArticle = async () => {
  loading.value = true
  try {
    const response = await articleApi.getById(route.params.id)
    article.value = response
  } catch (error) {
    console.error('Failed to fetch article:', error)
    router.push('/')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/')
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  fetchArticle()
})
</script>

<style scoped>
.article-detail {
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
  max-width: 800px;
  margin: 0 auto;
  padding: 0 1.5rem;
}

.main {
  flex: 1;
  padding: 2rem 0;
}

.article {
  background: #ffffff;
  padding: 2rem;
  border-radius: 8px;
}

.title {
  font-size: 2rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 1rem 0;
  line-height: 1.3;
}

.meta {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.date {
  font-size: 0.875rem;
  color: #6b7280;
  margin-left: auto;
}

.content {
  line-height: 1.8;
  color: #374151;
}

.content :deep(h1),
.content :deep(h2),
.content :deep(h3),
.content :deep(h4),
.content :deep(h5),
.content :deep(h6) {
  margin: 1.5em 0 0.5em 0;
  font-weight: 600;
  line-height: 1.3;
}

.content :deep(p) {
  margin: 1em 0;
}

.content :deep(pre) {
  background: #1f2937;
  color: #e5e7eb;
  padding: 1rem;
  border-radius: 4px;
  overflow-x: auto;
  margin: 1em 0;
}

.content :deep(code) {
  background: #f3f4f6;
  color: #dc2626;
  padding: 0.2em 0.4em;
  border-radius: 3px;
  font-size: 0.9em;
}

.content :deep(pre code) {
  background: transparent;
  color: inherit;
  padding: 0;
}

.content :deep(ul),
.content :deep(ol) {
    margin: 1em 0;
  padding-left: 2em;
}

.content :deep(li) {
  margin: 0.5em 0;
}

.content :deep(blockquote) {
  border-left: 4px solid #e5e7eb;
  padding-left: 1em;
  margin: 1em 0;
  color: #6b7280;
}

.content :deep(a) {
  color: #2563eb;
  text-decoration: underline;
}

.content :deep(a:hover) {
  color: #1d4ed8;
}
</style>
