// The line chart to be used in the detailed pages

<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      immediate: true,
      handler(val) {
        this.setOptions(val)
        this.chart.update()
      }
    }    
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
    },
    setOptions({open, close, high, low, current} = {}) {
      this.chart.setOption({
        xAxis: {
          // Since the data we have shows the last 100 days
          // xAxis should be from day 80 to day 100
          data: Array.from(Array(20).keys()),
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: false
          },
          type: 'value',
          min: 'dataMin',
          max: 'dataMax'
        },
        // With this page only actual data is used
        legend: {
          data: ['Opening Value', 'Closing Value', 'Highest Value', 'Lowest Value']
        },
        series: [
        {
          name: 'Opening Value', itemStyle: {
            normal: {
              color: '#900C3F',
              lineStyle: {
                color: '#900C3F',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          data: open.slice(open.length-21, open.length-1),
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: 'Closing Value', itemStyle: {
            normal: {
              color: '#581845',
              lineStyle: {
                color: '#581845',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          data: close.slice(close.length-21, close.length-1),
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: 'Highest Value', itemStyle: {
            normal: {
              color: '#1E4358',
              lineStyle: {
                color: '#1E4358',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          data: high.slice(high.length-21, high.length-1),
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: 'Lowest Value',
          smooth: true,
          type: 'line',
          itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2
              },
              areaStyle: {
                color: '#f3f8ff'
              }
            }
          },
          data: low.slice(low.length-21, low.length-1),
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    }
  }
}
</script>
