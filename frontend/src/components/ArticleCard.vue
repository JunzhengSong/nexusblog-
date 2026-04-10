<template>
  <div class="article-card" @click="$emit('click')">
    <h2 class="title">{{ article.title }}</h2>
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
    </div>
    <div class="date">
      {{ formatDate(article.createdAt) }}
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  article: {
    type: Object,
    required: true
  }
})

defineEmits(['click'])

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}
</script>

<style scoped>
.article-card {
  background: #ffffff;
  border-radius: 8px;
  padding: 1.5rem;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #e5e7eb;
}

.article-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 0.75rem 0;
}

.meta {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.date {
  font-size: 0.875rem;
  color: #6b7280;
}
</style>
