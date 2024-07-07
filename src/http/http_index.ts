import axios from 'axios'
import { ElMessage } from 'element-plus'
enum HTTP_STATUS {
  '成功' = 200,
  '用户不存在',
  '密码错误',
  '目标用户名已存在',
  'ERROR' = 500
}
const getCurrentUrl = () => {
  console.log(window.location.origin)
  return window.location.origin
}
const $http = axios.create({
  timeout: 2000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

$http.interceptors.request.use(
  (config) => {
    config.headers = config.headers || {}
    if (localStorage.getItem('token')) {
      config.headers.token = localStorage.getItem('token') || ''
    }
    return config
  },
  (error) => {
    console.log(error)
    ElMessage.error(error.message)
    return Promise.reject(error)
  }
)

$http.interceptors.response.use(
  (res) => {
    const code: number = res.data.code
    if (code !== 200) {
      ElMessage.error(HTTP_STATUS[code])
      return Promise.reject(res.data)
    }
    return res.data
  },
  (err) => {
    console.log(err)
    if (err.message.includes('timeout') && err.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请稍后再试')
    }
    return Promise.reject(err)
  }
)
export default $http
