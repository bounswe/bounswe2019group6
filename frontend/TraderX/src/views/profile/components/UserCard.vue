<template>
  <el-card style="margin-bottom:20px;">
    <div slot="header" class="clearfix">
      <span>About me</span>
    </div>
    <div class="user-profile">
      <div class="box-center">
        <pan-thumb :image="'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'" :height="'100px'" :width="'100px'" :hoverable="false" />
      </div>
      <div class="box-center">
        <div class="user-name text-center">{{ user.name }}</div>
      </div>
    </div>
    <div class="user-bio">
      <div class="user-personal-info user-bio-section">
        <div class="user-bio-section-header"><svg-icon icon-class="user" /><span>Personal Info</span></div>
        <div class="user-bio-section-body">
          <div class="user-stats">
            <p><b>Follower:</b> 0</p>
            <p><b>Following:</b> 0</p>
            <p><b>Articles:</b> 0</p>
            <p><b>Comments:</b> 0</p>
          </div>
          <div class="user-type">
            <p><b>User Type:</b> {{ user.role }}</p>
          </div>
          <p><b>IBAN:</b> {{ user.iban }}</p>
          <p><b>Location:</b> Tokyo</p>
          <el-switch
            v-model="value1"
            active-color="#13ce66"
            inactive-color="#ff4949"
            style="float: left"
            active-text="Public"
            inactive-text="Private"
          />
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
import PanThumb from '@/components/PanThumb'

export default {
  components: { PanThumb },
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          name: '',
          email: '',
          avatar: '',
          roles: ''
        }
      }
    }
  },
  data() {
    return {
      value1: true
    }
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
