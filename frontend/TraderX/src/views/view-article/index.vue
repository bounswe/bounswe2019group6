<template>
  <div class="app-container">
    <div class="article-wrapper">
      <div style="width: 60%">
        <div class="title-wrapper">
          <p style="font-size: 40px"><b>{{ this.title.toUpperCase() }}</b></p>
        </div>

        <a @click="redirectToUser()" style="">
          <img draggable="false" src="https://www.sackettwaconia.com/wp-content/uploads/default-profile.png" class="user-avatar">
          <div class="author-wrapper" style="float: left">
            <p style="font-size: 20px"><b>{{ this.author }}</b></p>
          </div>
        </a>

        <div id="annotation">
          <!-- <button @click="drawing = !drawing">{{drawing ? "stop" : "drawing" }}</button> -->
          
        </div>
          
        
        <div style="float: right">
          <p style="font-size: 20px">Publish Time: <b>{{ this.date }}</b></p>
        </div>
        <div>
          <img id="articleImage" class="annotatable" :src="articleImageUrl">
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
import annotator from 'annotator'
import VAnnotator from 'vue-annotator'
import 'annotorious'

export default {
  components: { 
    PanThumb,
    VAnnotator
  },
  name: 'ViewArticle',
  props: {},
  data() {
    return {
      date: "10.10.2020",
      title : "This is the title",
      author: "enozcan",
      body : "This is the body text, i don't know what to write, my hands are writing words, asdasdasdasds, haaaaaaandssssss",
      articleImageUrl : "https://i.sozcu.com.tr/wp-content/uploads/2019/12/09/iecrop/sis-bursa-sis-iha1_10228788_16_9_1575927508-880x495.jpg"
    }
  },
  created() {
    // Text annotation mock
    var anotator_app = new annotator.App();
    anotator_app.include(annotator.ui.main);
    anotator_app
    .start()
    .then(function () {
        anotator_app.annotations.load();
    });
  },
  mounted() {
    anno.makeAnnotatable(document.getElementById('articleImage'));
    this.createMockAnnotation()
  },
  methods: {
    redirectToUser() {
      this.$router.push({ path: `/user/${this.author}/profile` })
    },
    // Create a temp annotation
    createMockAnnotation() {
      var myAnnotation = {
          /** The URL of the image where the annotation should go **/
          src : this.articleImageUrl,

          /** The annotation text **/
          text : 'My annotation',

          /** The annotation shape **/
          shapes : [{
              /** The shape type **/
              type : 'rect',
              /** The shape geometry (relative coordinates) **/
              geometry : {
                height: 0.30707070707070705,
                width: 0.1318181818181818,
                x: 0.48863636363636365,
                y: 0.044444444444444446,
              }
          }]
      }

      anno.addAnnotation(myAnnotation)

    },



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

#annotatable {
  
}


</style>
