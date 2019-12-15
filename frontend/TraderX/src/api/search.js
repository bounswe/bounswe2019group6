import request from '@/utils/request'

export function searchUser(userName) {
  return request({
    url: '/users/profile/' + userName,
    method: 'get'
  })
}

export function getAllUsers() {
  return request({
    url: '/users/getAll',
    method: 'get'
  })
}

export function getAllArticles() {
  return request({
    url: '/article/all',
    method: 'get'
  })
}

export function writeArticle(data) {
  return request({
    url: '/article/write',
    method: 'post',
    data,
  })
}

export function getArticleByUserName(username) {
  return request({
    url: '/article/byUsername/'+username,
    method: 'get',
  })
}
