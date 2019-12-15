import { searchUser, getAllUsers, getAllArticles } from '@/api/search'

const state = {
  userSearchResult : {},
  articleSearchResult : {}
}

const mutations = {
  SET_USER_SEARCH_RESULT: (state, result) => {
    state.userSearchResult = result
  },
  SET_ARTICLE_SEARCH_RESULT: (state, result) => {
    state.articleSearchResult = result
  }
}

const actions = {
  searchUser({ commit }, searchText) {
    return new Promise((resolve, reject) => {
      searchUser(searchText).then(response => {
        const { data } = response
        commit('SET_USER_SEARCH_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  getAllUsers({ commit }) {
    return new Promise((resolve, reject) => {
      getAllUsers().then(response => {
        const { data } = response
        commit('SET_USER_SEARCH_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  getAllArticles({ commit }) {
    return new Promise((resolve, reject) => {
      getAllArticles().then(response => {
        const { data } = response
        commit('SET_ARTICLE_SEARCH_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
