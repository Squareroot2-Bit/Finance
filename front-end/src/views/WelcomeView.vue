<script setup lang="ts"></script>

<template>
  <div style="width: 100vw; height: 100vh; display: flex; overflow: hidden">
    <div style="flex: 1; background-color: #f5f5f5">
      <div class="welcome-title">
        <div style="font-size: 50px; font-weight: bold">我们的理财软件</div>
        <div style="margin-top: 10px"></div>
        <div style="margin-top: 5px"></div>
      </div>
    </div>

    <div class="right-card">
      <RouterView />
      <transition name="el-fade-in-linear" mode="out-in"> </transition>
    </div>
  </div>
</template>
<style scoped>
.right-card {
  width: 400px;
  z-index: 1;
  background-color: var(--el-bg-color);
}

.welcome-title {
  position: absolute;
  top: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}
</style>
