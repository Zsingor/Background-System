<!-- s -->
<template>
  <div class="chart" ref="chartDom">
    <slot></slot>
  </div>
</template>

<script setup>
import { onMounted, computed, watch, onUnmounted, shallowRef } from 'vue'
import { persistentConfig } from '@/layout/layout.js'
import * as echarts from 'echarts'

const props = defineProps([
  'title', //标题
  'legend', //图表上方按钮显示
  'tooltip', //图表提示
  'xAxis', //图表的x轴信息
  'grid', //图表的位置
  'yAxis', //图表的y轴信息
  'series', //图表的数据
  'toolbox', //图表右上方的工具设置
  'dataZoom', //图表的滑块
])

const emit = defineEmits(['handleClick'])

const chartDom = shallowRef()
const charts = shallowRef()

const options = computed(() => {
  return {
    tooltip: {
      trigger: 'axis',
      appendToBody: true,
      axisPointer: {
        type: 'shadow',
        shadowStyle: {
          color: 'rgba(255,255,255,0.2)',
        },
        lineStyle: {
          color: 'rgba(255,255,255,0.2)',
          width: 3,
        },
      },
      backgroundColor: 'rgba(255,255,255,0.8)',
      padding: 20,
      textStyle: {
        color: '#020F1F',
        fontSize: 12,
      },
      ...props.tooltip,
    },
    title: {
      text: '',
      textStyle: {
        color: '#000000',
        fontSize: 14,
        fontWeight: 'normal',
      },
      ...props.title,
    },
    legend: {
      data: [],
      icon: 'roundRect',
      itemWidth: 15,
      itemHeight: 10,
      selectedMode: true,
      textStyle: {
        fontSize: 13,
      },
      top: 0,
      ...props.legend,
    },
    xAxis: {
      boundaryGap: true,
      axisLine: {
        lineStyle: {
          color: '#E1E1E1',
          width: 0,
        },
      },
      axisTick: {
        show: true,
      },
      data: [],
      axisLabel: {
        show: true,
        color: '#9DA3B3',
        fontSize: 12,
      },
      z: 2,
      ...props.xAxis,
    },
    grid: {
      ...props.grid,
    },
    yAxis: {
      type: 'value',
      splitLine: {
        show: true,
        lineStyle: {
          color: '#E1E1E1',
        },
      },
      axisTick: {
        show: false,
      },
      axisLine: {
        show: false,
        lineStyle: {
          color: 'rgba(12, 165, 165, 0.5)',
          width: 1,
        },
      },
      axisLabel: {
        show: true,
        color: '#9DA3B3',
        fontSize: 12,
      },
      ...props.yAxis,
    },
    toolbox: {
      show: false, 
      feature: {
        mark: { show: true },
        dataView: { show: true,readOnly: false },
        magicType: { show: true,type: ['line', 'bar'] },
        restore: { show: true, },
        saveAsImage: { show: true,name:"图表", type: 'png' },
      },
      ...props.toolbox,
    },
    series: props.series,
    dataZoom: props.dataZoom,
  }
})

const chartInit = () => {
  charts.value = echarts.init(chartDom.value)
  charts.value.setOption(options.value, true)
  charts.value.on('click', (params) => {
    emit('handleClick', params)
  })
}

const chartResize = () => {
  charts.value.resize()
}

//当列表的值发生改变时重新渲染
watch(
  () => options,
  async () => {
    charts.value && charts.value.setOption(options.value, true, chartResize)
  },
  { deep: true }
)

//当侧边栏伸缩时图表要进行重渲染
watch(
  () => persistentConfig.isCollapse,
  async () => {
    chartResize()
  }
)

onMounted(async () => {
  chartInit()
  window.addEventListener('resize', chartResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', chartResize)
  charts.value && charts.value.dispose()
})
</script>

<style scoped lang='scss'>
.chart {
  width: 100%;
  height: 100%;
}
</style>