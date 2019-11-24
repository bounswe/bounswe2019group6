<template>
  <div>
    <div style="text-align: center" v-if="isSelf">
      <el-button @click="showDialog=true" type="primary"><svg-icon style="margin-right:10px" icon-class="documentation" />Create Portfolio</el-button>
    </div>
    <div style="text-align: center" v-else>
      <h2>This user has no portfolio to show</h2>
    </div>
    <div>
      <CoolCard :cardData="all_portfolios" :username="username"/>
    </div>
    <el-dialog title="Create Portfolio" :visible.sync="showDialog">
      <el-form @submit.native.prevent="handleCreatePortfolio" ref="createPortfolioForm" :model="createPortfolioForm">
        <el-form-item prop="Portfolio Name">
          <el-input ref="portfolioName" placeholder="Portfolio Name" v-model="createPortfolioForm.portfolioName" />
        </el-form-item>
        <el-button @click="handleCreatePortfolio" type="primary">
          Create
        </el-button>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>

import CoolCard from '@/components/CoolCard'

export default {
  props: {
    username: String
  },
  components: { CoolCard },
  data() {
    return {
      showDialog: false,
      all_portfolios: [],
      createPortfolioForm: {
        portfolioName: '',
        portfolioDescription: ''
      },
      isSelf: this.$route.path.split('/')[1] == 'profile' ? true : false
    }
  },
  methods: {
    handleCreatePortfolio(){
      if (this.createPortfolioForm.portfolioName == '') {
        this.$message.error("Portfolio Name Can Not Be Empty")
      } else {
        this.showDialog = false,
        this.all_portfolios.push({
          portfolioName : this.createPortfolioForm.portfolioName,
          portfolioDescription: this.createPortfolioForm.portfolioDescription
        })
        this.$notify({ title: 'Success', message: 'Portfolio is posted', type: 'success', duration: 2000 })
      }
    }
  }
}
</script>

<style scoped>

</style>