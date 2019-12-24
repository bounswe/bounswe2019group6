<template>
  <div class="app-container">
    <div style="text-align: right; padding-right: 60px;">
      <el-button @click="createArticle()" type="primary"><svg-icon style="margin-right:10px; display:inline-block;" icon-class="documentation" />Write Article</el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table
        :data="tableData"
        :default-sort = "{prop: 'date', order: 'descending'}"
        stripe
        style="width: 100%">
        <el-table-column
          prop="id"
          label="ID"
          sortable
          width="60">
        </el-table-column>
        <el-table-column
          prop="title"
          label="Title"
          sortable>
        </el-table-column>
        <el-table-column
          prop="timestamp"
          label="Date"
          sortable>
        </el-table-column>
        <el-table-column fixed="right" width="120">
          <template slot-scope="scope">
            <el-button @click="HandleRedirect(tableData[scope.$index])" type="text" round>See Article</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>

export default {
  components: { },
  props: {
  },
  data() {
    return { 
      tableData: []
    }
  },
  computed: {
  },
  created() {
    this.$store.dispatch('search/getMyArticleByUserName', this.$store.getters.userInfo.username).then(() => {
      for(var i = 0; i < this.$store.getters.myArticles.length; i++){
        this.tableData.push({
          "timestamp" : this.$store.getters.myArticles[i].createdAt,
          "title" : this.$store.getters.myArticles[i].header,
          "id" : this.$store.getters.myArticles[i].id
        })
        
      } 
    }).catch(err => {
      console.log(err)
    })
  },
  methods: {
    createArticle(){
      this.$router.push({ path: `/write` })
    },
    HandleRedirect(article) {
      this.$router.push({ path: `/view/${article.id}` })
    },
  }
}
</script>

<style lang="scss" scoped>

</style>
