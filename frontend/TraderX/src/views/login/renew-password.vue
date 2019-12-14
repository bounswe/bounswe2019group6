<template>
  <body>
  <div class="renew-password-container">
    <el-form
      ref="renewForm"
      :model="renewForm"
      :rules="renewRules"
      class="renew-form"
      label-position="left"
    >
      <div class="title-container">
        <h3 class="title">
          Renew Password
        </h3>
      </div>

      <el-tooltip
        v-model="isPasswordFocused"
        placement="right"
        manual
      >
        <div slot="content">
          * Minimum 6 characters
          <br>
          * At least a lowercase letter (a-z)
          <br>
          * At least an uppercase letter (A-Z)
          <br>
          * At least a digit (0-9)
          <br>
          * At least a special character (@#$%^&+=_.)
          <br>
        </div>
        <el-form-item prop="newPassword">
            <span class="svg-container">
              <svg-icon icon-class="password" />
            </span>
          <el-input
            :key="passwordType"
            ref="newPassword"
            v-model="renewForm.newPassword"
            :type="passwordType"
            placeholder="New Password"
            name="newPassword"
            tabindex="1"
            @focus="isPasswordFocused = true"
            @blur="isPasswordFocused = false"
            @keyup.enter.native="handleRenewPassword"
          />
          <span
            class="show-pwd"
            @click="showPwd"
          >
              <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
            </span>
        </el-form-item>
      </el-tooltip>

      <el-form-item prop="newPasswordAgain">
        <span class="svg-container">
              <svg-icon icon-class="password" />
            </span>
        <el-input
          :key="passwordType"
          ref="newPasswordAgain"
          v-model="checkPassword"
          :type="passwordType"
          placeholder="New Password Again"
          name="newPasswordAgain"
          tabindex="2"
          @focus="isPasswordFocused = true"
          @blur="isPasswordFocused = false"
          @keyup.enter.native="handleRenewPassword"
        />
      </el-form-item>

      <el-button
        type="primary"
        style="width:100%;margin-bottom:30px;"
        @click.native.prevent="handleRenewPassword"
      >
        Submit
      </el-button>

    </el-form>
  </div>
  </body>
</template>

<script>
  import { Message } from 'element-ui'
  import { validPassword } from '@/utils/validate'

  export default {
    name: 'RenewPassword',
    data() {
      const validatePassword = (rule, value, callback) => {
        if (!validPassword(value)) {
          callback(new Error('Please enter a valid password'))
        } else {
          callback()
        }
      }
      const validatePasswordAgain = (rule, value, callback) => {
        if (this.$data.checkPassword != this.$data.renewForm.newPassword) {
          callback(new Error('Passwords does not match'))
        } else {
          callback()
        }
      }
      const { query } = this.$route

      return {
        renewForm: {
          token: query.token,
          newPassword: ''
        },
        renewRules: {
          newPassword: [{ required: true, trigger: 'blur', validator: validatePassword }],
          newPasswordAgain: [{ required: true, trigger: 'blur', validator: validatePasswordAgain }]
        },
        checkPassword: '',
        passwordType: 'password',
        capsTooltip: false
      }
    },
    created() {
      // window.addEventListener('storage', this.afterQRScan)
    },
    mounted() {
      this.$refs.newPassword.focus()
    },
    destroyed() {
      // window.removeEventListener('storage', this.afterQRScan)
    },
    methods: {
      checkCapslock({ shiftKey, key } = {}) {
        this.$refs.renewForm.validate()
        if (key && key.length === 1) {
          if (shiftKey && (key >= 'a' && key <= 'z') || !shiftKey && (key >= 'A' && key <= 'Z')) {
            this.capsTooltip = true
          } else {
            this.capsTooltip = false
          }
        }
        if (key === 'CapsLock' && this.capsTooltip === true) {
          this.capsTooltip = false
        }
      },
      showPwd() {
        if (this.passwordType === 'password') {
          this.passwordType = ''
        } else {
          this.passwordType = 'password'
        }
        this.$nextTick(() => {
          this.$refs.newPassword.focus()
        })
      },
      handleRenewPassword() {
        this.$refs.renewForm.validate(valid => {
          if (valid) {
            this.$store.dispatch('user/renew', this.renewForm)
              .then(() => {
                Message.success('Password reset successful, you can log in now')
                this.$router.push({ path: '/login' })
              }).catch(() => {
                Message.error('Cannot reset your password')
            })
          } else {
            return false
          }
        })
      }
    }
  }
</script>

<style lang="scss">
  /* 修复input 背景不协调 和光标变色 */
  /* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

  /*$bg:#283443;
  $light_gray:#fff;*/
  $cursor: #424646;
  $bg:#2d3a4b;
  $dark_gray: #424646;
  $light_gray:#eee;

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .renew-password-container .el-input input {
      color: $cursor;
    }
  }

  body {
    background: url("https://thewallpaper.co//wp-content/uploads/2016/03/black-and-white-city-houses-skyline-landscape-amazing-city-view-beautiful-place-wallpaper-free-city-photos-best-town-city-images-for-windows-large-places-background-1600x1024.jpg") no-repeat;
    -webkit-background-size: cover;
    -moz-background-size: cover;
    -o-background-size: cover;
    background-size: cover;
  }

  /* reset element-ui css */
  .renew-password-container {
    .el-link {
      margin-top: -20px;
      margin-bottom: 20px;
      float: right;
    }

    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;

      input {
        background: transparent;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        color: $dark_gray;
        height: 47px;
        caret-color: $cursor;

        &:-webkit-autofill {
          box-shadow: 0 0 0px 1000px $bg inset !important;
          -webkit-text-fill-color: $cursor !important;
        }
      }
    }

    .el-button {
      /*padding: 15px 32px;
      text-align: center;*/
      transition-duration: 0.4s;
      // margin: 16px;
      text-decoration: none;
      font-size: 15px;
      cursor: pointer;
      color: $light_gray;
      border: 2px solid $dark_gray;
      border-radius: 4px;
      background-color: $dark_gray;
    }

    .el-button:hover {
      background-color: #f6f7f7;
      color: $dark_gray;
      border-color: #e7e7e7;
    }

    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
  }
</style>

<style lang="scss" scoped>
  $bg:#2d3a4b;
  $dark_gray: #424646;
  $light_gray:#eee;

  .renew-password-container {
    min-height: 100%;
    width: 100%;
    // background-color: $bg;
    overflow: hidden;

    .renew-form {
      position: relative;
      width: 520px;
      max-width: 100%;
      padding: 160px 35px 0;
      margin: 0 auto;
      overflow: hidden;
    }

    .tips {
      font-size: 14px;
      color: #fff;
      margin-bottom: 10px;

      span {
        &:first-of-type {
          margin-right: 16px;
        }
      }
    }

    .svg-container {
      padding: 6px 5px 6px 15px;
      color: $dark_gray;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
    }

    .title-container {
      position: relative;

      .title {
        font-size: 26px;
        color: $dark_gray;
        margin: 0px auto 40px auto;
        text-align: center;
        font-weight: bold;
      }
    }

    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: $dark_gray;
      cursor: pointer;
      user-select: none;
    }

    @media only screen and (max-width: 470px) {
      .thirdparty-button {
        display: none;
      }
    }
  }
</style>
