<template>
  <div>
    <el-collapse v-model="activeNames">
      <el-collapse-item title="Password" name="1">
        <el-tooltip v-model="isPasswordFocused" placement="left" manual>
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
          <el-input
            placeholder="Please enter new password"
            v-model="passwordinput"
            @focus="isPasswordFocused = true"
            @blur="isPasswordFocused = false"
          >
            <el-button slot="append" @click="updatePassword(passwordinput)">Update</el-button>
          </el-input>
        </el-tooltip>
      </el-collapse-item>
      <el-collapse-item title="IBAN" name="2" v-if="ibanshow">
        <el-input placeholder="Please enter new IBAN" v-model="newibaninput">
          <el-button slot="append" @click="updateIban(newibaninput)">Update</el-button>
        </el-input>
      </el-collapse-item>
      <el-collapse-item title="Privacy" name="3">
        <el-switch v-model="privatepublic" active-color="#13ce66" inactive-color="#ff4949" style="float: left" active-text="Public" inactive-text="Private"/>
        <el-button @click="updatePrivacy(privatepublic)" style="margin-left: 50px; margin-bottom: 10px" type="primary">Update</el-button>
      </el-collapse-item>
      <el-collapse-item title="Role" name="4">
        <el-switch v-model="istraderswitch" active-color="#13ce66" inactive-color="#ff4949" style="float: left" active-text="Trader" inactive-text="Basic"/>
        <el-button @click="updateRole(traderibaninput)" style="margin-left: 60px" type="primary">Update</el-button>
        <el-input placeholder="Please enter an IBAN" v-if="traderibanseen" v-model="traderibaninput" style="padding-top: 20px">
        </el-input>
      </el-collapse-item>
      <el-collapse-item title="Load Money" name="5" v-if="istraderloadmoney">
        <el-input placeholder="Please enter some money amount" v-model="loadmoneyinput" class="input-with-select">
          <el-select style="width: 100px" v-model="selectedFilter" slot="prepend" placeholder="Select">
            <!-- <el-option label="TRY" value="TRY"></el-option> -->
            <el-option label="USD" value="USD"></el-option>
            <!-- <el-option label="EUR" value="EUR"></el-option> -->
          </el-select>
          <el-button @click="loadMoney(loadmoneyinput)" slot="append">Load</el-button>
        </el-input>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>
<script>
  import { getToken } from '@/utils/auth' // get token from cookie
  import { becomeBasic, becomeTrader } from '@/api/user'
  import { depositMoney } from '@/api/equipment'

  export default {
    props: {
      user: Object
    },
    created() {
      if(this.istraderswitch) {
        this.traderibanseen = false
        this.ibanshow = true
      } else {
        this.traderibanseen = true
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
        loadmoneyinput: '',
        privatepublic: !this.user.isPrivate,
        istraderswitch: this.user.roles[0] == 'ROLE_TRADER' ? true : false,
        istraderloadmoney: this.user.roles[0] == 'ROLE_TRADER' ? true : false,
        traderibanseen: false,
        ibanshow: false,
        selectedFilter: "USD"
        isPasswordFocused: false
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
        if(!this.istraderswitch){
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
        if (!this.istraderswitch && this.user.roles[0] == 'ROLE_BASIC') {
          this.$message.error('Your Are Already Basic User!')
        } else if (this.istraderswitch && this.user.roles[0] == 'ROLE_TRADER'){
          this.$message.error('Your Are Already Trader!')
        } else if (!this.istraderswitch && this.user.roles[0] == 'ROLE_TRADER'){
          this.$store.dispatch('user/becomeBasic').then(response => {
            this.ibanshow = false
            this.istraderloadmoney = false
            this.traderibanseen = true
            this.traderibaninput = ""
            this.user.roles = ['ROLE_BASIC']
            this.user.iban = null
            this.$message.success('Your Are A Basic User Now!')
          }).catch(err => {
            console.log(err)
          })
        } else if (this.istraderswitch && this.user.roles[0] == 'ROLE_BASIC' ) {
          this.$store.dispatch('user/becomeTrader', {'iban' : iban}).then(response => {
            this.ibanshow = true
            this.istraderloadmoney = true
            this.traderibanseen = false
            this.traderibaninput = ""
            this.user.roles = ['ROLE_TRADER']
            this.user.iban = iban
            this.$message.success('Your Are A Trader Now!')
          }).catch(err => {
            console.log(err)
          })
        }
      },
      loadMoney(moneyamount) {
        this.$store.dispatch('equipment/depositMoney', { "amount" : moneyamount }).then(response => {
          this.$message.success('Money Is Deposited!')
        }).catch(error => {
          console.log("errorrr in money deposit")
          console.log(error)
        })

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
