import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/users/me',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/signout',
    method: 'post'
  })
}

export function register(data) {
  return request({
    url: '/signup',
    method: 'post',
    data
  })
}

export function confirm(query) {
  return request({
    url: '/signup/confirm',
    method: 'post',
    params: query
  })
}

export function getUser(username) {
  return request({
    url: '/users/profile/' + username,
    method: 'get'
  })
}
