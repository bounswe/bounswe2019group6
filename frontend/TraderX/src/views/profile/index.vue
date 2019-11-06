<template>
  <div class="app-container">
    <github-corner class="github-corner" />
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
                label="Explore"
                name="explore"
              >
                <explore />
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
              <el-tab-pane
                label="EditProfile"
                name="editprofile"
              >
                <editprofile />
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
import UserCard from './components/UserCard'
import Portfolio from './components/Portfolio'
import Explore from './components/Explore'
import Events from './components/Events'
import News from './components/News'
import Articles from './components/Articles'
import Editprofile from './components/Editprofile'
import GithubCorner from '@/components/GithubCorner'

export default {
  name: 'Profile',
  components: { UserCard, Portfolio, Explore, Events, News, Articles, Editprofile, GithubCorner },
  data() {
    return {
      user: this.$store.getters.userInfo,
      activeTab: 'portfolio'
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
  },
  methods: {
  },
  mounted() {
  }
}
</script>

<style lang="scss">
  $cursor: #424646;
  $bg:#2d3a4b;
  $dark_gray: #424646;
  $light_gray:#eee;

  .app-main {
    background-color: $light_gray;
  }
</style>