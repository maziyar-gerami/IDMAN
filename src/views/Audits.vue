<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("audits") }}</h3>
        <Dialog :header="$t('description')" v-model:visible="displayDescription" :breakpoints="{'960px': '75vw'}" :style="{width: '50vw'}" :modal="true">
          <p class="line-height-3 m-0">
            {{ auditDescription }}
          </p>
          <template #footer>
            <Button :label="$t('back')" @click="closeDescription()" class="p-button-danger"/>
          </template>
        </Dialog>
        <TabView v-model:activeIndex="tabActiveIndex">
          <TabPanel :header="$t('myAudits')">
            <Toolbar>
              <template #start>
                <span class="p-input-icon-left">
                  <i class="pi pi-times-circle" @click="removeFilters('audits', 'startDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="auditsFilter.startDate" type="text" :placeholder="$t('startDate')" class="datePickerFa" />
                </span>
                <span class="p-input-icon-left mx-3">
                  <i class="pi pi-times-circle" @click="removeFilters('audits', 'endDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="auditsFilter.endDate" type="text" :placeholder="$t('endDate')" class="datePickerFa" />
                </span>
                <Button :label="$t('filter')" class="p-button mx-1" @click="auditsRequestMaster('getUserAudits')" />
              </template>
              <template #end>
                <Button icon="pi pi-filter-slash" v-tooltip.top="$t('removeFilters')" class="p-button-danger mb-2 mx-1" @click="removeFilters('all')" />
              </template>
            </Toolbar>
            <DataTable :value="audits" filterDisplay="menu" dataKey="_id" :rows="rowsPerPage" :loading="loading" scrollHeight="50vh" :filters="filtersUser"
            class="p-datatable-gridlines" :rowHover="true" responsiveLayout="scroll" scrollDirection="vertical" :scrollable="false">
              <template #header>
                <div class="flex justify-content-center flex-column sm:flex-row">
                  <Paginator v-model:rows="rowsPerPage" v-model:totalRecords="totalRecordsCount" @page="onPaginatorEvent($event)" :rowsPerPageOptions="[10,20,50,100,500]"></Paginator>
                </div>
              </template>
              <template #empty>
                <div class="text-right">
                  {{ $t("noRecordsFound") }}
                </div>
              </template>
              <template #loading>
                <div class="text-right">
                  {{ $t("loadingRecords") }}
                </div>
              </template>
              <Column field="service" :header="$t('accessedService')" bodyClass="text-center" style="flex: 0 0 7rem">
                <template #body="{data}">
                  {{ data.service }}
                </template>
                <template #filter="{filterCallback}">
                  <Dropdown v-model="auditsUserFilter.service" @keydown.enter="filterCallback(); auditsRequestMaster('getUserAudits')"
                  :options="auditsUserFilterOptions" :loading="loadingUserDropdown" optionLabel="name" :placeholder="$t('service')" />
                </template>
                <template #filterapply="{filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); auditsRequestMaster('getUserAudits')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); removeFilters('audits', 'service')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                </template>
              </Column>
              <Column field="clientIpAddress" :header="$t('clientIP')" bodyClass="text-center" style="flex: 0 0 10rem">
                <template #body="{data}">
                  {{ data.clientIpAddress }}
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
            </DataTable>
          </TabPanel>
          <TabPanel v-if="$store.state.accessLevel > 1" :header="$t('usersAudits')">
            <Toolbar>
              <template #start>
                <span class="p-input-icon-left">
                  <i class="pi pi-times-circle" @click="removeFilters('auditsUsers', 'startDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="auditsUsersFilter.startDate" type="text" :placeholder="$t('startDate')" class="datePickerFa" />
                </span>
                <span class="p-input-icon-left mx-3">
                  <i class="pi pi-times-circle" @click="removeFilters('auditsUsers', 'endDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="auditsUsersFilter.endDate" type="text" :placeholder="$t('endDate')" class="datePickerFa" />
                </span>
                <Button :label="$t('filter')" class="p-button mx-1" @click="auditsRequestMaster('getUsersAudits')" />
              </template>
              <template #end>
                <Button icon="pi pi-upload" class="mx-1" @click="auditsRequestMaster('export')" v-tooltip.top="'Export'" />
              </template>
            </Toolbar>
            <DataTable :value="auditsUsers" filterDisplay="menu" dataKey="_id" :rows="rowsPerPageUsers" :loading="loadingUsers" scrollHeight="50vh" :filters="filtersUsers"
            class="p-datatable-gridlines" :rowHover="true" responsiveLayout="scroll" scrollDirection="vertical" :scrollable="false">
              <template #header>
                <div class="flex justify-content-between flex-column sm:flex-row">
                  <div></div>
                  <Paginator v-model:rows="rowsPerPageUsers" v-model:totalRecords="totalRecordsCountUsers" @page="onPaginatorEventUsers($event)" :rowsPerPageOptions="[10,20,50,100,500]"></Paginator>
                  <Button icon="pi pi-filter-slash" v-tooltip.top="$t('removeFilters')" class="p-button-danger mb-2 mx-1" @click="removeFilters('allUsers')" />
                </div>
              </template>
              <template #empty>
                <div class="text-right">
                  {{ $t("noRecordsFound") }}
                </div>
              </template>
              <template #loading>
                <div class="text-right">
                  {{ $t("loadingRecords") }}
                </div>
              </template>
              <Column field="service" :header="$t('accessedService')" bodyClass="text-center" style="flex: 0 0 7rem">
                <template #body="{data}">
                  {{ data.service }}
                </template>
                <template #filter="{filterCallback}">
                  <Dropdown v-model="auditsUsersFilter.service" @keydown.enter="filterCallback(); auditsRequestMaster('getUsersAudits')"
                  :options="auditsUsersFilterOptions" :loading="loadingUsersDropdown" optionLabel="name" :placeholder="$t('service')" />
                </template>
                <template #filterapply="{filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); auditsRequestMaster('getUsersAudits')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); removeFilters('auditsUsers', 'service')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                </template>
              </Column>
              <Column field="principal" :header="$t('id')" bodyClass="text-center" style="flex: 0 0 8rem">
                <template #body="{data}">
                  {{ data.principal }}
                </template>
                <template #filter="{filterCallback}">
                  <InputText type="text" v-model="auditsUsersFilter.userID" @keydown.enter="filterCallback(); auditsRequestMaster('getUsersAudits')" class="p-column-filter" :placeholder="$t('id')"/>
                </template>
                <template #filterapply="{filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); auditsRequestMaster('getUsersAudits')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); removeFilters('auditsUsers', 'userID')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                </template>
              </Column>
              <Column field="clientIpAddress" :header="$t('clientIP')" bodyClass="text-center" style="flex: 0 0 10rem">
                <template #body="{data}">
                  {{ data.clientIpAddress }}
                </template>
              </Column>
              <Column field="serverIpAddress" :header="$t('serverIP')" bodyClass="text-center" style="flex: 0 0 10rem">
                <template #body="{data}">
                  {{ data.serverIpAddress }}
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
              <Column bodyClass="text-center" style="flex: 0 0 5rem">
                <template #body="{data}">
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="fa fa-info" class="p-button-rounded p-button-info p-button-outlined mx-1" @click="openDescription(data.resourceOperatedUpon)" v-tooltip.top="$t('description')" />
                  </div>
                </template>
              </Column>
            </DataTable>
          </TabPanel>
        </TabView>
      </div>
    </div>
  </div>
