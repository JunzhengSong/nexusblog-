import axios from 'axios'
import { ElMessage } from 'element-plus'

const api = axios.create({
  baseURL: '/api/v1',
  timeout: 10000,
  withCredentials: true
})

api.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response) {
      const { status, data } = error.response

      switch (status) {
        case 401:
          ElMessage.error('未授权，请重新登录')
          localStorage.removeItem('authenticated')
          localStorage.removeItem('user')
          window.location.href = '/admin/login'
          break
        case 403:
          ElMessage.error('无权访问')
          break
        case 404:
          ElMessage.error(data.message || '请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器错误，请稍后重试')
          break
        default:
          ElMessage.error(data.message || '请求失败')
      }
    } else if (error.request) {
      ElMessage.error('网络错误，请检查网络连接')
    } else {
      ElMessage.error('请求配置错误')
    }

    return Promise.reject(error)
  }
)

// GitHub仓库配置相关API
export function getRepoConfigs() {
  return api.get('/github/repos')
}

export function getRepoConfig(id) {
  return api.get(`/github/repos/${id}`)
}

export function createRepoConfig(data) {
  return api.post('/github/repos', data)
}

export function updateRepoConfig(id, data) {
  return api.put(`/github/repos/${id}`, data)
}

export function deleteRepoConfig(id) {
  return api.delete(`/github/repos/${id}`)
}

export function testRepoConnection(data) {
  return api.post('/github/repos/test-connection', data)
}

// 同步操作相关API
export function triggerSync(repoId) {
  return api.post(`/github/sync/${repoId}`)
}

export function getSyncStatus(syncId) {
  return api.get(`/github/sync/status/${syncId}`)
}

export function getSyncHistory(params) {
  return api.get('/github/sync/history', { params })
}

export function getSyncHistoryByRepoId(repoId) {
  return api.get(`/github/sync/history/re/repo/${repoId}`)
}

// 同步文章相关API
export function getSyncArticles(params) {
  return api.get('/github/sync-articles', { params })
}

export function getSyncArticlesByRepoId(repoId) {
  return api.get(`/github/sync-articles/repo/${repoId}`)
}

export function deleteSyncArticle(id) {
  return api.delete(`/github/sync-articles/${id}`)
}

export function getSyncMappingByArticleId(articleId) {
  return api.get(`/github/sync-articles/article/${articleId}`)
}
