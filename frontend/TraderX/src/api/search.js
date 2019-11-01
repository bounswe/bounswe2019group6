import request from '@/utils/request'

export function searchUser(searchText) {
  return request({
    url: '/users/profile/' + searchText,
    method: 'get'
  })
}

export function getAllUsers() {
  return request({
    url: '/users/getAll',
    method: 'get'
  })
}


