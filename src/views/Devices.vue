<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("devices") }}</h3>
        <!-- <Toolbar>
          <template #end>
            <Button icon="pi pi-filter-slash" v-tooltip.top="$t('removeFilters')" class="p-button-danger mb-2 mx-1" @click="removeFilters('all')" />
          </template>
        </Toolbar> -->
        <DataTable :value="devices" filterDisplay="menu" dataKey="_id" :rows="rowsPerPage" v-model:filters="filters" :loading="loading"
        v-model:selection="selectedDevices" :filters="filters" class="p-datatable-gridlines" :rowHover="true"
        responsiveLayout="scroll" :scrollable="false" scrollHeight="50vh" scrollDirection="vertical">
          <template #header>
            <div class="flex justify-content-between flex-column sm:flex-row">
              <div />
              <Paginator v-model:rows="rowsPerPage" v-model:totalRecords="totalRecordsCount" @page="onPaginatorEvent($event)" :rowsPerPageOptions="[10,20,50,100,500]"></Paginator>
              <Button icon="pi pi-filter-slash" v-tooltip.top="$t('removeFilters')" class="p-button-danger mb-2 mx-1" @click="removeFilters('all')" />
            </div>
          </template>
          <template #empty>
            <div class="text-right">
              {{ $t("noDevicesFound") }}
            </div>
          </template>
          <template #loading>
            <div class="text-right">
              {{ $t("loadingDevices") }}
            </div>
          </template>
          <Column field="username" :header="$t('userId')" bodyClass="text-center" style="flex: 0 0 10rem">
            <template #body="{data}">
              {{ data.username }}
            </template>
            <template #filter="{filterCallback}">
              <InputText type="text" v-model="devicesFilters.username" @keydown.enter="filterCallback(); devicesRequestMaster('getDevices')" class="p-column-filter" :placeholder="$t('userId')"/>
            </template>
            <template #filterapply="{filterCallback}">
              <Button type="button" icon="pi pi-check" @click="filterCallback(); devicesRequestMaster('getDevices')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
            </template>
            <template #filterclear="{filterCallback}">
              <Button type="button" icon="pi pi-times" @click="filterCallback(); removeFilters('username')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
            </template>
          </Column>
          <Column field="name" :header="$t('deviceName')" bodyClass="text-center" style="flex: 0 0 10rem">
            <template #body="{data}">
              {{ data.name }}
            </template>
            <template #filter="{filterCallback}">
              <InputText type="text" v-model="devicesFilters.name" @keydown.enter="filterCallback(); devicesRequestMaster('getDevices')" class="p-column-filter" :placeholder="$t('deviceName')"/>
            </template>
            <template #filterapply="{filterCallback}">
              <Button type="button" icon="pi pi-check" @click="filterCallback(); devicesRequestMaster('getDevices')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
            </template>
            <template #filterclear="{filterCallback}">
              <Button type="button" icon="pi pi-times" @click="filterCallback(); removeFilters('name')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
            </template>
          </Column>
         <Column field="dateString" :header="$t('date')" bodyClass="text-center" style="flex: 0 0 7rem">
            <template #body="{data}">
              {{ data.dateString }}
            </template>
          </Column>
          <Column field="timeString" :header="$t('time')" bodyClass="text-center" style="flex: 0 0 7rem">
            <template #body="{data}">
              {{ data.timeString }}
            </template>
          </Column>
          <Column bodyStyle="display: flex;" bodyClass="flex justify-content-evenly flex-wrap card-container text-center w-full" style="flex: 0 0 5rem">
            <template #body="{data}">
              <div class="flex align-items-center justify-content-center">
                <Button icon="bx bx-trash bx-sm" class="p-button-rounded p-button-danger p-button-outlined mx-1" @click="deleteDevice(data.name)" v-tooltip.top="$t('deleteDevice')" />
              </div>
            </template>
          </Column>
        </DataTable>
      </div>
    </div>
  </div>
</template>

<script>
import { FilterMatchMode } from "primevue/api"
import iziToast from "@/assets/scripts/iziToast.min.js"

export default {
  name: "Devices",
  data () {
    return {
      devices: [],
      selectedDevices: [],
      devicesFilters: {
        username: "",
        name: ""
      },
      deviceToolbarBuffer: "",
      rowsPerPage: 20,
      newPageNumber: 1,
      totalRecordsCount: 20,
      loading: true,
      filters: null
    }
  },
  mounted () {
    this.initiateFilters()
    this.devicesRequestMaster("getDevices")
  },
  methods: {
    initiateFilters () {
      this.filters = {
        username: { value: null, matchMode: FilterMatchMode.CONTAINS },
        name: { value: null, matchMode: FilterMatchMode.CONTAINS }
      }
    },
    onPaginatorEvent (event) {
      this.newPageNumber = event.page + 1
      this.rowsPerPage = event.rows
      this.devicesRequestMaster("getDevices")
    },
    devicesRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getDevices") {
        this.loading = true
        this.axios({
          url: "/api/googleAuth",
          method: "GET",
          params: {
            username: vm.devicesFilters.username,
            deviceName: vm.devicesFilters.name,
            page: String(vm.newPageNumber),
            count: String(vm.rowsPerPage),
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            for (const i in res.data.data.devices) {
              res.data.data.devices[i].dateString = res.data.data.devices[i].time.year + "/" + res.data.data.devices[i].time.month + "/" + res.data.data.devices[i].time.day
              res.data.data.devices[i].timeString = res.data.data.devices[i].time.hours + ":" + res.data.data.devices[i].time.minutes + ":" + res.data.data.devices[i].time.seconds
            }
            vm.devices = res.data.data.devices
            vm.totalRecordsCount = res.data.data.size
            vm.loading = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "deleteDevice") {
        this.loading = true
        this.axios({
          url: "/api/googleAuth",
          method: "DELETE",
          params: {
            deviceName: vm.deviceToolbarBuffer,
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 204) {
            vm.loading = false
            vm.devicesRequestMaster("getDevices")
          } else if (res.data.status.code === 403) {
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
    deleteDevice (name) {
      this.deviceToolbarBuffer = name
      this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("deleteDevice") + "" + String(name), "pi-question-circle", "#F0EAAA", this.devicesRequestMaster, "deleteDevice")
    },
    removeFilters (filter) {
      if (filter === "username") {
        this.devicesFilters.username = ""
      } else if (filter === "name") {
        this.devicesFilters.name = ""
      } else if (filter === "all") {
        this.devicesFilters.username = ""
        this.devicesFilters.name = ""
      }
      this.devicesRequestMaster("getDevices")
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
.p-column-filter-menu {
  margin: 0;
}
</style>
