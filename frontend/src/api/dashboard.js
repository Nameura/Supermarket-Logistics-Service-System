import request from '@/utils/request'

export function getStatistics() {
  return request({
    url: '/dashboard/statistics',
    method: 'get'
  })
}

export function getEmployeeStatistics() {
  return request({
    url: '/dashboard/employee',
    method: 'get'
  })
}

export function getManagerStatistics() {
  return request({
    url: '/dashboard/manager',
    method: 'get'
  })
}
