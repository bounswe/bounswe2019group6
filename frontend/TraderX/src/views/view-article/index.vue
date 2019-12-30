<template>
  <div class="app-container">
    <el-row class="article-wrapper">
      <el-card >
        <div style="width: 60%">
          <div style="width: 60%" class="title-wrapper">
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
            <p style="font-size: 21px">{{ this.body }}</p>
          </div>
        </div>
      </el-card>
    </el-row>
    <el-row class='comment-container' style="margin-top:20px;">
      <el-card>
        <h4> Comments about this article </h4>
        <el-button class='create-comment' style="margin-top:20px;margin-bottom:20px;" @click="showCreateArticleCommentDialog=true">
          <svg-icon style="margin-right:10px" icon-class="edit" /> Write Comment </el-button>
        
        <el-row class='row' v-for="c in articleCommentList" :key="c.id" :gutter="20" style="padding:16px 16px 0;margin-bottom:20px;">
          <el-card class='comment-container'>
            <p>
              <span class="comment-author"> {{ c.author }} </span>
              <span class="comment-time"> {{ c.lastModifiedTime | parseTime('{y}-{m}-{d} {h}:{i}') }} - </span>
              <span class="comment-options"> {{ c.likes }} Likes - </span>
              <span class="comment-options"> {{ c.dislikes }} Dislikes </span>
            </p>
            <p class="comment-text"> {{ c.comment }} </p>
            <p class="comment-options">
              <a :class="{liked: c.status === 'LIKED'}" @click="likeComment(c.id)"><i class="el-icon-arrow-up"/> Like </a> 
              <a :class="{disliked: c.status === 'DISLIKED'}" @click="dislikeComment(c.id)"><i class="el-icon-arrow-down"/> Dislike </a> |
              <a @click="revokeComment(c.id)"> Revoke Vote </a> |
              <a class="delete-text" @click="deleteArticleComment(c.id)"><i class="el-icon-delete"/> Delete Comment </a> |
              <a class="delete-text" @click="showEditArticleCommentDialog=true;editArticleCommentContent=c.comment"><i class="el-icon-edit"/> Edit Comment </a>

              <el-dialog title="Edit Comment" :visible.sync="showEditArticleCommentDialog">
                <textarea class="comment-textarea" column="100" rows="10" v-model="editArticleCommentContent"></textarea>
                <div>
                  <el-button style="margin-top:10px;" @click="editArticleComment(c.id)"><svg-icon icon-class="edit"/> Edit Comment </el-button>
                </div>
              </el-dialog>

            </p>
          </el-card>
        </el-row>

        <el-dialog title="Create Comment" :visible.sync="showCreateArticleCommentDialog">
          <textarea class="comment-textarea" placeholder="Write your comment here" column="100" rows="10" v-model="createArticleCommentContent"></textarea>
          <div>
            <el-button style="margin-top:10px;" @click="postArticleComment()"><svg-icon icon-class="edit"/> Publish Comment </el-button>
          </div>
        </el-dialog>

      </el-card>
    </el-row>
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
      date: "",
      title : "",
      author: "",
      body : "",
      articleImageUrl: "",
      imageWidth: "",
      imageHeight: "",
      articleCommentList: [],
      postArticleCommentDict: {
        "comment": "",
      },
      editArticleCommentDict: {
        "comment": "",
      },
      showCreateArticleCommentDialog: false,
      showEditArticleCommentDialog: false,
      createArticleCommentContent: '',
      editArticleCommentContent: '',
    }
  },
  created() {
    this.getArticleCommentList()
    this.getArticleInfo()
  },
  mounted() {
    this.waitForImageUrl()
  },
  methods: {
    waitForImageUrl() {
      if(this.articleImageUrl !== ""){
        console.log('imageUrl in wait: ' + this.articleImageUrl)
        this.setupAnnotation()
      }
      else{
        console.log('imageUrl is undefined: ' + this.articleImageUrl)
        setTimeout(this.waitForImageUrl, 250);
      }
    },

    setupAnnotation() {
      console.log('this.articleImageUrl: ' + this.articleImageUrl)
      var articleImage = document.getElementById('articleImage'); 
      this.imageWidth = articleImage.clientWidth;
      this.imageHeight = articleImage.clientHeight;

      anno.makeAnnotatable(articleImage);
      var that = this
      anno.addHandler('onAnnotationCreated', function(annotation){
        that.createAnnotationHandler(annotation)
      })
      anno.addHandler('onAnnotationUpdated', function(annotation) {
        that.updateAnnotationHandler(annotation)
      })
      anno.addHandler('onAnnotationRemoved', function(annotation) {
        that.deleteAnnotationHandler(annotation)
      })
      this.getAnnotationList()
    },

    getArticleInfo(){
      var id = this.$route.path.split('/')[2]
      this.$store.dispatch('search/getArticleByID', id ).then(() => {
        this.date = this.$store.getters.oneArticle.createdAt
        this.author = this.$store.getters.oneArticle.username
        this.title = this.$store.getters.oneArticle.header
        this.body = this.$store.getters.oneArticle.body
        this.articleImageUrl = this.$store.getters.oneArticle.imageUrl
      }).catch(err => {
        console.log(err)
      })
    },

    redirectToUser() {
      this.$router.push({ path: `/user/${this.author}/profile` })
    },

    // Comment List will be received for the article
    getArticleCommentList(equipmentList) {
      this.$store.dispatch('comment/getArticleCommentList', this.$route.params.articleid).then(() => {
        var res = this.$store.getters.commentQueryResult
        console.log(res)
        var that = this
        res.forEach(function(c) {
          that.articleCommentList.push({
            id: c.id,
            author: c.author,
            comment: c.comment,
            likes: c.likes,
            dislikes: c.dislikes,
            lastModifiedTime: c.lastModifiedTime,
            status: c.status
          })
        })
      }).catch(err => {
        console.log(err)
      })
      console.log('articleCommentList is:')
      console.log(this.articleCommentList)
    },

    postArticleComment() {
      this.postArticleCommentDict["comment"] =  this.createArticleCommentContent
      // Posting to backend
      this.$store.dispatch('comment/postArticleComment', {"articleId": this.$route.params.articleid, "data": this.postArticleCommentDict}).then(() => {
        this.showCreateArticleCommentDialog = false
        this.createArticleCommentContent = ''
        this.$message.success('Comment is posted successfully!')
        var res = this.$store.getters.commentQueryResult
        console.log(res)
        this.articleCommentList.push({
          id: res.id,
          author: res.author,
          comment: res.comment,
          likes: res.likes,
          dislikes: res.dislikes,
          lastModifiedTime: res.lastModifiedTime,
          status: res.status
        })
      }).catch(err => {
        console.log(err)
      })
    },

    editArticleComment(commentId) {
      this.editArticleCommentDict["comment"] = this.editArticleCommentContent
      this.$store.dispatch('comment/editArticleComment', {"commentId": commentId, "data": this.editArticleCommentDict}).then(() => {
        var that = this
        this.articleCommentList.forEach(function(c) {
          if (c.id == commentId) {
            c.comment = that.editArticleCommentContent
          }
        })
        this.showEditArticleCommentDialog = false
        this.editArticleCommentContent = ''
        this.$message.success('Comment is edited successfully!')
      }).catch(err => {
        console.log(err)
      })
    },

    deleteArticleComment(commentId) {
      var that = this
      this.$store.dispatch('comment/deleteArticleComment', commentId).then(() => {
        that.$message.success('Comment deleted!')
        this.articleCommentList.forEach(function(c) {
          if (c.id == commentId) {
            c.comment = "[This comment is deleted by the user...]"
          }
        })
      }).catch(err => {
        console.log(err)
      })
        
    }, 

    likeComment(commentId) {
      this.$store.dispatch('comment/voteArticleComment', {"commentId": commentId, "voteType": "up"}).then(() => {
        this.$message.success('Comment liked!')
        this.articleCommentList.forEach(function(c) {
          if (c.id == commentId) {
            c.likes += 1
            c.status = "LIKED" 
          }
        })
      }).catch(err => {
        console.log(err)
      })
    },

    dislikeComment(commentId) {
      this.$store.dispatch('comment/voteArticleComment', {"commentId": commentId, "voteType": "down"}).then(() => {
        this.$message.success('Comment disliked!')
        this.articleCommentList.forEach(function(c) {
          if (c.id == commentId) {
            c.dislikes += 1
            c.status = "DISLIKED" 
          }
        })
      }).catch(err => {
        console.log(err)
      })
    },

    revokeComment(commentId) {
      var lastStatus = ""
      this.articleCommentList.forEach(function(c) {
        if (c.id == commentId) {
          lastStatus = c.status
        }
      })
      console.log('lastStatus is revokeComment : ' + lastStatus)
      this.$store.dispatch('comment/revokeArticleVote', commentId).then(() => {
        this.$message.success('Last vote revoked!')
        this.articleCommentList.forEach(function(c) {
          if (c.id == commentId) {
            if (lastStatus == "LIKED") {
              c.likes -= 1
            } else if (lastStatus == "DISLIKED") {
              c.dislikes -= 1
            } 
            c.status = "NOT_COMMENTED"
          }
        })
      }).catch(err => {
        console.log(err)
      })
    },

    // Annotation related methods
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

    createAnnotationHandler(annotation) {
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
          console.log(response)
      }).catch(error => {
          console.log(error)
      })
    },

    updateAnnotationHandler(annotation) {
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

    deleteAnnotationHandler(annotation) {
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
            that.addAnnotationToImage(backendAnnotation)
          })
      }).catch(error => {
          console.log(error)
      })
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

.comment-time {
  font-weight: normal;
  font-size: 8pt;
  color:#696969;
}

.comment-author {
  font-weight: bold;
  font-size: 11pt;
}

.comment-text {
  font-weight: normal;
  font-size: 10pt;
  color: #404040;
  white-space: pre-line;
}

.comment-options {
  font-weight: normal;
  font-size: 9pt;
  color: #696969
}

.disliked{
  font-weight: normal;
  font-size: 9pt;
  color: red;
}

.liked {
  font-weight: normal;
  font-size: 9pt;
  color: green;
}


.comment-textarea {
  resize: none;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  width: 100%;
}

</style>
