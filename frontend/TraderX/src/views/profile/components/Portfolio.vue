<template>
  <div>
    <div style="text-align: center" v-if="isSelf">
      <el-button @click="showCreateDialog=true" type="primary"><svg-icon style="margin-right:10px; display:inline-block;" icon-class="documentation" />Create Portfolio</el-button>
      <el-button @click="showDeleteDialog=true" type="danger"><svg-icon style="margin-right:10px; display:inline-block;" icon-class="documentation" />Delete Portfolio</el-button>
    </div>
    <div style="text-align: center" v-else>
      <h2>This user has no portfolio to show</h2>
    </div>
    <div>
      <CoolCard :cardData="all_portfolios" :username="username"/>
    </div>
    <el-dialog title="Create Portfolio" :visible.sync="showCreateDialog">
      <el-form @submit.native.prevent="handleCreatePortfolio" ref="createPortfolioForm" :model="createPortfolioForm">
        <el-form-item prop="Portfolio Name">
          <el-input ref="portfolioName" placeholder="Portfolio Name" v-model="createPortfolioForm.portfolioName" />
        </el-form-item>
        <el-button @click="handleCreatePortfolio" type="primary">
          Create
        </el-button>
      </el-form>
    </el-dialog>
    <el-dialog title="Delete Portfolio" :visible.sync="showDeleteDialog">
      <el-form @submit.native.prevent="handleDeletePortfolio" ref="createPortfolioForm" :model="deletePortfolioForm">
        <el-form-item prop="Portfolio Name">
          <el-input ref="portfolioName" placeholder="Portfolio Name" v-model="deletePortfolioForm.portfolioName" />
        </el-form-item>
        <el-button @click="handleDeletePortfolio" type="danger">
          Delete
        </el-button>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>

import CoolCard from '@/components/CoolCard'
import { getMyAllPortfolios, createPortfolio, deletePortfolio, addEquipmentToPortfolio, deleteEquipmentFromPortfolio } from '@/api/equipment'

export default {
  props: {
    username: String
  },
  components: { CoolCard },
  data() {
    return {
      showCreateDialog: false,
      showDeleteDialog: false,
      all_portfolios: [],
      createPortfolioForm: {
        portfolioName: '',
      },
      deletePortfolioForm: {
        portfolioName: '',
      },
      isSelf: this.$route.path.split('/')[1] == 'profile' ? true : false
    }
  },
  created() {
    this.getAllPortfolios()
  },
  methods: {
    getAllPortfolios() {
      this.$store.dispatch('equipment/getMyAllPortfolios').then(() => {
        for(var i = 0; i < this.$store.getters.allPortfolios.length; i++) {
          this.all_portfolios.push({
            portfolioName : this.$store.getters.allPortfolios[i].portfolioName,
          })
        }
      }).catch(error => {
        console.log("errorrr in iban change")
        console.log(error)
      })
    },
    handleCreatePortfolio(){
      if (this.createPortfolioForm.portfolioName == '') {
        this.$message.error("Portfolio Name Can Not Be Empty")
      } else {
        this.$store.dispatch('equipment/createPortfolio', { portfolioName: this.createPortfolioForm.portfolioName }).then(() => {
          this.showCreateDialog = false,
          this.all_portfolios.push({
            portfolioName : this.createPortfolioForm.portfolioName,
          })
          this.$notify({ title: 'Success', message: 'Portfolio is posted', type: 'success', duration: 2000 }) 
         }).catch(error => {
           console.log("errorrr in iban change")
           console.log(error)
         })
      }
    },
    handleDeletePortfolio(){
      if (this.deletePortfolioForm.portfolioName == '') {
        this.$message.error("Portfolio Name Can Not Be Empty")
      } else {
        this.$store.dispatch('equipment/deletePortfolio', { portfolioName: this.deletePortfolioForm.portfolioName }).then(() => {
          this.showCreateDialog = false,
          this.all_portfolios = this.all_portfolios.filter(portfolio => portfolio.portfolioName != this.deletePortfolioForm.portfolioName);
          
          this.$notify({ title: 'Success', message: 'Portfolio is deleted', type: 'success', duration: 2000 }) 
        }).catch(error => {
          console.log("errorrr in portfolio deletion")
          console.log(error)
        })
      }
    }
  },
}
</script>

<style scoped>

</style>