<template>
  <div class="grid mt-0 h-full">
    <div v-if="$store.state.accessLevel > 1" class="col-12 lg:col-4 xl:col-4 pt-0">
      <div class="card mb-0 h-full">
        <h4 :style="'font-family: ' + this.$store.state.farsiFont">{{ usersChartTitle }} ({{ usersChartAll }})</h4>
        <Chart type="pie" :data="usersChartData" :options="lightOptions" :width="550" :height="200" />
      </div>
    </div>
    <div v-if="$store.state.accessLevel > 1" class="col-12 lg:col-4 xl:col-4 pt-0">
      <div class="card mb-0 h-full">
        <h4 :style="'font-family: ' + this.$store.state.farsiFont">{{ servicesChartTitle }} ({{ servicesChartAll }})</h4>
        <Chart type="pie" :data="servicesChartData" :options="lightOptions" :width="550" :height="200" />
      </div>
    </div>
    <div v-if="$store.state.accessLevel > 1" class="col-12 lg:col-4 xl:col-4 pt-0">
      <div class="card mb-0 h-full">
        <h4 :style="'font-family: ' + this.$store.state.farsiFont">{{ todaysLoginsChartTitle }} ({{ todaysLoginsChartAll }})</h4>
        <Chart type="pie" :data="todaysLoginsChartData" :options="lightOptions" :width="550" :height="200" />
      </div>
    </div>
    <div class="col-12 lg:col-12 xl:col-12 mb-0 pb-0">
      <div class="card mb-0 h-full">
        <DataView :value="services" :layout="layout" paginatorPosition="both" :paginator="true" :rows="4" :rowsPerPageOptions="[4, 8, 16, 24]" :pageLinkSize="5">
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
      services: [
        {
          serviceId: "http://localhost:8080/login/cas",
          _id: 1638885239567,
          name: "Localhost",
          description: "11لوکال هاست",
          logo: "images/servicePlaceholder.jpg",
          url: "http://localhost:8080",
          position: 11,
          notification: {
            count: 22,
            notifications: [
              {
                title: "Test Parso #1",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #2",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #3",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #4",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              }
            ],
            inconsistency: null,
            return: {
              status: 405,
              message: "فرمت آدرس API صحیح نیست."
            }
          },
          dailyAccess: null,
          ipaddresses: null
        },
        {
          serviceId: "https://blog.pars-sso.ir/wp-admin",
          _id: 1609055559871,
          name: "weblog",
          description: "3وبلاگ پارسو",
          logo: "images/servicePlaceholder.jpg",
          url: "https://blog.pars-sso.ir",
          position: 3,
          notification: {
            count: 0,
            notifications: [],
            inconsistency: null,
            return: {
              status: 405,
              message: "فرمت آدرس API صحیح نیست."
            }
          },
          dailyAccess: null,
          ipaddresses: null
        },
        {
          serviceId: "https://blog.pars-sso.ir/wp-admin",
          _id: 1609055559555,
          name: "web",
          description: "1وبلاگ",
          logo: "images/servicePlaceholder.jpg",
          url: "https://blog.pars-sso.ir",
          position: 1,
          notification: {
            count: 4,
            notifications: [
              {
                title: "Test Parso #1",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #2",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #3",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #4",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              }
            ],
            inconsistency: null,
            return: {
              status: 405,
              message: "فرمت آدرس API صحیح نیست."
            }
          },
          dailyAccess: null,
          ipaddresses: null
        },
        {
          serviceId: "http://localhost:8080/login/cas",
          _id: 1638885236667,
          name: "Localhost",
          description: "6لوکال هاست",
          logo: "images/servicePlaceholder.jpg",
          url: "http://localhost:8080",
          position: 6,
          notification: {
            count: 22,
            notifications: [
              {
                title: "Test Parso #1",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #2",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #3",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #4",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              }
            ],
            inconsistency: null,
            return: {
              status: 405,
              message: "فرمت آدرس API صحیح نیست."
            }
          },
          dailyAccess: null,
          ipaddresses: null
        },
        {
          serviceId: "https://blog.pars-sso.ir/wp-admin",
          _id: 16090145559871,
          name: "weblog",
          description: "5وبلاگ پارسو",
          logo: "images/servicePlaceholder.jpg",
          url: "https://blog.pars-sso.ir",
          position: 5,
          notification: {
            count: 0,
            notifications: [],
            inconsistency: null,
            return: {
              status: 405,
              message: "فرمت آدرس API صحیح نیست."
            }
          },
          dailyAccess: null,
          ipaddresses: null
        },
        {
          serviceId: "https://blog.pars-sso.ir/wp-admin",
          _id: 1509055559555,
          name: "web",
          description: "20وبلاگ",
          logo: "images/servicePlaceholder.jpg",
          url: "https://blog.pars-sso.ir",
          position: 20,
          notification: {
            count: 4,
            notifications: [
              {
                title: "Test Parso #1",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #2",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #3",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #4",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              }
            ],
            inconsistency: null,
            return: {
              status: 405,
              message: "فرمت آدرس API صحیح نیست."
            }
          },
          dailyAccess: null,
          ipaddresses: null
        },
        {
          serviceId: "http://localhost:8080/login/cas",
          _id: 2638885239567,
          name: "Localhost",
          description: "13لوکال هاست",
          logo: "images/servicePlaceholder.jpg",
          url: "http://localhost:8080",
          position: 13,
          notification: {
            count: 22,
            notifications: [
              {
                title: "Test Parso #1",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #2",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #3",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #4",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              }
            ],
            inconsistency: null,
            return: {
              status: 405,
              message: "فرمت آدرس API صحیح نیست."
            }
          },
          dailyAccess: null,
          ipaddresses: null
        },
        {
          serviceId: "https://blog.pars-sso.ir/wp-admin",
          _id: 3609055559871,
          name: "weblog",
          description: "8وبلاگ پارسو",
          logo: "images/servicePlaceholder.jpg",
          url: "https://blog.pars-sso.ir",
          position: 8,
          notification: {
            count: 0,
            notifications: [],
            inconsistency: null,
            return: {
              status: 405,
              message: "فرمت آدرس API صحیح نیست."
            }
          },
          dailyAccess: null,
          ipaddresses: null
        },
        {
          serviceId: "https://blog.pars-sso.ir/wp-admin",
          _id: 1609055359553,
          name: "web",
          description: "16وبلاگ",
          logo: "images/servicePlaceholder.jpg",
          url: "https://blog.pars-sso.ir",
          position: 16,
          notification: {
            count: 4,
            notifications: [
              {
                title: "Test Parso #1",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #2",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #3",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              },
              {
                title: "Test Parso #4",
                url: "Http://thr.iooc.local",
                timestamp: 1630000000000
              }
            ],
            inconsistency: null,
            return: {
              status: 405,
              message: "فرمت آدرس API صحیح نیست."
            }
          },
          dailyAccess: null,
          ipaddresses: null
        }
      ],
      layout: "grid"
    }
  },
  created () {
    const vm = this
    /* this.axios.get("/api/dashboard")
      .then((res) => {
        vm.usersChartData.datasets.data[2] = res.data.users.active
        vm.usersChartData.datasets.data[1] = res.data.users.disabled
        vm.usersChartData.datasets.data[0] = res.data.users.locked
        vm.usersChartAll = res.data.users.total

        vm.servicesChartData.datasets.data[1] = res.data.services.enabled
        vm.servicesChartData.datasets.data[0] = res.data.services.disabled
        vm.servicesChartAll = res.data.services.total

        vm.todaysLoginsChartData.datasets.data[1] = res.data.logins.successful
        vm.todaysLoginsChartData.datasets.data[0] = res.data.logins.unsuccessful
        vm.todaysLoginsChartAll = res.data.logins.total
      })

    this.axios.get("/api/services/user")
      .then((res) => {
        for (let i = 0; i < res.data.length; ++i) {
          if (typeof res.data[i].logo !== "undefined") {
            if (res.data[i].logo === "") {
              res.data[i].logo = "images/servicePlaceholder.jpg"
            }
          } else {
            res.data[i].logo = "images/servicePlaceholder.jpg"
          }

          // eslint-disable-next-line prefer-const
          let translationId = "dashboardService_" + String(res.data[i]._id)
          vm.$store.state.translationsSwitch.Fa[translationId] = res.data[i].description
          vm.$store.state.translationsSwitch.En[translationId] = res.data[i].name
          vm.$store.state.translations[translationId] = res.data[i].description

          res.data[i].serviceId = res.data[i].serviceId.replace(/\((.*?)\)/g, "")
          res.data[i].serviceId = res.data[i].serviceId.replace(/\^/g, "")
          res.data[i].serviceId = res.data[i].serviceId.replace(/\\/g, "\\\\")

          if (vm.services[i].notification.count > 10) {
            vm.services[i].notification.realCount = "10+"
          } else {
            vm.services[i].notification.realCount = String(vm.services[i].notification.count)
          }

          vm.services = res.data
        }
        vm.services.sort((a, b) => b.position - a.position)
      }) */
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
  },
  methods: {
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
              rtl: rtl,
              layout: 2,
              timeout: false,
              progressBar: false,
              buttons: [
                ["<button class='service-notification-button'>" + this.$t("more") + "</button>",
                  function (instance, toast) {
                    window.open(vm.services[i].notification.notifications[j].url)
                  }
                ]
              ]
            })
          }
          break
        }
      }
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
