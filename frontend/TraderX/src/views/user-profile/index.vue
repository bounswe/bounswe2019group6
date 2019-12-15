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
          :style=infoTabDisplay
        >
         
          <PrivateAccount />
         
        </el-col>

        <el-col
          :span="18"
          :xs="24"
          :style=privacyDisplay
        >
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane
                label="Portfolio"
                name="portfolio"
              >
                <portfolio />
              </el-tab-pane>
              <el-tab-pane
                label="Article"
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
                      width="60">
                    </el-table-column>
                    <el-table-column
                      prop="title"
                      label="Title"
                      sortable>
                    </el-table-column>
                    <el-table-column
                      prop="timestamp"
                      label="Date"
                      sortable>
                    </el-table-column>
                    <el-table-column fixed="right" width="120">
                      <template slot-scope="scope">
                        <el-button @click="HandleRedirect(tableData[scope.$index])" type="text" round>See Article</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
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
import Portfolio from '@/views/profile/components/Portfolio'
import PrivateAccount from '@/views/profile/components/PrivateAccount'
import { getUser } from '@/api/user'

export default {
  name: 'UserProfile',
  components: { UserCard, Portfolio, PrivateAccount },
  data() {
    return {
      user: {},
      activeTab: 'portfolio',
      visibility: 'visibility: hidden',
      privacyDisplay: 'display: none',
      infoTabDisplay: 'display: none',
      tableData: []
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
    getUser(this.$route.path.split('/')[2]).then(response => {
      this.user = response.data
      this.visibility = 'visibility: visible'
      this.privacyDisplay = this.user.isPrivate ? 'display: none': 'display: block'
      this.infoTabDisplay = this.user.isPrivate ? 'display: block': 'display: none'
    })
  },
  methods: {
    
  }
}
</script>

<style lang="scss" scoped>

</style>
