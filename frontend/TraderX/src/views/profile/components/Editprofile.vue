<template>
  <div>
    <el-collapse v-model="activeNames">
      <el-collapse-item title="Password" name="1">
        <el-input placeholder="Please enter new password" v-model="passwordinput">
          <el-button slot="append" @click="updatePassword(passwordinput)">Update</el-button>
        </el-input>
      </el-collapse-item>
      <el-collapse-item title="IBAN" name="2" v-if="ibanshow">
        <el-input placeholder="Please enter new IBAN" v-model="newibaninput">
          <el-button slot="append" @click="updateIban(newibaninput)">Update</el-button>
        </el-input>
      </el-collapse-item>
      <el-collapse-item title="Privacy" name="3">
        <el-switch v-model="privatepublic" active-color="#13ce66" inactive-color="#ff4949" style="float: left" active-text="Public" inactive-text="Private"/>
        <el-button @click="updatePrivacy(privatepublic)" style="margin-left: 50px; margin-bottom: 10px">Update</el-button>
      </el-collapse-item>
      <el-collapse-item title="Role" name="4">
        <el-switch v-model="istrader" active-color="#13ce66" inactive-color="#ff4949" style="float: left" active-text="Trader" inactive-text="Basic"/>
        <el-input placeholder="Please enter an IBAN" v-if="seen" v-model="traderibaninput" style="padding-top: 20px">
          <el-button slot="append" @click="updateRole(traderibaninput)">Update</el-button>
        </el-input>
      </el-collapse-item>
      <el-collapse-item title="Load Money" name="2" v-if="!istrader">
        <el-input placeholder="Please enter some money amount" v-model="loadmoneyinput" class="input-with-select">
          <el-select style="width: 100px" v-model="selectedFilter" slot="prepend" placeholder="Select">
            <el-option label="TR" value="1"></el-option>
            <el-option label="USD" value="2"></el-option>
            <el-option label="EUR" value="3"></el-option>
          </el-select>
          <el-button @click="loadMoney(loadmoneyinput)" slot="append">Load</el-button>
        </el-input>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>
<script>
  import { getToken } from '@/utils/auth' // get token from cookie

  export default {
    props: {
      user: Object
    },
    created() {
      if(this.istrader) {
        this.seen = false
        this.ibanshow = true
      } else {
        this.seen = true
        this.ibanshow = false
      }
    },
    data() {
      return {
        activeNames: [''],
        usernameinput: '',
        passwordinput: '',
        newibaninput: '',
        traderibaninput: '',
        privatepublic: !this.user.isPrivate,
        istrader: this.user.roles[0] == 'ROLE_TRADER' ? true : false,
        seen: false,
        ibanshow: false,
        selectedFilter: "1"
      };
    },
    methods: {
      updatePassword(pass) {
        var temp = {
              "token": getToken(),
              "newPassword": pass
          }
        this.$store.dispatch('user/updatePassword', temp).then(() => {
              this.$message.success('Your Password Is Changed Successfully!')
              this.passwordinput = ""
           }).catch(error => {
            console.log("errorrr in password change")
            console.log(error)
          })
      },
      updateIban(iban) {
        if(!this.istrader){
          this.$message.error('Your Are Not A Trader!')
        } else {
          this.$store.dispatch('user/changeIBAN', {
              "newIBAN": iban.toString(),
              "newLatitude": "123",
              "newLongitude": "123"
          }).then(() => {
              this.$message.success('Your IBAN Is Changed Successfully!')
              this.user.iban = iban
          }).catch(error => {
            console.log("errorrr in iban change")
            console.log(error)
          })
        }
      },
      updateRole(iban){
      },
      loadMoney(moneyamount) {
      },
      updatePrivacy(isPrivate){
        if(isPrivate){
          if (this.user.isPrivate){
              this.$store.dispatch('user/setProfilePublic').then(response => {
                this.$message.success('Your Profile Is Public Now!')
                this.user.isPrivate = false
            }).catch(error => {
              console.log("errorrr in profile change")
              console.log(error)
            })
          } else {
            this.$message.error('Your Profile Is Public Already!')
          }
        } else {
          if (!this.user.isPrivate){
            this.$store.dispatch('user/setProfilePrivate').then(response => {
              this.$message.success('Your Profile Is Private Now!')
              this.user.isPrivate = true
            }).catch(error => {
              console.log("errorrr in profile change")
              console.log(error)
            })
          } else {
            this.$message.error('Your Profile Is Private Already!')
          }
        }
      }
    }
  }
</script>