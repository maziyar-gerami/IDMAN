<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("profile") }}</h3>
        <TabView v-model:activeIndex="tabActiveIndex">
          <TabPanel :header="$t('editUserInformation')">
            <div v-if="loading" class="text-center">
              <ProgressSpinner />
            </div>
            <div v-else>
              <div class="formgrid grid">
                <div class="field col-3">
                  <div class="flex align-content-center flex-wrap h-full">
                    <a :href="userAvatar" target="_blank" class="flex align-items-center justify-content-center avatar">
                      <AppAvatar :image="userAvatar" size="xlarge" shape="circle" class="avatar avatarBorder"></AppAvatar>
                    </a>
                    <form class="hidden">
                      <input id="user.avatar" type="file" name="file" v-on:change="profileRequestMaster('editUserAvatar')">
                    </form>
                    <div class="grid">
                      <div class="col-12">
                        <Button icon="pi pi-pencil" class="p-button-rounded mr-2 mb-2" @click="editAvatarHelper()" v-tooltip.top="$t('editAvatar')" />
                      </div>
                      <div class="col-12">
                        <Button icon="pi pi-trash" class="p-button-danger p-button-rounded mr-2 mb-2" @click="deleteUserAvatarCheckup()" v-tooltip.top="$t('deleteAvatar')" />
                      </div>
                    </div>
                  </div>
                </div>
                <div class="field col-9"></div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="user._id">{{ $t("id") }}<span style="color: red;"> * </span></label>
                    <InputText id="user._id" type="text" :class="userErrors._id" v-model="user._id" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" :readOnly="true" />
                    <small>{{ $t("inputEnglishFilterText") }}</small>
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="user.displayName">{{ $t("persianName") }}<span style="color: red;"> * </span></label>
                    <InputText id="user.displayName" type="text" :class="userErrors.displayName" v-model="user.displayName" @keypress="persianInputFilter($event)" @paste="persianInputFilter($event)" />
                    <small>{{ $t("inputPersianFilterText") }}</small>
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="user.firstName">{{ $t("englishFirstName") }}<span style="color: red;"> * </span></label>
                    <InputText id="user.firstName" type="text" :class="userErrors.firstName" v-model="user.firstName" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" />
                    <small>{{ $t("inputEnglishFilterText") }}</small>
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="user.lastName">{{ $t("englishLastName") }}<span style="color: red;"> * </span></label>
                    <InputText id="user.lastName" type="text" :class="userErrors.lastName" v-model="user.lastName" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" />
                    <small>{{ $t("inputEnglishFilterText") }}</small>
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="user.mobile">{{ $t("mobile") }}<span style="color: red;"> * </span></label>
                    <InputText id="user.mobile" type="text" :class="userErrors.mobile" v-model="user.mobile" />
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="user.mail">{{ $t("email") }}<span style="color: red;"> * </span></label>
                    <InputText id="user.mail" type="text" :class="userErrors.mail" v-model="user.mail" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="user.employeeNumber">{{ $t("employeeNumber") }}</label>
                    <InputText id="user.employeeNumber" type="text" v-model="user.employeeNumber" />
                  </div>
                </div>
                <div class="field col"></div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="user.description">{{ $t("description") }}</label>
                    <Textarea id="user.description" v-model="user.description" :autoResize="true" rows="3" />
                  </div>
                </div>
              </div>
              <Button id="user.editButton" :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="editUserInformationCheckup()" />
            </div>
          </TabPanel>
          <TabPanel :header="$t('editPassword')">
            <div v-if="loading" class="text-center">
              <ProgressSpinner />
            </div>
            <div v-else>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="user.password">{{ $t("password") }}<span style="color: red;"> * </span></label>
                    <Password id="user.password" :class="userErrors.userPassword" v-model="user.userPassword" :toggleMask="true" autocomplete="off">
                      <template #header>
                          <h6>{{ $t("passwordStrength") }}</h6>
                      </template>
                      <template #footer>
                          <Divider />
                          <p class="mt-3">{{ $t("passwordRequirement") }}</p>
                          <ul class="pl-2 ml-2 mt-0" style="line-height: 1.5">
                              <li>{{ $t("passwordRequirementText1") }}</li>
                              <li>{{ $t("passwordRequirementText2") }}</li>
                              <li>{{ $t("passwordRequirementText3") }}</li>
                              <li>{{ $t("passwordRequirementText4") }}</li>
                          </ul>
                      </template>
                    </Password>
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="user.passwordRepeat">{{ $t("passwordRepeat") }}<span style="color: red;"> * </span></label>
                    <Password id="user.passwordRepeat" :class="userErrors.userPasswordRepeat" v-model="user.userPasswordRepeat"
                    :toggleMask="true" onpaste="return false;" ondrop="return false;" autocomplete="off" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <div class="p-inputgroup">
                      <InputText v-model="user.verificationCode" :placeholder="$t('verificationCode')" v-tooltip.top="$t('profileText1')" />
                      <Button id="countdownButton" @click="profileRequestMaster('requestVerificationCode')">
                        {{ $t("request") }}&nbsp;<span id="countdownButtonCD"></span>
                      </Button>
                    </div>
                  </div>
                </div>
                <div class="field col"></div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mx-1" @click="editUserPasswordCheckup()" />
            </div>
          </TabPanel>
        </TabView>
      </div>
    </div>
  </div>
