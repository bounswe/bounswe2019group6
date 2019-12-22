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

        <div style="float: right">
          <p style="font-size: 20px">Publish Time: <b>{{ this.date }}</b></p>
        </div>
        <div>
          <img id="articleImage" class="annotatable" :src="articleImageUrl">
        </div>
        <div class="body-wrapper" style="float: right; padding-top: 30px; padding-bottom: 30px">
          <p id="articleText" class="articleText" style="font-size: 21px">{{ this.body }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import annotator from 'annotator'
import 'annotorious'
import { getAnnotationList, createAnnotation, updateAnnotation, deleteAnnotation } from '@/api/annotation'

export default {
  components: { },
  name: 'ViewArticle',
  props: {},
  data() {
    return {
      date: "10.10.2020",
      title : "This is the title",
      author: "enozcan",
      body : "This is the body text, i don't know what to write, my hands are writing words, asdasdasdasds, haaaaaaandssssss",
      articleImageUrl : "https://i.sozcu.com.tr/wp-content/uploads/2019/12/09/iecrop/sis-bursa-sis-iha1_10228788_16_9_1575927508-880x495.jpg",
      imageWidth: "",
      imageHeight: "",
      annotator_app: {}
    }
  },
  created() {
  },
  mounted() {
    // Text annotation mock
    this.annotator_app = new annotator.App();
    this.annotator_app.include(annotator.ui.main, {
      element: document.querySelector(".articleText")
    });
    this.annotator_app.include(this.textAnnotatorModule)
    this.annotator_app.start()

    var articleImage = document.getElementById('articleImage');
    this.imageWidth = articleImage.clientWidth;
    this.imageHeight = articleImage.clientHeight;

    anno.makeAnnotatable(articleImage);
    // this.addAnnotationToImage()
    var that = this
    anno.addHandler('onAnnotationCreated', function(annotation){
      that.createImageAnnotationHandler(annotation)
    })
    anno.addHandler('onAnnotationUpdated', function(annotation) {
      that.updateImageAnnotationHandler(annotation)
    })
    anno.addHandler('onAnnotationRemoved', function(annotation) {
      that.deleteImageAnnotationHandler(annotation)
    })
    this.getAnnotationList()
  },
  methods: {
    redirectToUser() {
      this.$router.push({ path: `/user/${this.author}/profile` })
    },
    addAnnotationToImage(backendAnnotation) {
      var newAnnotation = {
        src : this.articleImageUrl,
        text : backendAnnotation.body.value,
        shapes : [{
          type : 'rect',
          geometry : this.renderGeometry(backendAnnotation.target.id)
        }],
        id : backendAnnotation.id
      }
      anno.addAnnotation(newAnnotation)
    },

    renderGeometry(geometryStr) {
      var geometryValStr = geometryStr.split('=')[1]
      var geometryArr = geometryValStr.split(',')
      console.log(geometryArr)
      return {
        x: parseFloat(geometryArr[0]) / this.imageWidth,
        y: parseFloat(geometryArr[1]) / this.imageHeight,
        width: parseFloat(geometryArr[2]) / this.imageWidth,
        height: parseFloat(geometryArr[3]) / this.imageHeight
      }
    },

    createImageAnnotationHandler(annotation) {
      var newAnnotation = {
        "articleId" : parseInt(this.$route.params.articleid),
        "content" : annotation.text,
        "bodyType": "Text",
        "targetType": "Image",
        "imgX": this.imageWidth * annotation.shapes[0].geometry.x,
        "imgY": this.imageHeight * annotation.shapes[0].geometry.y,
        "imgW": this.imageWidth * annotation.shapes[0].geometry.width,
        "imgH": this.imageHeight * annotation.shapes[0].geometry.height
      }
      createAnnotation(newAnnotation).then(response => {
          annotation.id = response.data.id
      }).catch(error => {
          console.log(error)
      })
    },

    updateImageAnnotationHandler(annotation) {
      var updatedAnnotation = {
        "articleId" : parseInt(this.$route.params.articleid),
        "content" : annotation.text,
        "id" : annotation.id,
        "bodyType": "Text",
        "targetType": "Image",
        "imgX": this.imageWidth * annotation.shapes[0].geometry.x,
        "imgY": this.imageHeight * annotation.shapes[0].geometry.y,
        "imgW": this.imageWidth * annotation.shapes[0].geometry.width,
        "imgH": this.imageHeight * annotation.shapes[0].geometry.height
      }
      updateAnnotation(updatedAnnotation).then(response => {
          console.log(response)
      }).catch(error => {
          console.log(error)
      })
    },

    deleteImageAnnotationHandler(annotation) {
      deleteAnnotation({ "id" : annotation.id }).then(response => {
          console.log(response)
      }).catch(error => {
          console.log(error)
      })
    },

    getAnnotationList() {
      var articleId = parseInt(this.$route.params.articleid)
      getAnnotationList(articleId).then(response => {
          console.log(response)
          var that = this
          response.data.forEach((backendAnnotation) => {
            if (backendAnnotation.targetType === "Image") {
              that.addAnnotationToImage(backendAnnotation)
            } else {
              that.addAnnotationToText(backendAnnotation)
            }
          })
      }).catch(error => {
          console.log(error)
      })
    },

    textAnnotatorModule() {
      var that = this
      return {
        annotationCreated: function (annotation) {
          that.createTextAnnotationHandler(annotation)
        },
        annotationUpdated: function (annotation) {
          that.updateTextAnnotationHandler(annotation)
        },
        annotationDeleted: function (annotation) {
          that.deleteTextAnnotationHandler(annotation)
        }
      }
    },

    addAnnotationToText(backendAnnotation) {
      var newAnnotation = {
        "id": backendAnnotation.id,
        "text" : backendAnnotation.body.value,
        "ranges": [
          {
            "startOffset": backendAnnotation.posStart,
            "endOffset": backendAnnotation.posEnd
          }
        ]
      }

      // TODO load newAnnotation to Text
    },

    createTextAnnotationHandler(annotation) {
      var newAnnotation = {
        "articleId" : parseInt(this.$route.params.articleid),
        "content" : annotation.text,
        "bodyType": "Text",
        "targetType": "Text",
        "posStart": annotation.ranges[0].startOffset,
        "posEnd": annotation.ranges[0].endOffset
      }

      createAnnotation(newAnnotation).then(response => {
          annotation.id = response.data.id
      }).catch(error => {
          console.log(error)
      })
    },

    updateTextAnnotationHandler(annotation) {
      var updatedAnnotation = {
          "articleId" : parseInt(this.$route.params.articleid),
          "content" : annotation.text,
          "id" : annotation.id,
          "bodyType": "Text",
          "targetType": "Text",
          "posStart": annotation.ranges[0].startOffset,
          "posEnd": annotation.ranges[0].endOffset
      }

      updateAnnotation(updatedAnnotation).then(response => {
          console.log(response)
      }).catch(error => {
          console.log(error)
      })
    },

    deleteTextAnnotationHandler(annotation) {
      deleteAnnotation({ "id" : annotation.id }).then(response => {
          console.log(response)
      }).catch(error => {
          console.log(error)
      })
    }
  }
}
</script>

<style lang="scss" scoped>

.article-wrapper {
  text-align: -webkit-center;
  text-align: -moz-center;
}

.user-avatar {
  height: 60px;
  width: 60px;
  float: left;
  margin-right: 10px;
  border-radius: 50%;
}

</style>
