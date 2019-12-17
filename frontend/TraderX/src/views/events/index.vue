<template>
  <div>
    <div style="margin-top: 30px; margin-bottom: 50px; margin-left: 60px; margin-right: 60px">
      <el-table
        :data="eventData"
        :default-sort = "{prop: 'date', order: 'descending'}"
        stripe
        style="width: 100%">
        <el-table-column prop="event" label="Event" sortable> </el-table-column>
        <el-table-column prop="date" label="Date" sortable> </el-table-column>
        <el-table-column prop="country" label="Country" sortable> </el-table-column>

        <el-table-column prop="prev" label="Prev" width="80%"></el-table-column>
        <el-table-column prop="actual" label="Actual" width="80%"> </el-table-column>
        <el-table-column prop="forecast" label="Forecast" width="80%"> </el-table-column>


        <el-table-column label="Importance" sortable> 
          <template slot-scope="scope">
            <el-rate v-model="importanceValues[scope.$index]" :colors="colors" disabled> </el-rate> 
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Events',
  props: {},
  data() {
    return {
      eventData: [],
      importanceValues: [],
      colors: ['#99A9BF', '#F7BA2A', '#FF9900']
    }
  },
  created() {
    this.getEvents()
  },
  mounted() {
  },
  methods: {
    getEvents() {
      this.$store.dispatch('search/getEvents').then(() => {
        var res = this.$store.getters.allEvents
        for(var i = 0; i < res.length; i++){
          this.eventData.push({
            "event" : res[i].Event,
            "date" : res[i].Date,
            "country" : res[i].Country,
            "prev" : res[i].Previous,
            "actual" : res[i].Actual,
            "forecast" : res[i].Forecast
          })
          this.importanceValues.push(res[i].Importance)
        }
        
      }).catch(error => {
        console.log(error)
      }) 
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
