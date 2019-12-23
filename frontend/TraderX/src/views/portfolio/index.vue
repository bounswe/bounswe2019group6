<template>
  <div class="app-container">
    <div style="margin-top: 30px; margin-bottom: 50px; margin-left: 60px; margin-right: 60px">
      <div style="float:left">
        <font size=5>PORTFOLIO NAME : <b>{{ portfolioname }}</b></font>
      </div>

      <div style="padding-bottom: 30px; float:right">
        <el-button @click="showDialog=true" type="primary"><svg-icon style="margin-right:10px" icon-class="documentation" />Add Trading Equipment To Portfolio</el-button>
      </div>

      <el-table ref="alreadyAddedTable" :data="alreadyAddedTableData" style="width: 100%">
        <el-table-column label="Equipment Name">
          <template slot-scope="scope">{{ scope.row.equipmentName }}</template>
        </el-table-column>
        <el-table-column label="Equipment Type">
          <template slot-scope="scope">{{ scope.row.equipmentType }}</template>
        </el-table-column>
        <el-table-column label="Current Value">
          <template slot-scope="scope">{{ scope.row.currentValue }}</template>
        </el-table-column>
        <el-table-column label="Current Stock">
          <template slot-scope="scope">{{ scope.row.currentStock }}</template>
        </el-table-column>
        <el-table-column label="">
          <template slot-scope="scope"><el-button size="mini" type="danger" @click="handleDeleteEquipmentPortfolio(scope.row)">Delete</el-button></template>
        </el-table-column>
      </el-table>

      <el-dialog title="Select Trading Equipment" :visible.sync="showDialog">
        <el-button @click="handleAddEquipmentPortfolio" type="primary" style="">
          Add To Portfolio
        </el-button>
        
        <p></p>
        
        <el-collapse v-model="activeNames">

          <el-collapse-item title="Currency" name="1">
            <el-table ref="multipleCurrencyTable" :data="currencyTableData" style="width: 100%" @selection-change="handleSelectionChange">
              <el-table-column type="selection"></el-table-column>
              <el-table-column label="Equipment Name">
                <template slot-scope="scope">{{ scope.row.equipmentName }}</template>
              </el-table-column>
              <el-table-column label="Current Value">
                <template slot-scope="scope">{{ scope.row.currentValue }}</template>
              </el-table-column>
              <el-table-column label="Current Stock">
                <template slot-scope="scope">{{ scope.row.currentStock }}</template>
              </el-table-column>
            </el-table>
          </el-collapse-item>

          <el-collapse-item title="Crypto-Currency" name="2">
            <el-table ref="multipleCryptoCurrencyTable" :data="cryptoCurrencyTableData" style="width: 100%" @selection-change="handleSelectionChange">
              <el-table-column type="selection"></el-table-column>
              <el-table-column label="Equipment Name">
                <template slot-scope="scope">{{ scope.row.equipmentName }}</template>
              </el-table-column>
              <el-table-column label="Current Value">
                <template slot-scope="scope">{{ scope.row.currentValue }}</template>
              </el-table-column>
              <el-table-column label="Current Stock">
                <template slot-scope="scope">{{ scope.row.currentStock }}</template>
              </el-table-column>
            </el-table>
          </el-collapse-item>

          <el-collapse-item title="Stock" name="3">
            <el-table ref="multipleStockTable" :data="stockTableData" style="width: 100%" @selection-change="handleSelectionChange">
              <el-table-column type="selection"></el-table-column>
              <el-table-column label="Equipment Name">
                <template slot-scope="scope">{{ scope.row.equipmentName }}</template>
              </el-table-column>
              <el-table-column label="Current Value">
                <template slot-scope="scope">{{ scope.row.currentValue }}</template>
              </el-table-column>
              <el-table-column label="Current Stock">
                <template slot-scope="scope">{{ scope.row.currentStock }}</template>
              </el-table-column>
            </el-table>
          </el-collapse-item>

        </el-collapse>
      </el-dialog>   
      
    </div>
  </div>
</template>

<script>

import { getAllCurrencies, getAllCryptoCurrencies, getAllStocks, addEquipmentToPortfolio, 
        deleteEquipmentFromPortfolio, getAllEquipmentOfPortfolio } from '@/api/equipment'
import equipment from '../../store/modules/equipment'

