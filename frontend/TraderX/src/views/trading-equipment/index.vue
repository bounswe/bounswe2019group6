<template>

  <div class="trading-eqipment-list-container">
    <el-row class='row' v-for="te in tradingEquipments" :key="te.label" :gutter="20" style="padding:10px 10px 0px;margin-bottom:10px;">
      <el-col class='te-type-column' :span="8" :xs="25">
        <el-card class='te-type-container' style="padding: 10px 10px 0px;">
          <h1 class='te-type-text'>{{ te.label }}</h1>
          <el-card style="margin-top:50px;" class='raddar-chart-container'>
            <raddar-chart :chart-data="te.chartData"/>
          </el-card>
            <div style="margin-top:20px;">
              <div  class="te-radar-info-text">Categories are calculated as follows;</div>
              <div  class="te-radar-info-text"><b>Stabilitity:</b> 1 / [ sum( | average - ith data | / average ) / 100 ] --> i:[0,100]</div>
              <div  class="te-radar-info-text"><b>Growth:</b> sum( ith data - (i+1)th data ) / 100 --> i:[0,99]</div>
            </div>
        </el-card>
      </el-col>

      <el-col :span="17" :xs="25" >
        <el-card>
          <el-tabs v-model="te.activeTab" >
            <el-tab-pane class='te-tab-pane' v-for="t in te.data" :key="t.key" :label="t.label" :name="t.key" :type="t.key">
              <div v-if="te.activeTab==t.key">
                <p class="te-info-text">Last 100 days' American Dollar / {{ t.label }} is as follows:</p>
                <div style="margin-top:50px;margin-bottom:50px" class='chart-wrapper'>
                  <line-chart :type="t.key" :chart-data="t.data"/>
                </div>
                <el-button class='learn-more-button' @click="learnMoreAboutEquipment(te.label)"><svg-icon style="margin-right:10px" icon-class="chart" />Learn More About Equipment</el-button>
                <el-button class='buy-button' @click="showDialog=true; buyElem=te.label"><svg-icon style="margin-right:10px" icon-class="shopping" />Buy Equipment</el-button>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog title="Select Trading Equipment" :visible.sync="showDialog">
      <el-input placeholder="Please enter an amount" v-model="buyamountinput" class="input-with-select">
        <el-select style="width:120px" v-model="select" slot="prepend" placeholder="Select">
          <div v-if="this.buyElem=='Money Currencies'">
            <el-option v-for="(item, index) in this.currencyList" :key="index" :label="item.code" :value="item.code"></el-option>
          </div>
          <div v-if="this.buyElem=='Cryptocurrencies'">
            <el-option v-for="(item, index) in this.cryptoList" :key="index" :label="item.code" :value="item.code"></el-option>
          </div>
          <div v-if="this.buyElem=='Stocks'">
            <el-option v-for="(item, index) in this.stocksList" :key="index" :label="item.code" :value="item.code"></el-option>
          </div>
        </el-select>
        <el-button slot="append" @click="buyEquipment()">Buy</el-button>
      </el-input>  
    </el-dialog>   

  </div>

</template>

<script>
import LineChart from './components/LineChart'
import RaddarChart from './components/RaddarChart'

// The usual sorting in javascript sorts alphabetically which causes mistake in our code
const numberSort = function (a,b) {
    return a - b;
};

