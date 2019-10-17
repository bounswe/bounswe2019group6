import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login?username=' + data.username + '&password=' + data.password,
    method: 'post'
  })
}

export function getInfo(token) {
  return request({
    url: '/users/me',
    method: 'get',
    headers: {
      'Authorization': 'Bearer ' + token
    }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}
