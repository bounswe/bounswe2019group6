import { getEquipment, listEquipment } from '@/api/equipment'

const state = {
  equipmentQueryResult : {

  }
}

const mutations = {
  SET_QUERY_RESULT: (state, result) => {
    state.equipmentQueryResult = result
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
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
