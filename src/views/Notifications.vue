<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("notifications") }}</h3>
        <TabView v-model:activeIndex="tabActiveIndex">
          <TabPanel :header="$t('notificationsList')">
            <Toolbar>
              <template #start>
                <Button :label="$t('new')" icon="pi pi-plus" class="p-button-success mx-1" @click="createNotification()" />
                <Button :label="$t('delete')" icon="pi pi-trash" class="p-button-danger mx-1" @click="notificationsToolbar('delete')" />
              </template>
            </Toolbar>
            <DataTable :value="notifications" dataKey="id" :loading="loading" scrollDirection="vertical"
            v-model:selection="selectedNotifications" class="p-datatable-gridlines" :rowHover="true"
            responsiveLayout="scroll" :scrollable="false" scrollHeight="50vh" paginatorPosition="top"
            :paginator="true" :rows="20" :rowsPerPageOptions="[10,20,50,100,500]" :pageLinkSize="5">
              <template #empty>
                <div class="text-right">
                  {{ $t("noNotificationFound") }}
                </div>
              </template>
              <template #loading>
                <div class="text-right">
                  {{ $t("loadingNotifications") }}
                </div>
              </template>
              <Column selectionMode="multiple" bodyClass="text-center" style="width:5rem;"></Column>
              <Column field="title" :header="$t('title')" bodyClass="text-center" style="flex: 0 0 12rem">
                <template #body="{data}">
                  {{ data.title }}
                </template>
              </Column>
              <Column field="creator" :header="$t('creator')" bodyClass="text-center" style="flex: 0 0 8rem">
                <template #body="{data}">
                  {{ data.creator }}
                </template>
              </Column>
              <Column field="visible" :header="$t('visible')" bodyClass="text-center" style="flex: 0 0 5rem">
                <template #body="{data}">
                  <i v-if="data.visible" class="bx bxs-check-circle bx-md" style="color: #1ca70a;" />
                  <i v-else class="bx bxs-x-circle bx-md" style="color: #dc3545;" />
                </template>
              </Column>
              <Column field="createDateString" :header="$t('date')" bodyClass="text-center" style="flex: 0 0 7rem">
                <template #body="{data}">
                  {{ data.createDateString }}
                </template>
              </Column>
              <Column field="createTimeString" :header="$t('time')" bodyClass="text-center" style="flex: 0 0 7rem">
                <template #body="{data}">
                  {{ data.createTimeString }}
                </template>
              </Column>
              <Column bodyStyle="display: flex;" bodyClass="flex justify-content-evenly flex-wrap card-container text-center w-full" style="flex: 0 0 13rem">
                <template #body="{data}">
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="pi pi-pencil" class="p-button-rounded p-button-warning p-button-outlined mx-1" @click="editNotification(data.messageId)" v-tooltip.top="$t('edit')" />
                  </div>
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="pi pi-trash" class="p-button-rounded p-button-danger p-button-outlined mx-1" @click="deleteNotification(data.messageId)" v-tooltip.top="$t('delete')" />
                  </div>
                </template>
              </Column>
            </DataTable>
          </TabPanel>
          <TabPanel v-if="createNotificationFlag" :header="$t('createNotification')">
            <div v-if="createNotificationLoader" class="text-center">
              <ProgressSpinner />
            </div>
            <div v-else>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createNotification.title">{{ $t("title") }}<span style="color: red;"> * </span></label>
                    <InputText id="createNotification.title" type="text" :class="createNotificationErrors.title" v-model="createNotificationBuffer.title" />
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createNotification.visible" class="flex">{{ $t("visible") }}</label>
                    <InputSwitch id="createNotification.visible" v-model="createNotificationBuffer.visible" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createNotification.body">{{ $t("body") }}</label>
                    <Editor id="createNotification.body" v-model="createNotificationBuffer.body" editorStyle="height: 320px" :dir="$store.state.reverseDirection" />
                  </div>
                </div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="createNotificationCheckup()" />
              <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('createNotification')" />
            </div>
          </TabPanel>
          <TabPanel v-if="editNotificationFlag" :header="$t('editNotification')">
            <div v-if="editNotificationLoader" class="text-center">
              <ProgressSpinner />
            </div>
            <div v-else>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editNotification.messageId">{{ $t("id") }}</label>
                    <InputText id="editNotification.messageId" type="text" v-model="editNotificationBuffer.messageId" disabled />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editNotification.creator">{{ $t("creatorId") }}</label>
                    <InputText id="editNotification.creator" type="text" v-model="editNotificationBuffer.creator" disabled />
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editNotification.updater">{{ $t("editorId") }}</label>
                    <InputText id="editNotification.updater" type="text" v-model="editNotificationBuffer.updater" disabled />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editNotification.createDateTime">{{ $t("createDateTime") }}</label>
                    <InputText id="editNotification.createDateTime" type="text" v-model="editNotificationBuffer.createDateTimeString" disabled />
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editNotification.editDateTime">{{ $t("editDateTime") }}</label>
                    <InputText id="editNotification.editDateTime" type="text" v-model="editNotificationBuffer.updateDateTimeString" disabled />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editNotification.title">{{ $t("title") }}<span style="color: red;"> * </span></label>
                    <InputText id="editNotification.title" type="text" :class="editNotificationErrors.title" v-model="editNotificationBuffer.title" />
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editNotification.visible" class="flex">{{ $t("visible") }}</label>
                    <InputSwitch id="editNotification.visible" v-model="editNotificationBuffer.visible" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editNotification.body">{{ $t("body") }}</label>
                    <Editor id="editNotification.body" v-model="editNotificationBuffer.body" editorStyle="height: 320px" :dir="$store.state.reverseDirection" />
                  </div>
                </div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="editNotificationCheckup()" />
              <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('editNotification')" />
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
  name: "Notifications",
  data () {
    return {
      notifications: [],
      selectedNotifications: [],
      createNotificationBuffer: {
        title: "",
        visible: false,
        body: ""
      },
      editNotificationBuffer: {
        messageId: "",
        creator: "",
        title: "",
        visible: false,
        body: ""
      },
      createNotificationErrors: {
        title: ""
      },
      editNotificationErrors: {
        title: ""
      },
      tabActiveIndex: 0,
      notificationToolbarBuffer: "",
      loading: true,
      createNotificationFlag: false,
      editNotificationFlag: false,
      createNotificationLoader: false,
      editNotificationLoader: false
    }
  },
  mounted () {
    this.notificationsRequestMaster("getNotifications")
  },
  methods: {
    notificationsRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getNotifications") {
        this.loading = true
        this.axios({
          url: "/api/users/publicMessages",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.notifications = res.data.data
            for (const i in vm.notifications) {
              vm.notifications[i].createDateString = vm.notifications[i].createTime.year + "/" + vm.notifications[i].createTime.month + "/" + vm.notifications[i].createTime.day
              vm.notifications[i].createTimeString = vm.notifications[i].createTime.hours + ":" + vm.notifications[i].createTime.minutes + ":" + vm.notifications[i].createTime.seconds
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
      } else if (command === "getNotification") {
        this.editNotificationLoader = true
        this.axios({
          url: "/api/users/publicMessages",
          method: "GET",
          params: {
            id: vm.editNotificationBuffer.messageId,
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.editNotificationBuffer = res.data.data[0]
            vm.editNotificationBuffer.createDateString = vm.editNotificationBuffer.createTime.year + "/" + vm.editNotificationBuffer.createTime.month + "/" + vm.editNotificationBuffer.createTime.day
            vm.editNotificationBuffer.createTimeString = vm.editNotificationBuffer.createTime.hours + ":" + vm.editNotificationBuffer.createTime.minutes + ":" + vm.editNotificationBuffer.createTime.seconds
            vm.editNotificationBuffer.updateDateString = vm.editNotificationBuffer.updateTime.year + "/" + vm.editNotificationBuffer.updateTime.month + "/" + vm.editNotificationBuffer.updateTime.day
            vm.editNotificationBuffer.updateTimeString = vm.editNotificationBuffer.updateTime.hours + ":" + vm.editNotificationBuffer.updateTime.minutes + ":" + vm.editNotificationBuffer.updateTime.seconds
            vm.editNotificationBuffer.createDateTimeString = vm.editNotificationBuffer.createDateString + " - " + vm.editNotificationBuffer.createTimeString
            vm.editNotificationBuffer.updateDateTimeString = vm.editNotificationBuffer.updateDateString + " - " + vm.editNotificationBuffer.updateTimeString
            vm.editNotificationLoader = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.editNotificationLoader = false
            vm.resetState("editNotification")
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.editNotificationLoader = false
          vm.resetState("editNotification")
        })
      } else if (command === "createNotification") {
        this.createNotificationLoader = true
        this.axios({
          url: "/api/users/publicMessage",
          method: "POST",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            title: vm.createNotificationBuffer.title,
            visible: vm.createNotificationBuffer.visible,
            body: vm.createNotificationBuffer.body
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.resetState("createNotification")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.createNotificationLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.createNotificationLoader = false
        })
      } else if (command === "editNotification") {
        this.editNotificationLoader = true
        this.axios({
          url: "/api/users/publicMessage",
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            title: vm.editNotificationBuffer.title,
            visible: vm.editNotificationBuffer.visible,
            body: vm.editNotificationBuffer.body
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.resetState("editNotification")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.editNotificationLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.editNotificationLoader = false
        })
      } else if (command === "deleteNotification") {
        const selectedNotificationList = [this.notificationToolbarBuffer]
        this.loading = true
        this.axios({
          url: "/api/users/publicMessages",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: selectedNotificationList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.notificationsRequestMaster("getNotifications")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "deleteNotifications") {
        const selectedNotificationList = []
        for (const x in this.selectedNotifications) {
          selectedNotificationList.push(this.selectedNotifications[x].messageId)
        }
        this.loading = true
        this.axios({
          url: "/api/users/publicMessages",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: selectedNotificationList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.notificationsRequestMaster("getNotifications")
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
    createNotification () {
      this.createNotificationFlag = true
      this.tabActiveIndex = 1
    },
    createNotificationCheckup () {
      let errorCount = 0
      if (this.createNotificationBuffer.title === "") {
        this.createNotificationErrors.title = "p-invalid"
        errorCount += 1
      } else {
        this.createNotificationErrors.title = ""
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.groupsRequestMaster("createNotification")
      }
    },
    editNotification (id) {
      this.editNotificationBuffer.messageId = id
      this.editNotificationFlag = true
      if (this.createNotificationFlag) {
        this.tabActiveIndex = 2
      } else {
        this.tabActiveIndex = 1
      }
      this.notificationsRequestMaster("getNotification")
    },
    editNotificationCheckup () {
      let errorCount = 0
      if (this.editNotificationBuffer.title === "") {
        this.editNotificationErrors.title = "p-invalid"
        errorCount += 1
      } else {
        this.editNotificationErrors.title = ""
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.groupsRequestMaster("editNotification")
      }
    },
    deleteNotification (id) {
      this.notificationToolbarBuffer = id
      this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + String(id), "pi-question-circle", "#F0EAAA", this.notificationsRequestMaster, "deleteNotification")
    },
    notificationsToolbar (action) {
      if (this.selectedNotifications.length > 0) {
        if (action === "delete") {
          this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + this.$t("notifications"), "pi-question-circle", "#F0EAAA", this.notificationsRequestMaster, "deleteNotifications")
        }
      } else {
        this.alertPromptMaster(this.$t("noNotificationSelected"), "", "pi-exclamation-triangle", "#FDB5BA")
      }
    },
    resetState (command) {
      if (command === "createNotification") {
        this.tabActiveIndex = 0
        this.createNotificationFlag = false
        this.createNotificationBuffer = {
          title: "",
          visible: false,
          body: ""
        }
      } else if (command === "editNotification") {
        this.tabActiveIndex = 0
        this.editNotificationFlag = false
        this.editNotificationBuffer = {
          title: "",
          visible: false,
          body: ""
        }
      }
    }
  }
}
</script>

<style scoped>
.p-toolbar {
  border-radius: 6px 6px 0 0;
}
.p-column-header-content {
  justify-content: space-between !important;
}
</style>
