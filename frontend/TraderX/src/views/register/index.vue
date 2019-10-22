<template>
  <body>
    <div class="sign-up-container">
      <el-form
        ref="signupForm"
        :model="signupForm"
        :rules="signupRules"
        class="sign-up-form"
        autocomplete="on"
        label-position="left"
      >
        <div class="title-container">
          <h3 class="title">
            Sign Up
          </h3>
        </div>

        <el-form-item prop="email">
          <span class="svg-container">
            <svg-icon icon-class="email" />
          </span>
          <el-input
            ref="email"
            v-model="signupForm.email"
            placeholder="Email"
            name="email"
            type="text"
            tabindex="1"
            autocomplete="on"
          />
        </el-form-item>

        <el-form-item prop="username">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            ref="username"
            v-model="signupForm.username"
            placeholder="Username"
            name="username"
            type="text"
            tabindex="2"
            autocomplete="on"
          />
        </el-form-item>

        <el-tooltip
          v-model="capsTooltip"
          content="Caps lock is ON"
          placement="right"
          manual
        >
          <el-form-item prop="password">
            <span class="svg-container">
              <svg-icon icon-class="password" />
            </span>
            <el-input
              :key="passwordType"
              ref="password"
              v-model="signupForm.password"
              :type="passwordType"
              placeholder="Password"
              name="password"
              tabindex="3"
              autocomplete="on"
              @keyup.native="checkCapslock"
              @blur="capsTooltip = false"
              @keyup.enter.native="handleSignup"
            />
            <span
              class="show-pwd"
              @click="showPwd"
            >
              <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
            </span>
          </el-form-item>
        </el-tooltip>

        <el-form-item prop="latitude">
          <span class="svg-container">
            <svg-icon icon-class="international" />
          </span>
          <el-input
            ref="latitude"
            v-model="signupForm.latitude"
            placeholder="Latitude"
            name="latitude"
            type="text"
            tabindex="4"
            autocomplete="on"
          />
        </el-form-item>

        <el-form-item prop="longitude">
          <span class="svg-container">
            <svg-icon icon-class="international" />
          </span>
          <el-input
            ref="longitude"
            v-model="signupForm.longitude"
            placeholder="Longitude"
            name="longitude"
            type="text"
            tabindex="5"
            autocomplete="on"
          />
        </el-form-item>

        <div style="margin-bottom: 22px; margin-left: 10px;">
          <el-checkbox v-model="signupForm.isPrivate">
            Make your profile private!
          </el-checkbox>
        </div>

        <div style="margin-bottom: 22px">
          <el-radio-group
            v-model="isTrader"
            style="padding: 10px;"
          >
            <el-radio :label="false">
              Basic
            </el-radio>
            <el-radio :label="true">
              Trader
            </el-radio>
          </el-radio-group>

          <el-form-item
            v-if="isTrader"
            prop="iban"
          >
            <span class="svg-container">
              <svg-icon icon-class="money" />
            </span>
            <el-input
              ref="iban"
              v-model="signupForm.iban"
              placeholder="IBAN"
              name="iban"
              type="text"
              tabindex="6"
              autocomplete="on"
            />
          </el-form-item>
        </div>
        <el-button
          :loading="loading"
          type="primary"
          style="width:100%;margin-bottom:30px;"
          @click.native.prevent="handleSignup"
        >
          Register
        </el-button>

        <!-- <div style="position:relative">
          <div class="tips">
            <span>Username : 3-20 characters</span>
          </div>
          <div class="tips">
            <span>Password : At least 6 characters</span>
          </div>

          <el-button
            class="thirdparty-button"
            type="primary"
            @click="showDialog=true"
          >
            Or connect with
          </el-button>
        </div> -->
      </el-form>

      <el-dialog
        title="Or connect with"
        :visible.sync="showDialog"
      >
        Can not be simulated on local, so please combine you own business simulation! ! !
        <br>
        <br>
        <br>
        <social-sign />
      </el-dialog>
    </div>
  </body>
</template>

<script>
import { validUsername, validPassword, validIBAN, validLocation, validEmail } from '@/utils/validate'
import SocialSign from './components/SocialSignin'
import { Message } from 'element-ui'

