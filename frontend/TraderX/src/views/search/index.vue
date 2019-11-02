<template>
  <div style="margin-top: 50px; margin-left: 100px; margin-right: 100px">
    <div style="padding-bottom: 30px">
      <el-input placeholder="Please input" v-model="searchText" class="input-with-select" ref="searchText" type="text" auto-complete="on" @keyup.native="liveSearch">
        <el-select style="width: 110px" v-model="selectedFilter" slot="prepend" placeholder="Search In">
          <el-option label="User" value="user"></el-option>
          <!-- <el-option label="Event" value="event"></el-option>
          <el-option label="Trading Equipment" value="label"></el-option> -->
        </el-select>
        <el-button slot="append" icon="el-icon-search" @click.native.prevent="handleSearch">Search</el-button>
      </el-input>
    </div>
    <el-table :data="searchResult" style="width: 100%">
      <el-table-column prop="name" label="Name"></el-table-column>
      <el-table-column prop="privacy" label="Privacy"></el-table-column>
      <el-table-column prop="role" label="Role"></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'search',
  props: {},
  data() {
    return {
      searchText : "",
      searchResult : [],
      selectedFilter: "user",
      
    }
  },
  created() {
    this.$store.dispatch('search/getAllUsers').then(() => {
      var res = this.$store.getters.userSearchResult
      var temp = []
      res.forEach(function (user) {
        var privacy = user.isPrivate ? "Private" : !user.isPrivate ? 'Public': "";
        temp.push({
          'name': user.username,
          'privacy' : privacy,
          'role' : user.roles[0]
        })
      });
      this.searchResult = Array.from(new Set(temp.concat(this.searchResult)))
    })
  },
  mounted() {

  },
  methods: {
    handleSearch() {
      if (this.searchText == '') {
        this.$store.dispatch('search/getAllUsers').then(() => {
          var res = this.$store.getters.userSearchResult
          var temp = []
          res.forEach(function (user) {
            var privacy = user.isPrivate ? "Private" : !user.isPrivate ? 'Public': "";
            temp.push({
              'name': user.username,
              'privacy' : privacy,
              'role' : user.roles[0]
            })
          });
          this.searchResult = Array.from(new Set(temp.concat(this.searchResult)))
        })  
      } else {
        this.$store.dispatch('search/searchUser', this.searchText).then(() => {
          var res = this.$store.getters.userSearchResult
          var temp = []
          res.forEach(function (user) {
            var privacy = user.isPrivate ? "Private" : !user.isPrivate ? 'Public': "";
            temp.push({
              'name': user.username,
              'privacy' : privacy,
              'role' : user.roles[0]
            })
          });
          this.searchResult = Array.from(new Set(temp.concat(this.searchResult)))
        })
      }
    },
    liveSearch() {
      var res = this.$store.getters.userSearchResult.filter(user => user.username.includes(this.searchText))
      var temp = []
      res.forEach(function (user) {
        var privacy = user.isPrivate ? "Private" : !user.isPrivate ? 'Public': "";
        temp.push({
          'name': user.username,
          'privacy' : privacy,
          'role' : user.roles[0]
        })
      });
      // deleteMultipleUsers(dupArr) {
      //   var uniqueArr = []
      //   dupArr.forEach(function (dupUser) {
      //     var exists = false
      //     uniqueArr.forEach(function (uniUser) {
      //       if(dupUser.username == uniUser.username){
      //         exists = true
      //       }
      //     });
      //     if (!exists){
      //       uniqueArr.push(dupUser)
      //     }
      //   });
      //   return uniqueArr
      // }
      this.searchResult = Array.from(new Set(temp.concat(this.searchResult)))
      console.log(this.searchResult)
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
