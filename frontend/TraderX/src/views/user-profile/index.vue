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
                label="Event"
                name="events"
              >
                <events />
              </el-tab-pane>
              <el-tab-pane
                label="New"
                name="news"
              >
                <news />
              </el-tab-pane>
              <el-tab-pane
                label="Article"
                name="articles"
              >
                <articles />
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
import Events from '@/views/profile/components/Events'
import News from '@/views/profile/components/News'
import Articles from '@/views/profile/components/Articles'
import { getUser } from '@/api/user'

export default {
  name: 'UserProfile',
  components: { UserCard, Portfolio, Events, News, Articles },
  data() {
    return {
      user: {},
      activeTab: 'portfolio',
      visibility: 'visibility: hidden'
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
    // this.$route.path.split('/') = ["", "user", "username", "profile"] 
    getUser(this.$route.path.split('/')[2]).then(response => {
      this.user = response.data
      this.visibility = 'visibility: visible'
    })
  },
  methods: {
    
  }
}
</script>

<style lang="scss" scoped>

</style>
