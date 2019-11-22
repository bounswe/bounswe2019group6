<template>
 
  <div>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <el-card class='raddar-chart-container'>
        <raddar-chart :chart-data="chartData"/>
      </el-card>
    </el-row>

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <el-card class='raddar-chart-container'>
        <p class="te-info-text">Last 20 days' opening values of every currency is as follows:</p>
        <line-chart-comparison :chart-data="comparisonData"/>
      </el-card>
    </el-row>

    <el-row :gutter="20" style="padding:16px 16px 0;margin-bottom:32px;">
      <el-card>
        <el-tabs v-model="activeTab">
          <el-tab-pane class='te-tab-pane' v-for="ed in equipmentData" :key="ed.key" :label="ed.label" :name="ed.key" :type="ed.key">
              <div v-if="activeTab==ed.key">
                <p class="te-info-text">Last 100 days' American Dollar / {{ ed.label }} with openning values is as follows:</p>
                <div class='chart-wrapper'>
                  <line-chart :type="ed.key" :chart-data="ed.data"/>
                </div>

                <p class="te-info-text">Last 20 days' American Dollar / {{ ed.label }} with openning, closing, highest and lowest values is as follows:</p>
                <div class='chart-wrapper'>
                  <line-chart-detailed :type="ed.key" :chart-data="ed.data"/>
                </div>

                <el-button class='change-base-button' @click="changeBaseofEquipment(ed.label)"><svg-icon style="margin-right:10px" icon-class="chart" />Change the Base</el-button>
                <el-button class='buy-button' @click="buyEquipment"><svg-icon style="margin-right:10px" icon-class="shopping" />Buy Equipment</el-button>
              </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-row>
  </div>
   
</template>

<script>
import LineChart from './components/LineChart'
import LineChartDetailed from './components/LineChartDetailed'
import LineChartComparison from './components/LineChartComparison'
import RaddarChart from './components/RaddarChart'

// The usual sorting in javascript sorts alphabetically which causes mistake in our code
const numberSort = function (a,b) {
    return a - b;
};