export default {
  name: 'DashboardAdmin',
  components: {
    LineChart,
    RaddarChart
  },
  data() {
    return {
      tradingEquipments: [],
      currencyList: [],
      cryptoList: [],
      stocksList: [],
      showDialog: false,
      buyElem: "",
      buyamountinput: "",
      select: "",
    }
  },
  async created() {
    this.tradingEquipments.push({label: 'Money Currencies'})
    this.tradingEquipments.push({label: 'Crytocurrencies'})
    this.tradingEquipments.push({label: 'Stocks'})

this.currencyList = await this.getCurrencyList()
    this.cryptoList = await this.getCryptoList()
    this.stocksList = await this.getStocksList()

    var currencyData = await this.getEquipmentData(this.currencyList)
    this.addDataToTradingEquipments(this.currencyList.length, currencyData, 0)
    var cryptoData = await this.getEquipmentData(this.cryptoList)
    this.addDataToTradingEquipments(this.cryptoList.length, cryptoData, 1)
    var stocksData = await this.getEquipmentData(this.stocksList)
    this.addDataToTradingEquipments(this.stocksList.length, stocksData, 2)

    this.createRadarChartData()
  },

  methods: {
    buyEquipment() {
      this.$store.dispatch('equipment/buyEquipment', {'code' : this.select, "amount" : this.buyamountinput}).then(response => {
        this.$message.success('Equipment Is Bought Successfully!')
      }).catch(err => {
        console.log(err)
      })
    },
    async getCurrencyList() {
      try {
        await this.$store.dispatch('equipment/getAllCurrencies')
        var res = this.$store.getters.currencyResult
        return res.equipments
      } catch (error) {
        console.log(error)
        return error
      }  
    },

    async getCryptoList() {
      try {
        await this.$store.dispatch('equipment/getAllCryptoCurrencies')
        var res = this.$store.getters.cryptoCurrencyResult
        return res.equipments
      } catch (error) {
        console.log(error)
        return error
      }  
    },

    async getStocksList() {
      try {
        await this.$store.dispatch('equipment/getAllStocks')
        var res = this.$store.getters.stockResult
        return res.equipments
      } catch (error) {
        console.log(error)
        return error
      }  
    },

    // equipmentList is a list of the keys of the equipment
    // equipmentType is 0,1 or 2. 0 indicating Money Currencies, 1 indicating CryptoCurrencies, 2 indicating Stocks
    addKeyToTradingEquipments(equipmentList, equipmentType) {
      // Wait until the list is pulled properly
      if (equipmentList.length == 0) {
        setTimeout(() => {
          this.addKeyToTradingEquipments(equipmentList, equipmentType)
        }, 200)
      } else {
        for (let i = 0; i < equipmentList.length; i++) {
          if (i == 0) {
            this.tradingEquipments[equipmentType].activeTab = equipmentList[i]
          }
          this.tradingEquipments[equipmentType].data.push({
            key: equipmentList[i].code
          })
        }
      }
    },

    async getEquipmentData(equipmentList) {
      var equipmentData = []
      equipmentList.forEach(async function(e) {
        try {
          await this.$store.dispatch('equipment/getEquipment', e.code.toLowerCase())
          var res = this.$store.getters.equipmentQueryResult
          equipmentData.push({})
          equipmentData[equipmentData.length-1].key = e.code
          equipmentData[equipmentData.length-1].label = res.equipment.name
          equipmentData[equipmentData.length-1].data = {
            open: [],
            current: []
          }
          res.historicalValues.forEach(function(val) {
            equipmentData[equipmentData.length-1].data.open.push(val.open)
            equipmentData[equipmentData.length-1].data.current.push(res.equipment.currentValue)
          })
        } catch (error) {
          console.log(error)
        }
      }, this)
      return equipmentData
    },

    async returnEquipmentData(equipmentList, equipmentType) {
      this.tradingEquipments[equipmentType].data = []
      var that = this
      equipmentList.forEach(async function(e) {
        try{
          await that.$store.dispatch('equipment/getEquipment', e.code.toLowerCase())
          var res = that.$store.getters.equipmentQueryResult
          if (that.tradingEquipments[equipmentType].data.length == 0) {
            that.tradingEquipments[equipmentType].activeTab = e.code
          }
          that.tradingEquipments[equipmentType].data.push({})
          that.tradingEquipments[equipmentType].data[that.tradingEquipments[equipmentType].data.length-1].key = e.code
          that.tradingEquipments[equipmentType].data[that.tradingEquipments[equipmentType].data.length-1].label = res.equipment.name
          that.tradingEquipments[equipmentType].data[that.tradingEquipments[equipmentType].data.length-1].data = {
            open: [],
            current: []
          }

          res.historicalValues.forEach((val) => {
            that.tradingEquipments[equipmentType].data[that.tradingEquipments[equipmentType].data.length-1].data.open.push(val.open)
            that.tradingEquipments[equipmentType].data[that.tradingEquipments[equipmentType].data.length-1].data.current.push(res.equipment.currentValue)
          })
        } catch (error) {
          console.log(error)
        }
      })
    },

    addDataToTradingEquipments(equipmentSize, equipmentData, equipmentType) {
      // In order to decrease the latency the data is put to trading equipments as soon as it is pulled
      // And if it is not pulled properly the method is called again
      if (equipmentData.length < equipmentSize) {
        setTimeout(() => {
          this.addDataToTradingEquipments(equipmentSize, equipmentData, equipmentType)
        }, 200)
      }
      this.tradingEquipments[equipmentType].data = []
      for (let i = 0; i < equipmentData.length; i++) {
        if (i == 0){
          this.tradingEquipments[equipmentType].activeTab = equipmentData[i].key
        }
        this.tradingEquipments[equipmentType].data.push({})
        this.tradingEquipments[equipmentType].data[i].key = equipmentData[i].key
        this.tradingEquipments[equipmentType].data[i].label = equipmentData[i].label
        this.tradingEquipments[equipmentType].data[i].data = equipmentData[i].data
      }
    },

    createRadarChartData() {
      console.log('in createRadarChartData')
      if (this.tradingEquipments[0].data.length < this.currencyList.length ||
          this.tradingEquipments[1].data.length < this.cryptoList.length ||
          this.tradingEquipments[2].data.length < this.stocksList.length) {
        setTimeout(() => {
          this.createRadarChartData()
        }, 300)
      } else {
        this.tradingEquipments.forEach(function (te) {
          te.chartData = {}

          var stabilities = [] // calculate stability of each trading equipment
          var growth = [] // calculate growth rate for each equipment
          var values = []
          var legendData = [] // For getting legendData
          var graphData = []

          const equipmentSize = te.data.length
          
          for (let i = 0; i < equipmentSize; i++) {
            let valueAverage = 0
            te.data[i].data.open.forEach(function(val) {
              valueAverage += val / te.data[i].data.open.length
            })
            let stability = Math.abs(valueAverage - te.data[i].data.open[0]) / te.data[i].data.open.length
            let growthRate = 0
            for (let j = 0; j < te.data[i].data.open.length-1; j++) {
              stability += (Math.abs(valueAverage - te.data[i].data.open[j+1]) / valueAverage) / te.data[i].data.open.length
              growthRate += (te.data[i].data.open[j] - te.data[i].data.open[j+1]) / te.data[i].data.open.length
            }
            stabilities.push(1/stability)
            growth.push(growthRate)
            values.push(te.data[i].data.current[0])
            legendData.push(te.data[i].label)
            graphData.push({value: [1/stability, growthRate, te.data[i].data.current[0]], name: te.data[i].label})
          }
          stabilities.sort(numberSort)
          growth.sort(numberSort)
          values.sort(numberSort)
          for (let i = 0; i < equipmentSize; i++) {
            graphData[i].value = [stabilities.indexOf(graphData[i].value[0])+1, growth.indexOf(graphData[i].value[1])+1, values.indexOf(graphData[i].value[2])+1]
          }

          var indicatorData = [
            { name: 'Stability', max: Math.max(stabilities)},
            { name: 'Growth', max: Math.max(growth)},
            { name: 'Value', max: Math.max(values)}
          ]

          te.chartData.legendData = legendData
          te.chartData.graphData = graphData
          te.chartData.indicatorData = indicatorData
        })
      }
    },

    learnMoreAboutEquipment(equipmentLabel) {
      if (equipmentLabel == 'Money Currencies') {
        this.$router.push({ path: '/trading-equipment/money-currencies' })
      } else {
        this.$router.push({ path: '/trading-equipment/' +  equipmentLabel.toLowerCase()})
      }
    },
  }
}
</script>

<style lang="scss" scoped>
$dark_gray: #424646;
$light_gray:#eee;
$light_blue: #0c96e5;

.trading-eqipment-list-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .row {
    display: flex;
    flex-direction: row;
    align-items: stretch;
  }

  .chart-wrapper {
    position: relative;
    width: 100%;

  }

  .te-type-container {
    background: #fff;
    margin-bottom: 32px;
    height: 100%;
  }

  .te-type-text {
    color: $dark_gray;
    font-size: 1.5vw;
    margin-top: 10px;
    margin-bottom: 10px;
    text-align: center;
    vertical-align: middle;
  }

  .te-tab-pane {
    width: 100%;
  }

  .te-info-text {
    color: $dark_gray;
    font-size: 0.8vw;
  }

  .te-radar-info-text {
  color: $dark_gray;
  font-size: 0.7vw;
  }

}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
