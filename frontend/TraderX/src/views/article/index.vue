<template>
  <div class="app-container">
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
            <el-button @click.native.prevent="HandleRedirect(tableData[scope.$index])" type="text" round>See Article</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Article',
    data() {
      return {
        tableData: [{
          author: 'aTom',
          timestamp: '2016-05-04',
          title: 'Tittttttleeeeee',
          id: '1'
        },{
          author: 'zTom',
          timestamp: '2016-05-03',
          title: 'aaaaattleeeeee',
          id: '2'
        },{
          author: 'Tom',
          timestamp: '2016-05-02',
          title: 'zzzzzTittttttleeeeee',
          id: '3'
        },{
          author: 'yTom',
          timestamp: '2016-05-01',
          title: 'yyyyTittttttleeeeee',
          id: '4'
        }]
      }
    },
    methods: {
      HandleRedirect(aa) {
        console.log(aa.id)
      }
    }
  }
</script>