<template>
  <el-card style="margin-bottom:20px;">
    <div
      slot="header"
      class="clearfix"
    >
      <span>About me</span>
    </div>
    <div class="user-profile">
      <div class="box-center">
        <pan-thumb
          :image="randomImage"
          :height="'100px'"
          :width="'100px'"
          :hoverable="false"
        />
      </div>
      <div class="box-center">
        <div class="user-name text-center">
          {{ user.username }}
        </div>
        <div class="user-name text-center">
          {{ user.email }}
        </div>
        <div style='text-align: center' v-if="!isSelf">
          <el-button 
            @click.native.prevent="takeFollowUnfollowAction()"
            :type="this.isNotFollowing ? 'primary' : this.isFollowing ? 'danger' : 'warning'" 
            plain>{{ this.followText }}
          </el-button>
        </div>
      </div>
    </div>
    <div class="user-bio">
      <div class="user-personal-info user-bio-section">
        <div class="user-bio-section-header">
          <svg-icon icon-class="user" /><span>Personal Info</span>
        </div>
        <div class="user-bio-section-body">
          <div class="user-stats">
            <p><b>Follower:</b> {{ user.followersCount }}</p>
            <p><b>Following:</b> {{ user.followingsCount }}</p>
            <p><b>Articles:</b> {{ user.articlesCount }}</p>
            <p><b>Comments:</b> {{ user.commentsCount }}</p>
          </div>
          <div class="user-type">
            <p><b>User Type:</b> {{ user.roles[0] }}</p>
          </div>
          <p><b>IBAN:</b> {{ user.iban }}</p>
          
          <p><b>Latitude:</b> {{ user.latitude }}</p>
          <p><b>Longitude:</b> {{ user.longitude }}</p>
          <p><b>Current Status:</b> {{ user.isPrivate ? "Private" : !user.isPrivate ? 'Public': "" }}</p>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
import PanThumb from '@/components/PanThumb'
import { randomImage } from '@/utils'
import { getUser } from '@/api/user'

export default {
  components: { PanThumb },
  props: {
    user: Object
  },
  data() {
    return {
      isFollowing : '',
      isNotFollowing : '',
      isPending : '',
      followText : '',
      isSelf : true
    }
  },
  created() {
    var tempUser = this.user.username
    console.log(this.user.username)
    console.log(tempUser)
    console.log(this.$store.getters.userInfo.username)
    this.isSelf = tempUser.username == this.$store.getters.userInfo.name ? true : false
    this.isFollowing = tempUser.followingStatus == 'FOLLOWING' ? true : false
    this.isNotFollowing = tempUser.followingStatus == 'NOT_FOLLOWING' ? true : false
    this.isPending = tempUser.followingStatus == 'PENDING' ? true : false
    this.followText = tempUser.isNotFollowing ? "Follow" : tempUser.isFollowing ? "Unfollow" : 'Requested'
  },
  methods: {
    takeFollowUnfollowAction() {
      if(this.isNotFollowing){
        this.$store.dispatch('user/followUser', {'username' : this.user.username}).then(response => {
          this.isNotFollowing = false
          if (this.user.isPrivate){
            this.$message.success('Follow Request Is Sent')
            this.isPending = true
            this.isFollowing = false
          } else {
            this.$message.success('Follow User Success')
            this.isFollowing = true  
            this.isPending = false
          }
          this.followText = this.isNotFollowing ? "Follow" : this.isFollowing ? "Unfollow" : 'Requested'
        }).catch(err => {
          console.log(err)
        })
      } else {
        var tempIsPending = this.isPending
        this.$store.dispatch('user/unfollowUser', {'username' : this.user.username}).then(() => {
          this.isNotFollowing = true
          if (tempIsPending){
            this.$message.success('Follow Request Is Deleted')
            this.isPending = false
          } else {
            this.$message.success('Unfollowed User Success')
            this.isFollowing = false
          }
          this.followText = this.isNotFollowing ? "Follow" : this.isFollowing ? "Unfollow" : 'Requested'
        }).catch(() => {
          console.log("errorrr in unfollowing user")
        })
      }
    },
  },
  computed: {
    randomImage
  }
}
</script>

<style lang="scss" scoped>
 .box-center {
   margin: 0 auto;
   display: table;
 }

 .text-muted {
   color: #777;
 }

 .user-profile {
   .user-name {
     font-weight: bold;
     padding-bottom: 5px;
   }

   .box-center {
     padding-top: 10px;
   }

   .user-role {
     padding-top: 10px;
     font-weight: 400;
     font-size: 14px;
   }

   .box-social {
     padding-top: 30px;

     .el-table {
       border-top: 1px solid #dfe6ec;
     }
   }

   .user-follow {
     padding-top: 20px;
   }
 }

 .user-bio {
   margin-top: 20px;
   color: #606266;

   span {
     padding-left: 4px;
   }

   .user-bio-section {
     font-size: 14px;
     padding: 15px 0;

     .user-bio-section-header {
       border-bottom: 1px solid #dfe6ec;
       padding-bottom: 10px;
       margin-bottom: 10px;
       font-weight: bold;
     }
   }
 }
</style>
