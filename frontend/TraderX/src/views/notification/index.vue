<template>
  <div class="app-container">
    <el-table :data="notificationData" stripe style="width: 100%; margin-top:40px">
      <el-table-column fixed="left" width="120">
        <template slot-scope="scope">
          <el-button @click.native.prevent="markAsRead(notificationData[scope.$index].id)" type="success" round>Read</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="message" label="Message"> </el-table-column>
      <el-table-column prop="code" label="Code" width="100"> </el-table-column>
      <el-table-column prop="amount" label="Amount" width="100"> </el-table-column>
      <el-table-column prop="limit" label="Limit" width="100"> </el-table-column>
      <el-table-column prop="alertType" label="Allert Type" width="100"> </el-table-column>
      <el-table-column prop="alertMessager" label="Allert Message" width="120"> </el-table-column>
      <el-table-column fixed="right" width="120">
        <template slot-scope="scope">
          <el-button-group v-if="notificationData[scope.$index].isThereAnswer">
            <el-button style="margin-top:10px" type="primary" @click="AcceptFollowRequest(notificationData[scope.$index].username, notificationData[scope.$index].id)">Accept</el-button>
            <el-button style="margin-top:10px" type="danger" @click="DeclineFollowRequest(notificationData[scope.$index].username, notificationData[scope.$index].id)">Decline</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'Notification',
  props: {},
  data() {
    return {
      notificationData: [],
    }
  },
  created() {
    this.getNotifications()
  },
  mounted() {},
  methods: {
    markAsRead(idx){
      console.log(idx)
      this.$store.dispatch('user/readNotificationByID', {id : idx}).then(() => {
        console.log("success")
      }).catch(error => {
        console.log(error)
      }) 
    },
    getNotifications(){
      this.$store.dispatch('user/getNewNotifications').then(() => {
        var res = this.$store.getters.notifications
        console.log(res)
        for(var i = 0; i < res.length; i++){
          if(res[i].type == 'FOLLOWED') {
            this.notificationData.push({
              "message": "You Started To Follow " + res[i].payload.username,
              "type": res[i].type,
              "id": res[i].id,
            })
          } else if(res[i].type == 'FOLLOW_REQUESTED') {
            this.notificationData.push({
              "message": "Follow Requst From " + res[i].payload.username,
              "type": res[i].type,
              "id": res[i].id,
              "isThereAnswer" : true,
              "username": res[i].payload.username,
            })
          } else if(res[i].type == 'FOLLOW_REQUEST_ACCEPTED') {
            this.notificationData.push({
              "message": "Your Follow Request To " + res[i].payload.username + " Is Accepted",
              "type": res[i].type,
              "id": res[i].id,
            })
          } else if(res[i].type == 'FOLLOW_REQUEST_DENIED') {
            this.notificationData.push({
              "message": "Your Follow Request To " + res[i].payload.username + " Is Denied",
              "type": res[i].type,
              "id": res[i].id,
            })
          } else if(res[i].type == 'ALERT_TRANSACTION_SUCCESS' || res[i].type == 'ALERT_TRANSACTION_FAIL' || res[i].type == 'ALERT_NOTIFY') {
            this.notificationData.push({
              "type": res[i].type,
              "id": res[i].id,
              "code": res[i].payload.code,
              "amont": res[i].payload.amount,
              "limit": res[i].payload.limit,
              "alertType": res[i].payload.type,
              "alertMessage": res[i].payload.message
            })
          }
        } 
        
      }).catch(error => {
        console.log(error)
      }) 
    },
    AcceptFollowRequest(name, idx){
      this.$store.dispatch('user/acceptFollowRequest',  {'username' : name}).then(() => {
        this.$message({ title: 'Success', message: 'Follow Request Is Accepted', type: 'success', duration: 2000 })
        this.markAsRead(idx)
      }).catch(error => {
        console.log(error)
      }) 
    },
    DeclineFollowRequest(name, idx){
      this.$store.dispatch('user/declineFollowRequest', {'username' : name}).then(() => {
        this.$message({ title: 'Success', message: 'Follow Request Is Declined', type: 'success', duration: 2000 }) 
        this.markAsRead(idx)
      }).catch(error => {
        console.log(error)
      }) 
    },
  }
}
</script>

<style lang="scss" scoped>

</style>
