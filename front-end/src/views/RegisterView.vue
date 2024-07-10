<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'
import type { FormInstance } from 'element-plus'
import type { LoginFormStr } from '@/types/login'
import router from '@/router'
import { register } from '@/http/api'
const registerFormRef = ref<FormInstance>()

const validateName = (rule: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名不能包含特殊字符，只能是中文/英文'))
  } else {
    callback()
  }
}

const validatePass2 = (rule: any, value: string, callback: any) => {
  if (value !== registerForm.password) {
    callback(new Error('密码不一致'))
  } else {
    callback()
  }
}

const registerForm = reactive({
  userName: '',
  password: '',
  password_repeat: ''
})

const rules = reactive({
  userName: [
    { min: 3, max: 12, message: '用户名的长度必须在3-12个字符之间', trigger: ['blur', 'change'] },
    { validator: validateName, trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: ['blur', 'change'] },
    { min: 6, max: 16, message: '密码长度在 6 到 16 个字符', trigger: 'blur' }
  ],
  password_repeat: [
    { required: true, message: '请再次输入密码', trigger: ['blur', 'change'] },
    { validator: validatePass2, trigger: 'blur' }
  ]
})
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      const loginFormData: LoginFormStr = {
        userName: registerForm.userName,
        password: registerForm.password
      }
      console.log(loginFormData)
      register(loginFormData).then((res) => {
        console.log(res)
        ElMessage.success('注册成功，欢迎加入我们')
        router.push('/')
      })
      // ElMessage.success('注册成功，欢迎加入我们')
      // router.push('/')
    } else {
      console.log('error submit!', fields)
    }
  })
}
</script>

<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin-top: 100px">
      <div style="font-size: 25px; font-weight: bold">注册新用户</div>
    </div>
    <div style="margin-top: 50px">
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef">
        <el-form-item prop="userName">
          <el-input v-model="registerForm.userName" :maxlength="8" type="text" placeholder="用户名">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            :maxlength="20"
            type="password"
            placeholder="密码"
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input
            v-model="registerForm.password_repeat"
            :maxlength="20"
            type="password"
            placeholder="重复密码"
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 40px">
      <el-button
        name="register"
        style="width: 270px"
        type="warning"
        @click="submitForm(registerFormRef)"
        plain
        >立即注册</el-button
      >
    </div>
    <div style="margin-top: 20px">
      <span style="font-size: 14px; line-height: 15px; color: grey">已有账号? </span>
      <el-link type="primary" style="translate: 0 -2px" @click="router.push('/')">立即登录</el-link>
    </div>
  </div>
</template>
