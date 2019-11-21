<template>
  <div class="app-container">
    <div style="margin-top: 30px; margin-bottom: 50px; margin-left: 60px; margin-right: 60px">
      <el-collapse v-model="activeNames">

        <el-collapse-item title="Currency" name="1">
          <el-table ref="multipleCurrencyTable" :data="currencyTableData" style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection"></el-table-column>
            <el-table-column label="Equipment Name">
              <template slot-scope="scope">{{ scope.row.equipments }}</template>
            </el-table-column>
          </el-table>
        </el-collapse-item>

        <el-collapse-item title="Crypto-Currency" name="2">
          <el-table ref="multipleCryptoCurrencyTable" :data="cryptoCurrencyTableData" style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection"></el-table-column>
            <el-table-column label="Equipment Name">
              <template slot-scope="scope">{{ scope.row.equipments }}</template>
            </el-table-column>
          </el-table>
        </el-collapse-item>

        <el-collapse-item title="Stock" name="3">
          <el-table ref="multipleStockTable" :data="stockTableData" style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection"></el-table-column>
            <el-table-column label="Equipment Name">
              <template slot-scope="scope">{{ scope.row.equipments }}</template>
            </el-table-column>
          </el-table>
        </el-collapse-item>

      </el-collapse>
      
    </div>
  </div>
</template>

<script>

import { getAllCurrencies } from '@/api/equipment'

export default {
  data() {
    return {
      currencyTableData: [],
      cryptoCurrencyTableData: [],
      stockTableData: [],
      multipleSelection: []
    }
  },
  async created() {
    this.currencyTableData = await this.getCurrency()
  },
  methods: {
    async getCurrency() {
      await this.$store.dispatch('equipment/getAllCurrencies').then(response =>{
        var res = this.$store.getters.currencyResult
        console.log("here")
        console.log(res)
      }).catch(error => {
        console.log(error)
      })
      return 
    }
  }
}
</script>