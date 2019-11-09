<template>
  <div>
    <el-collapse v-model="activeNames">
      <el-collapse-item title="Username" name="1">
        <el-input placeholder="Please enter new username" v-model="usernameinput">
          <el-button slot="append" @click="updateUsername(usernameinput)">Update</el-button>
        </el-input>
      </el-collapse-item>
      <el-collapse-item title="Password" name="2">
        <el-input placeholder="Please enter new password" v-model="passwordinput">
          <el-button slot="append" @click="updatePassword(passwordinput)">Update</el-button>
        </el-input>
      </el-collapse-item>
      <el-collapse-item title="IBAN" name="3">
        <el-input placeholder="Please enter new IBAN" v-model="newibaninput">
          <el-button slot="append" @click="updateIban(newibaninput)">Update</el-button>
        </el-input>
      </el-collapse-item>
      <el-collapse-item title="Privacy" name="4">
        <el-switch v-model="privatepublic" active-color="#13ce66" inactive-color="#ff4949" style="float: left" active-text="Private" inactive-text="Public"/>
        <el-button @click="updatePrivacy(privatepublic)" style="margin-left: 50px; margin-bottom: 10px">Update</el-button>
      </el-collapse-item>
      <el-collapse-item title="Role" name="5">
        <el-switch v-model="istrader" active-color="#13ce66" inactive-color="#ff4949" style="float: left" active-text="Trader" inactive-text="Basic"/>
        <el-input placeholder="Please enter an IBAN" v-if="seen" v-model="traderibaninput" style="padding-top: 20px">
          <el-button slot="append" @click="updateRole(traderibaninput)">Update</el-button>
        </el-input>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>
<script>
  export default {
    props: {
      user: Object
    },
    created() {
      if(this.istrader) {
        this.seen = false
      } else {
        this.seen = true
      }
    },
    data() {
      return {
        activeNames: [''],
        usernameinput: '',
        passwordinput: '',
        newibaninput: '',
        traderibaninput: '',
        privatepublic: this.user.isPrivate,
        istrader: this.user.roles[0] == 'ROLE_TRADER' ? true : false,
        seen: false
      };
    },
    methods: {
      updateUsername(name) {
        console.log(name)
      },
      updatePassword(pass) {
        console.log(pass)
      },
      updateIban(iban) {
        console.log(iban)
      },
      updateRole(iban){
        console.log(iban)
      },
      updatePrivacy(isPrivate){
        console.log(isPrivate)
      }
    }
  }
</script>