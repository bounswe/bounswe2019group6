import request from '@/utils/request'

export function getCommentList(equipmentCode) {
  return request({
    url: '/comment/equipment/' + equipmentCode,
    method: 'get'
  })
}

export function deleteComment(commentId) {
    return request({
        url: '/comment/equipment/delete/' + commentId,
        method: 'delete',
    })
}

// query: {comment = 'str'}
export function editComment(commentId, commentDict) {
    return request({
        url: '/comment/equipment/edit/' + commentId,
        method: 'post',
        commentDict,
    })
}

// commentDict = {comment = 'str'}
export function postComment(equipmentCode, commentDict) {
    console.log('commentDict in api is:')
    console.log(commentDict)
    return request({
        url: '/comment/equipment/post/' + equipmentCode,
        method: 'post',
        commentDict,
    })
}

export function revokeVote(commentId) {
    console.log('commentId in revokeVote: ' + commentId)
    return request({
        url: '/comment/equipment/revoke/' + commentId,
        method: 'delete',
    })
}

export function voteComment(commentId, voteType) {
    return request({
        url: '/comment/equipment/vote/' + commentId + '/' + voteType,
        method: 'post',
    })
}