export default {
  data() {
    return {
      all_currency_codes: ['JPY','EUR','TRY','CZK','GBP','CNY'],
      all_stock_codes: ['AMZN','BABA','MSFT','ORCL','SILK','ENSG'],
      all_crypto_codes: ['BTC','ETC','VIB','ETH','BTG','ZEN'],
      currencyTableData: [],
      cryptoCurrencyTableData: [],
      stockTableData: [],
      alreadyAddedTableData: [],
      multipleSelection: [],
      activeNames: [],
      showDialog: false,
      portfolioname : ""
    }
  },
  async created() {
    this.portfolioname = this.$route.path.split('/')[3].toUpperCase()
    await this.getAllEquipmentsOfThisPortfolio()
    await this.getCurrency()
    await this.getCryptoCurrency()
    await this.getStock()
  },
  methods: {
    async getCurrency() {
      await this.$store.dispatch('equipment/getAllCurrencies').then(response =>{
        var res = this.$store.getters.currencyResult
        var equip = []
        for (var i = 0; i < res.equipments.length; i++) {
          equip.push({
            "equipmentName": res.equipments[i].code,
            "currentValue": res.equipments[i].data.currentValue,
            "currentStock": res.equipments[i].data.currentStock,
            "equipmentType": "Currency"
          })
        }
        this.currencyTableData = equip
      }).catch(error => {
        console.log(error)
      })
    },
    async getCryptoCurrency() {
      await this.$store.dispatch('equipment/getAllCryptoCurrencies').then(response =>{
        var res = this.$store.getters.cryptoCurrencyResult
        var equip = []
        for (var i = 0; i < res.equipments.length; i++) {
          equip.push({
            "equipmentName": res.equipments[i].code,
            "currentValue": res.equipments[i].data.currentValue,
            "currentStock": res.equipments[i].data.currentStock,
            "equipmentType": "Crypto Currency"
          })
        }
        this.cryptoCurrencyTableData = equip
      }).catch(error => {
        console.log(error)
      })
    },
    async getStock() {
      await this.$store.dispatch('equipment/getAllStocks').then(response =>{
        var res = this.$store.getters.stockResult
        var equip = []
        for (var i = 0; i < res.equipments.length; i++) {
          equip.push({
            "equipmentName": res.equipments[i].code,
            "currentValue": res.equipments[i].data.currentValue,
            "currentStock": res.equipments[i].data.currentStock,
            "equipmentType": "Stock"
          })
        }
        this.stockTableData = equip
      }).catch(error => {
        console.log(error)
      })
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    async getAllEquipmentsOfThisPortfolio(){
      const portfolioName = this.$route.path.split('/')[3]
      await this.$store.dispatch('equipment/getAllEquipmentOfPortfolio', portfolioName ).then(() => {
        for(var i = 0; i < this.$store.getters.allEquipments.length; i++) {
          var t_code = this.$store.getters.allEquipments[i].code
          var t_currentVal = this.$store.getters.allEquipments[i].currentValue
          var t_currentStock = this.$store.getters.allEquipments[i].currentStock
          var t_type = this.all_currency_codes.includes(t_code) ? "Currency" : 
                     this.all_crypto_codes.includes(t_code) ? "Crypto Currency" : "Stock"
          this.alreadyAddedTableData.push({
            "equipmentName": t_code,
            "currentValue": t_currentVal,
            "currentStock": t_currentStock,
            "equipmentType": t_type
          })
        }
      }).catch(error => {
        console.log(error)
      })
    },
    async handleAddEquipmentPortfolio(){
      var currencySelections = this.$refs.multipleCurrencyTable.selection
      var cryptoCurrencySelections = this.$refs.multipleCryptoCurrencyTable.selection
      var stockSelectinos = this.$refs.multipleStockTable.selection
      var selectedAll = currencySelections.concat(cryptoCurrencySelections).concat(stockSelectinos)
      for(var i = 0; i < selectedAll.length; i++) {
        if(!this.alreadyAddedTableData.includes(selectedAll[i])){
          await this.$store.dispatch('equipment/addEquipmentToPortfolio', { portfolioName: this.portfolioname, code: selectedAll[i].equipmentName }).then(() => {
            this.alreadyAddedTableData.push(selectedAll[i])
          }).catch(error => {
            console.log(error)
          })
        }
      }
      this.showDialog = false
      this.$refs.multipleCurrencyTable.clearSelection();
      this.$refs.multipleCryptoCurrencyTable.clearSelection();
      this.$refs.multipleStockTable.clearSelection();
      this.activeNames = []
    },
    handleDeleteEquipmentPortfolio(elem) {
      this.$store.dispatch('equipment/deleteEquipmentFromPortfolio', { portfolioName: this.portfolioname, code: elem.equipmentName }).then(() => {
        this.alreadyAddedTableData = this.alreadyAddedTableData.filter(item => item !== elem)
      }).catch(error => {
        console.log(error)
      })
    }

  }
}
</script>