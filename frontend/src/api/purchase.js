import request from '@/utils/request'

export function getPurchasePage(params) {
  return request({
    url: '/purchase/page',
    method: 'get',
    params
  })
}

export function getPurchaseList() {
  return request({
    url: '/purchase/list',
    method: 'get'
  })
}

export function getPurchaseById(id) {
  return request({
    url: `/purchase/${id}`,
    method: 'get'
  })
}

export function addPurchase(data) {
  return request({
    url: '/purchase',
    method: 'post',
    data
  })
}

export function updatePurchase(data) {
  return request({
    url: '/purchase',
    method: 'put',
    data
  })
}

export function approvePurchase(id, data) {
  return request({
    url: `/purchase/approve/${id}`,
    method: 'put',
    data
  })
}

export function deletePurchase(id) {
  return request({
    url: `/purchase/${id}`,
    method: 'delete'
  })
}

export function getPurchaseStatistics() {
  return request({
    url: '/purchase/statistics',
    method: 'get'
  })
}

export function batchApprovePurchase(data) {
  return request({
    url: '/purchase/batch-approve',
    method: 'put',
    data
  })
}
