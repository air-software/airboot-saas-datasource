import request from '@/utils/request'

// 查询调度日志列表
export function pageJobLog(query) {
  return request({
    url: '/monitor/jobLog/page',
    method: 'get',
    params: query
  })
}

// 删除调度日志
export function delJobLog(id) {
  return request({
    url: '/monitor/jobLog/' + id,
    method: 'delete'
  })
}

// 清空调度日志
export function cleanJobLog() {
  return request({
    url: '/monitor/jobLog/clean',
    method: 'delete'
  })
}

// 导出调度日志
export function exportJobLog(query) {
  return request({
    url: '/monitor/jobLog/export',
    method: 'get',
    params: query
  })
}
