<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("resetPassword") }}</h3>
        <TabView v-model:activeIndex="tabActiveIndex">
          <TabPanel :header="$t('employeeNumber')">
            <div v-if="idState === 'getId'">
              <div class="formgrid grid">
                <div class="field col-6">
                  <div class="field p-fluid">
                    <label for="idBuffer._id">{{ $t("employeeNumber") }}<span style="color: red;"> * </span></label>
                    <InputText id="idBuffer._id" type="text" :class="idBufferErrors._id" v-model="idBuffer._id" v-tooltip.top="$t('resetPasswordText1')" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col-3">
                  <div class="field p-fluid">
                    <label for="idBuffer.captchaAnswer">{{ $t("captcha") }}<span style="color: red;"> * </span></label>
                    <InputText id="idBuffer.captchaAnswer" type="text" :class="idBufferErrors.captchaAnswer" v-model="idBuffer.captchaAnswer" />
                  </div>
                </div>
                <div class="field col-3">
                  <div class="flex align-content-center flex-wrap h-full">
                    <img style="width: 170px; height: 60px;" v-bind:src="idBuffer.captchaSource">
                    <Button icon="bx bx-refresh bx-sm" class="p-button-rounded mx-2" @click="resetPasswordRequestMaster('idGetCaptcha')" v-tooltip.top="$t('reload')" />
                  </div>
                </div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="idSendIdCheckup()" />
            </div>
            <div v-else-if="idState === 'getCode'">
              <p class="mt-3">{{ $t("resetPasswordText2") }}</p>
              <div class="formgrid grid">
                <div class="field col-6">
                  <div class="field p-fluid">
                    <label for="idBuffer.code">{{ $t("verificationCode") }}<span style="color: red;"> * </span></label>
                    <input id="idBuffer.code" type="text" v-model="idBuffer.code" class="hidden" autocomplete="off">
                    <div class="flex-c-m digit-group">
                      <input type="text" id="digit-6" v-model="idBuffer.digit6" class="digit-member" data-previous="digit-5" autocomplete="off" />
                      <input type="text" id="digit-5" v-model="idBuffer.digit5" class="digit-member" data-next="digit-6" data-previous="digit-4" autocomplete="off" />
                      <input type="text" id="digit-4" v-model="idBuffer.digit4" class="digit-member" data-next="digit-5" data-previous="digit-3" autocomplete="off" />
                      <input type="text" id="digit-3" v-model="idBuffer.digit3" class="digit-member" data-next="digit-4" data-previous="digit-2" autocomplete="off" />
                      <input type="text" id="digit-2" v-model="idBuffer.digit2" class="digit-member" data-next="digit-3" data-previous="digit-1" autocomplete="off" />
                      <input type="text" id="digit-1" v-model="idBuffer.digit1" class="digit-member" data-next="digit-2" autocomplete="off" />
                    </div>
                  </div>
                </div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="idSendCodeCheckup()" />
              <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('id')" />
            </div>
            <div v-else-if="idState === 'getPassword'">
              <p class="mt-3">{{ $t("resetPasswordText3") }}</p>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="idBuffer.password">{{ $t("password") }}<span style="color: red;"> * </span></label>
                    <Password id="idBuffer.password" :class="idBufferErrors.password" v-model="idBuffer.password" :toggleMask="true" autocomplete="off">
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
                    <label for="idBuffer.passwordRepeat">{{ $t("passwordRepeat") }}<span style="color: red;"> * </span></label>
                    <Password id="idBuffer.passwordRepeat" :class="idBufferErrors.passwordRepeat" v-model="idBuffer.passwordRepeat"
                    :toggleMask="true" onpaste="return false;" ondrop="return false;" autocomplete="off" />
                  </div>
                </div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="idResetPasswordCheckup()" />
              <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('id')" />
            </div>
            <div v-else-if="idState === 'loading'" class="text-center">
              <ProgressSpinner />
            </div>
          </TabPanel>
          <TabPanel :header="$t('mobile')">
            <div v-if="mobileState === 'getMobile'">
              <div class="formgrid grid">
                <div class="field col-6">
                  <div class="field p-fluid">
                    <label for="mobileBuffer.mobile">{{ $t("mobile") }}<span style="color: red;"> * </span></label>
                    <InputText id="mobileBuffer.mobile" type="text" :class="mobileBufferErrors.mobile" v-model="mobileBuffer.mobile" v-tooltip.top="$t('resetPasswordText4')" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col-3">
                  <div class="field p-fluid">
                    <label for="mobileBuffer.captchaAnswer">{{ $t("captcha") }}<span style="color: red;"> * </span></label>
                    <InputText id="mobileBuffer.captchaAnswer" type="text" :class="mobileBufferErrors.captchaAnswer" v-model="mobileBuffer.captchaAnswer" />
                  </div>
                </div>
                <div class="field col-3">
                  <div class="flex align-content-center flex-wrap h-full">
                    <img style="width: 170px; height: 60px;" v-bind:src="mobileBuffer.captchaSource">
                    <Button icon="bx bx-refresh bx-sm" class="p-button-rounded mx-2" @click="resetPasswordRequestMaster('mobileGetCaptcha')" v-tooltip.top="$t('reload')" />
                  </div>
                </div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="mobileSendMobileCheckup()" />
            </div>
            <div v-if="mobileState === 'getId'">
              <div class="formgrid grid">
                <div class="field col-6">
                  <div class="field p-fluid">
                    <label for="mobileBuffer._id">{{ $t("employeeNumber") }}<span style="color: red;"> * </span></label>
                    <InputText id="mobileBuffer._id" type="text" :class="mobileBufferErrors._id" v-model="mobileBuffer._id" />
                  </div>
                </div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="mobileSendIdCheckup()" />
              <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('mobile')" />
            </div>
            <div v-else-if="mobileState === 'getCode'">
              <p class="mt-3">{{ $t("resetPasswordText2") }}</p>
              <div class="formgrid grid">
                <div class="field col-6">
                  <div class="field p-fluid">
                    <label for="mobileBuffer.code">{{ $t("verificationCode") }}<span style="color: red;"> * </span></label>
                    <input id="mobileBuffer.code" type="text" v-model="mobileBuffer.code" class="hidden" autocomplete="off">
                    <div class="flex-c-m digit-group">
                      <input type="text" id="digit-6-1" v-model="mobileBuffer.digit6" class="digit-member" data-previous="digit-5-1" autocomplete="off" />
                      <input type="text" id="digit-5-1" v-model="mobileBuffer.digit5" class="digit-member" data-next="digit-6-1" data-previous="digit-4-1" autocomplete="off" />
                      <input type="text" id="digit-4-1" v-model="mobileBuffer.digit4" class="digit-member" data-next="digit-5-1" data-previous="digit-3-1" autocomplete="off" />
                      <input type="text" id="digit-3-1" v-model="mobileBuffer.digit3" class="digit-member" data-next="digit-4-1" data-previous="digit-2-1" autocomplete="off" />
                      <input type="text" id="digit-2-1" v-model="mobileBuffer.digit2" class="digit-member" data-next="digit-3-1" data-previous="digit-1-1" autocomplete="off" />
                      <input type="text" id="digit-1-1" v-model="mobileBuffer.digit1" class="digit-member" data-next="digit-2-1" autocomplete="off" />
                    </div>
                  </div>
                </div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="mobileSendCodeCheckup()" />
              <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('mobile')" />
            </div>
            <div v-else-if="mobileState === 'getPassword'">
              <p class="mt-3">{{ $t("resetPasswordText3") }}</p>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="mobileBuffer.password">{{ $t("password") }}<span style="color: red;"> * </span></label>
                    <Password id="mobileBuffer.password" :class="mobileBufferErrors.password" v-model="mobileBuffer.password" :toggleMask="true" autocomplete="off">
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
                    <label for="mobileBuffer.passwordRepeat">{{ $t("passwordRepeat") }}<span style="color: red;"> * </span></label>
                    <Password id="mobileBuffer.passwordRepeat" :class="mobileBufferErrors.passwordRepeat" v-model="mobileBuffer.passwordRepeat"
                    :toggleMask="true" onpaste="return false;" ondrop="return false;" autocomplete="off" />
                  </div>
                </div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="mobileResetPasswordCheckup()" />
              <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('mobile')" />
            </div>
            <div v-else-if="mobileState === 'loading'" class="text-center">
              <ProgressSpinner />
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
  name: "ResetPassword",
  data () {
    return {
      idBuffer: {
        _id: "",
        code: "",
        captchaSource: "",
        captchaId: "",
        captchaAnswer: "",
        digit1: "",
        digit2: "",
        digit3: "",
        digit4: "",
        digit5: "",
        digit6: "",
        password: "",
        passwordRepeat: ""
      },
      mobileBuffer: {
        mobile: "",
        _id: "",
        code: "",
        captchaSource: "",
        captchaId: "",
        captchaAnswer: "",
        digit1: "",
        digit2: "",
        digit3: "",
        digit4: "",
        digit5: "",
        digit6: "",
        password: "",
        passwordRepeat: ""
      },
      idBufferErrors: {
        _id: "",
        captchaAnswer: "",
        password: "",
        passwordRepeat: ""
      },
      mobileBufferErrors: {
        mobile: "",
        _id: "",
        captchaAnswer: "",
        password: "",
        passwordRepeat: ""
      },
      tabActiveIndex: 0,
      idState: "getId",
      mobileState: "getMobile"
    }
  },
  mounted () {
    const checkExist = setInterval(function () {
      // eslint-disable-next-line no-undef
      if (jQuery(".digit-group").length) {
        // eslint-disable-next-line no-undef
        jQuery(".digit-group").find(".digit-member").each(function () {
          // eslint-disable-next-line no-undef
          jQuery(this).attr("maxlength", 1)
          const list = ["a", "b", "c", "d", "e", "f", "g", "h", "i"]
          // eslint-disable-next-line no-undef
          jQuery(this).on("keypress", function (e) {
            if (list.includes(String.fromCharCode(e.keyCode))) {
              e.preventDefault()
            } else if ((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105) || e.keyCode === 8 || e.keyCode === 37 || e.keyCode === 39) {
            } else {
              e.preventDefault()
            }
          })
          // eslint-disable-next-line no-undef
          jQuery(this).on("keyup", function (e) {
            // eslint-disable-next-line no-undef
            const parent = jQuery(jQuery(this).parent())

            if (e.keyCode === 8 || e.keyCode === 37) {
              // eslint-disable-next-line no-undef
              const prev = parent.find("input#" + jQuery(this).data("previous"))

              if (prev.length) {
                // eslint-disable-next-line no-undef
                jQuery(prev).select()
              }
            } else if ((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105) || e.keyCode === 39) {
              // eslint-disable-next-line no-undef
              const next = parent.find("input#" + jQuery(this).data("next"))

              if (next.length) { // eslint-disable-next-line no-undef
                jQuery(next).select()
              } else {
                if (parent.data("autosubmit")) {
                  parent.submit()
                }
              }
            }
          })
        })
        clearInterval(checkExist)
      }
    }, 100)
    this.resetPasswordRequestMaster("idGetCaptcha")
    this.resetPasswordRequestMaster("mobileGetCaptcha")
  },
  methods: {
    resetPasswordRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "idGetCaptcha") {
        this.idState = "loading"
        this.axios({
          url: "/api/captcha/request",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.idBuffer.captchaId = res.data.data.id
            vm.idBuffer.captchaSource = "data:image/png;base64," + res.data.data.img
            vm.idState = "getId"
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.idState = "getId"
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.idState = "getId"
        })
      } else if (command === "idSendId") {
        this.idState = "loading"
        this.axios({
          url: "/api/public/sendTokenUser/" + vm.idBuffer._id + "/" + vm.idBuffer.captchaId + "/" + vm.idBuffer.captchaAnswer,
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.idState = "getCode"
          } else if (res.data.status.code === 404) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.idState = "getId"
          } else if (res.data.status.code === 417) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.idState = "getId"
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.idState = "getId"
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.idState = "getId"
        })
      } else if (command === "idSendCode") {
        this.idBuffer.code = this.idBuffer.digit1 + this.idBuffer.digit2 + this.idBuffer.digit3 +
        this.idBuffer.digit4 + this.idBuffer.digit5 + this.idBuffer.digit6
        this.idState = "loading"
        this.axios({
          url: "/api/public/validateMessageToken/" + vm.idBuffer._id + "/" + vm.idBuffer.code,
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.idState = "getPassword"
          } else if (res.data.status.code === 400) {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.idState = "getCode"
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.idState = "getCode"
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.idState = "getCode"
        })
      } else if (command === "idResetPassword") {
        this.idState = "loading"
        this.axios({
          url: "/api/public/resetPass",
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            newPassword: vm.idBuffer.password,
            token: vm.idBuffer.code,
            userId: vm.idBuffer._id
          }).replace(/\\\\/g, "\\")
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
              bodyFormData.append("username", vm.idBuffer._id)
              bodyFormData.append("password", vm.idBuffer.password)
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
                vm.idState = "getPassword"
              })
            }).catch(() => {
              vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
              vm.idState = "getPassword"
            })
          } else if (res.data.status.code === 403) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.idState = "getPassword"
          } else if (res.data.status.code === 429) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.idState = "getPassword"
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.idState = "getPassword"
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.idState = "getPassword"
        })
      } else if (command === "mobileGetCaptcha") {
        this.mobileState = "loading"
        this.axios({
          url: "/api/captcha/request",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.mobileBuffer.captchaId = res.data.data.id
            vm.mobileBuffer.captchaSource = "data:image/png;base64," + res.data.data.img
            vm.mobileState = "getMobile"
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.mobileState = "getMobile"
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.mobileState = "getMobile"
        })
      } else if (command === "mobileSendMobile") {
        this.mobileState = "loading"
        this.axios({
          url: "/api/public/sendSMS",
          method: "GET",
          params: {
            mobile: vm.mobileBuffer.mobile,
            cid: vm.mobileBuffer.captchaId,
            answer: vm.mobileBuffer.captchaAnswer,
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.mobileBuffer._id = res.data.data.userId
            vm.mobileState = "getCode"
          } else if (res.data.status.code === 300) {
            vm.mobileState = "getId"
          } else if (res.data.status.code === 404) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.mobileState = "getMobile"
          } else if (res.data.status.code === 417) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.mobileState = "getMobile"
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.mobileState = "getMobile"
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.mobileState = "getMobile"
        })
      } else if (command === "mobileSendId") {
        this.mobileState = "loading"
        this.axios({
          url: "/api/public/sendSMS",
          method: "GET",
          params: {
            uid: vm.mobileBuffer._id,
            mobile: vm.mobileBuffer.mobile,
            cid: vm.mobileBuffer.captchaId,
            answer: vm.mobileBuffer.captchaAnswer,
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.mobileState = "getCode"
          } else if (res.data.status.code === 404) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.mobileState = "getId"
          } else if (res.data.status.code === 417) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.mobileState = "getId"
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.mobileState = "getId"
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.mobileState = "getId"
        })
      } else if (command === "mobileSendCode") {
        this.mobileBuffer.code = this.mobileBuffer.digit1 + this.mobileBuffer.digit2 + this.mobileBuffer.digit3 +
        this.mobileBuffer.digit4 + this.mobileBuffer.digit5 + this.mobileBuffer.digit6
        this.mobileState = "loading"
        this.axios({
          url: "/api/public/validateMessageToken/" + vm.mobileBuffer._id + "/" + vm.mobileBuffer.code,
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.mobileState = "getPassword"
          } else if (res.data.status.code === 400) {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.mobileState = "getCode"
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.mobileState = "getCode"
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.mobileState = "getCode"
        })
      } else if (command === "mobileResetPassword") {
        this.mobileState = "loading"
        this.axios({
          url: "/api/public/resetPass",
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            newPassword: vm.mobileBuffer.password,
            token: vm.mobileBuffer.code,
            userId: vm.mobileBuffer._id
          }).replace(/\\\\/g, "\\")
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
              bodyFormData.append("username", vm.mobileBuffer._id)
              bodyFormData.append("password", vm.mobileBuffer.password)
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
                vm.mobileState = "getPassword"
              })
            }).catch(() => {
              vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
              vm.mobileState = "getPassword"
            })
          } else if (res.data.status.code === 403) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.mobileState = "getPassword"
          } else if (res.data.status.code === 429) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.mobileState = "getPassword"
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.mobileState = "getPassword"
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.mobileState = "getPassword"
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
    resetState (tab) {
      if (tab === "id") {
        this.idState = "getId"
        this.resetPasswordRequestMaster("idGetCaptcha")
      } else if (tab === "mobile") {
        this.mobileState = "getMobile"
        this.resetPasswordRequestMaster("mobileGetCaptcha")
      }
    },
    idSendIdCheckup () {
      let errorCount = 0
      if (this.idBuffer._id === "") {
        this.idBufferErrors._id = "p-invalid"
        errorCount += 1
      } else {
        this.idBufferErrors._id = ""
      }
      if (this.idBuffer.captchaAnswer === "") {
        this.idBufferErrors.captchaAnswer = "p-invalid"
        errorCount += 1
      } else {
        this.idBufferErrors.captchaAnswer = ""
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.resetPasswordRequestMaster("idSendId")
      }
    },
    idSendCodeCheckup () {
      if (this.idBuffer.digit1 === "" || this.idBuffer.digit2 === "" || this.idBuffer.digit3 === "" ||
      this.idBuffer.digit4 === "" || this.idBuffer.digit5 === "" || this.idBuffer.digit6 === "") {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.resetPasswordRequestMaster("idSendCode")
      }
    },
    idResetPasswordCheckup () {
      let errorCount = 0
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})/
      if (this.idBuffer.password === "") {
        this.idBufferErrors.password = "p-invalid"
        errorCount += 1
      } else {
        if (this.idBuffer.passwordRepeat === "") {
          this.idBufferErrors.passwordRepeat = "p-invalid"
          errorCount += 1
        } else {
          if (this.idBuffer.password !== this.idBuffer.passwordRepeat) {
            this.idBufferErrors.password = "p-invalid"
            this.idBufferErrors.passwordRepeat = "p-invalid"
            errorCount += 1
          } else {
            if (!passwordRegex.test(this.idBuffer.password)) {
              this.idBufferErrors.password = "p-invalid"
              errorCount += 1
            } else {
              this.idBufferErrors.password = ""
              this.idBufferErrors.passwordRepeat = ""
            }
          }
        }
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.resetPasswordRequestMaster("idResetPassword")
      }
    },
    mobileSendMobileCheckup () {
      let errorCount = 0
      if (this.mobileBuffer.mobile === "") {
        this.mobileBufferErrors.mobile = "p-invalid"
        errorCount += 1
      } else {
        this.mobileBufferErrors.mobile = ""
      }
      if (this.mobileBuffer.captchaAnswer === "") {
        this.mobileBufferErrors.captchaAnswer = "p-invalid"
        errorCount += 1
      } else {
        this.mobileBufferErrors.captchaAnswer = ""
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.resetPasswordRequestMaster("mobileSendMobile")
      }
    },
    mobileSendIdCheckup () {
      let errorCount = 0
      if (this.mobileBuffer._id === "") {
        this.mobileBufferErrors._id = "p-invalid"
        errorCount += 1
      } else {
        this.mobileBufferErrors._id = ""
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.resetPasswordRequestMaster("mobileSendId")
      }
    },
    mobileSendCodeCheckup () {
      if (this.mobileBuffer.digit1 === "" || this.mobileBuffer.digit2 === "" || this.mobileBuffer.digit3 === "" ||
      this.mobileBuffer.digit4 === "" || this.mobileBuffer.digit5 === "" || this.mobileBuffer.digit6 === "") {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.resetPasswordRequestMaster("mobileSendCode")
      }
    },
    mobileResetPasswordCheckup () {
      let errorCount = 0
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})/
      if (this.mobileBuffer.password === "") {
        this.mobileBufferErrors.password = "p-invalid"
        errorCount += 1
      } else {
        if (this.mobileBuffer.passwordRepeat === "") {
          this.mobileBufferErrors.passwordRepeat = "p-invalid"
          errorCount += 1
        } else {
          if (this.mobileBuffer.password !== this.mobileBuffer.passwordRepeat) {
            this.mobileBufferErrors.password = "p-invalid"
            this.mobileBufferErrors.passwordRepeat = "p-invalid"
            errorCount += 1
          } else {
            if (!passwordRegex.test(this.mobileBuffer.password)) {
              this.mobileBufferErrors.password = "p-invalid"
              errorCount += 1
            } else {
              this.mobileBufferErrors.password = ""
              this.mobileBufferErrors.passwordRepeat = ""
            }
          }
        }
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.resetPasswordRequestMaster("mobileResetPassword")
      }
    }
  }
}
</script>

<style scoped>
.digit-member {
  width: 30px;
  height: 50px;
  background-color: #ffffff;
  border: 1px solid #1e3994;
  line-height: 1.2;
  text-align: center;
  font-size: 25px;
  font-weight: 200;
  color: #555555;
  margin: 0 2px;
  border-radius: 10px;
}

.digit-member:focus {
  border: 1px solid #f00;
}
</style>
