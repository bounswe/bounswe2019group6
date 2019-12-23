import { getEquipmentCommentList, deleteEquipmentComment, editEquipmentComment, postEquipmentComment, revokeEquipmentVote, voteEquipmentComment,
        getArticleCommentList, deleteArticleComment, editArticleComment, postArticleComment, revokeArticleVote, voteArticleComment } from '@/api/comment'
import equipment from './equipment'

const state = {
    commentQueryResult : {

    }
}

const mutations = {
    SET_QUERY_RESULT: (state, result) => {
        state.commentQueryResult = result
    },
}

const actions = {
    getEquipmentCommentList({ commit }, equipmentCode) {
        return new Promise((resolve, reject) => {
            getEquipmentCommentList(equipmentCode).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    deleteEquipmentComment({ commit }, commentId) {
        return new Promise((resolve, reject) => {
            deleteEquipmentComment(commentId).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    editEquipmentComment({ commit }, {commentId, commentDict}) {
        return new Promise((resolve, reject) => {
            editEquipmentComment(commentId, commentDict).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    postEquipmentComment( { commit }, {code, commentDict}) {
        return new Promise((resolve, reject) => {
            postEquipmentComment(code, commentDict).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },


    revokeEquipmentVote({ commit }, commentId) {
        return new Promise((resolve, reject) => {
            revokeEquipmentVote(commentId).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    voteEquipmentComment({ commit }, {commentId, voteType}) {
        return new Promise((resolve, reject) => {
            voteEquipmentComment(commentId, voteType).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    getArticleCommentList({ commit }, articleId) {
        return new Promise((resolve, reject) => {
            getArticleCommentList(articleId).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    deleteArticleComment({ commit }, commentId) {
        return new Promise((resolve, reject) => {
            deleteArticleComment(commentId).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    editArticleComment({ commit }, {articleId, data}) {
        return new Promise((resolve, reject) => {
            editArticleComment(articleId, data).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    postArticleComment( { commit }, {articleId, data}) {
        return new Promise((resolve, reject) => {
            postArticleComment(articleId, data).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },


    revokeArticleVote({ commit }, commentId) {
        return new Promise((resolve, reject) => {
            revokeArticleVote(commentId).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    voteArticleComment({ commit }, {commentId, voteType}) {
        return new Promise((resolve, reject) => {
            voteArticleComment(commentId, voteType).then(response => {
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