</template>

<script>
import { FilterMatchMode } from "primevue/api"
import "persian-datepicker/dist/js/persian-datepicker"
import iziToast from "@/assets/scripts/iziToast.min.js"

export default {
  name: "Audits",
  data () {
    return {
      audits: [],
      auditsUsers: [],
      auditsUserFilter: {
        service: {
          _id: ""
        }
      },
      auditsUsersFilter: {
        userID: "",
        service: {
          _id: ""
        }
      },
      auditsUserFilterOptions: [],
      auditsUsersFilterOptions: [],
      auditDescription: "",
      rowsPerPage: 20,
      newPageNumber: 1,
      totalRecordsCount: 20,
      rowsPerPageUsers: 20,
      newPageNumberUsers: 1,
      totalRecordsCountUsers: 20,
      tabActiveIndex: 0,
      loading: false,
      loadingUsers: false,
      loadingUserDropdown: false,
      loadingUsersDropdown: false,
      displayDescription: false,
      filtersUser: null,
      filtersUsers: null
    }
  },
  mounted () {
    // eslint-disable-next-line no-undef
    jQuery(".datePickerFa").pDatepicker({
      inline: false,
      format: "DD MMMM YYYY",
      viewMode: "day",
      initialValue: false,
      initialValueType: "persian",
      autoClose: true,
      position: "auto",
      altFormat: "lll",
      altField: ".alt-field",
      onlyTimePicker: false,
      onlySelectOnDate: true,
      calendarType: "persian",
      inputDelay: 800,
      observer: false,
      calendar: {
        persian: {
          locale: "fa",
          showHint: true,
          leapYearMode: "algorithmic"
        },
        gregorian: {
          locale: "en",
          showHint: true
        }
      },
      navigator: {
        enabled: true,
        scroll: {
          enabled: true
        },
        text: {
          btnNextText: "<",
          btnPrevText: ">"
        }
      },
      toolbox: {
        enabled: true,
        calendarSwitch: {
          enabled: true,
          format: "MMMM"
        },
        todayButton: {
          enabled: true,
          text: {
            fa: "امروز",
            en: "Today"
          }
        },
        submitButton: {
          enabled: true,
          text: {
            fa: "تایید",
            en: "Submit"
          }
        },
        text: {
          btnToday: "امروز"
        }
      },
      timePicker: {
        enabled: false,
        step: 1,
        hour: {
          enabled: false,
          step: null
        },
        minute: {
          enabled: false,
          step: null
        },
        second: {
          enabled: false,
          step: null
        },
        meridian: {
          enabled: false
        }
      },
      dayPicker: {
        enabled: true,
        titleFormat: "YYYY MMMM"
      },
      monthPicker: {
        enabled: true,
        titleFormat: "YYYY"
      },
      yearPicker: {
        enabled: true,
        titleFormat: "YYYY"
      },
      responsive: true
    })
    this.initiateFilters()
    this.auditsRequestMaster("getUserAudits")
    this.auditsRequestMaster("getUserAuditsFilter")
    if (this.$store.state.accessLevel > 1) {
      this.auditsRequestMaster("getUsersAudits")
      this.auditsRequestMaster("getUsersAuditsFilter")
    }
  },
  methods: {
    initiateFilters () {
      this.filtersUser = {
        service: { value: null, matchMode: FilterMatchMode.CONTAINS }
      }
      this.filtersUsers = {
        principal: { value: null, matchMode: FilterMatchMode.CONTAINS },
        service: { value: null, matchMode: FilterMatchMode.CONTAINS }
      }
    },
    onPaginatorEvent (event) {
      this.newPageNumber = event.page + 1
      this.rowsPerPage = event.rows
      this.auditsRequestMaster("getUserAudits")
    },
    onPaginatorEventUsers (event) {
      this.newPageNumberUsers = event.page + 1
      this.rowsPerPageUsers = event.rows
      this.auditsRequestMaster("getUsersAudits")
    },
    auditsRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getUserAudits") {
        this.loading = true
        this.axios({
          url: "/api/logs/audits/user",
          method: "GET",
          params: {
            service: vm.auditsUserFilter.service._id,
            startDate: vm.dateSerializer(document.getElementById("auditsFilter.startDate").value),
            endDate: vm.dateSerializer(document.getElementById("auditsFilter.endDate").value),
            page: String(vm.newPageNumber),
            count: String(vm.rowsPerPage),
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.audits = res.data.data.auditList
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
      } else if (command === "getUsersAudits") {
        this.loadingUsers = true
        this.axios({
          url: "/api/logs/audits/users",
          method: "GET",
          params: {
            service: vm.auditsUsersFilter.service._id,
            userID: vm.auditsUsersFilter.userID,
            startDate: vm.dateSerializer(document.getElementById("auditsUsersFilter.startDate").value),
            endDate: vm.dateSerializer(document.getElementById("auditsUsersFilter.endDate").value),
            page: String(vm.newPageNumberUsers),
            count: String(vm.rowsPerPageUsers),
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.auditsUsers = res.data.data.auditList
            vm.totalRecordsCountUsers = res.data.data.size
            vm.loadingUsers = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingUsers = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingUsers = false
        })
      } else if (command === "getUserAuditsFilter") {
        this.loadingUserDropdown = true
        this.axios({
          url: "/api/service/used",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.auditsUserFilterOptions = res.data.data
            vm.loadingUserDropdown = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingUserDropdown = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingUserDropdown = false
        })
      } else if (command === "getUsersAuditsFilter") {
        this.loadingUsersDropdown = true
        this.axios({
          url: "/api/services/main",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.auditsUsersFilterOptions = []
            for (const i in res.data.data) {
              vm.auditsUsersFilterOptions.push(
                {
                  _id: res.data.data[i]._id,
                  name: res.data.data[i].name
                }
              )
            }
            vm.loadingUsersDropdown = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingUsersDropdown = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingUsersDropdown = false
        })
      } else if (command === "export") {
        this.loadingUsers = true
        this.axios({
          url: "/api/logs/export?type=audits",
          method: "GET",
          responseType: "blob"
        }).then((res) => {
          const url = window.URL.createObjectURL(new Blob([res.data]))
          const link = document.createElement("a")
          link.href = url
          link.setAttribute("download", "audits.xlsx")
          document.body.appendChild(link)
          link.click()
          vm.loadingUsers = false
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingUsers = false
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
    removeFilters (scale, filter) {
      if (scale === "audits") {
        if (filter === "startDate") {
          document.getElementById("auditsFilter.startDate").value = ""
        } else if (filter === "endDate") {
          document.getElementById("auditsFilter.endDate").value = ""
        } else if (filter === "service") {
          this.auditsUserFilter.service = {
            name: ""
          }
        }
        this.auditsRequestMaster("getUserAudits")
      } else if (scale === "auditsUsers") {
        if (filter === "startDate") {
          document.getElementById("auditsUsersFilter.startDate").value = ""
        } else if (filter === "endDate") {
          document.getElementById("auditsUsersFilter.endDate").value = ""
        } else if (filter === "userID") {
          this.auditsUsersFilter.userID = ""
        } else if (filter === "service") {
          this.auditsUsersFilter.service = {
            name: ""
          }
        }
        this.auditsRequestMaster("getUsersAudits")
      } else if (scale === "all") {
        document.getElementById("auditsFilter.startDate").value = ""
        document.getElementById("auditsFilter.endDate").value = ""
        this.auditsUserFilter.service = {
          name: ""
        }
        this.auditsRequestMaster("getUserAudits")
      } else if (scale === "allUsers") {
        document.getElementById("auditsUsersFilter.startDate").value = ""
        document.getElementById("auditsUsersFilter.endDate").value = ""
        this.auditsUsersFilter = {
          userID: "",
          service: {
            name: ""
          }
        }
        this.auditsRequestMaster("getUsersAudits")
      }
    },
    openDescription (description) {
      this.auditDescription = description
      this.displayDescription = true
    },
    closeDescription () {
      this.displayDescription = false
      this.auditDescription = ""
    },
    dateSerializer (date) {
      if (date !== "") {
        const tempArray = date.split(" ")
        return this.faNumToEnNum(tempArray[0]) + this.faMonthtoNumMonth(tempArray[1]) + this.faNumToEnNum(tempArray[2])
      } else {
        return ""
      }
    },
    faMonthtoNumMonth (faMonth) {
      let numMonth = ""
      switch (faMonth) {
        case "فروردین":
          numMonth = "01"
          break
        case "اردیبهشت":
          numMonth = "02"
          break
        case "خرداد":
          numMonth = "03"
          break
        case "تیر":
          numMonth = "04"
          break
        case "مرداد":
          numMonth = "05"
          break
        case "شهریور":
          numMonth = "06"
          break
        case "مهر":
          numMonth = "07"
          break
        case "آبان":
          numMonth = "08"
          break
        case "آذر":
          numMonth = "09"
          break
        case "دی":
          numMonth = "10"
          break
        case "بهمن":
          numMonth = "11"
          break
        case "اسفند":
          numMonth = "12"
          break
      }
      return numMonth
    },
    faNumToEnNum (faNum) {
      const s = faNum.split("")
      let sEn = ""
      for (let i = 0; i < s.length; ++i) {
        if (s[i] === "۰") {
          sEn = sEn + "0"
        } else if (s[i] === "۱") {
          sEn = sEn + "1"
        } else if (s[i] === "۲") {
          sEn = sEn + "2"
        } else if (s[i] === "۳") {
          sEn = sEn + "3"
        } else if (s[i] === "۴") {
          sEn = sEn + "4"
        } else if (s[i] === "۵") {
          sEn = sEn + "5"
        } else if (s[i] === "۶") {
          sEn = sEn + "6"
        } else if (s[i] === "۷") {
          sEn = sEn + "7"
        } else if (s[i] === "۸") {
          sEn = sEn + "8"
        } else if (s[i] === "۹") {
          sEn = sEn + "9"
        }
      }
      return sEn
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
