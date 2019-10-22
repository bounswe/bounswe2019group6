<template>
  <body>
    <div class="welcome-block">
      <div class="traderx">
        <h3 class="traderx-text">TraderX</h3>
      </div>

      <div class="buttons">

        <button id="register-button" class="home-button" type="button" @click="goToRegister">Register</button>

        <button id="login-button" class="home-button" type="button" @click="goToLogin">Login</button>

      </div>
    </div>
  </body>
</template>

<script>
export default {
  name: 'Home',
  props: {},
  data() {
    return {
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
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created() {},
  mounted() {},
  methods: {
    goToLogin() {
      this.$router.push({ path: '/login', query: this.otherQuery })
    },
    goToRegister() {
      this.$router.push({ path: '/register', query: this.otherQuery })
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    }

  }
}
</script>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray: #424646;
$light_gray:#eee;

body {
  display: flex;
  align-items: center;
  justify-content: center;
  align-content: center;

  background: url("https://thewallpaper.co//wp-content/uploads/2016/03/black-and-white-city-houses-skyline-landscape-amazing-city-view-beautiful-place-wallpaper-free-city-photos-best-town-city-images-for-windows-large-places-background-1600x1024.jpg") no-repeat center center fixed;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
}

.welcome-block {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: stretch;
}

.traderx {
  display: flex;
  align-items: stretch;

}

.traderx-text {
  /*min-height: 100%;
  width: 100%;*/
  color: $dark_gray;
  font-size: 100px;
  margin-top: 10px;
  margin-bottom: 10px;
}

.buttons {
  flex-grow: 1;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: stretch;
  align-content: stretch;
}

.home-button {
  padding: 15px 32px;
  text-align: center;
  transition-duration: 0.4s;
  margin: 16px;
  text-decoration: none;
  font-size: 20px;
  cursor: pointer;
  color: $dark_gray;
  border: 2px solid #e7e7e7;
  border-radius: 4px;
  background-color: #e7e7e7;
}

.home-button:hover {
  background-color: #f6f7f7; /*very light gray*/
}

</style>