export default {
  name: 'Register',
  components: { SocialSign },
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('Please enter a valid username'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (!validPassword(value)) {
        callback(new Error('Please enter a valid password'))
      } else {
        callback()
      }
    }
    const validateIBAN = (rule, value, callback) => {
      if (value == null || !validIBAN(value)) {
        callback(new Error('Please enter a valid IBAN'))
      } else {
        callback()
      }
    }
    const validateLocation = (rule, value, callback) => {
      if (!validLocation(value)) {
        callback(new Error('Please enter a valid location'))
      } else {
        callback()
      }
    }
    const validateEmail = (rule, value, callback) => {
      if (!validEmail(value)) {
        callback(new Error('Please enter a valid email'))
      } else {
        callback()
      }
    }
    return {
      signupForm: {
        username: '',
        password: '',
        iban: '',
        latitude: '',
        longitude: '',
        email: '',
        isPrivate: false
      },
      isTrader: false,
      signupRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        iban: [{ required: true, trigger: 'blur', validator: validateIBAN }],
        latitude: [{ required: true, trigger: 'blur', validator: validateLocation }],
        longitude: [{ required: true, trigger: 'blur', validator: validateLocation }],
        email: [{ required: true, trigger: 'blur', validator: validateEmail }]
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      showDialog: false,
      redirect: undefined,
      otherQuery: {}
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = ''
        }
      },
      immediate: true
    }
  },
  created() {
    // window.addEventListener('storage', this.afterQRScan)
  },
  mounted() {
    if (this.signupForm.email === '') {
      this.$refs.email.focus()
    } else if (this.signupForm.username === '') {
      this.$refs.username.focus()
    } else if (this.signupForm.password === '') {
      this.$refs.password.focus()
    } else if (this.signupForm.latitude === '') {
      this.$refs.latitude.focus()
    } else if (this.signupForm.longitude === '') {
      this.$refs.longitude.focus()
    } else if (this.signupForm.iban === '') {
      this.$refs.iban.focus()
    }
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
    checkCapslock({ shiftKey, key } = {}) {
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
        this.$refs.password.focus()
      })
    },
    handleSignup() {
      if (!this.isTrader) {
        this.signupForm.iban = null
      }

      this.$refs.signupForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/register', this.signupForm)
            .then(() => {
              Message.success('Registered successfully, check your inbox for confirmation link')
              this.$router.push({ path: '/login', query: this.otherQuery })
              this.loading = false
            })
            .catch(() => {
              this.loading = false
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
/*
  $bg:#283443;
  $light_gray:#fff;*/
  $cursor: #424646;
  $bg:#2d3a4b;
  $dark_gray: #424646;
  $light_gray:#eee;

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .sign-up-container .el-input input {
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
  .sign-up-container {
    .el-input {
      display: inline-block;
      height: 40px;
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
      /*margin: 16px;*/
      text-decoration: none;
      font-size: 15px;
      cursor: pointer;
      color: $light_gray;
      border: 2px solid $dark_gray;
      border-radius: 4px;
      background-color: $dark_gray;
    }

    .el-button:hover {
      background-color: #f6f7f7; /*very light gray*/
      color: $dark_gray;
      border-color: #e7e7e7;
    }

    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }


    .el-radio {
      /*-webkit-appearance: none;
      -moz-appearance: none;
      appearance: none;
      display: inline-block;*/
      /*background-color: #f1f1f1;*/
      color: $dark_gray;
      border: 0;
      border-radius: 50px;
      cursor: pointer;     
      outline: none;
    }

  }
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray: #424646;
$light_gray:#eee;

  .sign-up-container {
    min-height: 100%;
    width: 100%;
    /*background-color: $bg;*/
    overflow: hidden;

    .sign-up-form {
      position: relative;
      width: 520px;
      max-width: 100%;
      padding: 160px 35px 0;
      margin: 0 auto;
      overflow: hidden;
    }

    .tips {
      font-size: 14px;
      color: $dark_gray;
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

    .thirdparty-button {
      position: absolute;
      right: 0;
      bottom: 6px;
    }

    @media only screen and (max-width: 470px) {
      .thirdparty-button {
        display: none;
      }
    }
  }
</style>
