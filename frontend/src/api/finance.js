import request from '@/utils/request'

export function getFinancePage(params) {
  return request({
    url: '/finance/page',
    method: 'get',
    params
  })
}

export function getFinanceList() {
  return request({
    url: '/finance/list',
    method: 'get'
  })
}

export function getFinanceById(id) {
  return request({
    url: `/finance/${id}`,
    method: 'get'
  })
}

export function addFinance(data) {
  return request({
    url: '/finance',
    method: 'post',
    data
  })
}

export function updateFinance(data) {
  return request({
    url: '/finance',
    method: 'put',
    data
  })
}

export function deleteFinance(id) {
  return request({
    url: `/finance/${id}`,
    method: 'delete'
  })
}

export function getFinanceStatistics() {
  return request({
    url: '/finance/statistics',
    method: 'get'
  })
}
