<template>
  <div class="github-repos-page">
    <div class="page-header">
      <h1>GitHub仓库配置</h1>
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        新增仓库
      </el-button>
    </div>

    <el-table :data="repoList" v-loading="loading" border>
      <el-table-column prop="name" label="配置名称" width="150" />
      <el-table-column prop="repoUrl" label="仓库地址" min-width="250" />
      <el-table-column prop="branch" label="分支" width="120" />
      <el-table-column prop="docRootPath" label="文档目录" width="150" />
      <el-table-column prop="defaultCategory" label="默认分类" width="120" />
      <el-table-column prop="defaultTags" label="默认标签" width="150" />
      <el-table-column prop="enabled" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.enabled ? 'success' : 'info'">
            {{ row.enabled ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" min-width="150" />
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="handleTestConnection(row)">
            测试连接
          </el-button>
          <el-button size="small" type="primary" @click="handleSync(row)">
            立即同步
          </el-button>
          <el-button size="small" @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="editMode ? '编辑仓库配置' : '新增仓库配置'"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="配置名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入配置名称" />
        </el-form-item>
        <el-form-item label="仓库地址" prop="repoUrl">
          <el-input v-model="form.repoUrl" placeholder="例如：https://github.com/username/repo" />
        </el-form-item>
        <el-form-item label="分支" prop="branch">
          <el-input v-model="form.branch" placeholder="例如：main" />
        </el-form-item>
        <el-form-item label="文档目录" prop="docRootPath">
          <el-input v-model="form.docRootPath" placeholder="例如：docs/ 留空表示根目录" />
        </el-form-item>
        <el-form-item label="访问令牌">
          <el-input v-model="form.accessToken" type="password" placeholder="私有仓库需要填写，公开仓库可留空" />
          <div class="form-tip">令牌只在修改时更新，不会明文显示</div>
        </el-form-item>
        <el-form-item label="默认分类">
          <el-input v-model="form.defaultCategory" placeholder="文章没有指定分类时使用" />
        </el-form-item>
        <el-form-item label="默认标签">
          <el-input v-model="form.defaultTags" placeholder="多个标签用逗号分隔，例如：Java,Spring" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.enabled" active-text="启用" inactive-text="禁用" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="form.description" :rows="3" placeholder="请输入描述信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
  getRepoConfigs,
  createRepoConfig,
  updateRepoConfig,
  deleteRepoConfig,
  testRepoConnection,
  triggerSync
} from '@/api/github'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const editMode = ref(false)
const formRef = ref()
const repoList = ref([])

const form = ref({
  name: '',
  repoUrl: '',
  branch: 'main',
  docRootPath: '',
  accessToken: '',
  defaultCategory: '',
  defaultTags: '',
  enabled: true,
  description: ''
})

const rules = {
  name: [
    { required: true, message: '请输入配置名称', trigger: 'blur' }
  ],
  repoUrl: [
    { required: true, message: '请输入仓库地址', trigger: 'blur' }
  ],
  branch: [
    { required: true, message: '请输入分支名称', trigger: 'blur' }
  ]
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getRepoConfigs()
    repoList.value = res
  } catch (error) {
    ElMessage.error('加载失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 新增
const handleCreate = () => {
  editMode.value = false
  form.value = {
    name: '',
    repoUrl: '',
    branch: 'main',
    docRootPath: '',
    accessToken: '',
    defaultCategory: '',
    defaultTags: '',
    enabled: true,
    description: ''
  }
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  editMode.value = true
  form.value = { ...row }
  // 编辑时清空令牌，用户不修改就不更新
  form.value.accessToken = ''
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitLoading.value = true
    try {
      if (editMode.value) {
        await updateRepoConfig(form.value.id, form.value)
        ElMessage.success('更新成功')
      } else {
        await createRepoConfig(form.value)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      loadData()
    } catch (error) {
      ElMessage.error('操作失败：' + error.message)
    } finally {
      submitLoading.value = false
    }
  })
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除仓库配置"${row.name}"吗？删除后相关同步历史不会删除，但会停止同步。`,
      '提示',
      { type: 'warning' }
    )

    await deleteRepoConfig(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error.action !== 'cancel') {
      ElMessage.error('删除失败：' + error.message)
    }
  }
}

// 测试连接
const handleTestConnection = async (row) => {
  try {
    const res = await testRepoConnection(row)
    if (res) {
      ElMessage.success('连接成功！')
    } else {
      ElMessage.error('连接失败，请检查配置是否正确')
    }
  } catch (error) {
    ElMessage.error('连接失败：' + error.message)
  }
}

// 触发同步
const handleSync = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要立即同步仓库"${row.name}"吗？同步会拉取仓库中的所有Markdown文件。`,
      '提示',
      { type: 'info' }
    )

    const res = await triggerSync(row.id)
    ElMessage.success(`同步任务已提交，任务ID：${res.syncId}，可以在同步历史中查看进度`)
  } catch (error) {
    if (error.action !== 'cancel') {
      ElMessage.error('操作失败：' + error.message)
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.github-repos-page {
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

.form-tip {
  font-size: 0.75rem;
  color: #9ca3af;
  margin-top: 0.25rem;
}
</style>
