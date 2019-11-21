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

                  <!-- <el-button class='learn-more-button' @click="learnMoreAboutEquipment(te.label)"><svg-icon style="margin-right:10px" icon-class="chart" />Change the Base</el-button> -->
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
      tradingEquipments : [],
    }
  },
  async created() {
    var equipmentList = await this.getEquipmentList() 
    var equipmentOpenningValues = await this.getEquipmentValues(equipmentList)
    var tempTradingEquipment = this.createTempTE(equipmentList, equipmentOpenningValues)
    this.tradingEquipments = tempTradingEquipment
  },

  methods: {
    // Promise for getting equipments list
    async getEquipmentList() {
      try {
        await this.$store.dispatch('equipment/listEquipment', 'currency')
        var res = this.$store.getters.equipmentQueryResult
        return res.equipments
      } catch (error) {
        console.log(error)
        return error
      }  
    },

    // equipment = ['JPY', 'TRY', ...]
    async getEquipmentValues(equipment) {
      // equipmentOpenningValues = [{label="Turkish Lira", key="TRY", data={actualData=[5.6, 4.34, 7.54,...]}}, {label="Japanese Yen", key="JPY", data={actualData=[12.2312, 11.234545, 10.23234, ...]}} ...]
      // This will only have the datas
      var equipmentOpenningValues = []
      equipment.forEach(async function(e) {
        try {
          await this.$store.dispatch('equipment/getEquipment', e.toLowerCase())
          var res = this.$store.getters.equipmentQueryResult
          equipmentOpenningValues.push({})
          equipmentOpenningValues[equipmentOpenningValues.length-1].key = e
          equipmentOpenningValues[equipmentOpenningValues.length-1].label = res.equipment.name
          equipmentOpenningValues[equipmentOpenningValues.length-1].data = {
            open: []
          }
          // equipmentOpenningValues.push([])
          res.historicalValues.forEach(function(val) {
            equipmentOpenningValues[equipmentOpenningValues.length-1].data.open.push(val.open)
          })
        } catch (error) {
          console.log(error)
        }
      }, this)
      return equipmentOpenningValues
    },

    createTempTE(equipmentList, equipmentOpenningValues) {
      let bitCoinsArray = [
        { label: 'Bit Coin', key: 'BC'}
      ];

      let stocksArray = [
        { label: 'Google', key: 'Google'},
        { label: 'Apple', key: 'Apple'},
        { label: 'Microsoft', key: 'Microsoft'},
        { label: 'Facebook', key: 'FB'}
      ]

      var tempTE = [
        { data: [], label: 'Money Currencies'}, // this will be added afterwards
        { data: bitCoinsArray, label: 'Cryptocurrencies', activeTab: bitCoinsArray[0].key},
        { data: stocksArray, label: 'Stocks', activeTab: stocksArray[0].key},
      ];

      var te;
      for (te of tempTE) {
        const teData = te.data;
        var t;
        for (t of teData) {
          t.data = {
            open: Array.from({length: 100}, () => Math.floor(Math.random() * 100))
          }
        }
      }

      tempTE[0].activeTab = equipmentList[0]

      tempTE[0].data = equipmentOpenningValues
      return tempTE

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
