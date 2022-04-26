<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("events") }}</h3>
        <TabView v-model:activeIndex="tabActiveIndex">
          <TabPanel :header="$t('myEvents')">
            <Toolbar>
              <template #start>
                <span class="p-input-icon-left">
                  <i class="pi pi-times-circle" @click="removeFilters('events', 'startDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="eventsFilter.startDate" type="text" :placeholder="$t('startDate')" class="datePickerFa" />
                </span>
                <span class="p-input-icon-left mx-3">
                  <i class="pi pi-times-circle" @click="removeFilters('events', 'endDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="eventsFilter.endDate" type="text" :placeholder="$t('endDate')" class="datePickerFa" />
                </span>
                <Button :label="$t('filter')" class="p-button mx-1" @click="eventsRequestMaster('getUserEvents')" />
              </template>
            </Toolbar>
            <DataTable :value="events" filterDisplay="menu" dataKey="_id" :rows="rowsPerPage" :loading="loading" scrollHeight="50vh"
            class="p-datatable-gridlines" :rowHover="true" responsiveLayout="scroll" scrollDirection="vertical" :scrollable="false">
              <template #header>
                <div class="flex justify-content-between flex-column sm:flex-row">
                  <div></div>
                  <Paginator v-model:rows="rowsPerPage" v-model:totalRecords="totalRecordsCount" @page="onPaginatorEvent($event)" :rowsPerPageOptions="[10,20,50,100,500]"></Paginator>
                  <Button icon="pi pi-filter-slash" v-tooltip.top="$t('removeFilters')" class="p-button-danger mb-2 mx-1" @click="removeFilters('all')" />
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
              <Column field="action" :header="$t('action')" bodyClass="text-center" style="flex: 0 0 20rem">
                <template #body="{data}">
                  <div v-if="data.action === 'Successful Login'">{{ $t("successfulLogin") }}</div>
                  <div v-else-if="data.action === 'Unsuccessful Login'" style="color: red;">{{ $t("unsuccessfulLogin") }}</div>
                </template>
                <template #filter="{filterCallback}">
                  <Dropdown v-model="eventsUserFilter.action" @keydown.enter="filterCallback(); eventsRequestMaster('getUserEvents')" :options="eventsUserActionFilterOptions" optionLabel="name" :placeholder="$t('eventType')" />
                </template>
                <template #filterapply="{filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); eventsRequestMaster('getUserEvents')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); removeFilters('events', 'action')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                </template>
              </Column>
              <Column field="clientIP" :header="$t('clientIP')" bodyClass="text-center" style="flex: 0 0 10rem">
                <template #body="{data}">
                  {{ data.clientIP }}
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
              <Column bodyClass="text-center" style="width:5rem;">
                <template #body="{data}">
                  <i :class="data.osIcon" :style="data.osColor" v-tooltip.top="data.agentInfo.os"></i>
                </template>
              </Column>
              <Column bodyClass="text-center" style="width:5rem;">
                <template #body="{data}">
                  <i :class="data.browserIcon" :style="data.browserColor" v-tooltip.top="data.agentInfo.browser"></i>
                </template>
              </Column>
            </DataTable>
          </TabPanel>
          <TabPanel v-if="$store.state.accessLevel > 1" :header="$t('usersEvents')">
            <Toolbar>
              <template #start>
                <span class="p-input-icon-left">
                  <i class="pi pi-times-circle" @click="removeFilters('eventsUsers', 'startDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="eventsUsersFilter.startDate" type="text" :placeholder="$t('startDate')" class="datePickerFa" />
                </span>
                <span class="p-input-icon-left mx-3">
                  <i class="pi pi-times-circle" @click="removeFilters('eventsUsers', 'endDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="eventsUsersFilter.endDate" type="text" :placeholder="$t('endDate')" class="datePickerFa" />
                </span>
                <Button :label="$t('filter')" class="p-button mx-1" @click="eventsRequestMaster('getUsersEvents')" />
              </template>
              <template #end>
                <Button icon="pi pi-upload" class="mx-1" @click="eventsRequestMaster('export')" v-tooltip.top="'Export'" />
              </template>
            </Toolbar>
            <DataTable :value="eventsUsers" filterDisplay="menu" dataKey="_id" :rows="rowsPerPageUsers" :loading="loadingUsers" scrollHeight="50vh" :filters="filters"
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
              <Column field="action" :header="$t('eventType')" bodyClass="text-center" style="flex: 0 0 20rem">
                <template #body="{data}">
                  <div v-if="data.action === 'Successful Login'">{{ $t("successfulLogin") }}</div>
                  <div v-else-if="data.action === 'Unsuccessful Login'" style="color: red;">{{ $t("unsuccessfulLogin") }}</div>
                </template>
                <template #filter="{filterCallback}">
                  <Dropdown v-model="eventsUsersFilter.action" @keydown.enter="filterCallback(); eventsRequestMaster('getUsersEvents')" :options="eventsUsersActionFilterOptions" optionLabel="name" :placeholder="$t('eventType')" />
                </template>
                <template #filterapply="{filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); eventsRequestMaster('getUsersEvents')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); removeFilters('eventsUsers', 'action')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                </template>
              </Column>
              <Column field="userID" :header="$t('id')" bodyClass="text-center" style="flex: 0 0 8rem">
                <template #body="{data}">
                  {{ data.userId }}
                </template>
                <template #filter="{filterCallback}">
                  <InputText type="text" v-model="eventsUsersFilter.userID" @keydown.enter="filterCallback(); eventsRequestMaster('getUsersEvents')" class="p-column-filter" :placeholder="$t('id')"/>
                </template>
                <template #filterapply="{filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); eventsRequestMaster('getUsersEvents')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); removeFilters('eventsUsers', 'userID')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                </template>
              </Column>
              <Column field="clientIP" :header="$t('clientIP')" bodyClass="text-center" style="flex: 0 0 10rem">
                <template #body="{data}">
                  {{ data.clientIP }}
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
              <Column bodyClass="text-center" style="width:5rem;">
                <template #body="{data}">
                  <i :class="data.osIcon" :style="data.osColor" v-tooltip.top="data.agentInfo.os"></i>
                </template>
              </Column>
              <Column bodyClass="text-center" style="width:5rem;">
                <template #body="{data}">
                  <i :class="data.browserIcon" :style="data.browserColor" v-tooltip.top="data.agentInfo.browser"></i>
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
  name: "Events",
  data () {
    return {
      events: [],
      eventsUsers: [],
      eventsUserFilter: {
        action: {}
      },
      eventsUsersFilter: {
        userID: "",
        action: {}
      },
      eventsUserActionFilterOptions: [
        {
          value: "?",
          name: this.$t("successfulLogin")
        },
        {
          value: "?",
          name: this.$t("unsuccessfulLogin")
        }
      ],
      eventsUsersActionFilterOptions: [
        {
          value: "?",
          name: this.$t("successfulLogin")
        },
        {
          value: "?",
          name: this.$t("unsuccessfulLogin")
        }
      ],
      rowsPerPage: 20,
      newPageNumber: 1,
      totalRecordsCount: 20,
      rowsPerPageUsers: 20,
      newPageNumberUsers: 1,
      totalRecordsCountUsers: 20,
      tabActiveIndex: 0,
      loading: false,
      loadingUsers: false,
      filters: null
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
    this.eventsRequestMaster("getUserEvents")
    if (this.$store.state.accessLevel > 1) {
      this.eventsRequestMaster("getUsersEvents")
    }
  },
  methods: {
    initiateFilters () {
      this.filters = {
        userID: { value: null, matchMode: FilterMatchMode.CONTAINS }
      }
    },
    onPaginatorEvent (event) {
      this.newPageNumber = event.page + 1
      this.rowsPerPage = event.rows
      this.eventsRequestMaster("getUserEvents")
    },
    onPaginatorEventUsers (event) {
      this.newPageNumberUsers = event.page + 1
      this.rowsPerPageUsers = event.rows
      this.eventsRequestMaster("getUsersEvents")
    },
    eventsRequestMaster (command) {
      const vm = this
      let parameterObject = null
      if (this.$i18n.locale === "Fa") {
        parameterObject = { lang: "fa" }
      } else if (this.$i18n.locale === "En") {
        parameterObject = { lang: "en" }
      }
      if (command === "getUserEvents") {
        parameterObject.action = this.eventsUserFilter.action.value
        parameterObject.startDate = this.dateSerializer(document.getElementById("eventsFilter.startDate").value)
        parameterObject.endDate = this.dateSerializer(document.getElementById("eventsFilter.endDate").value)
        parameterObject.page = String(this.newPageNumber)
        parameterObject.count = String(this.rowsPerPage)
        const query = new URLSearchParams(parameterObject).toString()
        this.loading = true
        this.axios({
          url: "/api/logs/events/user" + "?" + query,
          method: "GET"
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.events = res.data.data.eventList
            vm.totalRecordsCount = res.data.data.size
            for (let i = 0; i < vm.events.length; ++i) {
              const osName = vm.events[i].agentInfo.os.toLowerCase()
              if (osName.search("windows") !== -1) {
                vm.events[i].osColor = "color: darkblue;"
                vm.events[i].osIcon = "bx bxl-windows bx-lg"
              } else if (osName.search("ios") !== -1 || osName.search("mac") !== -1) {
                vm.events[i].osColor = "color: dimgray;"
                vm.events[i].osIcon = "bx bxl-apple bx-lg"
              } else if (osName.search("android") !== -1) {
                vm.events[i].osColor = "color: #56c736;"
                vm.events[i].osIcon = "bx bxl-android bx-lg"
              } else if (osName.search("linux") !== -1 || osName.search("ubuntu") !== -1 || osName.search("debian") !== -1) {
                vm.events[i].osColor = "color: #000;"
                vm.events[i].osIcon = "bx bxl-tux bx-lg"
              } else {
                vm.events[i].osIcon = "bx bx-help-circle bx-lg"
              }

              const browserName = vm.events[i].agentInfo.browser.toLowerCase()
              if (browserName.search("firefox") !== -1) {
                vm.events[i].browserColor = "color: #f65d2b;"
                vm.events[i].browserIcon = "bx bxl-firefox bx-lg"
              } else if (browserName.search("chrome") !== -1) {
                vm.events[i].browserColor = "color: #109855;"
                vm.events[i].browserIcon = "bx bxl-chrome bx-lg"
              } else if (browserName.search("safari") !== -1) {
                vm.events[i].browserColor = "color: #1688e2;"
                vm.events[i].browserIcon = "bx bxs-compass bx-lg"
              } else if (browserName.search("edge") !== -1) {
                vm.events[i].browserColor = "color: #44ce90;"
                vm.events[i].browserIcon = "bx bxl-edge bx-lg"
              } else if (browserName.search("opera") !== -1) {
                vm.events[i].browserColor = "color: #e21126;"
                vm.events[i].browserIcon = "bx bxl-opera bx-lg"
              } else if (browserName.search("internet explorer") !== -1 || browserName.search("ie") !== -1) {
                vm.events[i].browserColor = "color: #1db5e7;"
                vm.events[i].browserIcon = "bx bxl-internet-explorer bx-lg"
              } else {
                vm.events[i].browserIcon = "bx bx-help-circle bx-lg"
              }

              vm.events[i].dateString = vm.events[i].time.year + "/" + vm.events[i].time.month + "/" + vm.events[i].time.day
              vm.events[i].timeString = vm.events[i].time.hours + ":" + vm.events[i].time.minutes + ":" + vm.events[i].time.seconds
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
      } else if (command === "getUsersEvents") {
        parameterObject.userID = this.eventsUsersFilter.userID
        parameterObject.action = this.eventsUsersFilter.action.value
        parameterObject.startDate = this.dateSerializer(document.getElementById("eventsUsersFilter.startDate").value)
        parameterObject.endDate = this.dateSerializer(document.getElementById("eventsUsersFilter.endDate").value)
        parameterObject.page = String(this.newPageNumberUsers)
        parameterObject.count = String(this.rowsPerPageUsers)
        const query = new URLSearchParams(parameterObject).toString()
        this.loadingUsers = true
        this.axios({
          url: "/api/logs/events/users" + "?" + query,
          method: "GET"
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.eventsUsers = res.data.data.eventList
            vm.totalRecordsCountUsers = res.data.data.size
            for (let i = 0; i < vm.eventsUsers.length; ++i) {
              const osName = vm.eventsUsers[i].agentInfo.os.toLowerCase()
              if (osName.search("windows") !== -1) {
                vm.eventsUsers[i].osColor = "color: darkblue;"
                vm.eventsUsers[i].osIcon = "bx bxl-windows bx-lg"
              } else if (osName.search("ios") !== -1 || osName.search("mac") !== -1) {
                vm.eventsUsers[i].osColor = "color: dimgray;"
                vm.eventsUsers[i].osIcon = "bx bxl-apple bx-lg"
              } else if (osName.search("android") !== -1) {
                vm.eventsUsers[i].osColor = "color: #56c736;"
                vm.eventsUsers[i].osIcon = "bx bxl-android bx-lg"
              } else if (osName.search("linux") !== -1 || osName.search("ubuntu") !== -1 || osName.search("debian") !== -1) {
                vm.eventsUsers[i].osColor = "color: #000;"
                vm.eventsUsers[i].osIcon = "bx bxl-tux bx-lg"
              } else {
                vm.eventsUsers[i].osIcon = "bx bx-help-circle bx-lg"
              }

              const browserName = vm.eventsUsers[i].agentInfo.browser.toLowerCase()
              if (browserName.search("firefox") !== -1) {
                vm.eventsUsers[i].browserColor = "color: #f65d2b;"
                vm.eventsUsers[i].browserIcon = "bx bxl-firefox bx-lg"
              } else if (browserName.search("chrome") !== -1) {
                vm.eventsUsers[i].browserColor = "color: #109855;"
                vm.eventsUsers[i].browserIcon = "bx bxl-chrome bx-lg"
              } else if (browserName.search("safari") !== -1) {
                vm.eventsUsers[i].browserColor = "color: #1688e2;"
                vm.eventsUsers[i].browserIcon = "bx bxs-compass bx-lg"
              } else if (browserName.search("edge") !== -1) {
                vm.eventsUsers[i].browserColor = "color: #44ce90;"
                vm.eventsUsers[i].browserIcon = "bx bxl-edge bx-lg"
              } else if (browserName.search("opera") !== -1) {
                vm.eventsUsers[i].browserColor = "color: #e21126;"
                vm.eventsUsers[i].browserIcon = "bx bxl-opera bx-lg"
              } else if (browserName.search("internet explorer") !== -1 || browserName.search("ie") !== -1) {
                vm.eventsUsers[i].browserColor = "color: #1db5e7;"
                vm.eventsUsers[i].browserIcon = "bx bxl-internet-explorer bx-lg"
              } else {
                vm.eventsUsers[i].browserIcon = "bx bx-help-circle bx-lg"
              }

              vm.eventsUsers[i].dateString = vm.eventsUsers[i].time.year + "/" + vm.eventsUsers[i].time.month + "/" + vm.eventsUsers[i].time.day
              vm.eventsUsers[i].timeString = vm.eventsUsers[i].time.hours + ":" + vm.eventsUsers[i].time.minutes + ":" + vm.eventsUsers[i].time.seconds
            }
            vm.loadingUsers = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingUsers = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingUsers = false
        })
      } else if (command === "export") {
        this.loadingUsers = true
        this.axios({
          url: "/api/logs/export?type=events",
          method: "GET",
          responseType: "blob"
        }).then((res) => {
          const url = window.URL.createObjectURL(new Blob([res.data]))
          const link = document.createElement("a")
          link.href = url
          link.setAttribute("download", "events.xlsx")
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
      if (scale === "events") {
        if (filter === "startDate") {
          document.getElementById("eventsFilter.startDate").value = ""
        } else if (filter === "endDate") {
          document.getElementById("eventsFilter.endDate").value = ""
        } else if (filter === "action") {
          this.eventsUserFilter.action = ""
        }
        this.eventsRequestMaster("getUserEvents")
      } else if (scale === "eventsUsers") {
        if (filter === "startDate") {
          document.getElementById("eventsUsersFilter.startDate").value = ""
        } else if (filter === "endDate") {
          document.getElementById("eventsUsersFilter.endDate").value = ""
        } else if (filter === "userID") {
          this.eventsUsersFilter.userID = ""
        } else if (filter === "action") {
          this.eventsUsersFilter.action = ""
        }
        this.eventsRequestMaster("getUsersEvents")
      } else if (scale === "all") {
        document.getElementById("eventsFilter.startDate").value = ""
        document.getElementById("eventsFilter.endDate").value = ""
        this.eventsUserFilter = {
          action: ""
        }
        this.eventsRequestMaster("getUserEvents")
      } else if (scale === "allUsers") {
        document.getElementById("eventsUsersFilter.startDate").value = ""
        document.getElementById("eventsUsersFilter.endDate").value = ""
        this.eventsUsersFilter = {
          userID: "",
          action: ""
        }
        this.eventsRequestMaster("getUsersEvents")
      }
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
