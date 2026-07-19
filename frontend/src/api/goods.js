import request from '@/utils/request'

export function getGoodsPage(params) {
  return request({
    url: '/goods/page',
    method: 'get',
    params
  })
}

export function getGoodsList() {
  return request({
    url: '/goods/list',
    method: 'get'
  })
}

export function getGoodsById(id) {
  return request({
    url: `/goods/${id}`,
    method: 'get'
  })
}

export function addGoods(data) {
  return request({
    url: '/goods',
    method: 'post',
    data
  })
}

export function updateGoods(data) {
  return request({
    url: '/goods',
    method: 'put',
    data
  })
}

export function deleteGoods(id) {
  return request({
    url: `/goods/${id}`,
    method: 'delete'
  })
}

export function uploadGoodsImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/goods/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
