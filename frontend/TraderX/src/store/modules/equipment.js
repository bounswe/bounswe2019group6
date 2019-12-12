import { getEquipment, listEquipment, getAllCurrencies, getAllCryptoCurrencies, getAllStocks, depositMoney, 
        buyEquipment, sellEquipment, getAssetInfo, getMyAllPortfolios, createPortfolio, deletePortfolio, 
        addEquipmentToPortfolio, deleteEquipmentFromPortfolio} from '@/api/equipment'

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
  },
  SET_PORTFOLIO_RESULT: (state, result) => {
    state.allPortfolios = result
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
  sellEquipment({ commit }, code, amount) {
    return new Promise((resolve, reject) => {
      sellEquipment(code, amount).then(response => {
        const { data } = response
        commit('SET_QUERY_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  getAssetInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getAssetInfo().then(response => {
        const { data } = response
        commit('SET_QUERY_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  getMyAllPortfolios({ commit }) {
    return new Promise((resolve, reject) => {
      getMyAllPortfolios().then(response => {
        const { data } = response
        commit('SET_PORTFOLIO_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  createPortfolio({ commit }, username, portfolioname) {
    return new Promise((resolve, reject) => {
      createPortfolio(username, portfolioname).then(response => {
        const { data } = response
        commit('SET_QUERY_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  deletePortfolio({ commit }, username, portfolioname) {
    return new Promise((resolve, reject) => {
      deletePortfolio(username, portfolioname).then(response => {
        const { data } = response
        commit('SET_QUERY_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  addEquipmentToPortfolio({ commit }, username, portfolioname, equipmentname) {
    return new Promise((resolve, reject) => {
      addEquipmentToPortfolio(username, portfolioname, equipmentname).then(response => {
        const { data } = response
        commit('SET_QUERY_RESULT', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  deleteEquipmentFromPortfolio({ commit }, username, portfolioname, equipmentname) {
    return new Promise((resolve, reject) => {
      deleteEquipmentFromPortfolio(username, portfolioname, equipmentname).then(response => {
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
