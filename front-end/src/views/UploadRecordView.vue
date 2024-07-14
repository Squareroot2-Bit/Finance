<template>
    <el-form
    ref="recordFormRef"
    :model="recordForm"
    label-position="top"
    class="demo-ruleForm"
    :rules="rules"
    status-icon
    style="height: 50%;"
  >
  <h2>新增记录</h2>
    <el-form-item label="日期" prop="date">
      <el-date-picker v-model="recordForm.date" :disabled-date="disabledDate" type="date" placeholder="选择日期" style="width: 100%; " @change="formatUploadDate" />

    </el-form-item>
   <el-form-item label="金额" prop="money" >
      <el-input-number v-model="recordForm.money" style="width: 100%; "  placeholder="请输入数字" :precision="2" :step="0.1" :max="100000000" :min="-100000000" />
    </el-form-item>
    <el-form-item label="分类" prop="tag" style="margin-top: 10px;">
      <el-select  v-model="recordForm.tag" placeholder="请选择分类" style="width: 100%; ">
        <el-option v-for="(tag,index) in RecordTag" :key="tag" :label="tag" :value="index" :disabled="index==0" ></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="备注" prop="remark" style="margin-top: 10px;">
      <el-input  v-model="recordForm.remark" type="textarea" placeholder="请输入备注" style="width: 100%;" maxlength="100"
    show-word-limit></el-input>
    </el-form-item>
    <el-form-item>
      <el-button name="submit" type="primary" style="width: 100%; margin-top: 20px;" @click="submitForm(recordFormRef)">提交</el-button>
    </el-form-item>
  </el-form>
  <el-upload
  name="file"
    ref="uploadRef"
    class="upload-demo"
    drag
    action="#"
    :show-file-list="false"
    :http-request="handleRequest"
    :file-list="fileList"
    :accept="'.csv'"
    :multiple="false"
  >
    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
    <div class="el-upload__text">
      Drop file here or <em>click to upload</em>
    </div>
    <template #tip>
      <div class="el-upload__tip">
        只接受符合格式的.csv
      </div>
    </template>
  </el-upload>
</template>
<script setup lang="ts">
import { reactive, ref} from 'vue'
import { ElMessage, type FormInstance, type UploadRequestOptions } from 'element-plus'
import type { Record } from '@/types/record';
import { RecordTag, formatDate, convertToUploadeRecordArray} from '@/types/record'
import {record,records_upload} from '@/http/api'
import { UploadFilled } from '@element-plus/icons-vue'
import type { UploadUserFile } from 'element-plus'
import Papa from 'papaparse';



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
const emit = defineEmits(['upload-success'])
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {

      recordForm.money*=100
      const date = new Date(recordForm.date)
      // console.log(recordForm)
      record(recordForm).then(res => {
        console.log(recordForm.date)
        emit('upload-success', date)
        ElMessage.success('新增记录成功')
      }).catch(err => {
        console.log(err)
      })
      
      recordFormRef.value?.resetFields()
      

    } else {
      console.log('error submit!', fields)
    }
  })
}
const validateHeaders = (headers: string[]) => {
  const requiredHeaders = ['money', 'date', 'tag', 'remark'];
  const missingHeaders = requiredHeaders.filter(header => !headers.includes(header));
  if (missingHeaders.length > 0) {
    throw new Error(`Missing required headers: ${missingHeaders.join(', ')}`);
  }
};

const fileList = ref<UploadUserFile[]>([])
  // const handleChange: UploadProps['onChange'] = (uploadFile) => {
  //   console.log("-----")
  //   const reader= new FileReader()
  //   reader.onload = (e) => {
  //   const csvData = (e.target as FileReader).result as string;
  //   Papa.parse(csvData, {
  //     header: true,
  //     dynamicTyping: true,
  //     complete: (result) => {
  //       const headers = result.meta.fields;
  //       if (!headers) {
  //         throw new Error("CSV file missing headers");
  //       }
  //       try {
  //         validateHeaders(headers);
  //         const jsonData = convertToUploadeRecordArray(result.data);

  //         console.log("Valid JSON data:", jsonData);
  //       } catch (error) {
  //         console.error("Validation error:", error);
  //       }}})
  // };
  // if (uploadFile.raw) {
  //   reader.readAsText(uploadFile.raw);
  // }
  //   }
  const handleRequest = async (options: UploadRequestOptions) => {
  const { file } = options;
  
  const reader = new FileReader();
  reader.onload = async (e) => {
    const csvData = (e.target as FileReader).result as string;
      Papa.parse(csvData, {
      header: true,
      dynamicTyping: true,
      complete: (result) => {
        const headers = result.meta.fields;  
        try {
          if (!headers) {throw new Error("CSV file missing headers");}
           validateHeaders(headers);
          const jsonData = convertToUploadeRecordArray(result.data);
          records_upload({recordBodyList:jsonData}).then(res => {
            console.log(res)
            ElMessage.success('上传成功')
          }).catch(err => {
            console.log(err)
            // ElMessage.error('上传失败')
          })
          console.log("Valid JSON data:", jsonData);
        } catch (error) {
          ElMessage.error("文件格式错误!");
          console.error("Validation error:", error);
        }}})
  };
  reader.readAsText(file);
};
</script>
<style lang="scss" scoped>
.el-form-item {
      margin-bottom: 10px;
      width: 100%;
      
    }</style>