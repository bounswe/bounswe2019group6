import { getEquipment, listEquipment, getAllCurrencies} from '@/api/equipment'

const state = {
  equipmentQueryResult : {

  }
}

const mutations = {
  SET_QUERY_RESULT: (state, result) => {
    state.equipmentQueryResult = result
  },
  SET_CURRENCY_RESULT: (state, result) => {
    state.currencyResult = result
  }
}

const actions = {
  getEquipment({ commit }, equipmentName) {
    return new Promise((resolve, reject) => {
        getEquipment(equipmentName).then(response => {
        const { data } = response
        commit('SET_QUERY_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  listEquipment({ commit }) {
    return new Promise((resolve, reject) => {
      listEquipment().then(response => {
        const { data } = response
        commit('SET_QUERY_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  getAllCurrencies({ commit }) {
    return new Promise((resolve, reject) => {
        getAllCurrencies().then(response => {
        const { data } = response
        commit('SET_CURRENCY_RESULT', data)
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
