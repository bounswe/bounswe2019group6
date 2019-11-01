<template>
  <div>
    <el-form ref="searchForm" :model="searchForm" autocomplete="on" label-position="left">
      <el-form-item prop="searchText">
        <el-input ref="searchText" v-model="searchForm.searchText" placeholder="Search SMTGH" type="text" auto-complete="on" @keyup.native="liveSearch"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click.native.prevent="handleSearch">
          Search
        </el-button>
      </el-form-item>
    </el-form>

    <div>
      <li v-for="user in data" v-bind:key="user.username">
        {{ user.username }}
      </li>
    </div>

  </div>
</template>

<script>
export default {
  name: 'search',
  props: {},
  data() {
    return {
      searchForm: {
        searchText : ""
      },
      data : []
    }
  },
  created() {
    this.$store.dispatch('search/getAllUsers').then(() => {
      this.data = this.$store.getters.userSearchResult
    })
  },
  mounted() {

  },
  methods: {
    handleSearch() {
      if (this.searchForm.searchText == '') {
        this.$store.dispatch('search/getAllUsers').then(() => {
          this.data = this.$store.getters.userSearchResult
        })  
      } else {
        this.$store.dispatch('search/searchUser', this.searchForm.searchText).then(() => {
          this.data = [this.$store.getters.userSearchResult]
        })
      }
    },
    liveSearch() {
      this.data = this.$store.getters.userSearchResult.filter(user => user.username.includes(this.searchForm.searchText))
    }

  }
}
</script>

<style lang="scss" scoped>

</style>
