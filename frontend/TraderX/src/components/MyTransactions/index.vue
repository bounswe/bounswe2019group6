<template>
  <div>
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="equipment" label="Name"> </el-table-column>
      <el-table-column prop="transactionType" label="Transaction Type"> </el-table-column>
      <el-table-column prop="time" label="Time"> </el-table-column>
      <el-table-column prop="count" label="Amount"> </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'MyTransactions',
  props: {},
  data() {
    return {}
  },
  created() {
    this.getAllTransactons()
  },
  mounted() {},
  methods: {
    getAllTransactons() {
      
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
