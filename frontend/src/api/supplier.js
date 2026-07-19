import request from '@/utils/request'

export function getSupplierPage(params) {
  return request({
    url: '/supplier/page',
    method: 'get',
    params
  })
}

export function getSupplierList() {
  return request({
    url: '/supplier/list',
    method: 'get'
  })
}

export function getSupplierById(id) {
  return request({
    url: `/supplier/${id}`,
    method: 'get'
  })
}

export function addSupplier(data) {
  return request({
    url: '/supplier',
    method: 'post',
    data
  })
}

export function updateSupplier(data) {
  return request({
    url: '/supplier',
    method: 'put',
    data
  })
}

export function deleteSupplier(id) {
  return request({
    url: `/supplier/${id}`,
    method: 'delete'
  })
}
