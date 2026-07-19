import request from '@/utils/request'

export function getSchedulePage(params) {
  return request({
    url: '/schedule/page',
    method: 'get',
    params
  })
}

export function getScheduleList() {
  return request({
    url: '/schedule/list',
    method: 'get'
  })
}

export function getMySchedule() {
  return request({
    url: '/schedule/my',
    method: 'get'
  })
}

export function getScheduleById(id) {
  return request({
    url: `/schedule/${id}`,
    method: 'get'
  })
}

export function addSchedule(data) {
  return request({
    url: '/schedule',
    method: 'post',
    data
  })
}

export function addScheduleBatch(data) {
  return request({
    url: '/schedule/batch',
    method: 'post',
    data
  })
}

export function updateSchedule(data) {
  return request({
    url: '/schedule',
    method: 'put',
    data
  })
}

export function deleteSchedule(id) {
  return request({
    url: `/schedule/${id}`,
    method: 'delete'
  })
}

export function getScheduleStatistics() {
  return request({
    url: '/schedule/statistics',
    method: 'get'
  })
}
