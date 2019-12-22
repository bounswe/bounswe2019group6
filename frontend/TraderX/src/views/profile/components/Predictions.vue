<template>
  <div class="app-container">
    <el-table :data="predictionData" stripe style="width: 100%">
      <el-table-column prop="code" label="Code" width="90%"> </el-table-column>
      <el-table-column prop="isSucceeded" label="Success" width="90%"> </el-table-column>
      <el-table-column prop="predType" label="Type"> </el-table-column>
      <el-table-column prop="date" label="Date"> </el-table-column>
      <el-table-column prop="isEditDeleteAvailable">
        <el-button-group slot-scope="scope" v-if="predictionData[scope.$index].isEditDeleteAvailable">
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
    this.getMyStats()
  },
  methods: {
    getMyStats(){
      
    },
    deletePrediction(pred){
      this.$store.dispatch('user/deletePrediction', { "id" : pred.id }).then(() => {
        this.predictionData = this.predictionData.filter(prediction => prediction.id != pred.id)
      }).catch(err => {
        console.log(err)
      })
    },
    editPrediction(){
      var type = this.selectPredType
      var pred = this.currentElem
      this.$store.dispatch('user/editPrediction', { id : pred.id, type: type.toLowerCase() }).then(() => {
        this.$message.success('Editttt Yeeeeaaayhh!')
      }).catch(err => {
        console.log(err)
      })
    },
    getMyPredictions(){
      this.$store.dispatch('user/getPredictionList', this.$store.getters.userInfo.username).then(() => {
        var res = this.$store.getters.predictionList.predictions
        for(var i = 0; i < res.length; i++) {
          var d = new Date(res[i].predictionDay).toDateString()
          this.predictionData.push({
            "id": res[i].predictionId,
            "code": res[i].equipmentCode,
            "predType": res[i].predictionType,
            "isSucceeded": res[i].isSucceeded ? 'Success' : (!res[i].isSucceeded) ? 'Fail' : '',
            "date": d,
            "isEditDeleteAvailable" : res[i].isSucceeded === null ? true : false,
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
