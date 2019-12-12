<template>
  <div>
    <el-table :data="transactionTableData" stripe style="width: 100%">
      <el-table-column prop="equipment" label="Name"> </el-table-column>
      <el-table-column prop="transactionType" label="Transaction Type"> </el-table-column>
      <el-table-column prop="time" label="Time"> </el-table-column>
      <el-table-column prop="amount" label="Amount"> </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'MyTransactions',
  props: {},
  data() {
    return {
      transactionTableData : []
    }
  },
  created() {
    this.getAllTransactons()
  },
  mounted() {},
  methods: {
    getAllTransactons() {
      this.$store.dispatch('equipment/getAllTransactions', this.$store.getters.userInfo.username).then(() => {
        var res = this.$store.getters.transaction
        for (var i = 0; i < res.length; i++) {
          this.transactionTableData.push({
            "equipment": res[i].equipment,
            "amount": res[i].amount,
            "transactionType": res[i].transactionType,
            "time": res[i].createdAt
          })
        }
      }).catch(error => {
        console.log(error)
      }) 
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
