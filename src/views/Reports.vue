<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("reports") }}</h3>
        <TabView v-model:activeIndex="tabActiveIndex">
          <TabPanel :header="$t('myReports')">
            <Toolbar>
              <template #start>
                <span class="p-input-icon-left">
                  <i class="pi pi-times-circle" @click="removeFilters('reports', 'startDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="reportsFilter.startDate" type="text" :placeholder="$t('startDate')" class="datePickerFa" />
                </span>
                <span class="p-input-icon-left mx-3">
                  <i class="pi pi-times-circle" @click="removeFilters('reports', 'endDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="reportsFilter.endDate" type="text" :placeholder="$t('endDate')" class="datePickerFa" />
                </span>
                <Button :label="$t('filter')" class="p-button mx-1" @click="reportsRequestMaster('getUserReports')" />
              </template>
              <template #end>
                <Button icon="pi pi-filter-slash" v-tooltip.top="$t('removeFilters')" class="p-button-danger mb-2 mx-1" @click="removeFilters('all')" />
              </template>
            </Toolbar>
            <DataTable :value="reports" filterDisplay="menu" dataKey="_id" :rows="rowsPerPage" :loading="loading" scrollHeight="50vh"
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
              <Column field="message" :header="$t('message')" bodyClass="text-center" style="flex: 0 0 20rem">
                <template #body="{data}">
                  {{ data.message }}
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
          <TabPanel v-if="$store.state.accessLevel > 1" :header="$t('usersReports')">
            <Toolbar>
              <template #start>
                <span class="p-input-icon-left">
                  <i class="pi pi-times-circle" @click="removeFilters('reportsUsers', 'startDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="reportsUsersFilter.startDate" type="text" :placeholder="$t('startDate')" class="datePickerFa" />
                </span>
                <span class="p-input-icon-left mx-3">
                  <i class="pi pi-times-circle" @click="removeFilters('reportsUsers', 'endDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="reportsUsersFilter.endDate" type="text" :placeholder="$t('endDate')" class="datePickerFa" />
                </span>
                <Button :label="$t('filter')" class="p-button mx-1" @click="reportsRequestMaster('getUsersReports')" />
              </template>
              <template #end>
                <Button icon="pi pi-upload" class="mx-1" @click="reportsRequestMaster('export')" v-tooltip.top="'Export'" />
              </template>
            </Toolbar>
            <DataTable :value="reportsUsers" filterDisplay="menu" dataKey="_id" :rows="rowsPerPageUsers" :loading="loadingUsers" scrollHeight="50vh" :filters="filters"
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
              <Column field="message" :header="$t('message')" bodyClass="text-center" style="flex: 0 0 20rem">
                <template #body="{data}">
                  {{ data.message }}
                </template>
              </Column>
              <Column field="loggerName" :header="$t('id')" bodyClass="text-center" style="flex: 0 0 8rem">
                <template #body="{data}">
                  {{ data.loggerName }}
                </template>
                <template #filter="{filterCallback}">
                  <InputText type="text" v-model="reportsUsersFilter.loggerName" @keydown.enter="filterCallback(); reportsRequestMaster('getUsersReports')" class="p-column-filter" :placeholder="$t('id')"/>
                </template>
                <template #filterapply="{filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); reportsRequestMaster('getUsersReports')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); removeFilters('reportsUsers', 'loggerName')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
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
  name: "Reports",
  data () {
    return {
      reports: [],
      reportsUsers: [],
      reportsUsersFilter: {
        loggerName: ""
      },
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
    this.reportsRequestMaster("getUserReports")
    if (this.$store.state.accessLevel > 1) {
      this.reportsRequestMaster("getUsersReports")
    }
  },
  methods: {
    initiateFilters () {
      this.filters = {
        loggerName: { value: null, matchMode: FilterMatchMode.CONTAINS }
      }
    },
    onPaginatorEvent (event) {
      this.newPageNumber = event.page + 1
      this.rowsPerPage = event.rows
      this.reportsRequestMaster("getUserReports")
    },
    onPaginatorEventUsers (event) {
      this.newPageNumberUsers = event.page + 1
      this.rowsPerPageUsers = event.rows
      this.reportsRequestMaster("getUsersReports")
    },
    reportsRequestMaster (command) {
      const vm = this
      let parameterObject = null
      if (this.$i18n.locale === "Fa") {
        parameterObject = { lang: "fa" }
      } else if (this.$i18n.locale === "En") {
        parameterObject = { lang: "en" }
      }
      if (command === "getUserReports") {
        parameterObject.startDate = this.dateSerializer(document.getElementById("reportsFilter.startDate").value)
        parameterObject.endDate = this.dateSerializer(document.getElementById("reportsFilter.endDate").value)
        parameterObject.page = String(this.newPageNumber)
        parameterObject.count = String(this.rowsPerPage)
        const query = new URLSearchParams(parameterObject).toString()
        this.loading = true
        this.axios({
          url: "/api/logs/reports/user" + "?" + query,
          method: "GET"
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.reports = res.data.data.reportsList
            vm.totalRecordsCount = res.data.data.size
            for (let i = 0; i < vm.reports.length; ++i) {
              vm.reports[i].dateString = vm.reports[i].dateTime.year + "/" + vm.reports[i].dateTime.month + "/" + vm.reports[i].dateTime.day
              vm.reports[i].timeString = vm.reports[i].dateTime.hours + ":" + vm.reports[i].dateTime.minutes + ":" + vm.reports[i].dateTime.seconds
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
      } else if (command === "getUsersReports") {
        parameterObject.loggerName = this.reportsUsersFilter.loggerName
        parameterObject.startDate = this.dateSerializer(document.getElementById("reportsUsersFilter.startDate").value)
        parameterObject.endDate = this.dateSerializer(document.getElementById("reportsUsersFilter.endDate").value)
        parameterObject.page = String(this.newPageNumberUsers)
        parameterObject.count = String(this.rowsPerPageUsers)
        const query = new URLSearchParams(parameterObject).toString()
        this.loadingUsers = true
        this.axios({
          url: "/api/logs/reports/users" + "?" + query,
          method: "GET"
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.reportsUsers = res.data.data.reportsList
            vm.totalRecordsCountUsers = res.data.data.size
            for (let i = 0; i < vm.reportsUsers.length; ++i) {
              vm.reportsUsers[i].dateString = vm.reportsUsers[i].dateTime.year + "/" + vm.reportsUsers[i].dateTime.month + "/" + vm.reportsUsers[i].dateTime.day
              vm.reportsUsers[i].timeString = vm.reportsUsers[i].dateTime.hours + ":" + vm.reportsUsers[i].dateTime.minutes + ":" + vm.reportsUsers[i].dateTime.seconds
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
          url: "/api/logs/export?type=reports",
          method: "GET",
          responseType: "blob"
        }).then((res) => {
          const url = window.URL.createObjectURL(new Blob([res.data]))
          const link = document.createElement("a")
          link.href = url
          link.setAttribute("download", "reports.xlsx")
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
      if (scale === "reports") {
        if (filter === "startDate") {
          document.getElementById("reportsFilter.startDate").value = ""
        } else if (filter === "endDate") {
          document.getElementById("reportsFilter.endDate").value = ""
        }
        this.reportsRequestMaster("getUserReports")
      } else if (scale === "reportsUsers") {
        if (filter === "startDate") {
          document.getElementById("reportsUsersFilter.startDate").value = ""
        } else if (filter === "endDate") {
          document.getElementById("reportsUsersFilter.endDate").value = ""
        } else if (filter === "loggerName") {
          this.reportsUsersFilter.loggerName = ""
        }
        this.reportsRequestMaster("getUsersReports")
      } else if (scale === "all") {
        document.getElementById("reportsFilter.startDate").value = ""
        document.getElementById("reportsFilter.endDate").value = ""
        this.reportsRequestMaster("getUserReports")
      } else if (scale === "allUsers") {
        document.getElementById("reportsUsersFilter.startDate").value = ""
        document.getElementById("reportsUsersFilter.endDate").value = ""
        this.reportsUsersFilter = {
          loggerName: ""
        }
        this.reportsRequestMaster("getUsersReports")
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
