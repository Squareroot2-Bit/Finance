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
    <el-form
    ref="recordFormRef"
    :model="recordForm"
    label-position="top"
    class="demo-ruleForm"
    :rules="rules"
    status-icon
  >
  <h2>新增记录</h2>
    <el-form-item label="日期" prop="date">
      <el-date-picker v-model="recordForm.date" :disabled-date="disabledDate" type="date" placeholder="选择日期" style="width: 100%;" @change="formatUploadDate" />

    </el-form-item>
   <el-form-item label="金额" prop="money" >
      <el-input-number v-model="recordForm.money" style="width: 100%;"  placeholder="请输入数字" :precision="2" :step="0.1" :max="100000000" :min="-100000000" />
    </el-form-item>
    <el-form-item label="分类" prop="tag" >
      <el-select  v-model="recordForm.tag" placeholder="请选择分类" style="width: 100%;">
        <el-option v-for="(tag,index) in RecordTag" :key="tag" :label="tag" :value="index" :disabled="index==0" ></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="备注" prop="remark">
      <el-input  v-model="recordForm.remark" type="textarea" placeholder="请输入备注" style="width: 100%;" maxlength="100"
    show-word-limit></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" style="width: 100%; margin-top: 20px;" @click="submitForm(recordFormRef)">提交</el-button>
    </el-form-item>

  </el-form>
      

      
  </div>
  </div>
</template>


<script setup lang="ts">
import { reactive, ref} from 'vue'
import { ElMessage, type FormInstance } from 'element-plus'
import type { Record } from '@/types/record';
import {record} from '@/http/api'
const RecordTag = [
    "无",
    "穿着",
    "饮食",
    "交通",
    "住宿",
    "娱乐",
    "医疗",
    "缴费",
    "其他"
]
const rules = reactive({
  date: [
    { required: true, message: '请选择日期', trigger: 'blur' },
  ],
  money: [
    { required: true, message: '请输入金额', trigger: 'blur' },
    {validator: (rule: any, value: number, callback: (arg0: Error | undefined) => void) => {
      if (value === 0) {
        callback(new Error('金额不能为0'));
      } else {
        callback(undefined);
      }
    }, trigger: 'blur'},
    // { type: 'number', message: '请输入数字', trigger: 'blur' },
    // { min: -100000000, max: 100000000, message: '金额范围为-100000000~100000000', trigger: 'blur' },
  ],
  tag: [
    { required: true, message: '请选择分类', trigger: 'blur' },
    { validator: (rule: any, value: number, callback: (arg0: Error | undefined) => void) => {
      if (value === 0) {
        callback(new Error('分类不能为无'));
      } else {
        callback(undefined);
      }
    },
    trigger: 'blur'
  }
  ],
})
const formatDate = (date: Date) => {
    return date.toISOString().slice(0, 10).replace(/-/g, '');
}
const formatUploadDate = (date: Date) => {
  if(date){recordForm.date = formatDate(date)}
}
const disabledDate = (date: Date) => {
  return date.getTime()> Date.now()
}
const recordFormRef = ref<FormInstance>()
const recordForm = reactive<Record>({
  date: '',
  money: 0,
  tag:0,
  remark: '',
})
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      recordForm.money*=100
      // record(recordForm).then(res => {
      //   ElMessage.success('新增记录成功')
      //   console.log(res)
      //   // recordForm.clear()
      //   recordFormRef.value?.resetFields()
      // }).catch(err => {
      //   console.log(err)
      // })
      console.log(recordForm)

    } else {
      console.log('error submit!', fields)
    }
  })
}
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
    .el-form-item {
      margin-bottom: 10px;
      width: 100%;
      
    }
  }
</style >: any: number: (arg0: Error | undefined) => void
