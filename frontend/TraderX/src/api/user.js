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

export function resetPassword(query) {
  return request({
    url: '/password/forgot',
    method: 'post',
    params: query
  })
}

export function renew(data) {
  return request({
    url: '/password/renew',
    method: 'post',
    data
  })
}

export function getUser(username) {
  return request({
    url: '/users/profile/' + username,
    method: 'get'

  })
}

export function unfollowUser(username) {
  return request({
    url: '/follow/unfollow_user',
    method: 'post',
    params: username
  })
}

export function followUser(username) {
  return request({
    url: '/follow/follow_user',
    method: 'post',
    params: username
  })
}

export function setProfilePublic() {
  return request({
    url: '/users/set_profile/public',
    method: 'post',
  })
}

export function setProfilePrivate() {
  return request({
    url: '/users/set_profile/private',
    method: 'post',
  })
}

export function changeIBAN(data) {
  return request({
    url: '/users/edit',
    method: 'post',
    data
  })
}
