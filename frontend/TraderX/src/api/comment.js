import request from '@/utils/request'

export function getEquipmentCommentList(equipmentCode) {
  return request({
    url: '/comment/equipment/' + equipmentCode,
    method: 'get'
  })
}

export function deleteEquipmentComment(commentId) {
    return request({
        url: '/comment/equipment/delete/' + commentId,
        method: 'delete',
    })
}

// data: {comment = 'str'}
export function editEquipmentComment(id, data) {
    return request({
        url: '/comment/equipment/edit/' + id,
        method: 'post',
        data,
    })
}

// data = {comment = 'str'}
export function postEquipmentComment(code, data) {
    return request({
        url: '/comment/equipment/post/' + code,
        method: 'post',
        data
    })
}

export function revokeEquipmentVote(commentId) {
    return request({
        url: '/comment/equipment/revoke/' + commentId,
        method: 'delete',
    })
}

export function voteEquipmentComment(commentId, voteType) {
    return request({
        url: '/comment/equipment/vote/' + commentId + '/' + voteType,
        method: 'post',
    })
}

export function getArticleCommentList(articleId) {
    return request({
      url: '/comment/article/' + articleId,
      method: 'get'
    })
  }
  
  export function deleteArticleComment(commentId) {
      return request({
          url: '/comment/article/delete/' + commentId,
          method: 'delete',
      })
  }
  
  export function editArticleComment(articleId, data) {
      return request({
          url: '/comment/article/edit/' + articleId,
          method: 'post',
          data,
      })
  }
  
  export function postArticleComment(articleId, data) {
      return request({
          url: '/comment/article/post/' + articleId,
          method: 'post',
          data
      })
  }
  
  export function revokeArticleVote(commentId) {
      return request({
          url: '/comment/article/revoke/' + commentId,
          method: 'delete',
      })
  }
  
  export function voteArticleComment(commentId, voteType) {
      return request({
          url: '/comment/article/vote/' + commentId + '/' + voteType,
          method: 'post',
      })
  }