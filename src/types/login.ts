import type { FormInstance } from 'element-plus'
import { ref } from 'vue'
export interface LoginFormStr {
  userName: string
  password: string
}
export class InitLoginForm {
  loginForm: LoginFormStr = {
    userName: '',
    password: ''
  }
  loginFormRef = ref<FormInstance>()
}
