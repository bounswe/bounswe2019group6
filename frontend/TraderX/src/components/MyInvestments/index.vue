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
      tableData: [],
      sellamount: '',
      sellectedAsset: '',
    }
  },
  async created() {
    await this.getAllEquipments()
  },
  methods: {
    handleSellEquipment() {
      this.$store.dispatch('equipment/sellEquipment', {'code' : this.sellectedAsset, "amount" : this.sellamount}).then(response => {
        for(var i = 0; i < this.tableData.length; i++) {
          if (this.tableData[i].code == this.sellectedAsset) {
            this.tableData[i].amount -= this.sellamount
            if (this.tableData[i].amount == 0){
              this.tableData.splice(i,1)
            }
          }
        }
        this.$message.success('Equipment Is Sold Successfully!')
      }).catch(err => {
        console.log(err)
      })
    },
    async getAllEquipments(){
      await this.$store.dispatch('equipment/getAssetInfo').then(response => {
        for(var i = 0; i < this.$store.getters.equipmentQueryResult.length; i++) {
          this.tableData.push({
            'code': this.$store.getters.equipmentQueryResult[i].code,
            'amount': this.$store.getters.equipmentQueryResult[i].amount
          })
        }
      }).catch(err => {
        console.log(err)
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
