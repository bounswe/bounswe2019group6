<template>
  <el-form
    ref="form"
    :model="form"
    label-width="120px"
  >
    <el-form-item label="Username">
      <el-input v-model="form.username" />
    </el-form-item>
    <el-form-item label="New Password">
      <el-input
        v-model="form.newpassword"
        show-password
      />
    </el-form-item>
    <el-form-item label="IBAN">
      <el-input v-model="form.newiban" />
    </el-form-item>
    <el-form-item label="Location">
      <el-input v-model="form.newlocation" />
    </el-form-item>
    <el-form-item label="">
      <el-switch
        v-model="form.privatepublic"
        active-color="#13ce66"
        inactive-color="#ff4949"
        style="float: left"
        active-text="Public"
        inactive-text="Private"
      />
    </el-form-item>
    <el-form-item label="">
      <el-switch
        v-model="form.traderbasic"
        active-color="#13ce66"
        inactive-color="#ff4949"
        style="float: left"
        active-text="Trader"
        inactive-text="Basic"
      />
    </el-form-item>
    <el-form-item>
      <el-button
        type="primary"
        @click="onSubmit"
      >
        Update
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    props: {
      user: {
        type: Object,
        default: () => {
          return {
            name: '',
            email: '',
            avatar: '',
            roles: ''
          }
        }
      }
    },
    data() {
      return {
        form: {
          username: '',
          newpassword: '',
          newlocation: '',
          newiban: '',
          privatepublic: false,
          traderbasic: false,
        }
      }
    },
    methods: {
      onSubmit() {
        if(this.$data.form.username == '' && this.$data.form.newpassword == '' && this.$data.form.newiban == '' && this.$data.form.newlocation == '') {
          this.$notify({
            message: 'All contents are empty',
            type: 'error'
          })
          return
        }
        if (this.$store.state.user.name == this.$data.form.username) {
          this.$notify({
            message: 'Selected username is the same as current username',
            type: 'error'
          })
          return
        }
        // if (this.$store.state.user.iban.length != 26) {
        //   this.$notify({
        //     message: 'IBAN number cannot be this size',
        //     type: 'error'
        //   })
        //   return
        // }
        console.log(this.form)
      }
    }
  }
</script>