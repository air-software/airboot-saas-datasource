import request from '@/utils/request'

// 查询数据源分页
export function pageDataSource(query) {
  return request({
    url: '/system/datasource/page',
    method: 'get',
    params: query
  })
}

// 查询数据源列表
export function listDataSource(query) {
  return request({
    url: '/system/datasource/list',
    method: 'get',
    params: query
  })
}

// 查询数据源详细
export function getDataSource(id) {
  return request({
    url: '/system/datasource/' + id,
    method: 'get'
  })
}

// 新增数据源
export function addDataSource(data) {
  return request({
    url: '/system/datasource',
    method: 'post',
    data: data
  })
}

// 修改数据源
export function updateDataSource(data) {
  return request({
    url: '/system/datasource',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function delDataSource(ids) {
  return request({
    url: '/system/datasource/' + ids,
    method: 'delete'
  })
}

