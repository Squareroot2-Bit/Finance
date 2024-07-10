import $http from './http_index'
interface LoginFormStr {
  userName: string
  password: string
}

export const login = (data: LoginFormStr) => $http({ url: '/login', method: 'post', data })
export const logout = () =>
  $http({
    url: '/logout',
    method: 'get'
  })
export const register = (data: LoginFormStr) => $http({ url: '/register', method: 'post', data })
