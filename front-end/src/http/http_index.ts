import axios from 'axios'
import { ElMessage } from 'element-plus'
enum JSON_STATUS {
  '成功' = 0,
  '用户不存在' = -1,
  '密码错误' = -2,
  '目标用户名已存在' = -3,
  '添加失败' = -4,
  '查询失败'=-5,
  '删除失败' = -6,
}

const getCurrentUrl = () => {
  console.log(window.location.origin)
  return window.location.origin
}
const $http = axios.create({
  timeout: 2000,
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
    'user_id': localStorage.getItem('user_id') || ''
  }
})

$http.interceptors.request.use(
  (config) => {
    config.headers = config.headers || {}
    if (localStorage.getItem('user_id')) {
      config.headers.user_id = localStorage.getItem('user_id') || ''
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

    if (code < 0) {
      ElMessage.error(JSON_STATUS[code])
      return Promise.reject(res.data.message)
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
