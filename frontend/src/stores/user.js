import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => {
    return {
      user: null,
      isAuthenticated: false
    }
  },

  getters: {
    getUser: (state) => {
      return state.user
    },
    checkIsAuthenticated: (state) => {
      return state.isAuthenticated
    }
  },

  actions: {
    setUser(user) {
      this.user = user
      this.isAuthenticated = true
      localStorage.setItem('authenticated', 'true')
      localStorage.setItem('user', JSON.stringify(user))
    },

    logout() {
      this.user = null
      this.isAuthenticated = false
      localStorage.removeItem('authenticated')
      localStorage.removeItem('user')
    },

    loadFromStorage() {
      const user = localStorage.getItem('user')
      const authenticated = localStorage.getItem('authenticated')
      if (user && authenticated === 'true') {
        this.user = JSON.parse(user)
        this.isAuthenticated = true
      }
    }
  }
})
