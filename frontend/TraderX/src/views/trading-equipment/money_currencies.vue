<template>
 
  <div>
    <el-row style="padding:16px 16px 0;margin-bottom:32px;">
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
                <el-button class='buy-button' @click="showDialog=true"><svg-icon style="margin-right:10px" icon-class="shopping" />Buy Equipment</el-button>
                
                <el-card class='container-in-tab'>
                  <p> alerts comes here </p>
                </el-card>

                <el-card class='container-in-tab'>
                  <h4> Most reviewed articles about {{ed.label}} </h4>

                  <!-- Got this from complex-table component -->
                  <el-table
                    :data="articleList"
                    border
                    fit
                    highlight-current-row
                    style="width: 100%;"
                  >
                    <el-table-column label="ID" prop="id" align="center">
                      <template slot-scope="scope">
                        <span>{{ scope.row.id }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="Date" align="center">
                      <template slot-scope="scope">
                        <span>{{ scope.row.timestamp | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
                      </template>
                    </el-table-column>
                    <!-- TODO: when pressed to one of the titles it should direct the user to the corresponding article -->
                    <el-table-column label="Title" >
                      <template slot-scope="{row}">
                        <span class="link-type">{{ row.title }}</span>
                        <!-- <el-tag>{{ row.type | typeFilter }}</el-tag> -->
                      </template>
                    </el-table-column>
                    <el-table-column label="Author" align="center">
                      <template slot-scope="scope">
                        <span>{{ scope.row.author }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="Importance" >
                      <template slot-scope="scope">
                        <svg-icon v-for="n in +scope.row.importance" :key="n" icon-class="star" class="meta-item__icon" />
                      </template>
                    </el-table-column>
                    <el-table-column label="Readings" align="center">
                      <template slot-scope="{row}">
                        <span v-if="row.pageviews" class="link-type">{{ row.pageviews }}</span>
                        <span v-else>0</span>
                      </template>
                    </el-table-column>
                  </el-table>
                  <el-button class='read-article' style="margin-top:20px;" @click="readArticle(ed.label)"><svg-icon style="margin-right:10px" icon-class="shopping" /> Read More Articles about {{ ed.label }} </el-button>
                </el-card>

                <el-card class='container-in-tab'>
                  <p> comments comes here </p>
                </el-card>

              </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-row>

    <el-dialog title="Select Trading Equipment" :visible.sync="showDialog">
      <el-input placeholder="Please enter an amount" v-model="buyamountinput" class="input-with-select">
        <el-select style="width:120px" v-model="select" slot="prepend" placeholder="Select">
          <div>
            <el-option v-for="(item, index) in this.equipmentData" :key="index" :label="item.key" :value="item.key"></el-option>
          </div>
        </el-select>
        <el-button slot="append" @click="buyEquipment()">Buy</el-button>
      </el-input>  
    </el-dialog>

  </div>
   
</template>

<script>
import LineChart from './components/LineChart'
import LineChartDetailed from './components/LineChartDetailed'

import { buyEquipment } from '@/api/equipment'

// The usual sorting in javascript sorts alphabetically which causes mistake in our code
const numberSort = function (a,b) {
    return a - b;
};

export default {
  name: 'DashboardAdmin',
  components: {
    LineChartDetailed,
    LineChart,
  },
  data() {
    return {
      showDialog: false,
      buyamountinput: '',
      select: '',
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
      activeTab: 'JPY',
      articleList: null
    }
  },
  async created() {
    this.returnArticleList()
    var equipmentList = await this.getEquipmentList()
    // Fill the equipmentData with equipment list
    equipmentList.forEach(function(equipmentKey) {
      this.equipmentData.push({key: equipmentKey.code})
    }, this)
    var equipmentValues = await this.getEquipmentValues(equipmentList) 
    this.equipmentData = equipmentValues
    this.activeTab = this.equipmentData[0].key
  },
  methods: {

    // For now it returns mock data
    // TODO: change this method to get actual articles
    returnArticleList() {
      this.articleList = [
        {
          id: 1,
          timestamp: 1024316325463,
          title: 'Trolollolo',
          author: 'Irmos',
          importance: 3,
          pageviews: 245
        },

        {
          id: 2,
          timestamp: 1012345625463,
          title: 'Random articlezz',
          author: 'Sado',
          importance: 2,
          pageviews: 12420425
        },

        {
          id: 3,
          timestamp: 1024316325463,
          title: 'adfniun biyg oiniug',
          author: 'Yukseljim',
          importance: 3,
          pageviews: 15
        },

        {
          id: 4,
          timestamp: 1024313478569,
          title: 'U picku materinu',
          author: 'More Irmos',
          importance: 1,
          pageviews: 945231
        },

        {
          id: 5,
          timestamp: 1024313456789,
          title: 'My hands are typing words',
          author: 'Irmos',
          importance: 2,
          pageviews: 8245
        }
      ]
    },

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
          await this.$store.dispatch('equipment/getEquipment', e.code.toLowerCase())
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

    buyEquipment() {
      this.$store.dispatch('equipment/buyEquipment', {'code' : this.select, "amount" : this.buyamountinput}).then(response => {
        this.showDialog = false
        this.buyamountinput =  ''
        this.$message.success('Equipment Is Bought Successfully!')
      }).catch(err => {
        console.log(err)
      })
    },

    changeBaseofEquipment(equipmentLabel) {
      this.equipmentData.forEach(function(currency) {
        if (currency.label == equipmentLabel) {
          for (let i = 0; i < currency.data.open.length; i++) {
            currency.data.open[i] = 1/currency.data.open[i]
            currency.data.close[i] = 1/currency.data.close[i]
            currency.data.low[i] = 1/currency.data.low[i]
            currency.data.high[i] = 1/currency.data.high[i]
            currency.data.current[i] = 1/currency.data.current[i]
          }
        }
      }, this)
    },

    readArticle(equipmentLabel) {
      this.$notify({
        title: 'Success',
        message: 'Directing you to the articles about ' + equipmentLabel,
        type: 'success',
        duration: 2000
      })
    }
  }
}
</script>

<style lang="scss" scoped>

.container-in-tab {
    margin-top: 30px;
}

</style>
