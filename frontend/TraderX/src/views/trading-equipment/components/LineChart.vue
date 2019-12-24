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
      handler(val) {
        this.setOptions(val)
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
      var datesArray = []
      var today = new Date()
      for(let i = 0; i < 100; i++) {
        today.setDate(today.getDate() - 1)
        var dateString = today.toDateString()
        datesArray.unshift(dateString)
      }
      this.chart.setOption({
        xAxis: {
          // Since the data we have shows the last 100 days
          // xAxis should be from day 1 to day 100
          data: datesArray,
          boundaryGap: false,
          axisTick: {
            show: true,
            interval: 7,
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
          data: ['Openning Values', 'Current Value']
        },
        series: [
        {
          name: 'Openning Values',
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
          data: open,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }, 
        {
          name: 'Current Value',
          smooth: true,
          type: 'line',
          itemStyle: {
            normal: {
              color: '#1F8255',
              lineStyle: {
                color: '#1F8255',
                width: 2
              },
              areaStyle: {
                color: '#E6FCF2'
              }
            }
          },
          data: current,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    }
  }
}
</script>
