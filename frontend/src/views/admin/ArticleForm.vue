<template>
  <div class="article-form">
    <div class="header">
      <h2>{{ isEdit ? '编辑文章' : '新建文章' }}</h2>
      <el-button @click="goBack">返回</el-button>
    </div>

    <el-card class="form-card">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="请输入文章标题"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="分类">
          <el-select
            v-model="form.categoryId"
            placeholder="请选择分类"
            clearable
            style="width: 100%;"
          >
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="标签">
          <el-select
            v-model="form.tagIds"
            placeholder="请选择标签"
            multiple
            style="width: 100%;"
          >
            <el-option
              v-for="tag in tags"
              :key="tag.id"
              :label="tag.name"
              :value="tag.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <div class="editor-container">
            <div class="editor-pane">
              <el-input
                v-model="form.content"
                type="textarea"
                :rows="20"
                placeholder="请输入 Markdown 内容..."
                @input="updatePreview"
              />
            </div>
            <div class="preview-pane">
              <div class="preview-header">预览</div>
              <div class="preview-content" v-html="previewContent"></div>
            </div>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            @click="handleSubmit"
          >
            {{ isEdit ? '更新' : '发布' }}
          </el-button>
          <el-button @click="goBack" style="margin-left: 10px;">
            取消
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { articleApi, categoryApi, tagApi } from '@/api'
import { renderMarkdown } from '@/utils/markdown'

const router = useRouter()
const route = useRoute()

const formRef = ref()
const loading = ref(false)
const categories = ref([])
const tags = ref([])

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  title: '',
  content: '',
  categoryId: null,
  tagIds: []
})

const previewContent = ref('')

const rules = {
  title: [
    { required: true, message: '请输入文章标题', trigger: 'blur' }
  ]
}

const updatePreview = () => {
  previewContent.value = renderMarkdown(form.content || '')
}

const fetchCategories = async () => {
  try {
    const response = await categoryApi.getAll()
    categories.value = response.data
  } catch (error) {
    console.error('Failed to fetch categories:', error)
  }
}

const fetchTags = async () => {
  try {
    const response = await tagApi.getAll()
    tags.value = response.data
  } catch (error) {
    console.error('Failed to fetch tags:', error)
  }
}

const fetchArticle = async (id) => {
  try {
    const response = await articleApi.getById(id)
    const article = response.data

    form.title = article.title || ''
    form.content = article.content || ''
    form.categoryId = article.category?.id || null
    form.tagIds = article.tags?.map(t => t.id) || []

    updatePreview()
  } catch (error) {
    console.error('Failed to fetch article:', error)
    ElMessage.error('获取文章失败')
    goBack()
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  const valid = await formRef.value.validate().catch(() => false)
  if (valid) {
    loading.value = true
    try {
      if (isEdit.value) {
        await articleApi.update(route.params.id, form)
        ElMessage.success('文章更新成功')
      } else {
        await articleApi.create(form)
        ElMessage.success('文章发布成功')
      }
      router.push('/admin/articles')
    } catch (error) {
      console.error('Failed to save article:', error)
      ElMessage.error(isEdit.value ? '更新失败' : '发布失败')
    } finally {
      loading.value = false
    }
  }
}

const goBack = () => {
  router.push('/admin/articles')
}

onMounted(async () => {
  await Promise.all([fetchCategories(), fetchTags()])

  if (isEdit.value) {
    await fetchArticle(route.params.id)
  }
})
</script>

<style scoped>
.article-form {
  max-width: 1200px;
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

.form-card {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.editor-container {
  display: flex;
  gap: 1rem;
  width: 100%;
}

.editor-pane {
  flex: 1;
}

.preview-pane {
  flex: 1;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.preview-header {
  background: #f5f7fa;
  padding: 0.5rem 1rem;
  font-weight: 500;
  color: #606266;
  border-bottom: 1px solid #dcdfe6;
}

.preview-content {
  padding: 1rem;
  min-height: 400px;
  overflow: auto;
}

.preview-content :deep(h1),
.preview-content :deep(h2),
.preview-content :deep(h3),
.preview-content :deep(h4),
.preview-content :deep(h5),
.preview-content :deep(h6) {
  margin: 1em 0 0.5em 0;
  font-weight: 600;
}

.preview-content :deep(p) {
  margin: 1em 0;
}

.preview-content :deep(pre) {
  background: #f4f4f4;
  padding: 0.75rem;
  border-radius: 4px;
  overflow-x: auto;
  margin: 1em 0;
}

.preview-content :deep(code) {
  background: #f4f4f4;
  padding: 0.2em 0.4em;
  border-radius: 3px;
}

.preview-content :deep(pre code) {
  background: transparent;
  padding: 0;
}

.preview-content :deep(ul),
.preview-content :deep(ol) {
  margin: 1em 0;
  padding-left: 2em;
}

.preview-content :deep(li) {
  margin: 0.5em 0;
}

.preview-content :deep(blockquote) {
  border-left: 4px solid #dcdfe6;
  padding-left: 1em;
  margin: 1em 0;
  color: #606266;
}

.preview-content :deep(a) {
  color: #409eff;
}

.preview-content :deep(a:hover) {
  color: #66b1ff;
}
</style>
