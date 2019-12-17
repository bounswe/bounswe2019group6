<template>
  <div class="app-container">
    <div style="margin-top: 30px; margin-bottom: 50px; margin-left: 60px; margin-right: 60px">
      <div style="padding-bottom: 30px">
        <el-input placeholder="Please input" v-model="searchText" class="input-with-select" ref="searchText" type="text" auto-complete="on" @keyup.native="handleSearch">
          <el-select @change="updateFilterType()" style="width: 170px" v-model="selectedFilter" slot="prepend" placeholder="Search In">
            <el-option label="User" value="user"></el-option>
            <el-option label="Trading Equipment" value="equipment"></el-option>
            <el-option label="Article" value="article"></el-option> 
            <el-option label="Event" value="event"></el-option> 
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
          <el-table-column fixed="right" width="120">
            <template slot-scope="scope">
              <el-button @click.native.prevent="showTradingEquipment(searchEquipmentResultToShow[scope.$index])" type="text" round>Details</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="article" :style=articleVisibility>
        <el-table ref="articleTable" :data="searchArticleResultToShow" style="width: 100%">
          <el-table-column prop="id" label="ID">
            <template slot-scope="scope">{{ scope.row.id }}</template>
          </el-table-column>
          <el-table-column prop="title" label="Title">
            <template slot-scope="scope">{{ scope.row.title }}</template>
          </el-table-column>
          <el-table-column prop="author" label="Author">
            <template slot-scope="scope">{{ scope.row.author }}</template>
          </el-table-column>
          <el-table-column prop="time" label="Time">
            <template slot-scope="scope">{{ scope.row.timestamp }}</template>
          </el-table-column>
          <el-table-column fixed="right" width="120">
            <template slot-scope="scope">
              <el-button @click.native.prevent="showArticle(searchArticleResultToShow[scope.$index])" type="text" round>See Article</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="article" :style=eventVisibility>
        <el-table ref="articleTable" :data="searchEventResultToShow" style="width: 100%">
          <el-table-column prop="event" label="Event">
            <template slot-scope="scope">{{ scope.row.event }}</template>
          </el-table-column>
          <el-table-column prop="date" label="Date">
            <template slot-scope="scope">{{ scope.row.date }}</template>
          </el-table-column>
          <el-table-column prop="country" label="Country">
            <template slot-scope="scope">{{ scope.row.country }}</template>
          </el-table-column>
          <el-table-column prop="prev" label="Prev" width="80%">
            <template slot-scope="scope">{{ scope.row.prev }}</template>
          </el-table-column>
          <el-table-column prop="actual" label="Actual" width="80%">
            <template slot-scope="scope">{{ scope.row.actual }}</template>
          </el-table-column>
          <el-table-column prop="forecast" label="Forecast" width="80%">
            <template slot-scope="scope">{{ scope.row.forecast }}</template>
          </el-table-column>
          <el-table-column fixed="right" width="120">
            <template slot-scope="scope">
              <el-rate v-model="importanceValues[scope.$index]" :colors="colors" disabled> </el-rate> 
            </template>
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
      all_currency_codes: ['JPY','EUR','TRY','CZK','GBP','CNY'],
      all_stock_codes: ['AMZN','BABA','MSFT','ORCL','SILK','ENSG'],
      all_crypto_codes: ['BTC','ETC','VIB','ETH','BTG','ZEN'],
      searchEquipmentResult: [],
      searchEquipmentResultToShow: [],
      searchArticleResult: [],
      searchArticleResultToShow: [],
      searchEventResult: [],
      searchEventResultToShow: [],
      colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
      importanceValues: [],
      selectedFilter: "user",
      userVisibility: "display: block",
      equipmentVisibility: "display: none",
      articleVisibility: "display: none",
      eventVisibility: "display: none",
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
    this.getAllEvents()
    this.getAllArticles()
    this.getCurrencyList()
    this.getCryptoList()
    this.getStocksList()
  },
  methods: {
    getAllEvents(){
      this.$store.dispatch('search/getEvents').then(() => {
        var res = this.$store.getters.allEvents
        console.log(res)
        for(var i = 0; i < res.length; i++){
          this.searchEventResult.push({
            "event" : res[i].Event,
            "date" : res[i].Date,
            "country" : res[i].Country,
            "prev" : res[i].Previous,
            "actual" : res[i].Actual,
            "forecast" : res[i].Forecast,
          })
          this.searchEventResultToShow.push({
            "event" : res[i].Event,
            "date" : res[i].Date,
            "country" : res[i].Country,
            "prev" : res[i].Previous,
            "actual" : res[i].Actual,
            "forecast" : res[i].Forecast,
          })
          this.importanceValues.push(res[i].Importance)
        }
      }).catch(error => {
        console.log(error)
      }) 
    },
    showArticle(article) {
      this.$router.push({ path: `/view/${article.id}` })
    },
    getAllArticles(){
      this.$store.dispatch('search/getAllArticles').then(() => {
        for(var i = 0; i < this.$store.getters.articleSearchResult.length; i++){
          this.searchArticleResult.push({
            "author" : this.$store.getters.articleSearchResult[i].username,
            "timestamp" : this.$store.getters.articleSearchResult[i].createdAt,
            "title" : this.$store.getters.articleSearchResult[i].header,
            "id" : this.$store.getters.articleSearchResult[i].id
          })
          this.searchArticleResultToShow.push({
            "author" : this.$store.getters.articleSearchResult[i].username,
            "timestamp" : this.$store.getters.articleSearchResult[i].createdAt,
            "title" : this.$store.getters.articleSearchResult[i].header,
            "id" : this.$store.getters.articleSearchResult[i].id
          })
        } 
      }).catch(err => {
        console.log(err)
      })
    },
    showTradingEquipment(equip) {
      var e_type = this.all_currency_codes.includes(equip.equipmentName) ? "Currency" : 
                     this.all_crypto_codes.includes(equip.equipmentName) ? "Crypto Currency" : "Stock"
      if (e_type == "Currency") {
        this.$router.push('/trading-equipment/money-currencies')
      } else if (e_type == "Crypto Currency") {
        this.$router.push('/trading-equipment/cryptocurrencies')
      } else if (e_type == "Stock") {
        this.$router.push('/trading-equipment/stocks')
      }
    },
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
        this.articleVisibility = "display: none"
        this.userVisibility = "display: block"
        this.eventVisibility = "display: none"
      } else if (this.selectedFilter == "equipment") {
        this.equipmentVisibility = "display: block"
        this.userVisibility = "display: none"
        this.articleVisibility = "display: none"
        this.eventVisibility = "display: none"
      } else if (this.selectedFilter == "article") {
        this.equipmentVisibility = "display: none"
        this.userVisibility = "display: none"
        this.articleVisibility = "display: block"
        this.eventVisibility = "display: none"
      } else if (this.selectedFilter == "event") {
        this.equipmentVisibility = "display: none"
        this.userVisibility = "display: none"
        this.articleVisibility = "display: none"
        this.eventVisibility = "display: block"
      }
    },
    showUserProfile(user) {
      this.$router.push({ path: `/user/${user.name}/profile` })
    },
    takeFollowUnfollowAction(user) {
      if(user.isNotFollowing){
        this.$store.dispatch('user/followUser', {'username' : user.name}).then(() => {
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
        }).catch(err => {
          console.log(err)
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
      } else if (this.selectedFilter == "article"){
        this.searchArticleResultToShow = this.searchArticleResult.filter(article => article.title.toUpperCase().includes(this.searchText.toUpperCase()))
      } else if (this.selectedFilter == "event"){
        this.searchEventResultToShow = this.searchEventResult.filter(event => event.event.toUpperCase().includes(this.searchText.toUpperCase()))
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