</template>

<script>
import iziToast from "@/assets/scripts/iziToast.min.js"

export default {
  name: "Profile",
  data () {
    return {
      user: {
        _id: "",
        displayName: "",
        firstName: "",
        lastName: "",
        mobile: "",
        mail: "",
        employeeNumber: "",
        description: "",
        userPassword: "",
        userPasswordRepeat: "",
        verificationCode: ""
      },
      userErrors: {
        _id: "",
        displayName: "",
        firstName: "",
        lastName: "",
        mobile: "",
        mail: "",
        userPassword: "",
        userPasswordRepeat: "",
        verificationCode: ""
      },
      userAvatar: "images/avatarPlaceholder.png",
      verificationCodeCountdown: "",
      tabActiveIndex: 0,
      loading: false,
      persianRex: null
    }
  },
  mounted () {
    this.persianRex = require("persian-rex/dist/persian-rex")
    this.profileRequestMaster("getUserAvatar")
    this.profileRequestMaster("getUser")
  },
  methods: {
    profileRequestMaster (command) {
      const vm = this
      let parameterObject = null
      if (this.$i18n.locale === "Fa") {
        parameterObject = { lang: "fa" }
      } else if (this.$i18n.locale === "En") {
        parameterObject = { lang: "en" }
      }
      if (command === "getUser") {
        this.user = {
          _id: "",
          displayName: "",
          firstName: "",
          lastName: "",
          mobile: "",
          mail: "",
          employeeNumber: "",
          description: "",
          userPassword: "",
          userPasswordRepeat: "",
          verificationCode: ""
        }
        const query = new URLSearchParams(parameterObject).toString()
        this.loading = true
        this.axios({
          url: "/api/user" + "?" + query,
          method: "GET"
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.user._id = res.data.data._id
            vm.user.displayName = res.data.data.displayName
            vm.user.firstName = res.data.data.firstName
            vm.user.lastName = res.data.data.lastName
            vm.user.mobile = res.data.data.mobile
            vm.user.mail = res.data.data.mail
            vm.user.employeeNumber = res.data.data.employeeNumber
            vm.user.description = res.data.data.description
            if (res.data.data.profileInaccessibility) {
              document.getElementById("user.displayName").readOnly = true
              document.getElementById("user.firstName").readOnly = true
              document.getElementById("user.lastName").readOnly = true
              document.getElementById("user.mobile").readOnly = true
              document.getElementById("user.mail").readOnly = true
              document.getElementById("user.employeeNumber").readOnly = true
              document.getElementById("user.description").readOnly = true
              document.getElementById("user.editButton").style = "display: none;"
            }
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "editUserInformation") {
        const query = new URLSearchParams(parameterObject).toString()
        this.loading = true
        this.axios({
          url: "/api/user" + "?" + query,
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          data: JSON.stringify({
            _id: vm.user._id,
            displayName: vm.user.displayName,
            firstName: vm.user.firstName,
            lastName: vm.user.lastName,
            mobile: vm.user.mobile,
            mail: vm.user.mail,
            employeeNumber: vm.user.employeeNumber,
            description: vm.user.description
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.profileRequestMaster("getUser")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "editUserPassword") {
        const query = new URLSearchParams(parameterObject).toString()
        this.loading = true
        this.axios({
          url: "/api/user/password" + "?" + query,
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          data: JSON.stringify({
            newPassword: vm.user.userPassword,
            token: vm.user.verificationCode
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.profileRequestMaster("getUser")
          } else if (res.data.status.code === 302) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          } else if (res.data.status.code === 403) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          } else if (res.data.status.code === 429) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "requestVerificationCode") {
        const query = new URLSearchParams(parameterObject).toString()
        this.axios({
          url: "/api/user/password/request" + "?" + query,
          method: "GET"
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.setCountdown(res.data.data * 60)
            document.getElementById("countdownButton").disabled = true
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
        })
      } else if (command === "getUserAvatar") {
        this.axios({
          url: "/api/user/photo",
          method: "GET"
        }).then((res) => {
          if (res.data !== "Problem" && res.data !== "NotExist") {
            vm.userAvatar = "/api/user/photo"
          }
        })
      } else if (command === "editUserAvatar") {
        const bodyFormData = new FormData()
        bodyFormData.append("file", document.getElementById("user.avatar").files[0])
        this.loading = true
        this.axios({
          url: "/api/user/photo",
          method: "POST",
          headers: { "Content-Type": "application/json" },
          data: bodyFormData
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.profileRequestMaster("getUserAvatar")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "deleteUserAvatar") {
        this.loading = true
        this.axios({
          url: "/api/user/photo",
          method: "DELETE"
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.profileRequestMaster("getUserAvatar")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      }
    },
    alertPromptMaster (title, message, icon, background) {
      let rtl = true
      if (this.$store.state.direction === "ltr") {
        rtl = false
      }
      iziToast.show({
        title: title,
        message: message,
        position: "center",
        icon: "pi " + icon,
        backgroundColor: background,
        transitionIn: "fadeInLeft",
        rtl: rtl,
        layout: 2,
        timeout: false,
        progressBar: false,
        buttons: [
          [
            "<button class='service-notification-button my-3' style='border-radius: 6px;'>" + this.$t("confirm") + "</button>",
            function (instance, toast) {
              instance.hide({
                transitionOut: "fadeOutRight"
              }, toast)
            }
          ]
        ]
      })
    },
    confirmPromptMaster (title, message, icon, background, functionCallback, callBackParameter) {
      let rtl = true
      if (this.$store.state.direction === "ltr") {
        rtl = false
      }
      iziToast.show({
        title: title,
        message: message,
        position: "center",
        icon: "pi " + icon,
        backgroundColor: background,
        transitionIn: "fadeInLeft",
        rtl: rtl,
        layout: 2,
        timeout: false,
        progressBar: false,
        buttons: [
          [
            "<button class='service-notification-button mx-3 my-3' style='background: #22C55E; color: #FFFFFF; border-radius: 6px;'>" + this.$t("yes") + "</button>",
            function (instance, toast) {
              instance.hide({
                transitionOut: "fadeOutRight"
              }, toast)
              functionCallback(callBackParameter)
            }
          ],
          [
            "<button class='service-notification-button mx-3 my-3' style='background: #EF4444; color: #FFFFFF; border-radius: 6px;'>" + this.$t("no") + "</button>",
            function (instance, toast) {
              instance.hide({
                transitionOut: "fadeOutRight"
              }, toast)
            }
          ]
        ]
      })
    },
    editUserInformationCheckup () {
      let errorCount = 0
      const emailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      const mobileRegex = /^(\+98|0)?9\d{9}$/
      if (this.user._id === "") {
        this.userErrors._id = "p-invalid"
        errorCount += 1
      } else {
        this.userErrors._id = ""
      }
      if (this.user.displayName === "") {
        this.userErrors.displayName = "p-invalid"
        errorCount += 1
      } else {
        this.userErrors.displayName = ""
      }
      if (this.user.firstName === "") {
        this.userErrors.firstName = "p-invalid"
        errorCount += 1
      } else {
        this.userErrors.firstName = ""
      }
      if (this.user.lastName === "") {
        this.userErrors.lastName = "p-invalid"
        errorCount += 1
      } else {
        this.userErrors.lastName = ""
      }
      if (this.user.mobile === "") {
        this.userErrors.mobile = "p-invalid"
        errorCount += 1
      } else {
        if (!mobileRegex.test(Number(this.user.mobile))) {
          this.userErrors.mobile = "p-invalid"
          errorCount += 1
        } else {
          this.userErrors.mobile = ""
        }
      }
      if (this.user.mail === "") {
        this.userErrors.mail = "p-invalid"
        errorCount += 1
      } else {
        if (!emailRegex.test(this.user.mail)) {
          this.userErrors.mail = "p-invalid"
          errorCount += 1
        } else {
          this.userErrors.mail = ""
        }
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("editUserInformation"), "pi-question-circle", "#F0EAAA", this.profileRequestMaster, "editUserInformation")
      }
    },
    editUserPasswordCheckup () {
      let errorCount = 0
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})/
      if (this.user.verificationCode === "") {
        this.userErrors.verificationCode = "p-invalid"
        errorCount += 1
      } else {
        this.userErrors.verificationCode = ""
      }
      if (this.user.userPassword === "") {
        this.userErrors.userPassword = "p-invalid"
        errorCount += 1
      } else {
        if (this.user.userPasswordRepeat === "") {
          this.userErrors.userPasswordRepeat = "p-invalid"
          errorCount += 1
        } else {
          if (this.user.userPassword !== this.user.userPasswordRepeat) {
            this.userErrors.userPassword = "p-invalid"
            this.userErrors.userPasswordRepeat = "p-invalid"
            errorCount += 1
          } else {
            if (!passwordRegex.test(this.user.userPassword)) {
              this.userErrors.userPassword = "p-invalid"
              errorCount += 1
            } else {
              this.userErrors.userPassword = ""
              this.userErrors.userPasswordRepeat = ""
            }
          }
        }
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("editPassword"), "pi-question-circle", "#F0EAAA", this.profileRequestMaster, "editUserPassword")
      }
    },
    deleteUserAvatarCheckup () {
      this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("deleteAvatar"), "pi-question-circle", "#F0EAAA", this.profileRequestMaster, "deleteUserAvatar")
    },
    editAvatarHelper () {
      document.getElementById("user.avatar").click()
    },
    setCountdown (duration) {
      let timer = duration
      let minutes, seconds
      const cd = setInterval(function () {
        minutes = parseInt(timer / 60, 10)
        seconds = parseInt(timer % 60, 10)
        minutes = minutes < 10 ? "0" + minutes : minutes
        seconds = seconds < 10 ? "0" + seconds : seconds
        if (typeof document.querySelector("#countdownButtonCD") !== "undefined") {
          document.querySelector("#countdownButtonCD").textContent = " (" + minutes + ":" + seconds + ") "
        }
        if (--timer < 0) {
          clearInterval(cd)
          document.querySelector("#countdownButtonCD").textContent = ""
          document.getElementById("countdownButton").disabled = false
        }
      }, 1000)
    },
    englishInputFilter ($event) {
      if ($event.type === "keypress") {
        const keyCode = ($event.keyCode ? $event.keyCode : $event.which)
        if (keyCode < 48 || keyCode > 122) {
          $event.preventDefault()
        } else if (keyCode > 57 && keyCode < 65) {
          $event.preventDefault()
        } else if (keyCode > 90 && keyCode < 97) {
          $event.preventDefault()
        }
      } else if ($event.type === "paste") {
        const text = $event.clipboardData.getData("text")
        for (let i = 0; i < text.length; ++i) {
          if (text[i].charCodeAt(0) < 48 || text[i].charCodeAt(0) > 122) {
            $event.preventDefault()
            break
          } else if (text[i].charCodeAt(0) > 57 && text[i].charCodeAt(0) < 65) {
            $event.preventDefault()
            break
          } else if (text[i].charCodeAt(0) > 90 && text[i].charCodeAt(0) < 97) {
            $event.preventDefault()
            break
          }
        }
      }
    },
    persianInputFilter ($event) {
      if ($event.type === "keypress") {
        const key = ($event.key ? $event.key : $event.which)
        const keyCode = ($event.keyCode ? $event.keyCode : $event.which)
        if (keyCode < 48 || keyCode > 57) {
          if (keyCode > 32 && keyCode < 65) {
            $event.preventDefault()
          } else if (keyCode > 90 && keyCode < 97) {
            $event.preventDefault()
          } else if (keyCode > 122 && keyCode < 127) {
            $event.preventDefault()
          } else if (!this.persianRex.text.test(key)) {
            $event.preventDefault()
          }
        }
      } else if ($event.type === "paste") {
        const text = $event.clipboardData.getData("text")
        for (let i = 0; i < text.length; ++i) {
          if (text[i].charCodeAt(0) < 48 || text[i].charCodeAt(0) > 57) {
            if (text[i].charCodeAt(0) > 32 && text[i].charCodeAt(0) < 65) {
              $event.preventDefault()
              break
            } else if (text[i].charCodeAt(0) > 90 && text[i].charCodeAt(0) < 97) {
              $event.preventDefault()
              break
            } else if (text[i].charCodeAt(0) > 122 && text[i].charCodeAt(0) < 127) {
              $event.preventDefault()
              break
            } else if (!this.persianRex.text.test(text[i])) {
              $event.preventDefault()
              break
            }
          }
        }
      }
    }
  }
}
</script>

<style scoped>
.avatar{
  width: 5.5rem;
  height: 5.5rem;
}
.avatarBorder{
  border-style: solid;
  border-width: medium;
}
.container{
  background-color: #dee2e6;
  color: #495057;
  border-radius: 6px;
  padding: .25rem;
}
</style>
