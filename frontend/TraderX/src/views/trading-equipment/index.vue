<template>

  <div class="trading-eqipment-list-container">
    <el-row class='row' v-for="te in tradingEquipments" v-bind:key="te.label" :gutter="20" style="padding:16px 16px 0;margin-bottom:32px;">
      <el-col class='te-type-column' :span="4" :xs="24">
        <el-card class='te-type-container'>
          <h1 class='te-type-text'>{{ te.label }}</h1>
        </el-card>
      </el-col>

      <el-col :span="20" :xs="24" >
        <el-card>
          <el-tabs v-model="te.activeTab" >
            <el-tab-pane class='te-tab-pane' v-for="t in te.data" :key="t.key" :label="t.label" :name="t.key" :type="t.key">
                <div v-if="te.activeTab==t.key">
                  <p class="te-info-text">Last 100 days' American Dollar / {{ t.label }} is as follows:</p>
                 
                  <div class='chart-wrapper'>
                    <line-chart :type="t.key" :chart-data="t.data"/>
                  </div>

                  <el-button class='learn-more-button' @click="learnMoreAboutEquipment(te.label)"><svg-icon style="margin-right:10px" icon-class="chart" />Learn More About Equipment</el-button>
                  <el-button class='buy-button' @click="buyEquipment"><svg-icon style="margin-right:10px" icon-class="shopping" />Buy Equipment</el-button>
                </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>

</template>

<script>
import LineChart from './components/LineChart'

export default {
  name: 'DashboardAdmin',
  components: {
    LineChart
  },
  data() {
    return {
      // tradingEquipments : [
      //   {
      //     label: 'Money Currencies',
      //     activeTab: 'JPY',
      //     data: [
      //       {
      //         key: 'JPY',
      //         label: 'Japanese Yen',
      //         data: {
      //           open: [...],
      //           current: [...]
      //         }
      //       },
      //       {
      //         key: 'EUR',
      //         label: 'Euro',
      //         data: {
      //           open: [...],
      //           current: [...]
      //         }
      //       },
      //       ...
      //     ]
      //   },
      //   {
      //     label: 'Cryptocurrencies',
      //     activeTab: '...',
      //     data: [
      //       {
      //         key: 'BTC',
      //         label: 'BitCoin',
      //         data: {
      //           open: [...],
      //           current: [...]
      //         }
      //       },
      //       ...
      //     ]
      //   },
      //   ...
      // ]
      tradingEquipments : [
        {label: 'Money Currencies', data: []},
        {label: 'Cryptocurrencies', data: []},
        {label: 'Stocks', data: []}
      ],
    }
  },
  async created() {
    var currencyList = await this.getCurrencyList()
    this.addKeyToTradingEquipments(currencyList, 0)
    var cryptoList = await this.getCryptoList()
    this.addKeyToTradingEquipments(cryptoList, 1)
    var stocksList = await this.getStocksList()
    this.addKeyToTradingEquipments(stocksList, 2)
    var currencyData = await this.getEquipmentData(currencyList)
    this.addDataToTradingEquipments(currencyData, 0)
    var cryptoData = await this.getEquipmentData(cryptoList)
    this.addDataToTradingEquipments(cryptoData, 1)
    var stocksData = await this.getEquipmentData(stocksList)
    this.addDataToTradingEquipments(stocksData, 2)
  },

  methods: {
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
            key: equipmentList[i]
          })
        }
      }
    },

    async getEquipmentData(equipmentList) {
      var equipmentData = []
      equipmentList.forEach(async function(e) {
        try {
          await this.$store.dispatch('equipment/getEquipment', e.toLowerCase())
          var res = this.$store.getters.equipmentQueryResult
          equipmentData.push({})
          equipmentData[equipmentData.length-1].key = e
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

    addDataToTradingEquipments(equipmentData, equipmentType) {
      // In order to decrease the latency the data is put to trading equipments as soon as it is pulled
      // And if it is not pulled properly the method is called again
      for (let i = 0; i < equipmentData.length; i++) {
        if (i == 0){
          this.tradingEquipments[equipmentType].activeTab = equipmentData[i].key
        }
        this.tradingEquipments[equipmentType].data[i].key = equipmentData[i].key
        this.tradingEquipments[equipmentType].data[i].label = equipmentData[i].label
        this.tradingEquipments[equipmentType].data[i].data = equipmentData[i].data
      }
      if (equipmentData.length != this.tradingEquipments[equipmentType].data.length) {
        setTimeout(() => {
          this.addDataToTradingEquipments(equipmentData, equipmentType)
        }, 200)
      }
    },

    learnMoreAboutEquipment(equipmentLabel) {
      if (equipmentLabel == 'Money Currencies') {
        this.$router.push({ path: '/trading-equipment/money-currencies' })
      } else {
        this.$router.push({ path: '/trading-equipment/' +  equipmentLabel.toLowerCase()})
      }
    },

    buyEquipment() {
      this.$notify({
        title: 'Success',
        message: 'Equipment Bought',
        type: 'success',
        duration: 2000
      })
    }
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
    font-size: 40px;
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
  }

}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
