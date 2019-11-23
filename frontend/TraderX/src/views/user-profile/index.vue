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
import Articles from '@/views/profile/components/Articles'
import PrivateAccount from '@/views/profile/components/PrivateAccount'
import { getUser } from '@/api/user'

export default {
  name: 'UserProfile',
  components: { UserCard, Portfolio, Articles, PrivateAccount },
  data() {
    return {
      user: {},
      activeTab: 'portfolio',
      visibility: 'visibility: hidden',
      privacyDisplay: 'display: none',
      infoTabDisplay: 'display: none'
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
