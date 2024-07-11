<template>
  <div class="main_con">
    cal
    <!-- <el-calendar class="calendar">
      <template v-slot:dateCell="{ date, data }">
        {{ data.day.split('-').slice(1).join('-') }}
        <div style="width: 100%" v-for="item in scheduleData" :key="item">
          <el-tag
            type="danger"
            v-if="item.workingDay.indexOf(data.day.split('-').slice(2).join('-')) != -1"
          >
            {{ item.content }}
          </el-tag>
        </div>
      </template>
    </el-calendar> -->
  </div>
</template>

<script setup lang="ts"></script>
<style scoped>
.calendar {
  /* box-sizing: border-box; */
  /* padding: 5px; */

  width: 50%;
}
</style>
