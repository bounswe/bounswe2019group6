<template>
  <div class="createPost-container">
    <el-form
      v-if="isSelf"
      ref="postForm"
      :model="postForm"
      class="form-container"
    >
      <sticky
        :z-index="10"
        :class-name="'sub-navbar '+postForm.status"
      >
        <CommentDropdown v-model="postForm.comment_disabled" />
        <!-- <PlatformDropdown v-model="postForm.platforms" />
        <SourceUrlDropdown v-model="postForm.source_uri" /> -->
        <el-button
          v-loading="loading"
          style="margin-left: 10px;"
          type="success"
          @click="submitForm"
        >
          Publish
        </el-button>
        <!-- <el-button v-loading="loading" type="warning" @click="draftForm">
          Draft
        </el-button> -->
      </sticky>

      <div class="createPost-main-container">
        <el-row :gutter="100">
          <el-col :span="15">
            <el-form-item
              style="margin-bottom: 40px;"
              prop="title"
            >
              <MDinput
                v-model="postForm.title"
                :maxlength="100"
                name="name"
                required
              >
                Title
              </MDinput>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item
              style="margin-top:40px;"
              label-width="90px"
              label="Importance:"
              class="postInfo-container-item"
            >
              <el-rate
                v-model="postForm.importance"
                :max="3"
                :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                :low-threshold="1"
                :high-threshold="3"
                style="display:inline-block"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item
          style="margin-bottom: 40px;"
          label-width="70px"
          label="Summary:"
        >
          <el-input
            v-model="postForm.content_short"
            :rows="1"
            type="textarea"
            class="article-textarea"
            autosize
            placeholder="Please enter the content"
          />
          <span
            v-show="contentShortLength"
            class="word-counter"
          >{{ contentShortLength }}words</span>
        </el-form-item>

        <el-form-item
          prop="content"
          style="margin-bottom: 30px;"
        >
          <Tinymce
            ref="editor"
            v-model="postForm.content"
            :height="400"
          />
        </el-form-item>

        <!-- <el-form-item prop="image_uri" style="margin-bottom: 30px;">
          <Upload v-model="postForm.image_uri" />
        </el-form-item> -->
      </div>
    </el-form>
    <div style="text-align: center" v-else>
      <h2>This user has no article to show</h2>
    </div>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import MDinput from '@/components/MDinput'
import Sticky from '@/components/Sticky' // 粘性header组件
import { fetchArticle } from '@/api/article'
import { searchUser } from '@/api/remote-search'
import { CommentDropdown } from '@/views/profile/components/Dropdown'

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
  components: { Tinymce, MDinput, Sticky, CommentDropdown },
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
      isSelf: this.$route.path.split('/')[1] == 'profile' ? true : false
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
    console.log("path is: ")
    console.log(this.$route.path.split('/'))
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
            this.$notify({
              title: 'Success',
              message: 'Your article is posted',
              type: 'success',
              duration: 2000
            })
            this.postForm.status = 'published'
            this.loading = false
          } else {
            return false
          }
        })
      }
    },
    draftForm() {
      if (this.postForm.title == '') {
        this.$notify({
          message: 'Title cannot be empty',
          type: 'warning'
        })
      } else if (this.postForm.content == '') {
        this.$notify({
          message: 'Content cannot be empty',
          type: 'warning'
        })
      } else {
        this.$notify({
          message: 'Draft successful',
          type: 'success',
          showClose: true,
          duration: 1000
        })
        this.postForm.status = 'draft'
      }
    },
    getRemoteUserList(query) {
      searchUser(query).then(response => {
        if (!response.data.items) return
        this.userListOptions = response.data.items.map(v => v.name)
      })
    }
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
