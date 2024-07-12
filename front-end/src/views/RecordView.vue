<template>
  <div class="main_view">
    <div class="record_view">
  <div class="top_box" >
    <el-form :inline="true" :model="view_options_data">
  <el-form-item style="width: 400px;">
    <el-date-picker
      v-model="view_options_data.dateRange"
      type="daterange"
      start-placeholder="起始日期"
      end-placeholder="结束日期"
      :clearable=false
    />
  </el-form-item>
  <el-form-item  style="width: 100px;">
    <el-select v-model="view_options_data.income" placeholder="收入/支出" >
      <el-option label="全部" :value="0"></el-option>
      <el-option label="收入" :value="1"></el-option>
      <el-option label="支出" :value="2"></el-option>
    </el-select>
  </el-form-item>
  <el-form-item style="width: 100px;">
    <el-select v-model="view_options_data.tag" placeholder="分类属性" >
      <el-option v-for="(tag, index) in RecordTag" :key="tag" :label="tag" :value="index"></el-option>
    </el-select>
  </el-form-item>
  <el-form-item style="width: 200px;">
    <el-button type="primary" @click="handleSearch" style="width: 100%;">搜索</el-button>
  </el-form-item>
  <el-form-item>
    <el-button type="primary" @click="convertRecordToFile" style="width: 100%;">导出</el-button>
  </el-form-item>
</el-form>

  </div>

  <div class="record_list">
    <el-table
    border 
    :data="tableData"
    :default-sort="{ prop: 'date', order: 'descending' }"
    style="width: 100%"
  >
    <el-table-column prop="date" label="日期" sortable width="180" />
    <el-table-column prop="income" label="性质" width="100" :formatter="formatIncome"/>
    <el-table-column prop="tag" label="分类" width="100"  :formatter="formatTag"/>
    <el-table-column prop="money" label="金额" sortable width="150"  :formatter="formatMoney"/>
    <el-table-column prop="remark" label="备注" width="600" />
    <el-table-column fixed="right" label="操作" min-width="120">
      <template #default="scope">
        <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
      </template>
    
    </el-table-column>
  </el-table>
  </div>

  </div>
  <div class="add_record">
      <UploadRecordView @upload-success="handleUploadSuccess"/>
  </div>
  </div>

</template>


<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue';
import { RecordTag, formatDate, convertToRecordArray} from '@/types/record';
import UploadRecordView from './UploadRecordView.vue';
import { record_view, record_del, record } from '@/http/api';
import type { record_response,output_Record } from '@/types/record';
import type { TableColumnCtx } from 'element-plus';
import Papa from 'papaparse';
interface view_options {
  income: number;
  tag: number;
  dateRange: [Date, Date];
}

const view_options_data = reactive(ref<view_options>({
  income: 0,
  tag: 0,
  dateRange: [new Date(new Date().getFullYear(), new Date().getMonth(), 1, 0, 0, 0), new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate(), 23, 59, 59)],
}));

let startDateString: string = formatDate(view_options_data.value.dateRange[0]);
let endDateString: string = formatDate(view_options_data.value.dateRange[1]);
let cache_optionsURL: string;

function formatViewOptions(options: view_options): string {
  const { income, tag, dateRange } = options;
  const [startDate, endDate] = dateRange;
  startDateString = formatDate(startDate);
  endDateString = formatDate(endDate);

  return `/record/view/${income}/${tag}/${startDateString}/${endDateString}`;
}

const tableData = ref<record_response[]>([]);

const formatIncome = (row: record_response, column: TableColumnCtx<record_response>) => {
  if (row.income) {
    return "收入";
  } else return "支出";
};

const formatTag = (row: record_response, column: TableColumnCtx<record_response>) => {
  return RecordTag[row.tag];
};

const formatMoney = (row: record_response, column: TableColumnCtx<record_response>) => {
  return row.money / 100;
};

const fetchData = async () => {
  const res = await record_view(cache_optionsURL);
  tableData.value = res.data;
  localStorage.setItem('tableData', JSON.stringify(res.data));
};


const handleDelete = async(row: record_response) => {
  const data = { "record_id": row.record_id };
  console.log(data);
  await (record_del(data));
  fetchData();
};
watch(tableData, (newVal) => {
  localStorage.setItem('tableData', JSON.stringify(newVal));
});
const handleUploadSuccess = (date: string) => {
  if (date < startDateString || date > endDateString) {
    console.log(date);
    return;
  }
  fetchData();
};

const handleSearch = () => {
  cache_optionsURL = formatViewOptions(view_options_data.value);
  record_view(cache_optionsURL).then(res => {
    tableData.value = res.data;
    localStorage.setItem('tableData', JSON.stringify(res.data));
  });
};

onMounted(() => {
  const savedData = localStorage.getItem('tableData');
  if (savedData) {
    tableData.value = JSON.parse(savedData);
  }
});

const convertRecordToFile= ()=> {
  const data = convertToRecordArray(tableData.value);
  const csv = Papa.unparse(data);
  const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
  const link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = `record_${startDateString}_${endDateString}.csv`;
  document.body.appendChild(link);
  link.click();

document.body.removeChild(link);
}
</script>





<style scoped lang="scss">
.main_view {
  display: flex;
  justify-content: space-between;
.record_view {
  display: flex;
  flex-direction: column;
  width: 75%;
  .top_box { 
  width: 100%;
  height: 50px;
  padding: 5px 0;


  }
  .record_list{
    width: 100%;
  }
}
  .add_record {
    width: 25%;
    height: 100vh;
    text-align: center;
    border-left: 1px solid #ccc;
  }
}
</style >: any: { money: number; }: any