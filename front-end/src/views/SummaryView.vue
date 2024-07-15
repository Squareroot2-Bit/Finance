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
import { LineChart, type LineSeriesOption,PieChart, type PieSeriesOption } from 'echarts/charts';
import { UniversalTransition,LabelLayout } from 'echarts/features';
import { CanvasRenderer } from 'echarts/renderers';

import {generateDaysArray,generatePieData, type pieOut} from '@/types/summary'
echarts.use([
  TitleComponent,
  ToolboxComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  LineChart,
  CanvasRenderer,
  UniversalTransition,
  PieChart,
  LabelLayout
]);

type EChartsOption1 = echarts.ComposeOption<
  | TitleComponentOption
  | ToolboxComponentOption
  | TooltipComponentOption
  | GridComponentOption
  | LegendComponentOption
  | LineSeriesOption
>;
type EChartsOption2 = echarts.ComposeOption<
  | TitleComponentOption
  | TooltipComponentOption
  | LegendComponentOption
  | PieSeriesOption
>;
const income_line= ref<number[]>([]);
const expense_line = ref<number[]>([]);
const date_range = ref<string[]>([]);
const pieData = ref<pieOut[]>([]);

const updateChart =()=>{
  var option: EChartsOption1;
  var option2 : EChartsOption2;
  const lineChart = echarts.init(document.getElementById('chart1')!);
  const pieChart = echarts.init(document.getElementById('chart2')!);


option = {
 "title": {
    "text": "月度收支趋势图"
  },
  "tooltip": {
    "trigger": "axis"
  },
  "legend": {
    "data": ["收入", "支出"]
  },
  "grid": {
    "left": "3%",
    "right": "4%",
    "bottom": "3%",
    "containLabel": true
  },
  "toolbox": {
    "feature": {
      "saveAsImage": {}
    }
  },
  "xAxis": {
    "type": "category",
    "boundaryGap": false,
    "data": date_range.value
  },
  "yAxis": {
    "type": "value"
  },
  "series": [
    {
      "name": "收入",
      "type": "line",
      "data": income_line.value,
      "itemStyle": {
        "color": "green"
      },
      "lineStyle": {
        "color": "green"
      }
    },
    {
      "name": "支出",
      "type": "line",
      "data":expense_line.value,
      "itemStyle": {
        "color": "red"
      },
      "lineStyle": {
        "color": "red"
      }
    }
  ]
};

option2 = {
  title: {
    text: '月度各项支出占比',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '支出',
      type: 'pie',
      radius: '50%',
      data: pieData.value,
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
};
console.log(option);
lineChart.setOption(option);
pieChart.setOption(option2);
}

// var lineChart : echarts.ECharts;
// option && myChart.setOption(option);
// onMounted(() => {
//   updateChart();
// })


const disabledDate = (date: Date) => {
  return date.getTime() > Date.now()
}
const month = ref(new Date())
const totalIncome = ref(0);
const totalExpense = ref(0);
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

    income_line.value = data.income.map((value: number) => value / 100);
    expense_line.value = data.expense.map((value: number) => value / 100);
    totalIncome.value = (data.income_sum/100);
    totalExpense.value = data.expense_sum/100;
    netValue.value = totalIncome.value - totalExpense.value;
    date_range.value = generateDaysArray(income_line.value.length);
    pieData.value = generatePieData(data.expense_pie_chart);
    updateChart();
    // console.log(income_line.value);
    // lineChart.setOption(option);
    // console.log(option);
    // pieChart.setOption(option2);
  })
}
  
</script>

<style lang="scss" scoped>
.el-row {
  margin-bottom: 20px;
}

</style>
