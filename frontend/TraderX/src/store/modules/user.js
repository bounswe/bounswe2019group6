import { login, getInfo, logout, register, confirm } from '@/api/user'
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
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
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

        console.log(data)
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
  register({ commit }, data) {
    return new Promise((resolve, reject) => {
      register(data).then(() => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  confirm({ dispatch }, query) {
    return new Promise((resolve, reject) => {
      confirm(query).then(() => {
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
