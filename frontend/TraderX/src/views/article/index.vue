<template>
  <div class="app-container">
    <div style="text-align: right; padding-right: 60px; padding-top: 20px;">
      <el-button @click="createArticle()" type="primary"><svg-icon style="margin-right:10px; display:inline-block;" icon-class="documentation" />Write Article</el-button>
    </div>
    <div style="margin-top: 30px; margin-bottom: 50px; margin-left: 60px; margin-right: 60px">
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
          prop="author"
          label="Author"
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

import { writeArticle } from '@/api/search'

export default {
  name: 'Article',
  data() {
    return {
      tableData: []
    }
  },
  created() {
    this.getAllArticles()
  },
  methods: {
    HandleRedirect(article) {
      this.$router.push({ path: `/view/${article.id}` })
    },
    getAllArticles(){
      this.$store.dispatch('search/getAllArticles').then(() => {
        for(var i = 0; i < this.$store.getters.articleSearchResult.length; i++){
          this.tableData.push({
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
    createArticle(){
      this.$store.dispatch('search/writeArticle', { header: "aaaa", body: "bbbbal", tags: ["t1", "t2"] }).then(() => {
        console.log("done")
      }).catch(err => {
        console.log(err)
      })
    }
  }
}
</script>