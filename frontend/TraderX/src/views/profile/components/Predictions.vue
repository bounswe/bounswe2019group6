<template>
  <div class="app-container">
    <el-table :data="predictionData" stripe style="width: 100%">
      <el-table-column prop="code" label="Code" width="90%"> </el-table-column>
      <el-table-column prop="isSucceeded" label="Success" width="90%"> </el-table-column>
      <el-table-column prop="predType" label="Type"> </el-table-column>
      <el-table-column prop="date" label="Date"> </el-table-column>
      <el-table-column>
        <el-button-group>
          <el-button type="danger" @click="deletePrediction()">Delete</el-button>
          <el-button type="warning" @click="editPrediction()">Edit</el-button>
        </el-button-group>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      predictionData: [],
    }
  },
  created(){
    this.getMyPredictions()
  },
  methods: {
    deletePrediction(){

    },
    editPrediction(){

    },
    getMyPredictions(){
      console.log(this.$store.getters.userInfo.username)
      this.$store.dispatch('user/getPredictionList', this.$store.getters.userInfo.username).then(() => {
        var res = this.$store.getters.predictionList.predictions
        for(var i = 0; i < res.length; i++) {
          var d = new Date(res[i].predictionDay).toDateString()
          this.predictionData.push({
            "code": res[i].equipmentCode,
            "predType": res[i].predictionType,
            "isSucceeded": res[i].isSucceeded ? 'Success' : 'Fail',
            "date": d,
          })
        }
        this.$message.success('Prediction Yeeeeaaayhh!')
      }).catch(err => {
        console.log(err)
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
