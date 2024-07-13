<template>
    <div><el-form :inline="true">
  <el-form-item style="width: 400px;">
    <el-date-picker
      
      type="daterange"
      start-placeholder="起始日期"
      end-placeholder="结束日期"
      :clearable=false
    />
  </el-form-item>
  <el-form-item style="width: 200px;">
    <el-button type="primary" style="width: 100%;">搜索</el-button>
  </el-form-item>
</el-form></div>
</template>

<script setup lang="ts">
import { use } from 'echarts/core'
import { LineChart,PieChart  } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  ToolboxComponent,
  GridComponent
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import type { ComposeOption } from 'echarts/core'
import type { LineSeriesOption,PieSeriesOption } from 'echarts/charts'
import type {
  TitleComponentOption,
  TooltipComponentOption,
  LegendComponentOption,
  ToolboxComponentOption,
  GridComponentOption
} from 'echarts/components'
use([
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  ToolboxComponent,
  GridComponent,
  LineChart,
  PieChart,
  CanvasRenderer
])
type EChartsOption = ComposeOption<
  | TitleComponentOption
  | TooltipComponentOption
  | LegendComponentOption
  | ToolboxComponentOption
  | GridComponentOption
  | LineSeriesOption
  | PieSeriesOption
>

</script>