export default {
  name: 'DashboardAdmin',
  components: {
    LineChartComparison,
    LineChartDetailed,
    LineChart,
    RaddarChart,
  },
  data() {
    return {
      chartData: {},
      // equipmentData is expected to be: 
      // [
      //   {
      //     label: Euro,
      //     key: EUR,
      //     data: {
      //       open: [...],
      //       close: [...],
      //       high: [...],
      //       low: [...],
      //       current: [...] 
      //     }
      //   }, 
      //   {
      //     label: Japanese Yen,
      //     key: JPY
      //   }, ...
      // ]
      equipmentData: [],
      comparisonData: {equipmentData: []},
      activeTab: 'JPY'
    }
  },
  async created() {
    var equipmentList = await this.getEquipmentList()
    // Fill the equipmentData with equipment list
    equipmentList.forEach(function(equipmentKey) {
      this.equipmentData.push({key: equipmentKey})
    }, this)
    var equipmentValues = await this.getEquipmentValues(equipmentList) 
    this.createRadarChartData(equipmentValues)
    this.equipmentData = equipmentValues
    
    this.comparisonData.equipmentData = this.equipmentData
  },
  methods: {
    // Promise for getting equipments list 
    async getEquipmentList() {
      try {
        await this.$store.dispatch('equipment/getAllCurrencies')
        var res = this.$store.getters.currencyResult
        return res.equipments
      } catch (error) {
        console.log(error)
        return error
      }  
    },

    // equipment = ['JPY', 'TRY', ...]
    async getEquipmentValues(equipmentList) {
      // equipmentValues = [{label="Turkish Lira", key="TRY", data={open=[5.6, 4.34, 7.54,...], close=[..], high=[..], low=[..]}}, {label="Japanese Yen", key="JPY", data={actualData=[12.2312, 11.234545, 10.23234, ...]}} ...]
      var equipmentValues = []
      
      equipmentList.forEach(async function(e) {
        try {
          await this.$store.dispatch('equipment/getEquipment', e.toLowerCase())
          var res = this.$store.getters.equipmentQueryResult
          equipmentValues.push({})
          equipmentValues[equipmentValues.length-1].key = res.equipment.code
          equipmentValues[equipmentValues.length-1].label = res.equipment.name
          equipmentValues[equipmentValues.length-1].currentValue = res.equipment.currentValue
          equipmentValues[equipmentValues.length-1].data = {
            open: [],
            close: [],
            high: [],
            low: [],
            current: [],
          }
          res.historicalValues.forEach(function(val) {
            equipmentValues[equipmentValues.length-1].data.open.push(val.open)
            equipmentValues[equipmentValues.length-1].data.close.push(val.close)
            equipmentValues[equipmentValues.length-1].data.high.push(val.high)
            equipmentValues[equipmentValues.length-1].data.low.push(val.low)
            equipmentValues[equipmentValues.length-1].data.current.push(res.equipment.currentValue)
          })
        } catch (error) {
          console.log(error)
        }
      }, this)
      return equipmentValues
    },

    // Put values from 1 to 3 to each of equipment in categories: Stability, Growth Rate, Values
    createRadarChartData(equipmentValues) {
      // wait for data to be pulled properly
      if (equipmentValues.length == 0) {
        setTimeout(() => {
          this.createRadarChartData(equipmentValues)
        }, 500)
      } else {
        this.activeTab = equipmentValues[0].key
        var stabilities = [] // calculate stability of each trading equipment
        var growth = [] // calculate growth rate for each equipment
        var values = []
        var legendData = [] // For getting legendData
        var graphData = []

        const equipmentSize = equipmentValues.length
        for (let i = 0; i < equipmentSize; i++) {
          let valueAverage = 0
          equipmentValues[i].data.open.forEach(function(val) {
            valueAverage += val / equipmentValues[i].data.open.length
          })
          let stability = Math.abs(valueAverage - equipmentValues[i].data.open[0]) / equipmentValues[i].data.open.length
          let growthRate = 0
          for (let j = 0; j < equipmentValues[i].data.open.length-1; j++) {
            stability += Math.abs(valueAverage - equipmentValues[i].data.open[j+1]) / equipmentValues[i].data.open.length
            growthRate += (1/equipmentValues[i].data.open[j] - 1/equipmentValues[i].data.open[j+1]) / equipmentValues[i].data.open.length
          }
          stabilities.push(stability)
          growth.push(growthRate)
          values.push(1/equipmentValues[i].currentValue)
          legendData.push(equipmentValues[i].label)
          graphData.push({value: [stability, growthRate, 1/equipmentValues[i].currentValue], name: equipmentValues[i].label})
        }
        stabilities.sort(numberSort)
        growth.sort(numberSort)
        values.sort(numberSort)
        for (let i = 0; i < equipmentSize; i++) {
          graphData[i].value = [stabilities.indexOf(graphData[i].value[0])+1, growth.indexOf(graphData[i].value[1])+1, values.indexOf(graphData[i].value[2])+1]
        }
        var indicatorData = [
          { name: 'Stability', max: equipmentSize+0.5 },
          { name: 'Growth', max: equipmentSize+0.5 },
          { name: 'Value', max: equipmentSize+0.5 }
        ]
        this.chartData = {
          legendData: legendData,
          graphData: graphData,
          indicatorData: indicatorData
        }
      }
     
    },

    buyEquipment() {
      this.$notify({
        title: 'Success',
        message: 'Equipment Bought',
        type: 'success',
        duration: 2000
      })
    },

    changeBaseofEquipment(equipmentLabel) {
      this.equipmentData.forEach(function(currency) {
        if (currency.label == equipmentLabel) {
          console.log(currency)
          for (let i = 0; i < currency.data.open.length; i++) {
            currency.data.open[i] = 1/currency.data.open[i]
            currency.data.close[i] = 1/currency.data.close[i]
            currency.data.low[i] = 1/currency.data.low[i]
            currency.data.high[i] = 1/currency.data.high[i]
            currency.data.current[i] = 1/currency.data.current[i]
          }
        }
      }, this)
    }
  }
}
</script>

<style lang="scss" scoped>

.raddar-chart-container {
    margin-top: 10;
}

</style>
