<template>
  <div class="app-container">
    <div style="margin-top: 30px; margin-bottom: 50px; margin-left: 60px; margin-right: 60px">
      <div style="padding-bottom: 30px">
        <el-input placeholder="Please input" v-model="searchText" class="input-with-select" ref="searchText" type="text" auto-complete="on" @keyup.native="handleSearch">
          <el-select @change="updateFilterType()" style="width: 170px" v-model="selectedFilter" slot="prepend" placeholder="Search In">
            <el-option label="User" value="user"></el-option>
            <el-option label="Trading Equipment" value="equipment"></el-option>
            <!-- <el-option label="Event" value="event"></el-option>  -->
          </el-select>
          <el-button slot="append" icon="el-icon-search" @click.native.prevent="handleSearch">Search</el-button>
        </el-input>
      </div>
      <div class="user" :style=userVisibility>
        <el-table :data="searchResult" style="width: 100%">
          <el-table-column prop="name" label="Name"></el-table-column>
          <el-table-column prop="privacy" label="Privacy"></el-table-column>
          <el-table-column prop="role" label="Role"></el-table-column>
          <el-table-column prop="followinfo">
            <template slot-scope="scope" >
              <el-button 
                @click.native.prevent="takeFollowUnfollowAction(searchResult[scope.$index])" 
                :type="scope.row.isNotFollowing ? 'primary' : scope.row.isFollowing ? 'danger' : 'warning'" 
                style="float:right" plain>{{ scope.row.followText }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column fixed="right" width="120">
            <template slot-scope="scope">
              <el-button @click.native.prevent="showUserProfile(searchResult[scope.$index])" type="text" round>See Profile</el-button>
            </template>
          </el-table-column>
        </el-table>  
      </div>
      <div class="equipment" :style=equipmentVisibility>
        <el-table ref="tradingEquipmentTable" :data="searchEquipmentResultToShow" style="width: 100%">
          <el-table-column prop="name" label="Equipment Name">
            <template slot-scope="scope">{{ scope.row.equipmentName }}</template>
          </el-table-column>
          <el-table-column prop="value" label="Current Value">
            <template slot-scope="scope">{{ scope.row.currentValue }}</template>
          </el-table-column>
          <el-table-column prop="stock" label="Current Stock">
            <template slot-scope="scope">{{ scope.row.currentStock }}</template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>

import { deleteMultipleUsers } from '@/utils'
import { followUser, unfollowUser } from '@/api/user'
import { getAllCurrencies, getAllCryptoCurrencies, getAllStocks } from '@/api/equipment'

export default {
  name: 'search',
  props: {},
  data() {
    return {
      searchText : "",
      searchResult : [{
        isFollowing : '',
        isNotFollowing : '',
        isPending : '',
        followText : ''
      }],
      searchEquipmentResult: [],
      searchEquipmentResultToShow: [],
      selectedFilter: "user",
      userVisibility: "display: block",
      equipmentVisibility: "display: none"
    }
  },
  created() {
    var currentUserName = this.$store.state.user.name
    this.$store.dispatch('search/getAllUsers').then(() => {
      var res = this.$store.getters.userSearchResult
      var temp = []
      res.forEach(function (user) {
        var followText = user.followingStatus == 'NOT_FOLLOWING' ? "Follow" : user.followingStatus == 'FOLLOWING' ? "Unfollow" : 'Requested'
        var isFollowing = user.followingStatus == 'FOLLOWING' ? true : false
        var isNotFollowing = user.followingStatus == 'NOT_FOLLOWING' ? true : false
        var isPending = user.followingStatus == 'PENDING' ? true : false
        if (currentUserName != user.username) {
          temp.push({
            name: user.username,
            privacy : user.isPrivate ? "Private" : 'Public',
            role : user.roles[0] == "ROLE_TRADER" ? "Trader" : "Basic",
            followText : followText,
            isFollowing : isFollowing,
            isNotFollowing : isNotFollowing,
            isPending : isPending,
          })
        }
      });
      this.searchResult = temp
    })
    this.getCurrencyList()
    this.getCryptoList()
    this.getStocksList()
  },
  mounted() {

  },
  methods: {
    getCurrencyList() {
      this.$store.dispatch('equipment/getAllCurrencies').then(() => {
        var res = this.$store.getters.currencyResult
        for (var i = 0; i < res.equipments.length; i++) {
          this.searchEquipmentResult.push({
            "equipmentName" : res.equipments[i].code,
            "currentValue": res.equipments[i].data.currentValue, 
            "currentStock": res.equipments[i].data.currentStock,
          })
          this.searchEquipmentResultToShow.push({
            "equipmentName" : res.equipments[i].code,
            "currentValue": res.equipments[i].data.currentValue, 
            "currentStock": res.equipments[i].data.currentStock,
          })
        }
        console.log(res)
      }).catch(error => {
        console.log(error)
      }) 
    },
    getCryptoList() {
      this.$store.dispatch('equipment/getAllCryptoCurrencies').then(() => {
        var res = this.$store.getters.cryptoCurrencyResult
        for (var i = 0; i < res.equipments.length; i++) {
          this.searchEquipmentResult.push({
            "equipmentName" : res.equipments[i].code,
            "currentValue": res.equipments[i].data.currentValue, 
            "currentStock": res.equipments[i].data.currentStock,
          })
          this.searchEquipmentResultToShow.push({
            "equipmentName" : res.equipments[i].code,
            "currentValue": res.equipments[i].data.currentValue, 
            "currentStock": res.equipments[i].data.currentStock,
          })
        }
      }).catch(error => {
        console.log(error)
      })  
    },
    getStocksList() {
      this.$store.dispatch('equipment/getAllStocks').then(() => {
        var res = this.$store.getters.stockResult
        for (var i = 0; i < res.equipments.length; i++) {
          this.searchEquipmentResult.push({
            "equipmentName" : res.equipments[i].code,
            "currentValue": res.equipments[i].data.currentValue, 
            "currentStock": res.equipments[i].data.currentStock,
          })
          this.searchEquipmentResultToShow.push({
            "equipmentName" : res.equipments[i].code,
            "currentValue": res.equipments[i].data.currentValue, 
            "currentStock": res.equipments[i].data.currentStock,
          })
        }
      }).catch(error => {
        console.log(error)
      })
    },
    updateFilterType() {
      if (this.selectedFilter == "user") {
        this.equipmentVisibility = "display: none"
        this.userVisibility = "display: block"
        console.log(this.equipmentVisibility)
      } else if (this.selectedFilter == "equipment") {
        this.equipmentVisibility = "display: block"
        this.userVisibility = "display: none"
        console.log(this.equipmentVisibility)
      }
    },
    showUserProfile(user) {
      this.$router.push({ path: `/user/${user.name}/profile` })
    },
    takeFollowUnfollowAction(user) {
      if(user.isNotFollowing){
        this.$store.dispatch('user/followUser', {'username' : user.name}).then(response => {
          user.isNotFollowing = false
          if (user.privacy == 'Private'){
            this.$message.success('Follow Request Is Sent')
            user.isPending = true
            user.isFollowing = false
          } else {
            this.$message.success('Follow User Success')
            user.isFollowing = true  
            user.isPending = false
          }
          user.followText = user.isNotFollowing ? "Follow" : user.isFollowing ? "Unfollow" : 'Requested'
        }).catch(err => {
          console.log(err)
        })
      } else {
        var tempIsPending = user.isPending
        this.$store.dispatch('user/unfollowUser', {'username' : user.name}).then(() => {
          user.isNotFollowing = true
          if (tempIsPending){
            this.$message.success('Follow Request Is Deleted')
            user.isPending = false
          } else {
            this.$message.success('Unfollowed User Success')
            user.isFollowing = false
          }
          user.followText = user.isNotFollowing ? "Follow" : user.isFollowing ? "Unfollow" : 'Requested'
        }).catch(() => {
          console.log("errorrr in unfollowing user")
        })
      }
    },
    handleSearch() {
      if (this.selectedFilter == "user") {
        this.$store.dispatch('search/getAllUsers').then(() => {
          if (this.searchText == ''){
            var res = this.$store.getters.userSearchResult
          } else {
            var res = this.$store.getters.userSearchResult.filter(user => user.username.includes(this.searchText))
          }
          var temp = []
          
          res.forEach(function (user) {
            var followText = user.followingStatus == 'NOT_FOLLOWING' ? "Follow" : user.followingStatus == 'FOLLOWING' ? "Unfollow" : 'Requested'
            var isFollowing = user.followingStatus == 'FOLLOWING' ? true : false
            var isNotFollowing = user.followingStatus == 'NOT_FOLLOWING' ? true : false
            var isPending = user.followingStatus == 'PENDING' ? true : false
            temp.push({
              'name': user.username,
              'privacy' : user.isPrivate ? "Private" : 'Public',
              'role' : user.roles[0] == "ROLE_TRADER" ? "Trader" : "Basic",
              'followText' : followText,
              'isFollowing' : isFollowing,
              'isNotFollowing' : isNotFollowing,
              'isPending' : isPending,
  
            })
          });
          this.searchResult = temp
        })
      } else if (this.selectedFilter == "equipment"){
        this.searchEquipmentResultToShow = this.searchEquipmentResult.filter(equipment => equipment.equipmentName.includes(this.searchText.toUpperCase()))
      }
    } 
  } 
}
</script>

<style lang="scss" scoped>
  .el-select .el-input {
    width: 110px;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
</style>
