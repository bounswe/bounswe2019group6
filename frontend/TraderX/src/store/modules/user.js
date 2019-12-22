import { login, getInfo, logout, register, confirm, resetPassword, renew, unfollowUser,
   followUser, setProfilePublic, setProfilePrivate, changeIBAN, updatePassword, 
   becomeBasic, becomeTrader, getAllNotifications, getNewNotifications, acceptFollowRequest, declineFollowRequest,
   readAllNotifications, createPrediction, getPredictionList } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'

const state = {
  token: getToken(),
  userInfo: {},

  // TODO these are deprecated but keep useful ones
  roles: [],
  avatar: '',
  name: '',
  introduction: '',
  isPrivate: false,
  iban: ''
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USERINFO: (state, userInfo) => {
    state.userInfo = userInfo
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_PRIVACY: (state, isPrivate) => {
    state.isPrivate = isPrivate
  },
  SET_IBAN: (state, iban) => {
    state.iban = iban
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_NOTIFICATIONS: (state, notifications) => {
    state.notifications = notifications
  },
  SET_PREDICTION_LIST: (state, predictionList) => {
    state.predictionList = predictionList
  },
  // TODO these are deprecated but keep useful ones
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  }

}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password, googleToken } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password, googleToken: googleToken }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        if (!data) {
          reject('Verification failed, please Login again.')
        }

        const { username, isPrivate, iban, roles } = data

        // roles must be a non-empty array
        if (!roles || roles.length <= 0) {
          reject('getInfo: roles must be a non-null array!')
        }

        // TODO this is deprecated but will be kept until reorganized
        commit('SET_USERINFO', data)
        commit('SET_NAME', username)
        commit('SET_PRIVACY', isPrivate)
        commit('SET_IBAN', iban)
        commit('SET_ROLES', roles)
        // TODO set the ones that we decide to keep
        /*
        commit('SET_AVATAR', avatar)
        commit('SET_INTRODUCTION', introduction)
        */
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, { root: true })

        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // user registration
  // eslint-disable-next-line no-use-before-define
  register({ commit }, data) {
    return new Promise((resolve, reject) => {
      register(data).then(() => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // eslint-disable-next-line no-use-before-define
  confirm({ dispatch }, query) {
    return new Promise((resolve, reject) => {
      confirm(query).then(() => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  resetPassword({ commit }, query) {
    return new Promise((resolve, reject) => {
      resetPassword(query).then(() => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  renew({ commit }, rawData) {
    const { token, newPassword } = rawData
    const data = { token: token, newPassword: newPassword }
    return new Promise((resolve, reject) => {
      renew(data).then(() => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  followUser({ commit }, username) {
    return new Promise((resolve, reject) => {
      followUser(username).then(response => {
        // const { data } = response
        // commit('SET_USER_SEARCH_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  unfollowUser({ commit }, username) {
    return new Promise((resolve, reject) => {
      unfollowUser(username).then(response => {
        // const { data } = response
        // commit('SET_USER_SEARCH_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  acceptFollowRequest({ commit }, username) {
    return new Promise((resolve, reject) => {
      acceptFollowRequest(username).then(response => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  declineFollowRequest({ commit }, username) {
    return new Promise((resolve, reject) => {
      declineFollowRequest(username).then(response => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  setProfilePublic({ commit }, username) {
    return new Promise((resolve, reject) => {
      setProfilePublic().then(response => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  setProfilePrivate({ commit }, username) {
    return new Promise((resolve, reject) => {
      setProfilePrivate().then(response => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  changeIBAN({ commit }, iban) {
    return new Promise((resolve, reject) => {
      changeIBAN(iban).then(() => {
        commit('SET_IBAN', iban)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  getAllNotifications({ commit }) {
    return new Promise((resolve, reject) => {
      getAllNotifications().then(response => {
        commit('SET_NOTIFICATIONS', response.data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  getNewNotifications({ commit }) {
    return new Promise((resolve, reject) => {
      getNewNotifications().then(response => {
        commit('SET_NOTIFICATIONS', response.data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  
  readAllNotifications({ commit }) {
    return new Promise((resolve, reject) => {
      readAllNotifications().then(response => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  updatePassword({ commit }, password) {
    return new Promise((resolve, reject) => {
      updatePassword(password).then(() => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  becomeBasic({ commit }) {
    return new Promise((resolve, reject) => {
      becomeBasic().then(() => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  becomeTrader({ commit }, iban) {
    return new Promise((resolve, reject) => {
      becomeTrader(iban).then(() => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  createPrediction({ commit }, code, type) {
    return new Promise((resolve, reject) => {
      createPrediction(code, type).then(() => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  getPredictionList({ commit }, username) {
    return new Promise((resolve, reject) => {
      getPredictionList(username).then(response => {
        commit('SET_PREDICTION_LIST', response.data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // TODO this is deprecated, will be removed
  // dynamically modify permissions
  changeRoles({ commit, dispatch }, role) {
    // eslint-disable-next-line no-async-promise-executor
    return new Promise(async resolve => {
      const token = role + '-token'

      commit('SET_TOKEN', token)
      setToken(token)

      const { roles } = await dispatch('getInfo')

      resetRouter()

      // generate accessible routes map based on roles
      const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })

      // dynamically add accessible routes
      router.addRoutes(accessRoutes)

      // reset visited views and cached views
      dispatch('tagsView/delAllViews', null, { root: true })

      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
