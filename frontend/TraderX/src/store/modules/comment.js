import { getCommentList, deleteComment, editComment, postComment, revokeVote, voteComment } from '@/api/comment'

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
    getCommentList({ commit }, equipmentCode) {
        return new Promise((resolve, reject) => {
            getCommentList(equipmentCode).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    deleteComment({ commit }, commentId) {
        return new Promise((resolve, reject) => {
            deleteComment(commentId).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    editComment({ commit }, commentId, commentDict) {
        return new Promise((resolve, reject) => {
            editComment(commentId, commentDict).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    postComment( { commit }, data) {
        console.log('equipmentCode in modules is: ')
        console.log(data.equipmentCode)
        console.log('commentDict in modules is: ')
        console.log(data.commentDict)
        return new Promise((resolve, reject) => {
            postComment(data.equipmentCode, data.commentDict).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },


    revokeVote({ commit }, commentId) {
        return new Promise((resolve, reject) => {
            revokeVote(commentId).then(response => {
                const { data } = response
                commit('SET_QUERY_RESULT', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    voteComment({ commit }, commentId, voteType) {
        return new Promise((resolve, reject) => {
            voteComment(commentId, voteType).then(response => {
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
