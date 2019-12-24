<template>
  <div class="app-container" :style=visibility>
    <div v-if="user">
      <el-row :gutter="20">
        <el-col
          :span="6"
          :xs="24"
        >
          <user-card :user="user" />
        </el-col>

        <el-col
          :span="18"
          :xs="24"
          :style=privacyDisplay
        >
         
          <PrivateAccount />
         
        </el-col>

        <el-col
          :span="18"
          :xs="24"
          :style=infoTabDisplay
        >
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane
                label="Articles"
                name="articles"
              >
                <div style="margin-top: 10px">
                  <el-table
                    :data="tableData"
                    :default-sort = "{prop: 'date', order: 'descending'}"
                    stripe
                    style="width: 100%">
                    <el-table-column
                      prop="id"
                      label="ID"
                      sortable
                      width="60"/>
                    <el-table-column
                      prop="title"
                      label="Title"
                      sortable/>
                    <el-table-column
                      prop="timestamp"
                      label="Date"
                      sortable/>
                    <el-table-column fixed="right" width="120">
                      <template slot-scope="scope">
                        <el-button @click="HandleRedirect(tableData[scope.$index])" type="text" round>See Article</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </el-tab-pane>
              <el-tab-pane
                label="Predictions"
                name="predictions"
              >
                <el-table :data="predictionData" stripe style="width: 100%">
                  <el-table-column prop="code" label="Code" width="90%"> </el-table-column>
                  <el-table-column prop="isSucceeded" label="Success" width="90%"> </el-table-column>
                  <el-table-column prop="predType" label="Type"> </el-table-column>
                  <el-table-column prop="date" label="Date"> </el-table-column>
                </el-table>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>

import { mapGetters } from 'vuex'
import UserCard from '@/views/profile/components/UserCard'
import PrivateAccount from '@/views/profile/components/PrivateAccount'
import { getUser } from '@/api/user'

export default {
  name: 'UserProfile',
  components: { UserCard, PrivateAccount },
  data() {
    return {
      user: {},
      activeTab: 'articles',
      visibility: 'visibility: hidden',
      privacyDisplay: 'display: none',
      infoTabDisplay: 'display: none',
      tableData: [],
      predictionData: []
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'avatar',
      'roles'
    ])
  },
  created() {
    getUser(this.$route.path.split('/')[2]).then(response => {
      this.user = response.data
      this.visibility = 'visibility: visible'
      this.privacyDisplay = this.user.isPrivate && this.user.followingStatus != 'FOLLOWING' ? 'display: block': 'display: none'
      this.infoTabDisplay = !this.user.isPrivate || this.user.followingStatus == 'FOLLOWING'  ? 'display: block': 'display: none'
      this.getUserArticle()
      this.getUserPredictions()
    })
  },
  methods: {
    getUserPredictions(){
      this.$store.dispatch('user/getUserPredictionList', this.user.username).then(() => {
        var res = this.$store.getters.userPredictionList.predictions
        for(var i = 0; i < res.length; i++) {
          var d = new Date(res[i].predictionDay).toDateString()
          this.predictionData.push({
            "code": res[i].equipmentCode,
            "predType": res[i].predictionType,
            "isSucceeded": res[i].isSucceeded ? 'Success' : (res[i].isSucceeded == false) ? 'Fail' : '',
            "date": d,
          })
        }
      }).catch(err => {
        console.log(err)
      })
    },
    getUserArticle(){
      if(!this.user.isPrivate){
        this.$store.dispatch('search/getArticleByUserName', this.$route.path.split('/')[2]).then(() => {
          for(var i = 0; i < this.$store.getters.userArticle.length; i++){
            this.tableData.push({
              "timestamp" : this.$store.getters.userArticle[i].createdAt,
              "title" : this.$store.getters.userArticle[i].header,
              "id" : this.$store.getters.userArticle[i].id
            }) 
          } 
        }).catch(err => {
          console.log(err)
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
