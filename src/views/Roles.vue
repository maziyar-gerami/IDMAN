<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("roles") }}</h3>
        <Toolbar>
          <template #start>
            <Dropdown v-model="selectedRoleOption" :options="roleOptions" optionLabel="name" :placeholder="$t('chooseRole')" />
            <Button :label="$t('changeRole')" class="p-button-success mx-1" @click="changeRole()" />
          </template>
        </Toolbar>
        <DataTable :value="users" filterDisplay="menu" dataKey="_id" :rows="rowsPerPage" v-model:filters="filters" :loading="loading"
        v-model:selection="selectedUsers" :filters="filters" class="p-datatable-gridlines" :rowHover="true"
        responsiveLayout="scroll" :scrollable="false" scrollHeight="50vh" scrollDirection="vertical">
          <template #header>
            <div class="flex justify-content-between flex-column sm:flex-row">
              <div></div>
              <Paginator v-model:rows="rowsPerPage" v-model:totalRecords="totalRecordsCount" @page="onPaginatorEvent($event)" :rowsPerPageOptions="[10,20,50,100,500]"></Paginator>
              <Button icon="pi pi-filter-slash" v-tooltip.top="$t('removeFilters')" class="p-button-danger mb-2 mx-1" @click="initiateFilters('clearFilters')" />
            </div>
          </template>
          <template #empty>
            <div class="text-right">
              {{ $t("noUsersFound") }}
            </div>
          </template>
          <template #loading>
            <div class="text-right">
              {{ $t("loadingUsers") }}
            </div>
          </template>
          <Column selectionMode="multiple" bodyClass="text-center" style="width:5rem;"></Column>
          <Column bodyClass="text-center" style="width:5rem;">
            <template #body="{data}">
              <span class="fa-stack fa-lg">
                <i class="fa fa-user fa-lg" :style="data.icon"></i>
              </span>
            </template>
          </Column>
          <Column field="_id" :header="$t('id')" bodyClass="text-center" style="flex: 0 0 10rem">
            <template #body="{data}">
              {{ data._id }}
            </template>
            <template #filter="{filterModel,filterCallback}">
              <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback(); onFilterEvent('_id', filterModel.value)" class="p-column-filter" :placeholder="$t('id')"/>
            </template>
            <template #filterapply="{filterModel,filterCallback}">
              <Button type="button" icon="pi pi-check" @click="filterCallback(); onFilterEvent('_id', filterModel.value)" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
            </template>
            <template #filterclear="{filterCallback}">
              <Button type="button" icon="pi pi-times" @click="filterCallback(); onFilterEvent('_id', '')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
            </template>
          </Column>
          <Column field="role" :header="$t('role')" bodyClass="text-center" style="flex: 0 0 10rem">
            <template #body="{data}">
              {{ data.roleFa }}
            </template>
          </Column>
          <Column field="displayName" :header="$t('persianName')" bodyClass="text-center" style="flex: 0 0 15rem">
            <template #body="{data}">
              {{ data.displayName }}
            </template>
            <template #filter="{filterModel,filterCallback}">
              <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback(); onFilterEvent('displayName', filterModel.value)" class="p-column-filter" :placeholder="$t('persianName')"/>
            </template>
            <template #filterapply="{filterModel,filterCallback}">
                <Button type="button" icon="pi pi-check" @click="filterCallback(); onFilterEvent('displayName', filterModel.value)" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
            </template>
            <template #filterclear="{filterCallback}">
                <Button type="button" icon="pi pi-times" @click="filterCallback(); onFilterEvent('displayName', '')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
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
  name: "Users",
  data () {
    return {
      users: [],
      selectedUsers: [],
      roleOptions: [
        {
          id: "SUPERUSER",
          name: this.$t("superUser")
        },
        {
          id: "SUPPORTER",
          name: this.$t("supporter")
        },
        {
          id: "ADMIN",
          name: this.$t("admin")
        },
        {
          id: "PRESENTER",
          name: this.$t("presenter")
        },
        {
          id: "USER",
          name: this.$t("user")
        }
      ],
      selectedRoleOption: {},
      rowsPerPage: 20,
      newPageNumber: 1,
      totalRecordsCount: 20,
      loading: true,
      filters: null,
      filterValues: null
    }
  },
  mounted () {
    this.initiateFilters()
    this.rolesRequestMaster("getUsers")
  },
  methods: {
    initiateFilters (from) {
      this.filters = {
        _id: { value: null, matchMode: FilterMatchMode.CONTAINS },
        displayName: { value: null, matchMode: FilterMatchMode.CONTAINS }
      }
      this.filterValues = {
        _id: "",
        displayName: ""
      }
      if (from === "clearFilters") {
        this.onFilterEvent("")
      }
    },
    onPaginatorEvent (event) {
      this.newPageNumber = event.page + 1
      this.rowsPerPage = event.rows
      this.onFilterEvent("")
    },
    onFilterEvent (filterName, filterValue) {
      if (filterName !== "") {
        this.filterValues[filterName] = filterValue
      }
      this.rolesRequestMaster("getUsers", {
        searchUid: this.filterValues._id,
        searchDisplayName: this.filterValues.displayName
      })
    },
    rolesRequestMaster (command, parameterObject) {
      const vm = this
      let parameterObjectBuffer = {}
      if (typeof parameterObject !== "undefined") {
        parameterObjectBuffer = parameterObject
      }
      if (this.$i18n.locale === "Fa") {
        parameterObjectBuffer.lang = "fa"
      } else if (this.$i18n.locale === "En") {
        parameterObjectBuffer.lang = "en"
      }
      if (command === "getUsers") {
        const superAdminTempList = []
        const supporterTempList = []
        const adminTempList = []
        const presenterTempList = []
        const userTempList = []
        const query = new URLSearchParams(parameterObjectBuffer).toString()
        this.loading = true
        this.axios({
          url: "/api/users/" + String(vm.newPageNumber) + "/" + String(vm.rowsPerPage) + "?" + query,
          method: "GET"
        }).then((res) => {
          if (res.data.status.code === 200) {
            for (let i = 0; i < res.data.data.userList.length; ++i) {
              if (res.data.data.userList[i].role === "SUPERUSER") {
                res.data.data.userList[i].roleFa = "مدیر کل"
                res.data.data.userList[i].icon = "color: #dc3545;"
                superAdminTempList.push(res.data.data.userList[i])
              } else if (res.data.data.userList[i].role === "SUPPORTER") {
                res.data.data.userList[i].roleFa = "پشتیبانی"
                res.data.data.userList[i].icon = "color: #28a745;"
                supporterTempList.push(res.data.data.userList[i])
              } else if (res.data.data.userList[i].role === "ADMIN") {
                res.data.data.userList[i].roleFa = "مدیر"
                res.data.data.userList[i].icon = "color: #007bff;"
                adminTempList.push(res.data.data.userList[i])
              } else if (res.data.data.userList[i].role === "PRESENTER") {
                res.data.data.userList[i].roleFa = "ارائه دهنده"
                res.data.data.userList[i].icon = "color: #00f7f7;"
                presenterTempList.push(res.data.data.userList[i])
              } else if (res.data.data.userList[i].role === "USER") {
                res.data.data.userList[i].roleFa = "کاربر"
                res.data.data.userList[i].icon = "color: #ffc107;"
                userTempList.push(res.data.data.userList[i])
              }
            }
            vm.users = superAdminTempList.concat(supporterTempList, adminTempList, presenterTempList, userTempList)
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
      } else if (command === "editRole") {
        const selectedUsersList = []
        for (const x in this.selectedUsers) {
          selectedUsersList.push(this.selectedUsers[x]._id)
        }
        const query = new URLSearchParams(parameterObjectBuffer).toString()
        this.loading = true
        this.axios({
          url: "/api/roles/" + vm.selectedRoleOption.id + "?" + query,
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          data: JSON.stringify({
            names: selectedUsersList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loading = false
            vm.rolesRequestMaster("getUsers")
          } else if (res.data.status.code === 206) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
            vm.rolesRequestMaster("getUsers")
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
    changeRole () {
      if (this.selectedUsers.length > 0) {
        if (Object.keys(this.selectedRoleOption).length !== 0) {
          this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("changeRole") + " " + this.$t("users"), "pi-question-circle", "#F0EAAA", this.rolesRequestMaster, "editRole")
        } else {
          this.alertPromptMaster(this.$t("noRoleSelected"), "", "pi-exclamation-triangle", "#FDB5BA")
        }
      } else {
        this.alertPromptMaster(this.$t("noUserSelected"), "", "pi-exclamation-triangle", "#FDB5BA")
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
.p-column-filter-menu {
  margin: 0;
}
</style>
