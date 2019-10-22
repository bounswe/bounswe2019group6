<template>
  <div>    
    <el-button @click="createPortfolio"><svg-icon style="margin-right:10px" icon-class="documentation" />Create Portfolio</el-button>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>Portfolio 1</span>
        <!-- <el-button style="float: right; padding: 3px 0" type="text">Hide</el-button> -->
      </div>
      <div v-for="o in 4" :key="o" class="text item">
        {{'Trading Equipment ' + o }}
      </div>
    </el-card>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>Portfolio 2</span>
        <!-- <el-button style="float: right; padding: 3px 0" type="text">Hide</el-button> -->
      </div>
      <div v-for="o in 2" :key="o" class="text item">
        {{'Trading Equipment ' + o }}
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  methods: {
    createPortfolio(){
      this.$notify({
              title: 'Success',
              message: 'Portfolio is posted',
              type: 'success',
              duration: 2000
            })
    }
  }
}
</script>

<style>
  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }

  .box-card {
    width: 480px;
    margin-top: 15px;
  }
</style>