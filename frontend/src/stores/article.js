import { defineStore } from 'pinia'
import axios from 'axios'

export const useArticleStore = defineStore('article', {
  state: () => {
    return {
      articles: [],
      categories: [],
      tags: [],
      loading: false,
      error: null
    }
  },

  getters: {
    getArticles: (state) => {
      return state.articles
    },
    getCategories: (state) => {
      return state.categories
    },
    getTags: (state) => {
      return state.tags
    },
    isLoading: (state) => {
      return state.loading
    },
    getError: (state) => {
      return state.error
    }
  },

  actions: {
    async fetchArticles() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get('/api/v1/articles')
        this.articles = response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async fetchArticle(id) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/api/v1/articles/${id}`)
        return response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async createArticle(articleData) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post('/api/v1/articles', articleData)
        this.articles.unshift(response.data)
        return response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateArticle(id, articleData) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.put(`/api/v1/articles/${id}`, articleData)
        const index = this.articles.findIndex(a => a.id === id)
        if (index !== -1) {
          this.articles[index] = response.data
        }
        return response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async deleteArticle(id) {
      this.loading = true
      this.error = null
      try {
        await axios.delete(`/api/v1/articles/${id}`)
        this.articles = this.articles.filter(a => a.id !== id)
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async fetchCategories() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get('/api/v1/categories')
        this.categories = response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async fetchTags() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get('/api/v1/tags')
        this.tags = response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})
