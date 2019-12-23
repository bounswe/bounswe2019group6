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
    url: '/follow/unfollow',
    method: 'post',
    params: username
  })
}

export function followUser(username) {
  return request({
    url: '/follow/follow',
    method: 'post',
    params: username
  })
}

export function acceptFollowRequest(username) {
  return request({
    url: '/follow/request/accept',
    method: 'post',
    params: username
  })
}

export function declineFollowRequest(username) {
  return request({
    url: '/follow/request/decline',
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

export function updatePassword(password) {
  return request({
    url: '/password/change',
    method: 'post',
    data: password
  })
}

export function becomeBasic() {
  return request({
    url: '/users/set_profile/basic',
    method: 'post',
  })
}

export function becomeTrader(iban) {
  return request({
    url: '/users/set_profile/trader',
    method: 'post',
    params: iban
  })
}

export function getAllNotifications() {
  return request({
    url: '/notification',
    method: 'get',
  })
}

export function getNewNotifications() {
  return request({
    url: '/notification/news',
    method: 'get',
  })
}

export function readNotificationByID(query) {
  return request({
    url: '/notification/read/by_id',
    method: 'post',
    params: query
  })
}

export function readAllNotifications() {
  return request({
    url: '/notification/read',
    method: 'post',
  })
}

export function createPrediction(query) {
  return request({
    url: '/prediction/create',
    method: 'post',
    params: query
  })
}

export function getPredictionList(username) {
  return request({
    url: '/prediction/list/'+username,
    method: 'get',
  })
}

export function getMyStats(username) {
  return request({
    url: '/prediction/stats/'+username,
    method: 'get',
  })
}

export function getUserPredictionList(username) {
  return request({
    url: '/prediction/list/'+username,
    method: 'get',
  })
}

export function deletePrediction(id) {
  return request({
    url: '/prediction/delete',
    method: 'delete',
    params: id
  })
}

export function editPrediction(query) {
  return request({
    url: '/prediction/edit',
    method: 'post',
    params: query
  })
}