import api from './index'

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
  return api.get(`/github/sync/history/repo/${repoId}`)
}

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
