<template>
  <div />
</template>

<script>
export default {
  name: 'Events',
  props: {},
  data() {
    return {}
  },
  created() {},
  mounted() {
  },
  methods: {
    getEvents() {
      this.$store.dispatch('search/getEvents').then(() => {
        var res = this.$store.getters.allEvents
        console.log(res)  
        
      }).catch(error => {
        console.log(error)
      }) 
    },}
}
</script>

<style lang="scss" scoped>

</style>
