<template>
  <el-table
    :data="tableData"
    :default-sort = "{prop: 'date', order: 'descending'}"
    border
    stripe
    style="width: 100%">
    <el-table-column
      prop="id"
      label="ID"
      sortable>
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
  </el-table>
</template>

<script>
  export default {
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
      formatter(row, column) {
        return row.address;
      }
    }
  }
</script>