<template>
  <div class="record_view">
  <div class="top_box" >
<el-row  justify="start" :gutter="30">
  <el-col :span="8">
    <span class="time_select">选择时间范围</span>
    <el-date-picker style="width: 100%;"
      v-model="time_range_data"
      type="daterange"
      start-placeholder="起始日期"
      end-placeholder="结束日期"
      :default-time="defaultTime"
    />
  </el-col>
  <el-col :span="3">
    <span class="type_select" >选择性质</span>
    <el-select  placeholder="收入/支出" style="width: 100%;">
      <el-option label="全部" value="all"></el-option>
      <el-option label="收入" value="income"></el-option>
      <el-option label="支出" value="expense"></el-option>
    </el-select>
  </el-col>
  <el-col :span="4">
    <span class="category_select">选择分类</span>
    <el-select  placeholder="分类属性" style="width: 100%;">
      <el-option label="Category 1" value="category1"></el-option>
      <el-option label="Category 2" value="category2"></el-option>
    </el-select>
  </el-col>
</el-row>
  </div>

  <div class="add_record">
    
      

      <UploadRecordView />
  </div>
  </div>
</template>


<script setup lang="ts">
import {ref} from 'vue'
import UploadRecordView from './UploadRecordView.vue';
const defaultTime = ref<[Date, Date]>([
new Date(new Date().getFullYear(), new Date().getMonth(), 1, 0, 0, 0),
new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate(), 23, 59, 59),
])
const time_range_data = ref(defaultTime.value)
</script>
<style scoped lang="scss">
.record_view {
  display: flex;
  width: 100%;
}
.top_box { 
  width: 75%;
  padding: 5px 0;
  text-align: left;
  border-right: solid 1px var(--el-border-color);
  flex: 1;
  .time_select, .type_select, .category_select {
  display: block;
  color: var(--el-text-color-secondary);
  font-size: 14px;
  margin-bottom: 5px;
  }
  }
  .add_record {
    width: 25%;
    height: 100%;
    padding: 5px 0;
    text-align: center;
    
  }
</style >