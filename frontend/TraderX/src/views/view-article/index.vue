<template>
  <div class="app-container">
    <div class="article-wrapper">
      <div style="width: 60%">
        <div class="title-wrapper">
          <p style="font-size: 40px"><b>{{ this.title.toUpperCase() }}</b></p>
        </div>
        <a @click="redirectToUser()" style="">
          <img src="https://www.sackettwaconia.com/wp-content/uploads/default-profile.png" class="user-avatar">
          <div class="author-wrapper" style="float: left">
            <p style="font-size: 20px"><b>{{ this.author }}</b></p>
          </div>
        </a>
        <div style="float: right">
          <p style="font-size: 20px">Publish Time: <b>{{ this.date }}</b></p>
        </div>
        <div>
          <img style="padding-top:30px;width: 100%; height: 100%;" src="https://i.sozcu.com.tr/wp-content/uploads/2019/12/09/iecrop/sis-bursa-sis-iha1_10228788_16_9_1575927508-880x495.jpg" alt="">
        </div>
        <div class="body-wrapper" style="float: right; padding-top: 30px; padding-bottom: 30px">
          <p style="font-size: 21px">{{ this.body }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import PanThumb from '@/components/PanThumb'

export default {
  components: { PanThumb },
  name: 'ViewArticle',
  props: {},
  data() {
    return {
      date: "10.10.2020",
      title : "This is the title",
      author: "enozcan",
      body : "This is the body text, i don't know what to write, my hands are writing words, asdasdasdasds, haaaaaaandssssss"
    }
  },
  created() {},
  mounted() {},
  methods: {
    redirectToUser() {
      this.$router.push({ path: `/user/${this.author}/profile` })
    }
  }
}
</script>

<style lang="scss" scoped>

.article-wrapper {
  text-align: -webkit-center;
}

.user-avatar {
  height: 60px;
  width: 60px;
  float: left;
  margin-right: 10px;
  border-radius: 50%;
}


</style>
