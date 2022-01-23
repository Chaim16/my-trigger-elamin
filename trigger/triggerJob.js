import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/triggerJob',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/triggerJob/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/triggerJob',
    method: 'put',
    data
  })
}

export default { add, edit, del }
