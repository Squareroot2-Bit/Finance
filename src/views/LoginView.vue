<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px; font-weight: bold">登录</div>
    </div>
    <div style="margin-top: 50px"></div>
    <el-form ref="loginFormRef" :model="loginForm" :rules="rules" hide-required-asterisk>
      <el-form-item prop="userName">
        <el-input v-model="loginForm.userName" maxlength="10" placeholder="用户名">
          <template #prefix>
            <el-icon>
              <User />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          autocomplete="off"
          maxlength="20"
          style="margin-top: 10px"
          placeholder="密码"
        >
          <template #prefix>
            <el-icon>
              <Lock />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          plain
          style="width: 100%; text-align: center"
          @click="submitForm(loginFormRef)"
        >
          登录
        </el-button>
      </el-form-item>
    </el-form>

    <div>
      <el-button @click="router.push('/register')" link>注册账号</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, toRefs } from 'vue'
import type { FormInstance } from 'element-plus'
import { InitLoginForm } from '@/types/login'
import { login } from '@/http/api'
import router from '@/router'
export default defineComponent({
  setup() {
    const data = reactive(new InitLoginForm())
    const rules = reactive({
      userName: [{ required: true, message: '请输入用户名！', trigger: ['blur', 'change'] }],
      password: [{ required: true, message: '请输入密码！', trigger: ['blur', 'change'] }]
    })
    const submitForm = async (formEl: FormInstance | undefined) => {
      if (!formEl) return
      await formEl.validate((valid, fields) => {
        if (valid) {
          // console.log(data.loginForm)
          login(data.loginForm).then((res) => {
            console.log(res)
            localStorage.setItem('token', res.data.token)
            router.push('/home')
          })
        } else {
          console.log('error submit!', fields)
        }
      })
    }

    return { ...toRefs(data), rules, submitForm, router }
  }
})
</script>
<style scoped></style>
