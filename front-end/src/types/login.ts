import type { FormInstance } from 'element-plus'
import { ref } from 'vue'
export interface LoginFormStr {
  username: string
  password: string
}
export class InitLoginForm {
  loginForm: LoginFormStr = {
    username: '',
    password: ''
  }
  loginFormRef = ref<FormInstance>()
}
