<template>
  <div class="admin-dashboard">
    <aside class="sidebar">
      <div class="logo">
        <h2>NexusBlog</h2>
        <p>管理后台</p>
      </div>

      <nav class="nav">
        <el-menu
          :default-active="activeMenu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="/admin">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>

          <el-menu-item index="/admin/articles">
            <el-icon><Document /></el-icon>
            <span>文章管理</span>
          </el-menu-item>

          <el-sub-menu index="github">
            <template #title>
              <el-icon><Link /></el-icon>
              <span>GitHub同步</span>
            </template>
            <el-menu-item index="/admin/github/repos">
              <el-icon><Files /></el-icon>
              <span>仓库配置</span>
            </el-menu-item>
            <el-menu-item index="/admin/github/sync-history">
              <el-icon><Refresh /></el-icon>
              <span>同步历史</span>
            </el-menu-item>
            <el-menu-item index="/admin/github/sync-articles">
              <el-icon><Document /></el-icon>
              <span>同步文章</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </nav>

      <div class="footer">
        <el-button type="text" @click="handleLogout">
          退出登录
        </el-button>
      </div>
    </aside>

    <main class="main">
      <header class="header">
        <div class="user-info">
          <el-icon><User /></el-icon>
          <span>{{ userStore.getUser?.username }}</span>
        </div>
      </header>

      <div class="content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { House, Document, User, Link, Refresh, Files } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { authApi } from '@/api'

const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => router.currentRoute.value.path)

const handleMenuSelect = (index) => {
  router.push(index)
}

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要退出登录吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await authApi.logout()
    userStore.logout()
    router.push('/admin/login')
  } catch (error) {
    // User cancelled
  }
}
</script>

<style scoped>
.admin-dashboard {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 250px;
  background: #1f2937;
  color: #ffffff;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.logo {
  padding: 1.5rem;
  border-bottom: 1px solid #374151;
}

.logo h2 {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0 0 0.25rem 0;
}

.logo p {
  font-size: 0.875rem;
  color: #9ca3af;
  margin: 0;
}

.nav {
  flex: 1;
  padding: 1rem 0;
}

.nav :deep(.el-menu) {
  background: transparent;
  border: none;
}

.nav :deep(.el-sub-menu) {
  color: #d1d5db;
}

.nav :deep(.el-sub-menu__title) {
  color: #d1d5db;
}

.nav :deep(.el-menu-item) {
  color: #d1d5db;
}

.nav :deep(.el-menu-item:hover) {
  background: #374151;
  color: #ffffff;
}

.nav :deep(.el-menu-item.is-active) {
  background: #374151;
  color: #ffffff;
}

.footer {
  padding: 1rem;
  border-top: 1px solid #374151;
}

.footer :deep(.el-button) {
  color: #d1d5db;
  width: 100%;
}

.footer :deep(.el-button:hover) {
  color: #ffffff;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #f3fari;
}

.header {
  background: #ffffff;
  padding: 1rem 2rem;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #374151;
  font-weight: 500;
}

.content {
  flex: 1;
  padding: 2rem;
  overflow: auto;
}
</style>
