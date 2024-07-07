<script setup lang="ts">
import { logout } from '@/http/api'
import router from '@/router'
function userLogout() {
  logout()
    .then((res) => {
      console.log(res)
      router.push('/')
    })
    .catch((error) => {
      console.error('Logout failed:', error)
    })
}
</script>

<template>
  <div>
    <el-button>退出登录</el-button>
  </div>
</template>
