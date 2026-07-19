import request from '@/utils/request'

export function getInventoryPage(params) {
  return request({
    url: '/inventory/page',
    method: 'get',
    params
  })
}

export function getInventoryList() {
  return request({
    url: '/inventory/list',
    method: 'get'
  })
}

export function getInventoryWarning() {
  return request({
    url: '/inventory/warning',
    method: 'get'
  })
}

export function getInventoryById(id) {
  return request({
    url: `/inventory/${id}`,
    method: 'get'
  })
}

export function addInventory(data) {
  return request({
    url: '/inventory',
    method: 'post',
    data
  })
}

export function updateInventory(data) {
  return request({
    url: '/inventory',
    method: 'put',
    data
  })
}

export function deleteInventory(id) {
  return request({
    url: `/inventory/${id}`,
    method: 'delete'
  })
}

export function getInventoryStatistics() {
  return request({
    url: '/inventory/statistics',
    method: 'get'
  })
}
