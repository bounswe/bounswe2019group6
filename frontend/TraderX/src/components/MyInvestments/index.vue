<template>
  <div>
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="code" label="Code"> </el-table-column>
      <el-table-column prop="amount" label="Amount"> </el-table-column>
    </el-table>
    
    <el-input placeholder="Please enter the amount" v-model="sellamount" class="input-with-select" style="padding-top: 50px">
      <el-select style="width: 120px" v-model="sellectedAsset" slot="prepend" placeholder="Select">
        <el-option v-for="(item, index) in this.tableData" :key="index" :label="item.code" :value="item.code"></el-option>
      </el-select>
      <el-button slot="append" @click="handleSellEquipment">Sell</el-button>
    </el-input>

  </div>
</template>

<script>

import { getAssetInfo } from '@/api/equipment'
import { sellEquipment } from '@/api/equipment'

export default {
  name: 'MyInvestments',
  props: {},
  data() {
    return {
      currencyList: [],
      cryptoList: [],
      stocksList: [],
      tableData: [],
      sellamount: '',
      sellectedAsset: '',
    }
  },
  async created() {
    await this.getCurrencyList()
    await this.getCryptoList()
    await this.getStocksList()
    var allEquipmentList = []
    for(var i = 0; i < this.currencyList.equipments.length; i++) {
      allEquipmentList.push(this.currencyList.equipments[i].code)
    }
    for(var i = 0; i < this.cryptoList.equipments.length; i++) {
      allEquipmentList.push(this.cryptoList.equipments[i].code)
    }
    for(var i = 0; i < this.stocksList.equipments.length; i++) {
      allEquipmentList.push(this.stocksList.equipments[i].code)
    }
    for(var i = 0; i < allEquipmentList.length; i++) {
      this.getAssetAmount(allEquipmentList[i])
    }   
  },
  methods: {
    handleSellEquipment() {
      this.$store.dispatch('equipment/sellEquipment', {'code' : this.sellectedAsset, "amount" : this.sellamount}).then(response => {
        for(var i = 0; i < this.tableData.length; i++) {
          if (this.tableData[i].code == this.sellectedAsset) {
            this.tableData[i].amount -= this.sellamount
          }
        }
        // this.$message.success('Equipment Is Bought Successfully!')
      }).catch(err => {
        console.log(err)
      })
    },
    getAssetAmount(equipmentName){
      this.$store.dispatch('equipment/getAssetInfo', equipmentName).then(response => {
        this.tableData.push({
          'code': equipmentName,
          'amount': this.$store.getters.equipmentQueryResult.amount
        })
      }).catch(err => {
        console.log(err)
      })
    },
    async getCurrencyList() {
      try {
        await this.$store.dispatch('equipment/getAllCurrencies')
        this.currencyList = this.$store.getters.currencyResult
      } catch (error) {
        console.log(error)
      }  
    },
    async getCryptoList() {
      try {
        await this.$store.dispatch('equipment/getAllCryptoCurrencies')
        this.cryptoList = this.$store.getters.cryptoCurrencyResult
      } catch (error) {
        console.log(error)
      }  
    },
    async getStocksList() {
      try {
        await this.$store.dispatch('equipment/getAllStocks')
        this.stocksList = this.$store.getters.stockResult
      } catch (error) {
        console.log(error)
      }  
    },
  }
}
</script>

<style lang="scss" scoped>

</style>
