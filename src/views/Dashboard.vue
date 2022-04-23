<template>
  <div class="grid mt-0 h-full">
    <div v-if="$store.state.accessLevel > 1" class="col-12 lg:col-4 xl:col-4 pt-0">
      <div class="card mb-0 h-full">
        <h4 :style="'font-family: ' + this.$store.state.persianFont">{{ usersChartTitle }} ({{ usersChartAll }})</h4>
        <Chart type="pie" :data="usersChartData" :options="lightOptions" :width="550" :height="200" />
      </div>
    </div>
    <div v-if="$store.state.accessLevel > 1" class="col-12 lg:col-4 xl:col-4 pt-0">
      <div class="card mb-0 h-full">
        <h4 :style="'font-family: ' + this.$store.state.persianFont">{{ servicesChartTitle }} ({{ servicesChartAll }})</h4>
        <Chart type="pie" :data="servicesChartData" :options="lightOptions" :width="550" :height="200" />
      </div>
    </div>
    <div v-if="$store.state.accessLevel > 1" class="col-12 lg:col-4 xl:col-4 pt-0">
      <div class="card mb-0 h-full">
        <h4 :style="'font-family: ' + this.$store.state.persianFont">{{ todaysLoginsChartTitle }} ({{ todaysLoginsChartAll }})</h4>
        <Chart type="pie" :data="todaysLoginsChartData" :options="lightOptions" :width="550" :height="200" />
      </div>
    </div>
    <div class="col-12 lg:col-12 xl:col-12 mb-0 pb-0">
      <div class="card mb-0 h-full">
        <DataView :value="services" :layout="layout" paginatorPosition="both" :paginator="true" :rows="4" :rowsPerPageOptions="[4,8,16,24]" :pageLinkSize="5">
          <template #header>
            <div class="grid grid-nogutter">
              <div :class="'col-6 align-self-center text-' + this.$store.state.alignRTL">
                <h3 class="m-0">{{ $t("services") }}</h3>
              </div>
              <div :class="'col-6 align-self-center text-' + this.$store.state.alignLTR">
                <DataViewLayoutOptions v-model="layout"/>
              </div>
            </div>
          </template>

          <template #list="slotProps">
            <div class="col-12">
              <div class="flex flex-column md:flex-row align-items-center p-3 w-full">
                <a :href="slotProps.data.url" target="_blank" class="service-link">
                  <img :src="slotProps.data.logo" class="my-4 md:my-0 shadow-2 mx-5" style="width: 80px; height: 80px;" />
                </a>
                <div class="flex-1">
                  <a :href="slotProps.data.url" target="_blank" class="service-link">
                    <div class="font-bold text-2xl">{{ $store.state.translations["dashboardService_" + String(slotProps.data._id)] }}</div>
                  </a>
                </div>
                <div class="flex flex-row md:flex-column justify-content-between w-full md:w-auto align-items-center md:align-items-end mt-5 md:mt-0">
                  <i @click="showServiceNotification(slotProps.data._id)" class="pi pi-bell mr-4 p-text-secondary service-notification p-overlay-badge">
                    <Badge v-if="slotProps.data.notification.count > 0" :value="slotProps.data.notification.realCount" severity="danger"></Badge>
                  </i>
                </div>
              </div>
            </div>
          </template>

          <template #grid="slotProps">
            <div class="col-12 lg:col-4 xl:col-3">
              <div class="card m-3 border-1 surface-border">
                <div class="flex align-items-center justify-content-end">
                  <i @click="showServiceNotification(slotProps.data._id)" class="pi pi-bell mr-4 p-text-secondary service-notification p-overlay-badge">
                    <Badge v-if="slotProps.data.notification.count > 0" :value="slotProps.data.notification.realCount" severity="danger"></Badge>
                  </i>
                </div>
                <div class="text-center">
                  <a :href="slotProps.data.url" target="_blank" class="service-link">
                    <img :src="slotProps.data.logo" class="shadow-2 my-3 mx-0" style="width: 100px; height: 100px;" />
                    <div class="text-2xl font-bold">{{ $store.state.translations["dashboardService_" + String(slotProps.data._id)] }}</div>
                  </a>
                </div>
              </div>
            </div>
          </template>
        </DataView>
      </div>
    </div>
  </div>
