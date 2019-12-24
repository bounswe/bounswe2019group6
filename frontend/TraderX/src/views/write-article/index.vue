<template>
  <div class="createPost-container">
    <el-button
      v-loading="loading"
      style="float:right; margin-top:80px; margin-right:100px;"
      type="success"
      @click.native.prevent="submitForm()"
    >
      Publish
    </el-button>


    <el-form
      ref="postForm"
      :model="postForm"
      class="form-container"
    >
      <div class="createPost-main-container">
        
        <el-form-item
          style="margin-bottom: 40px; width:300px"
          prop="title"
        >
          <MDinput
            style="width:500px"
            v-model="postForm.title"
            :maxlength="100"
            name="name"
            required
          >
            Title
          </MDinput>
        </el-form-item>
                
        <el-input placeholder="Please input image URl" v-model="inputurl" style="margin-bottom:30px"></el-input>

        <el-form-item
          prop="content"
          style="margin-bottom: 30px;text-align:center"
        >
          <textarea 
            class="comment-textarea" 
            placeholder="Write your article here" 
            cols="100" rows="25" v-model="postForm.content"></textarea>
        </el-form-item>

        <!-- <el-form-item prop="image_uri" style="margin-bottom: 30px;">
          <Upload v-model="postForm.image_uri" />
        </el-form-item> -->
      </div>
    </el-form>
    
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import MDinput from '@/components/MDinput'
import { fetchArticle } from '@/api/article'

const defaultForm = {
  status: 'draft',
  title: '', // 文章题目
  content: '', // 文章内容
  content_short: '', // 文章摘要
  display_time: undefined, // 前台展示时间
  id: undefined,
  platforms: ['a-platform'],
  comment_disabled: false,
  importance: 0
}

export default {
  name: 'ArticleDetail',
  components: { Tinymce, MDinput },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      postForm: Object.assign({}, defaultForm),
      loading: false,
      userListOptions: [],
      tempRoute: {},
      inputdata: {
        "header": "",
        "body": "",
        "tags" : []
      },
      inputurl : "",
    }
  },
  computed: {
    contentShortLength() {
      return this.postForm.content_short.length
    },
    displayTime: {
      // set and get is useful when the data
      // returned by the back end api is different from the front end
      // back end return => "2013-06-25 06:59:25"
      // front end need timestamp => 1372114765000
      get() {
        return new Date()
      },
      set(val) {
        this.postForm.display_time = new Date(val)
      }
    }
  },
  created() {
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      this.fetchData(id)
    }

    // Why need to make a copy of this.$route here?
    // Because if you enter this page and quickly switch tag, may be in the execution of the setTagsViewTitle function, this.$route is no longer pointing to the current page
    // https://github.com/PanJiaChen/vue-element-admin/issues/1221
    this.tempRoute = Object.assign({}, this.$route)
  },
  methods: {
    fetchData(id) {
      fetchArticle(id).then(response => {
        this.postForm = response.data

        // just for test
        this.postForm.title += `   Article Id:${this.postForm.id}`
        this.postForm.content_short += `   Article Id:${this.postForm.id}`

        // set tagsview title
        this.setTagsViewTitle()

        // set page title
        this.setPageTitle()
      }).catch(err => {
        console.log(err)
      })
    },
    setTagsViewTitle() {
      const title = 'Edit Article'
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.postForm.id}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    },
    setPageTitle() {
      const title = 'Edit Article'
      document.title = `${title} - ${this.postForm.id}`
    },
    submitForm() {
      if (this.postForm.title == '') {
        this.$notify({
          message: 'Title cannot be empty',
          type: 'error'
        })
      } else if (this.postForm.content == '') {
        this.$notify({
          message: 'Content cannot be empty',
          type: 'error'
        })
      } else {
        this.$refs.postForm.validate(valid => {
          if (valid) {
            // TODO later send a request to backend from here
            this.loading = true
            this.inputdata["header"] = this.postForm.title
            this.inputdata["body"] = this.postForm.content
            this.inputdata["tags"] = ["t1", "t2"]
            this.inputdata["imageUrl"] = this.inputurl
            this.$store.dispatch('search/writeArticle', this.inputdata ).then(() => {
              this.$notify({ title: 'Success', message: 'Article is posted', type: 'success', duration: 2000 }) 
              this.$router.push({ path: `/profile/index` })
            }).catch(err => {
              console.log(err)
            })
            this.postForm.status = 'published'
            this.loading = false
          } else {
            return false
          }
        })
      }
    },
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/mixin.scss";

.createPost-container {
  position: relative;

  .createPost-main-container {
    padding: 40px 45px 20px 50px;

    .postInfo-container {
      position: relative;
      @include clearfix;
      margin-bottom: 10px;

      .postInfo-container-item {
        float: left;
      }
    }
  }

  .word-counter {
    width: 40px;
    position: absolute;
    right: 10px;
    top: 0px;
  }
}

.article-textarea /deep/ {
  textarea {
    padding-right: 40px;
    resize: none;
    border: none;
    border-radius: 0px;
    border-bottom: 1px solid #bfcbd9;
  }
}
</style>
