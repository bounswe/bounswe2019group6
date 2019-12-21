<template>
  <div class="navbar">
    <hamburger
      id="hamburger-container"
      :is-active="sidebar.opened"
      class="hamburger-container"
      @toggleClick="toggleSideBar"
    />

    <breadcrumb
      id="breadcrumb-container"
      class="breadcrumb-container"
    />

    <div class="right-menu">
      

      <el-dropdown
        @command="markNotificationsAsNotNew"
        class="right-menu-item hover-effect"
        trigger="click">
        <el-badge :value="this.isThereNew ? 'new' : '' " class="item" style="margin-top: 7px; margin-right: 40px;">
          <el-button type="primary" icon="el-icon-bell"></el-button>
        </el-badge>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item v-for="(notif, index) in this.notifications" :key="index" :command="index"> 
            <div v-if="notif.type=='FOLLOW_REQUESTED'">
              <div>
                <span style="float:left; ">
                  <p>Follow Requst From: <b>{{ notif.payload.username }}</b></p>
                </span>
                <span style="float:right">
                  <el-button style="margin-left:10px; margin-top:10px" type="primary" @click="AcceptFollowRequest(notif.payload.username)">Accept</el-button>
                  <el-button style="margin-left:10px; margin-top:10px" type="danger" @click="DeclineFollowRequest(notif.payload.username)">Decline</el-button>
                </span>
              </div>
            </div>
            <div v-if="notif.type=='FOLLOWED'" >
              <div>
                <p>You started to follow <b>{{ notif.payload.username }}</b></p>
              </div>
            </div>
            <div v-if="notif.type=='FOLLOW_REQUEST_ACCEPTED'">
              <div>
                <p>Your follow request to <b>{{ notif.payload.username }}</b> is accepted</p>
              </div>
            </div>
            <div v-if="notif.type=='FOLLOW_REQUEST_DENIED'">
              <div>
                <p>Your follow request to <b>{{ notif.payload.username }}</b> is denied</p>
              </div>
            </div>
            <div v-if="notif.type=='ALERT_TRANSACTION_SUCCESS'">
              <div>
                <span style="float:left; ">
                  <p>{{ notif.type }} </p>
                </span>
                <span style="float:right">
                  <el-popover ref="popover" placement="left" width="400" trigger="hover">
                    <p>Code: {{ notif.patload.code }}</p>
                    <p>Amont: {{ notif.patload.amount }}</p>
                    <p>Limit: {{ notif.patload.limit }}</p>
                    <p>Type: {{ notif.patload.type }}</p>
                    <p>Message: {{ notif.patload.message }}</p>
                  </el-popover>
                </span>
              </div>
            </div>
            <div v-if="notif.type=='ALERT_TRANSACTION_FAIL'">
              <div>
                <span style="float:left; ">
                  <p>{{ notif.type }} </p>
                </span>
                <span style="float:right">
                  <el-popover ref="popover" placement="left" width="400" trigger="hover">
                    <p>Code: {{ notif.patload.code }}</p>
                    <p>Amont: {{ notif.patload.amount }}</p>
                    <p>Limit: {{ notif.patload.limit }}</p>
                    <p>Type: {{ notif.patload.type }}</p>
                    <p>Message: {{ notif.patload.message }}</p>
                  </el-popover>
                </span>
              </div>
            </div>
            <div v-if="notif.type=='ALERT_NOTIFY'">
              <div>
                <span style="float:left; ">
                  <p>{{ notif.type }} </p>
                </span>
                <span style="float:right">
                  <el-popover ref="popover" placement="left" width="400" trigger="hover">
                    <p>Code: {{ notif.patload.code }}</p>
                    <p>Amont: {{ notif.patload.amount }}</p>
                    <p>Limit: {{ notif.patload.limit }}</p>
                    <p>Type: {{ notif.patload.type }}</p>
                    <p>Message: {{ notif.patload.message }}</p>
                  </el-popover>
                </span>
              </div>
            </div>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

      <el-dropdown
        class="avatar-container right-menu-item hover-effect"
        trigger="click"
      >
        <div class="avatar-wrapper">
          <img
            src="https://www.sackettwaconia.com/wp-content/uploads/default-profile.png"
            class="user-avatar"
          >
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/profile/index">
            <el-dropdown-item>Profile</el-dropdown-item>
          </router-link>
          <router-link to="/">
            <el-dropdown-item>Dashboard</el-dropdown-item>
          </router-link>
          
          <el-dropdown-item divided>
            <span
              style="display:block;"
              @click="logout"
            >Log Out</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'

export default {
  data() {
    return {
      isThereNew : false,
      notifications : [],
    }
  },
  components: {
    Breadcrumb,
    Hamburger,
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
  },
  created() {
    this.getNotifications()
  },
  methods: {
    markNotificationsAsNotNew(){
      this.$store.dispatch('user/readAllNotifications').then(() => {

      }).catch(error => {
        console.log(error)
      }) 
    },
    AcceptFollowRequest(name){
      this.$store.dispatch('user/acceptFollowRequest',  {'username' : name}).then(() => {
        this.$message({ title: 'Success', message: 'Follow Request Is Accepted', type: 'success', duration: 2000 }) 
      }).catch(error => {
        console.log(error)
      }) 
    },
    DeclineFollowRequest(name){
      this.$store.dispatch('user/declineFollowRequest', {'username' : name}).then(() => {
        this.$message({ title: 'Success', message: 'Follow Request Is Declined', type: 'success', duration: 2000 }) 
      }).catch(error => {
        console.log(error)
      }) 
    },
    getNotifications(){
      this.$store.dispatch('user/getNotifications').then(() => {
        this.notifications = this.$store.getters.notifications
        for(var i = 0; i < this.notifications.length; i++){
          if (this.notifications[i].isNew) {
            this.isThereNew = true
          }
        }
      }).catch(error => {
        console.log(error)
      }) 
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
