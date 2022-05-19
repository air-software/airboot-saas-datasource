import request from '@/utils/request'

// 查询登录日志列表
export function pageLogininfor(query) {
  return request({
    url: '/monitor/logininfor/page',
    method: 'get',
    params: query
  })
}

// 删除登录日志
export function delLogininfor(id) {
  return request({
    url: '/monitor/logininfor/' + id,
    method: 'delete'
  })
}

// 清空登录日志
export function cleanLogininfor() {
  return request({
    url: '/monitor/logininfor/clean',
    method: 'delete'
  })
}

// 导出登录日志
export function exportLogininfor(query) {
  return request({
    url: '/monitor/logininfor/export',
    method: 'get',
    params: query
  })
}
