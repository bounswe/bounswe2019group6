<template>
  <div class="app-container">
    <el-table :data="predictionData" stripe style="width: 100%">
      <el-table-column prop="code" label="Code" width="90%"> </el-table-column>
      <el-table-column prop="isSucceeded" label="Success" width="90%"> </el-table-column>
      <el-table-column prop="predType" label="Type"> </el-table-column>
      <el-table-column prop="date" label="Date"> </el-table-column>
      <el-table-column>
        <el-button-group slot-scope="scope">
          <el-button type="danger" @click="deletePrediction(predictionData[scope.$index])">Delete</el-button>
          <el-button type="warning" @click="editPredictionDialog=true; currentElem=predictionData[scope.$index]">Edit</el-button>
        </el-button-group>
      </el-table-column>
    </el-table>

    <el-dialog title="Edit Prediction" :visible.sync="editPredictionDialog">
      <el-select style="width:120px; margin-left: 40px" v-model="selectPredType" placeholder="Select">
        <el-option value="Increase">Increase</el-option>
        <el-option value="Decrease">Decrease</el-option>
        <el-option value="Stable">Stable</el-option>
      </el-select>
      <el-button style="margin-left: 40px" type="warning" @click="editPrediction()">Edit Prediction</el-button>
    </el-dialog>

  </div>
</template>

<script>
export default {
  data() {
    return {
      predictionData: [],
      editPredictionDialog: false,
      selectPredType: "",
      currentElem: {}
    }
  },
  created(){
    this.getMyPredictions()
  },
  methods: {
    deletePrediction(pred){
      this.$store.dispatch('user/deletePrediction', { "id" : pred.id }).then(() => {
        this.predictionData = this.predictionData.filter(prediction => prediction.id != pred.id)
        this.$message.success('Delete Yeeeeaaayhh!')
      }).catch(err => {
        console.log(err)
      })
    },
    editPrediction(){
      var type = this.selectPredType
      var pred = this.currentElem
      console.log(type)
      console.log(pred)
      // this.$store.dispatch('user/editPrediction', { id : pred.predictionId, type: type }).then(() => {
      //   this.$message.success('Editttt Yeeeeaaayhh!')
      // }).catch(err => {
      //   console.log(err)
      // })
    },
    getMyPredictions(){
      this.$store.dispatch('user/getPredictionList', this.$store.getters.userInfo.username).then(() => {
        var res = this.$store.getters.predictionList.predictions
        console.log(res)
        for(var i = 0; i < res.length; i++) {
          var d = new Date(res[i].predictionDay).toDateString()
          this.predictionData.push({
            "id": res[i].predictionId,
            "code": res[i].equipmentCode,
            "predType": res[i].predictionType,
            "isSucceeded": res[i].isSucceeded ? 'Success' : 'Fail',
            "date": d,
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
