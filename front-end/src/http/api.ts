
import $http from './http_index'
import type {Record} from '@/types/record'
interface LoginFormStr {
  username: string
  password: string
}

export const login = (data: LoginFormStr) => $http({ url: '/login', method: 'post', data })
export const logout = () =>
  $http({
    url: '/logout',
    method: 'get'
  })
export const register = (data: LoginFormStr) => $http({ url: '/register', method: 'post', data })
export const record  = (data: Record) => $http({ url: '/record', method: 'post', data })
export const record_view = (RecordURL:string) => $http({ url: RecordURL, method: 'get' })
export const record_del = (data: any) => $http({ url: '/record/delete', method: 'post', data })
export const records_upload = (data: any) => $http({ url: '/input', method: 'post', data })