</template>

<script>
import iziToast from "@/assets/scripts/iziToast.min.js"

export default {
  name: "Dashboard",
  data () {
    return {
      usersChartAll: 99,
      servicesChartAll: 99,
      todaysLoginsChartAll: 99,
      usersChartData: {
        labels: [this.$t("locked"), this.$t("disabled"), this.$t("active")],
        datasets: [
          {
            data: [11, 22, 66],
            backgroundColor: ["#8C564B", "#9467BD", "#8AD4EB"],
            hoverBackgroundColor: ["#8C675F", "#A080BD", "#A4DAEB"]
          }
        ]
      },
      servicesChartData: {
        labels: [this.$t("disabled"), this.$t("active")],
        datasets: [
          {
            data: [22, 77],
            backgroundColor: ["#D62728", "#2CA02C"],
            hoverBackgroundColor: ["#D64B4B", "#48A148"]
          }
        ]
      },
      todaysLoginsChartData: {
        labels: [this.$t("unsuccessful"), this.$t("successful")],
        datasets: [
          {
            data: [22, 77],
            backgroundColor: ["#FF7F0E", "#1F77B4"],
            hoverBackgroundColor: ["#FF9130", "#3881B5"]
          }
        ]
      },
      lightOptions: {
        maintainAspectRatio: false,
        plugins: {
          legend: {
            labels: {
              font: {
                family: "IRANSansWeb-PersianNumbers",
                size: 14
              },
              color: "#495057"
            }
          },
          tooltip: {
            displayColors: false,
            titleFont: {
              family: "IRANSansWeb-PersianNumbers",
              size: 14
            },
            bodyFont: {
              family: "IRANSansWeb-PersianNumbers",
              size: 14
            }
          }
        }
      },
      services: [],
      layout: "grid"
    }
  },
  mounted () {
    if (this.$store.state.accessLevel > 1) {
      this.ticketingRequestMaster("getDashboardInfo")
    }
    this.ticketingRequestMaster("getUserServices")
  },
  methods: {
    ticketingRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getDashboardInfo") {
        this.axios({
          url: "/api/dashboard",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.usersChartData.datasets.data[2] = res.data.data.users.active
            vm.usersChartData.datasets.data[1] = res.data.data.users.disabled
            vm.usersChartData.datasets.data[0] = res.data.data.users.locked
            vm.usersChartAll = res.data.data.users.total

            vm.servicesChartData.datasets.data[1] = res.data.data.services.enabled
            vm.servicesChartData.datasets.data[0] = res.data.data.services.disabled
            vm.servicesChartAll = res.data.data.services.total

            vm.todaysLoginsChartData.datasets.data[1] = res.data.data.logins.successful
            vm.todaysLoginsChartData.datasets.data[0] = res.data.data.logins.unsuccessful
            vm.todaysLoginsChartAll = res.data.data.logins.total
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
        })
      } else if (command === "getUserServices") {
        this.axios({
          url: "/api/services/user",
          method: "GET"
        }).then((res) => {
          if (res.data.status.code === 200) {
            for (let i = 0; i < res.data.data.length; ++i) {
              if (typeof res.data.data[i].logo !== "undefined") {
                if (res.data.data[i].logo === "") {
                  res.data.data[i].logo = "images/servicePlaceholder.jpg"
                }
              } else {
                res.data.data[i].logo = "images/servicePlaceholder.jpg"
              }

              // eslint-disable-next-line prefer-const
              let translationId = "dashboardService_" + String(res.data.data[i]._id)
              vm.$store.state.translationsSwitch.Fa[translationId] = res.data.data[i].description
              vm.$store.state.translationsSwitch.En[translationId] = res.data.data[i].name
              vm.$store.state.translations[translationId] = res.data.data[i].description

              res.data.data[i].serviceId = res.data.data[i].serviceId.replace(/\((.*?)\)/g, "")
              res.data.data[i].serviceId = res.data.data[i].serviceId.replace(/\^/g, "")
              res.data.data[i].serviceId = res.data.data[i].serviceId.replace(/\\/g, "\\\\")

              if (vm.services[i].notification.count > 10) {
                vm.services[i].notification.realCount = "10+"
              } else {
                vm.services[i].notification.realCount = String(vm.services[i].notification.count)
              }

              vm.services = res.data.data
            }
            vm.services.sort((a, b) => b.position - a.position)
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
        })

        for (let i = 0; i < vm.services.length; ++i) {
          if (typeof vm.services[i].logo !== "undefined") {
            if (vm.services[i].logo === "") {
              vm.services[i].logo = "images/servicePlaceholder.jpg"
            }
          } else {
            vm.services[i].logo = "images/servicePlaceholder.jpg"
          }

          // eslint-disable-next-line prefer-const
          let translationId = "dashboardService_" + String(vm.services[i]._id)
          vm.$store.state.translationsSwitch.Fa[translationId] = vm.services[i].description
          vm.$store.state.translationsSwitch.En[translationId] = vm.services[i].name
          vm.$store.state.translations[translationId] = vm.services[i].description

          vm.services[i].serviceId = vm.services[i].serviceId.replace(/\((.*?)\)/g, "")
          vm.services[i].serviceId = vm.services[i].serviceId.replace(/\^/g, "")
          vm.services[i].serviceId = vm.services[i].serviceId.replace(/\\/g, "\\\\")

          if (vm.services[i].notification.count > 10) {
            vm.services[i].notification.realCount = "10+"
          } else {
            vm.services[i].notification.realCount = String(vm.services[i].notification.count)
          }
        }
        vm.services.sort((a, b) => b.position - a.position)
      }
    },
    showServiceNotification (id) {
      const vm = this
      for (let i = 0; i < this.services.length; ++i) {
        if (this.services[i]._id === id) {
          for (let j = 0; j < this.services[i].notification.notifications.length; ++j) {
            let position = "topLeft"
            let rtl = true
            if (this.$store.state.direction === "ltr") {
              position = "topRight"
              rtl = false
            }
            iziToast.show({
              title: this.services[i].description,
              message: this.services[i].notification.notifications[j].title,
              position: position,
              backgroundColor: "#A3DBF1",
              icon: "pi pi-bell",
              transitionIn: "fadeInLeft",
              rtl: rtl,
              layout: 2,
              timeout: false,
              progressBar: false,
              buttons: [
                [
                  "<button class='service-notification-button' style='border-radius: 6px;'>" + this.$t("more") + "</button>",
                  function (instance, toast) {
                    window.open(vm.services[i].notification.notifications[j].url)
                    instance.hide({
                      transitionOut: "fadeOutRight"
                    }, toast)
                  }
                ]
              ]
            })
          }
          break
        }
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
    }
  },
  computed: {
    usersChartTitle () {
      // eslint-disable-next-line vue/no-side-effects-in-computed-properties
      this.usersChartData.labels = [this.$t("locked"), this.$t("disabled"), this.$t("active")]
      return this.$t("users")
    },
    servicesChartTitle () {
      // eslint-disable-next-line vue/no-side-effects-in-computed-properties
      this.servicesChartData.labels = [this.$t("disabled"), this.$t("active")]
      return this.$t("services")
    },
    todaysLoginsChartTitle () {
      // eslint-disable-next-line vue/no-side-effects-in-computed-properties
      this.todaysLoginsChartData.labels = [this.$t("unsuccessful"), this.$t("successful")]
      return this.$t("todaysLogins")
    }
  }
}
</script>

<style scoped>
canvas {
  height: 230px;
}
.service-link {
  color: #212121;
  cursor: pointer;
  text-decoration: none;
}
.service-link:hover {
  color: #007bff;
}
.service-notification {
  cursor: pointer;
  font-size: 2rem;
}
</style>
