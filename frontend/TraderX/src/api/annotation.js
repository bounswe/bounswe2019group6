import request from '@/utils/request'

export function getAnnotationList(articleId) {
  return request({
    url: '/annotation/all/' + articleId,
    method: 'get',
  })
}

export function createAnnotation(data) {
  return request({
    url: '/annotation/create',
    method: 'post',
    data
  })
}

export function updateAnnotation(data) {
    return request({
      url: '/annotation/update',
      method: 'post',
      data
    })
  }

export function deleteAnnotation(query) {
  return request({
    url: '/annotation/delete',
    method: 'delete',
    params: query
  })
}


