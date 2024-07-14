<template>
    <div><el-form :inline="true">
  <el-form-item >
    <el-date-picker
      type="month"
      placeholder="选择月份"
      :clearable=false
      v-model="month"
      :disabled-date="disabledDate"
    />
  </el-form-item>
  <el-form-item style="width: 200px;">
    <el-button type="primary" style="width: 100%;" @click="handleSearch">查看报表</el-button>
  </el-form-item>
</el-form></div>
<div>
    <div id="sum" style="width: 100%;text-align: center;" borderBottom="1px solid #ccc"><el-row>
      <el-col :span="8">
        总收入
        <div :style="{ color: 'green',  fontSize: '24px' }">{{ totalIncome }}</div>
      </el-col>
      <el-col :span="8">
        总支出
        <div :style="{ color: 'red', fontSize: '24px'}">{{ totalExpense }}</div>
      </el-col>
      <el-col :span="8">
        净值
        <div :style="{ color: netValue >= 0 ? 'green' : 'red',fontSize: '24px' }">{{ netValue }}</div>
      </el-col>
    </el-row></div>
    <div id="chart1" style="width: 100%;height: 400px;"></div>
    <div id="chart2" style="width: 100%;height: 400px;"></div>
    
</div>
</template>
<!-- <template>
  <div id="chart" style="width: 100%;height: 400px;"></div>
</template> -->

<script setup lang="ts">
// import * as echarts from 'echarts/core';
// import { use } from 'echarts/core'
// import { LineChart,PieChart  } from 'echarts/charts'
// import {
//   TitleComponent,
//   TooltipComponent,
//   LegendComponent,
//   ToolboxComponent,
//   GridComponent
// } from 'echarts/components'
// import { CanvasRenderer } from 'echarts/renderers'
// import type { ComposeOption } from 'echarts/core'
// import type { LineSeriesOption,PieSeriesOption } from 'echarts/charts'
// import type {
//   TitleComponentOption,
//   TooltipComponentOption,
//   LegendComponentOption,
//   ToolboxComponentOption,
//   GridComponentOption
// } from 'echarts/components'
// use([
//   TitleComponent,
//   TooltipComponent,
//   LegendComponent,
//   ToolboxComponent,
//   GridComponent,
//   LineChart,
//   PieChart,
//   CanvasRenderer
// ])
// type EChartsOption = ComposeOption<
//   | TitleComponentOption
//   | TooltipComponentOption
//   | LegendComponentOption
//   | ToolboxComponentOption
//   | GridComponentOption
//   | LineSeriesOption
// >
import { onMounted, ref } from 'vue'
import {formatDate}from '@/types/record'
import {record_view}from'@/http/api'
import * as echarts from 'echarts/core';
import {
  TitleComponent,
  type TitleComponentOption,
  ToolboxComponent,
  type ToolboxComponentOption,
  TooltipComponent,
  type TooltipComponentOption,
  GridComponent,
  type GridComponentOption,
  LegendComponent,
  type LegendComponentOption
} from 'echarts/components';
import { LineChart, type LineSeriesOption } from 'echarts/charts';
import { UniversalTransition } from 'echarts/features';
import { CanvasRenderer } from 'echarts/renderers';

echarts.use([
  TitleComponent,
  ToolboxComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  LineChart,
  CanvasRenderer,
  UniversalTransition
]);

type EChartsOption1 = echarts.ComposeOption<
  | TitleComponentOption
  | ToolboxComponentOption
  | TooltipComponentOption
  | GridComponentOption
  | LegendComponentOption
  | LineSeriesOption
>;

// var chartDom = document.getElementById('chart')!;
// var myChart = echarts.init(chartDom);
var option: EChartsOption1;

option = {
  title: {
    text: 'Stacked Line'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['Email', 'Union Ads', 'Video Ads', 'Direct', 'Search Engine']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  toolbox: {
    feature: {
      saveAsImage: {}
    }
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: 'Email',
      type: 'line',
      stack: 'Total',
      data: [120, 132, 101, 134, 90, 230, 210]
    },
    {
      name: 'Union Ads',
      type: 'line',
      stack: 'Total',
      data: [220, 182, 191, 234, 290, 330, 310]
    },
    {
      name: 'Video Ads',
      type: 'line',
      stack: 'Total',
      data: [150, 232, 201, 154, 190, 330, 410]
    },
    {
      name: 'Direct',
      type: 'line',
      stack: 'Total',
      data: [320, 332, 301, 334, 390, 330, 320]
    },
    {
      name: 'Search Engine',
      type: 'line',
      stack: 'Total',
      data: [820, 932, 901, 934, 1290, 1330, 1320]
    }
  ]
};

// option && myChart.setOption(option);
onMounted(() => {
  const chartDom = document.getElementById('chart1')!;
  const myChart = echarts.init(chartDom);
  myChart.setOption(option);
})



const disabledDate = (date: Date) => {
  return date.getTime() > Date.now()
}
const month = ref(new Date())
const totalIncome = ref(10000);
const totalExpense = ref(5000);
const netValue = ref(totalIncome.value - totalExpense.value);
const handleSearch = () => {
  const currentDate = month.value;
  // console.log(currentDate.toDateString());
  const firstDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
  const lastDay = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);
  const startDate = formatDate(firstDay);
  const endDate = formatDate(lastDay);
  const url = `/summary/${startDate}/${endDate}`;
  console.log(url);
  record_view(url).then((res) => {
    const data = res.data;
    console.log(data);
  })
}
  
</script>

<style lang="scss" scoped>
.el-row {
  margin-bottom: 20px;
}

</style>
