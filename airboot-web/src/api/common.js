import request from '@/utils/request'

// 获取IP真实地址
export function getIpLocation(ip) {
  return request({
    url: `/common/ip-location?ip=${ip}`,
    method: 'get'
  })
}
