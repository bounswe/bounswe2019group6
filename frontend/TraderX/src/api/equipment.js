import request from '@/utils/request'

export function listEquipment(equipmentType) {
  return request({
    url: '/equipment/' + equipmentType + '/list',
    method: 'get'
  })
}

export function getEquipment(equipmentName) {
  return request({
    url: '/equipment/' + equipmentName,
    method: 'get'
  })
}