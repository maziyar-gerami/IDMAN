<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("settings") }}</h3>
        <div v-if="loading" class="text-center">
          <ProgressSpinner />
        </div>
        <div v-else>
          <Toolbar>
            <template #start>
              <Dropdown v-model="selectedBackup" :options="backupList" optionLabel="name" :placeholder="$t('chooseBackup')" />
              <Button icon="bx bx-revision bx-sm" class="p-button mx-1" @click="backupHelper('restoreBackup')" v-tooltip.top="$t('restore')" />
            </template>
            <template #end>
              <Button icon="bx bx-save bx-sm" class="p-button mx-1" @click="settingsRequestMaster('getBackup')" v-tooltip.top="$t('getBackup')" />
              <Button icon="bx bx-sync bx-sm" class="p-button-warning mx-1" @click="toggleRefresh($event)" v-tooltip.top="$t('refresh')" />
              <OverlayPanel ref="refresh">
                <div class="p-inputgroup mx-1">
                  <Button icon="bx bx-sync bx-sm" @click="refreshHelper()" v-tooltip.top="$t('refresh')" />
                  <Dropdown v-model="selectedRefresh" :options="refreshList" optionLabel="name" :placeholder="$t('select')" />
                </div>
              </OverlayPanel>
              <Button icon="bx bx-reset bx-sm" class="p-button-danger mx-1" @click="backupHelper('reset')" v-tooltip.top="$t('reset')" />
            </template>
          </Toolbar>
          <TabView v-model:activeIndex="tabActiveIndex">
            <TabPanel v-for="(group, index) in settings" :key="index" :header="group[0].group">
              <div class="formgrid grid">
                <div class="field col-6" v-for="setting in group" :key="setting._id">
                  <div class="field p-fluid">
                    <label :for="setting._id" class="flex">
                      {{ setting.description }}
                      <span v-tooltip.top="setting.help" class="flex align-items-center justify-content-center text-white border-circle mx-2" style="background-color: #007bff;">
                        <i class="bx bx-question-mark bx-sm" />
                      </span>
                    </label>
                    <InputText v-if="setting.type.class === 'string' || setting.type.class === 'path' || setting.type.class === 'url'" :id="setting._id" type="text" v-model="setting.value" />
                    <div v-else-if="setting.type.class === 'integer'" :dir="$store.state.reverseDirection"><InputNumber :id="setting._id" v-model="setting.value" showButtons mode="decimal" /></div>
                    <InputSwitch v-else-if="setting.type.class === 'switch'" v-model="setting.value" :id="setting._id" />
                    <Dropdown v-else-if="setting._id === 'SMS.SDK'" v-model="setting.value" :id="setting._id" :options="setting.type.values" :placeholder="setting.value" @change="settingsRequestMaster('editSMSSetting')" />
                    <Dropdown v-else-if="setting.type.class === 'list'" v-model="setting.value" :id="setting._id" :options="setting.type.values" :placeholder="setting.value" />
                  </div>
                </div>
              </div>
            </TabPanel>
          </TabView>
          <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="backupHelper('editSettings')" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import iziToast from "@/assets/scripts/iziToast.min.js"

