import { getEquipment, listEquipment, getAllCurrencies, getAllCryptoCurrencies, getAllStocks, depositMoney, buyEquipment, getAssetInfo} from '@/api/equipment'

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
  },
  SET_CRYPTO_CURRENCY_RESULT: (state, result) => {
    state.cryptoCurrencyResult = result
  },
  SET_STOCK_RESULT: (state, result) => {
    state.stockResult = result
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
  listEquipment({ commit }, equipmentType) {
    return new Promise((resolve, reject) => {
      listEquipment(equipmentType).then(response => {
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
  getAllCryptoCurrencies({ commit }) {
    return new Promise((resolve, reject) => {
        getAllCryptoCurrencies().then(response => {
        const { data } = response
        commit('SET_CRYPTO_CURRENCY_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  getAllStocks({ commit }) {
    return new Promise((resolve, reject) => {
        getAllStocks().then(response => {
        const { data } = response
        commit('SET_STOCK_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  depositMoney({ commit }, amount) {
    return new Promise((resolve, reject) => {
      depositMoney(amount).then(response => {
        const { data } = response
        commit('SET_QUERY_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  buyEquipment({ commit }, code, amount) {
    return new Promise((resolve, reject) => {
      buyEquipment(code, amount).then(response => {
        const { data } = response
        commit('SET_QUERY_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  getAssetInfo({ commit }, equipmentName) {
    return new Promise((resolve, reject) => {
      getAssetInfo(equipmentName).then(response => {
        const { data } = response
        commit('SET_QUERY_RESULT', data)
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
