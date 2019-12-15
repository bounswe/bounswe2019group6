<template>
  <div class="app-container">
    <div style="text-align: right; padding-right: 60px;">
      <el-button @click="createArticle()" type="primary"><svg-icon style="margin-right:10px; display:inline-block;" icon-class="documentation" />Write Article</el-button>
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
    }
  },
  computed: {
  },
  created() {
  },
  methods: {
    createArticle(){
      this.$router.push({ path: `/write` })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