export default {
  name: "Settings",
  data () {
    return {
      settings: [],
      refreshList: [
        {
          value: "services",
          name: this.$t("refreshServices")
        },
        {
          value: "users",
          name: this.$t("refreshUsers")
        },
        {
          value: "captcha",
          name: this.$t("refreshCAPTCHA")
        },
        {
          value: "",
          name: this.$t("refreshAll")
        }
      ],
      backupList: [],
      selectedBackup: {},
      selectedRefresh: {},
      tabActiveIndex: 0,
      loading: false
    }
  },
  mounted () {
    this.settingsRequestMaster("getSettings")
    this.settingsRequestMaster("getBackupList")
  },
  watch: {
    "$i18n.locale": {
      handler: function () {
        this.settingsRequestMaster("getSettings")
        this.settingsRequestMaster("getBackupList")
      },
      deep: true
    }
  },
  methods: {
    settingsRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getSettings") {
        const groups = []
        this.settings = []
        this.loading = true
        this.axios({
          url: "/api/properties/settings",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            for (const i in res.data.data) {
              if (res.data.data[i].type.class === "switch") {
                if (res.data.data[i].value === "true") {
                  res.data.data[i].value = true
                } else if (res.data.data[i].value === "false") {
                  res.data.data[i].value = false
                }
              } else if (res.data.data[i].type.class === "integer") {
                res.data.data[i].value = parseInt(res.data.data[i].value)
              }
              if (groups.indexOf(res.data.data[i].group) === -1) {
                groups.push(res.data.data[i].group)
                vm.settings.push([res.data.data[i]])
              } else {
                vm.settings[groups.indexOf(res.data.data[i].group)].push(res.data.data[i])
              }
            }
            vm.loading = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "editSettings") {
        const editList = []
        for (const i in this.settings) {
          for (const j in this.settings[i]) {
            if (this.settings[i][j].type.class === "switch") {
              if (this.settings[i][j].value) {
                this.settings[i][j].value = "true"
              } else {
                this.settings[i][j].value = "false"
              }
            } else if (this.settings[i][j].type.class === "integer") {
              this.settings[i][j].value = this.settings[i][j].value.toString()
            }
            editList.push({ _id: this.settings[i][j]._id, value: this.settings[i][j].value })
          }
        }
        this.loading = true
        this.axios({
          url: "/api/properties/settings",
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify(
            editList
          ).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.settingsRequestMaster("getSettings")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
            vm.settingsRequestMaster("getSettings")
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
          vm.settingsRequestMaster("getSettings")
        })
      } else if (command === "editSMSSetting") {
        const editList = []
        for (const i in this.settings) {
          for (const j in this.settings[i]) {
            if (this.settings[i][j]._id === "SMS.SDK") {
              editList.push({ _id: this.settings[i][j]._id, value: this.settings[i][j].value })
            }
          }
        }
        this.loading = true
        this.axios({
          url: "/api/properties/settings",
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify(
            editList
          ).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.settingsRequestMaster("getSettings")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
            vm.settingsRequestMaster("getSettings")
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
          vm.settingsRequestMaster("getSettings")
        })
      } else if (command === "getBackupList") {
        this.loading = true
        this.axios({
          url: "/api/properties/settings/backup",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.backupList = res.data.data
            vm.loading = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "getBackup") {
        this.loading = true
        this.axios({
          url: "/api/properties/settings/backup",
          method: "POST",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.settingsRequestMaster("getBackupList")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "restoreBackup") {
        this.loading = true
        this.axios({
          url: "/api/properties/settings/restore",
          method: "GET",
          params: {
            id: vm.selectedBackup._id,
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.settingsRequestMaster("getSettings")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "reset") {
        this.loading = true
        this.axios({
          url: "/api/properties/settings/reset",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.settingsRequestMaster("getSettings")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "refresh") {
        this.loading = true
        this.axios({
          url: "/api/refresh",
          method: "GET",
          params: {
            type: vm.selectedRefresh.value,
            lang: langCode
          }
        }).then((res) => {
          if (typeof res.data.status !== "undefined") {
            if (res.data.status.code === 200) {
              vm.alertPromptMaster(res.data.status.result, "", "pi-check-circle", "#A2E1B1")
            } else {
              vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            }
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-check-circle", "#FDB5BA")
          }
          vm.loading = false
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-check-circle", "#FDB5BA")
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
    backupHelper (command) {
      if (command === "editSettings") {
        this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("edit") + " " + this.$t("settings"), "pi-question-circle", "#F0EAAA", this.settingsRequestMaster, "editSettings")
      } else if (command === "restoreBackup") {
        if (Object.keys(this.selectedBackup).length !== 0) {
          this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("restore") + " " + this.$t("backup"), "pi-question-circle", "#F0EAAA", this.settingsRequestMaster, "restoreBackup")
        } else {
          this.alertPromptMaster(this.$t("noBackupSelected"), "", "pi-exclamation-triangle", "#FDB5BA")
        }
      } else if (command === "reset") {
        this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("reset"), "pi-question-circle", "#F0EAAA", this.settingsRequestMaster, "reset")
      }
    },
    refreshHelper () {
      if (Object.keys(this.selectedRefresh).length !== 0) {
        this.confirmPromptMaster(this.$t("confirmPromptText"), this.selectedRefresh.name, "pi-question-circle", "#F0EAAA", this.settingsRequestMaster, "refresh")
      } else {
        this.alertPromptMaster(this.$t("noRefreshSelected"), "", "pi-exclamation-triangle", "#FDB5BA")
      }
    },
    toggleRefresh (event) {
      this.$refs.refresh.toggle(event)
    }
  }
}
</script>
