<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("accessReports") }}</h3>
        <TabView v-model:activeIndex="tabActiveIndex">
          <TabPanel :header="$t('accessChanges')">
            <Toolbar>
              <template #start>
                <span class="p-input-icon-left">
                  <i class="pi pi-times-circle" @click="removeFilters('accessChanges', 'startDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="accessChangesFilters.startDate" type="text" :placeholder="$t('startDate')" class="datePickerFa" />
                </span>
                <span class="p-input-icon-left mx-3">
                  <i class="pi pi-times-circle" @click="removeFilters('accessChanges', 'endDate')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                  <InputText id="accessChangesFilters.endDate" type="text" :placeholder="$t('endDate')" class="datePickerFa" />
                </span>
                <Button :label="$t('filter')" class="p-button mx-1" @click="accessReportsRequestMaster('getAccessChanges')" />
              </template>
            </Toolbar>
            <DataTable :value="accessChanges" filterDisplay="menu" dataKey="instance" :rows="rowsPerPageAccessChanges" :loading="loadingAccessChanges" :filters="filtersAccessChanges"
            scrollHeight="50vh" class="p-datatable-gridlines" :rowHover="true" responsiveLayout="scroll" scrollDirection="vertical" :scrollable="false">
              <template #header>
                <div class="flex justify-content-between flex-column sm:flex-row">
                  <div></div>
                  <Paginator v-model:rows="rowsPerPageAccessChanges" v-model:totalRecords="totalRecordsCountAccessChanges" @page="onPaginatorEventAccessChanges($event)" :rowsPerPageOptions="[10,20,50,100,500]"></Paginator>
                  <Button icon="pi pi-filter-slash" v-tooltip.top="$t('removeFilters')" class="p-button-danger mb-2 mx-1" @click="removeFilters('allAccessChanges')" />
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
              <Column field="instanceName" :header="$t('service')" bodyClass="text-center" style="flex: 0 0 7rem">
                <template #body="{data}">
                  {{ data.instanceName }}
                </template>
                <template #filter="{filterCallback}">
                  <InputText type="text" v-model="accessChangesFilters.instanceName" @keydown.enter="filterCallback(); accessReportsRequestMaster('getAccessChanges')" class="p-column-filter" :placeholder="$t('service')"/>
                </template>
                <template #filterapply="{filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); accessReportsRequestMaster('getAccessChanges')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); removeFilters('accessChanges', 'instanceName')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                </template>
              </Column>
              <Column field="accessChange" :header="$t('accessChange')" bodyClass="text-center" style="flex: 0 0 7rem">
                <template #body="{data}">
                  <div v-if="data.accessChange === 'Access Add'">{{ $t("accessAdd") }}</div>
                  <div v-else-if="data.accessChange === 'Access Remove'">{{ $t("accessRemove") }}</div>
                </template>
              </Column>
              <Column field="entity" :header="$t('entity')" bodyClass="text-center" style="flex: 0 0 10rem">
                <template #body="{data}">
                  {{ data.type }}{{ data.separator }}{{ data.item }}
                </template>
              </Column>
              <Column field="result" :header="$t('result')" bodyClass="text-center" style="flex: 0 0 7rem">
                <template #body="{data}">
                  <div v-if="data.result === 'Success'">{{ $t("successful") }}</div>
                  <div v-else style="color: red;">{{ $t("unsuccessful") }}</div>
                </template>
              </Column>
              <Column field="doerID" :header="$t('doerID')" bodyClass="text-center" style="flex: 0 0 7rem">
                <template #body="{data}">
                  {{ data.doerID }}
                </template>
                <template #filter="{filterCallback}">
                  <InputText type="text" v-model="accessChangesFilters.doerID" @keydown.enter="filterCallback(); accessReportsRequestMaster('getAccessChanges')" class="p-column-filter" :placeholder="$t('doerID')"/>
                </template>
                <template #filterapply="{filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); accessReportsRequestMaster('getAccessChanges')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); removeFilters('accessChanges', 'doerID')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
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
          <!-- <TabPanel :header="$t('usersAccess')">
          </TabPanel>
          <TabPanel :header="$t('groupsAccess')">
          </TabPanel>
          <TabPanel :header="$t('servicesAccess')">
          </TabPanel> -->
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
  name: "AccessReports",
  data () {
    return {
      accessChanges: [],
      accessChangesFilters: {
        instanceName: "",
        doerID: ""
      },
      rowsPerPageAccessChanges: 20,
      newPageNumberAccessChanges: 1,
      totalRecordsCountAccessChanges: 20,
      tabActiveIndex: 0,
      loadingAccessChanges: false,
      filtersAccessChanges: null
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
    this.accessReportsRequestMaster("getAccessChanges")
  },
  methods: {
    initiateFilters () {
      this.filtersAccessChanges = {
        instanceName: { value: null, matchMode: FilterMatchMode.CONTAINS },
        doerID: { value: null, matchMode: FilterMatchMode.CONTAINS }
      }
    },
    onPaginatorEventAccessChanges (event) {
      this.newPageNumberAccessChanges = event.page + 1
      this.rowsPerPageAccessChanges = event.rows
      this.accessReportsRequestMaster("getAccessChanges")
    },
    accessReportsRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getAccessChanges") {
        this.loadingAccessChanges = true
        this.axios({
          url: "/api/logs/reports/serviceAccess",
          method: "GET",
          params: {
            name: vm.accessChangesFilters.instanceName,
            doerId: vm.accessChangesFilters.doerID,
            startDate: vm.dateSerializer(document.getElementById("accessChangesFilters.startDate").value),
            endDate: vm.dateSerializer(document.getElementById("accessChangesFilters.endDate").value),
            page: String(vm.newPageNumberAccessChanges),
            count: String(vm.rowsPerPageAccessChanges),
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            for (const i in res.data.data.reportMessageList) {
              res.data.data.reportMessageList[i].dateString = res.data.data.reportMessageList[i].time.year + "/" + res.data.data.reportMessageList[i].time.month + "/" + res.data.data.reportMessageList[i].time.day
              res.data.data.reportMessageList[i].timeString = res.data.data.reportMessageList[i].time.hours + ":" + res.data.data.reportMessageList[i].time.minutes + ":" + res.data.data.reportMessageList[i].time.seconds
            }
            vm.accessChanges = res.data.data.reportMessageList
            vm.totalRecordsCountAccessChanges = res.data.data.size
            vm.loadingAccessChanges = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingAccessChanges = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingAccessChanges = false
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
      if (scale === "accessChanges") {
        if (filter === "startDate") {
          document.getElementById("accessChangesFilters.startDate").value = ""
        } else if (filter === "endDate") {
          document.getElementById("accessChangesFilters.endDate").value = ""
        } else if (filter === "instanceName") {
          this.accessChangesFilters.instanceName = ""
        } else if (filter === "doerID") {
          this.accessChangesFilters.doerID = ""
        }
        this.accessReportsRequestMaster("getAccessChanges")
      } else if (scale === "allAccessChanges") {
        document.getElementById("accessChangesFilters.startDate").value = ""
        document.getElementById("accessChangesFilters.endDate").value = ""
        this.accessChangesFilters = {
          instanceName: "",
          doerID: ""
        }
        this.accessReportsRequestMaster("getAccessChanges")
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
