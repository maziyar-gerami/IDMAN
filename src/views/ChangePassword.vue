<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("changePassword") }}</h3>
        <div v-if="loading" class="text-center">
          <ProgressSpinner />
        </div>
        <div v-else>
          <div class="mt-3">
              <p>{{ $t("changePasswordText1") }}</p>
              <p>{{ $t("changePasswordText2") }}</p>
              <p>
                {{ $t("changePasswordText3") }}
                <span style="cursor: pointer; color: blue;" @click="$router.push('/resetpassword')">{{ $t("link") }}</span>
                {{ $t("changePasswordText4") }}
              </p>
          </div>
          <div v-if="getUsernamePassword" class="mt-5">
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="buffer._id">{{ $t("employeeNumber") }}<span style="color: red;"> * </span></label>
                  <InputText id="buffer._id" type="text" :class="bufferErrors._id" v-model="buffer._id" />
                </div>
              </div>
              <div class="field col">
                <div class="field p-fluid">
                  <label for="buffer.temporaryCode">{{ $t("temporaryCode") }}<span style="color: red;"> * </span></label>
                  <InputText id="buffer.temporaryCode" type="text" :class="bufferErrors.temporaryCode" v-model="buffer.temporaryCode" />
                </div>
              </div>
            </div>
          </div>
          <div v-bind:class="getUsernamePassword ? '' : 'mt-5' ">
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="buffer.password">{{ $t("password") }}<span style="color: red;"> * </span></label>
                  <Password id="buffer.password" :class="bufferErrors.password" v-model="buffer.password" :toggleMask="true" autocomplete="off">
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
                  <label for="buffer.passwordRepeat">{{ $t("passwordRepeat") }}<span style="color: red;"> * </span></label>
                  <Password id="buffer.passwordRepeat" :class="bufferErrors.passwordRepeat" v-model="buffer.passwordRepeat"
                  :toggleMask="true" onpaste="return false;" ondrop="return false;" autocomplete="off" />
                </div>
              </div>
            </div>
            <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="checkup()" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import iziToast from "@/assets/scripts/iziToast.min.js"

export default {
  name: "ChangePassword",
  data () {
    return {
      buffer: {
        _id: "",
        temporaryCode: "",
        password: "",
        passwordRepeat: ""
      },
      bufferErrors: {
        _id: "",
        temporaryCode: "",
        password: "",
        passwordRepeat: ""
      },
      loading: false,
      getUsernamePassword: false
    }
  },
  mounted () {
    const currentUrl = new URL(window.location.href)
    if (typeof currentUrl.searchParams.get("i") === "undefined" || currentUrl.searchParams.get("i") === null) {
      this.getUsernamePassword = true
    }
  },
  methods: {
    changePasswordRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "changePassword") {
        if (!this.getUsernamePassword) {
          const currentUrl = new URL(window.location.href)
          const tempInfo = decodeURIComponent(escape(window.atob(currentUrl.searchParams.get("i")))).split(" - ")
          this.buffer._id = tempInfo[0]
          this.buffer.temporaryCode = tempInfo[1]
        }
        this.loading = true
        this.axios({
          url: "/api/public/changePassword",
          method: "PUT",
          params: {
            lang: langCode
          },
          headers: { "Content-Type": "application/json" },
          data: JSON.stringify({
            userId: vm.buffer._id,
            currentPassword: vm.buffer.temporaryCode,
            newPassword: vm.buffer.password
          }
          ).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            let index = 0
            const customTimer = window.setInterval(function () {
              if (index === 1) {
                clearInterval(customTimer)
              }
              ++index
            }, 2000)
            vm.axios({
              url: "/cas/login?service=" + window.location.protocol + "//" + window.location.hostname + "/login/cas",
              method: "GET"
            }).then((res1) => {
              const loginPage = document.createElement("html")
              loginPage.innerHTML = res1.data
              const execution = loginPage.getElementsByTagName("form")[0].getElementsByTagName("input")[2].value
              const bodyFormData = new FormData()
              bodyFormData.append("username", vm.buffer._id)
              bodyFormData.append("password", vm.buffer.password)
              bodyFormData.append("execution", execution)
              bodyFormData.append("geolocation", "")
              bodyFormData.append("_eventId", "submit")
              vm.axios({
                url: "/cas/login",
                method: "POST",
                headers: { "Content-Type": "multipart/form-data" },
                data: bodyFormData
              }).then(() => {
                vm.$router.push("/")
              }).catch(() => {
                vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                vm.loading = false
              })
            }).catch(() => {
              vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
              vm.loading = false
            })
          } else if (res.data.status.code === 302) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          } else if (res.data.status.code === 403) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          } else if (res.data.status.code === 404) {
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
    checkup () {
      let errorCount = 0
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})/
      if (this.buffer.password === "") {
        this.bufferErrors.password = "p-invalid"
        errorCount += 1
      } else {
        if (this.buffer.passwordRepeat === "") {
          this.bufferErrors.passwordRepeat = "p-invalid"
          errorCount += 1
        } else {
          if (this.buffer.password !== this.buffer.passwordRepeat) {
            this.bufferErrors.password = "p-invalid"
            this.bufferErrors.passwordRepeat = "p-invalid"
            errorCount += 1
          } else {
            if (!passwordRegex.test(this.buffer.password)) {
              this.bufferErrors.password = "p-invalid"
              errorCount += 1
            } else {
              this.bufferErrors.password = ""
              this.bufferErrors.passwordRepeat = ""
            }
          }
        }
      }
      if (this.getUsernamePassword) {
        if (this.buffer._id === "") {
          this.bufferErrors._id = "p-invalid"
          errorCount += 1
        } else {
          this.bufferErrors._id = ""
        }
        if (this.buffer.temporaryCode === "") {
          this.bufferErrors.temporaryCode = "p-invalid"
          errorCount += 1
        } else {
          this.bufferErrors.temporaryCode = ""
        }
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.changePasswordRequestMaster("changePassword")
      }
    }
  }
}
</script>
