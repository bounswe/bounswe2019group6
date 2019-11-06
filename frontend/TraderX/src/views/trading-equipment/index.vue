<template>

  <div class="trading-eqipment-list-container">
    <el-row class='row' v-for="te in tradingEquipments" :gutter="20" style="padding:16px 16px 0;margin-bottom:32px;">
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
                  <p class="te-info-text">Last week's data for {{ t.label }} is as follows:</p>
                 
                  <div class='chart-wrapper'>
                    <line-chart :type="t.key" :chart-data="t.data"/>
                  </div>

                  <el-button class='learn-more-button' @click="learnMoreAboutEquipment"><svg-icon style="margin-right:10px" icon-class="chart" />Learn More About Equipment</el-button>
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

// Creating mock data
// This part of the code will be deleted when we will be receiving data from backend
let bitCoinsArray = [
  { label: 'Bit Coin', key: 'BC'}
];

let stocksArray = [
  { label: 'Google', key: 'Google'},
  { label: 'Apple', key: 'Apple'},
  { label: 'Microsoft', key: 'Microsoft'},
  { label: 'Facebook', key: 'FB'}
]

let moneyCurrenciesArray = [
  { label: 'United States Dollar', key: 'US'},
  { label: 'Euro', key: 'EU'},
  { label: 'Pound Sterling', key: 'PS'},
  { label: 'Japanese Yen', key: 'JP'}
];

const tradingEquipments = [
  { data: moneyCurrenciesArray, label: 'Money Currencies', activeTab: moneyCurrenciesArray[0].key},
  { data: bitCoinsArray, label: 'Bit Coin', activeTab: bitCoinsArray[0].key},
  { data: stocksArray, label: 'Stocks', activeTab: stocksArray[0].key},
];

export default {
  name: 'DashboardAdmin',
  components: {
    LineChart
  },
  data() {
    return {
      tradingEquipments: tradingEquipments
    }
  },
  created() {
    this.initializeData();
  },
  methods: {
    initializeData() {
      var te;
      for (te of tradingEquipments) {
        const teData = te.data;
        var t;
        for (t of teData) {
          t.data = {
            expectedData: Array.from({length: 7}, () => Math.floor(Math.random() * 1000)),
            actualData: Array.from({length: 7}, () => Math.floor(Math.random() * 1000))
          }
        }
      }
    },
    learnMoreAboutEquipment() {
      this.$notify({
        title: 'Success',
        message: 'Learning More About Equipment',
        type: 'success',
        duration: 2000
      })
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
