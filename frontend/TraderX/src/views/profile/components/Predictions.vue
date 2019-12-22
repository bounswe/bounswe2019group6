<template>
  <div class="app-container">

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
    getMyPredictions(){
      console.log(this.$store.getters.userInfo.username)
      this.$store.dispatch('user/getPredictionList', this.$store.getters.userInfo.username).then(() => {
        console.log(this.$store.getters.predictionList)
        this.predictionData = this.$store.getters.predictionList
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
