<script setup lang="ts">
import { ArrowDown } from '@element-plus/icons-vue'
import { logout } from '@/http/api'
import type { FormInstance } from 'element-plus'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { useRouter } from 'vue-router'
import { RouterView } from 'vue-router'
const Name = localStorage.getItem('token')
const homeRouter = useRouter()
const list = homeRouter.getRoutes().filter((v) => v.meta.homeList)
console.log(list)
function userLogout() {
  // logout()
  //   .then((res) => {
  //     console.log(res)
  //     localStorage.removeItem('token')
  //     router.push('/')
  //   })
  //   .catch((error) => {
  //     ElMessage.error('退出登录失败')
  //     console.error('Logout failed:', error)
  //   })
  localStorage.removeItem('token')
  localStorage.removeItem('user_id')
  router.push('/')
  ElMessage.success('退出登录成功')
}
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header height="80px">
        <el-row :gutter="20">
          <el-col :span="4"
            ><div>
              <el-dropdown>
                <span class="el-dropdown-link">
                  你好，{{ Name }}
                  <el-icon class="el-icon--right">
                    <arrow-down />
                  </el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item divided @click="userLogout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div></el-col
          >
          <el-col :span="16"></el-col>
          <el-col :span="4"><div /></el-col>
        </el-row>
      </el-header>
      <el-container>
        <el-aside width="150px">
          <el-menu default-active="2" class="menu" router>
            <el-menu-item v-for="item in list" :key="item.path" :index="item.path">
              <span>{{ item.meta.title }}</span>
            </el-menu-item>
            <!-- <el-menu-item index="3" disabled>
              <el-icon><document /></el-icon>
              <span>Navigator Three</span>
            </el-menu-item> -->
          </el-menu>
        </el-aside>
        <el-main><RouterView /></el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style lang="scss" scoped>
h2 {
  color: #fff;
}
.el-header {
  .el-dropdown-link {
    color: #fff;
    font-size: 16px;
    font-weight: bold;
    outline: none;
    cursor: pointer;
    padding: 0 20px;
    height: 80px;
    line-height: 80px;
    display: flex;
    align-items: center;
  }
  background-color: #409eff;
}
.el-aside {
  .el-menu {
    height: calc(100vh - 80px);
  }
}
</style>
