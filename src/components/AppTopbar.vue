<template>
  <div class="layout-topbar" :dir="$store.state.direction">
    <router-link to="/" class="layout-topbar-logo">
      <span>{{ $store.state.clientName }}</span>
    </router-link>
    <p :style="'font-family: ' + this.$store.state.farsiFont" class="dateTime mx-3">{{ setDate }}|{{ time }}</p>
    <button class="p-link layout-menu-button layout-topbar-button" @click="onMenuToggle">
      <i class="pi pi-bars"></i>
    </button>

    <button class="p-link layout-topbar-menu-button layout-topbar-button"
      v-styleclass="{ selector: '@next', enterClass: 'hidden', enterActiveClass: 'scalein',
      leaveToClass: 'hidden', leaveActiveClass: 'fadeout', hideOnOutsideClick: true}">
      <i class="pi pi-ellipsis-v"></i>
    </button>
    <ul class="layout-topbar-menu hidden lg:flex origin-top">
      <li v-if="$store.state.accessLevel > 0">
        <button class="topbarBtn" :dir="$store.state.direction">
          <SpeedDial :model="profileList" direction="down" showIcon="pi pi-user" :rotateAnimation="false" :tooltipOptions="{ position: this.$store.state.alignRTL }" style="top: 0;" />
        </button>
      </li>
      <li>
        <button class="topbarBtn" :dir="$store.state.direction">
          <SpeedDial :model="languageList" direction="down" showIcon="fa fa-language" :rotateAnimation="false" :tooltipOptions="{ position: this.$store.state.alignRTL }" style="top: 0;" />
        </button>
      </li>
    </ul>
  </div>
</template>

<script>
/* eslint-disable new-cap */
export default {
  name: "AppTopbar",
  data () {
    return {
      persianDate: null,
      time: ""
    }
  },
  created () {
    this.persianDate = require("persian-date/dist/persian-date")
    setInterval(this.setTime, 1000)
    this.setTime()
  },
  methods: {
    onMenuToggle (event) {
      this.$emit("menu-toggle", event)
    },
    onTopbarMenuToggle (event) {
      this.$emit("topbar-menu-toggle", event)
    },
    zeroPadding (num, digit) {
      let zero = ""
      for (let i = 0; i < digit; i++) {
        zero += "0"
      }
      return (zero + num).slice(-digit)
    },
    setTime () {
      const clock = new Date()
      this.time = "  " + this.zeroPadding(clock.getHours(), 2) + ":" + this.zeroPadding(clock.getMinutes(), 2) + ":" + this.zeroPadding(clock.getSeconds(), 2) + "  "
    }
  },
  computed: {
    profileList () {
      return [
        {
          label: this.$t("profile"),
          icon: "pi pi-id-card",
          command: () => {
            this.$router.push("Profile")
          }
        },
        {
          label: this.$t("logout"),
          icon: "pi pi-sign-out",
          command: () => {
            window.location.href = "/logout"
          }
        }
      ]
    },
    languageList () {
      // eslint-disable-next-line prefer-const
      let langList = []
      for (const language in this.$store.state.languages) {
        if (this.$i18n.locale !== language) {
          langList.push(
            {
              label: this.$t(language),
              icon: "parssoIcon parssoIcon-" + language,
              command: () => {
                this.$i18n.locale = language
                this.$store.commit("languageChange", language)
              }
            }
          )
        }
      }
      return langList
    },
    setDate () {
      let date
      if (this.$i18n.locale === "Fa") {
        date = new this.persianDate().format("  dddd، DD MMMM YYYY  ")
      } else {
        date = new this.persianDate().toCalendar("gregorian").toLocale("en").format("  dddd، DD MMMM YYYY  ")
      }
      return date
    }
  }
}
</script>

<style scoped>
.topbarBtn[dir="rtl"] {
  margin-right: 1rem;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  position: relative;
  width: 3rem;
  height: 3rem;
  border-radius: 50%;
  text-align: left;
  background-color: transparent;
  padding: 0;
  border: 0;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
.topbarBtn[dir="ltr"] {
  margin-left: 1rem;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  position: relative;
  width: 3rem;
  height: 3rem;
  border-radius: 50%;
  text-align: left;
  background-color: transparent;
  padding: 0;
  border: 0;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
.dateTime {
  display: flex;
  margin: 0;
  align-items: center;
  color: #ffffff;
  border-radius: 12px;
}
</style>
