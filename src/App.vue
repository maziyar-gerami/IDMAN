<template>
  <div :class="containerClass" @click="onWrapperClick" :dir="direction">
    <AppTopBar @menu-toggle="onMenuToggle" @click="aaaaa" />
    <div class="layout-sidebar" @click="onSidebarClick" :dir="direction">
        <AppMenu :model="translationObject.menu" @menuitem-click="onMenuItemClick" />
    </div>

    <div class="layout-main-container" :dir="direction">
        <div class="layout-main" :dir="direction">
            <router-view />
        </div>
        <!-- <AppFooter /> -->
    </div>

<!-- <AppConfig :layoutMode="layoutMode" @layout-change="onLayoutChange" />
    <transition name="layout-mask">
        <div class="layout-mask p-component-overlay" v-if="mobileMenuActive"></div>
    </transition> -->
  </div>
</template>

<script>
import AppTopBar from "@/components/AppTopbar.vue"
import AppMenu from "@/components/AppMenu.vue"
/* import AppConfig from "./AppConfig.vue"
import AppFooter from "./AppFooter.vue" */

export default {
  data () {
    return {
      layoutMode: "static",
      staticMenuInactive: false,
      overlayMenuActive: false,
      mobileMenuActive: false
    }
  },
  watch: {
    /* $route () {
      this.menuActive = false
      this.$toast.removeAllGroups()
    } */
  },
  methods: {
    aaaaa () {
      console.log(this.$direction)
    },
    onWrapperClick () {
      if (!this.menuClick) {
        this.overlayMenuActive = false
        this.mobileMenuActive = false
      }
      this.menuClick = false
    },
    onMenuToggle () {
      this.menuClick = true
      if (this.isDesktop()) {
        if (this.layoutMode === "overlay") {
          if (this.mobileMenuActive === true) {
            this.overlayMenuActive = true
          }
          this.overlayMenuActive = !this.overlayMenuActive
          this.mobileMenuActive = false
        } else if (this.layoutMode === "static") {
          this.staticMenuInactive = !this.staticMenuInactive
        }
      } else {
        this.mobileMenuActive = !this.mobileMenuActive
      }
      event.preventDefault()
    },
    onSidebarClick () {
      this.menuClick = true
    },
    onMenuItemClick (event) {
      if (event.item && !event.item.items) {
        this.overlayMenuActive = false
        this.mobileMenuActive = false
      }
    },
    onLayoutChange (layoutMode) {
      this.layoutMode = layoutMode
    },
    addClass (element, className) {
      if (element.classList) {
        element.classList.add(className)
      } else {
        element.className += " " + className
      }
    },
    removeClass (element, className) {
      if (element.classList) {
        element.classList.remove(className)
      } else {
        element.className = element.className.replace(new RegExp("(^|\\b)" + className.split(" ").join("|") + "(\\b|$)", "gi"), " ")
      }
    },
    isDesktop () {
      return window.innerWidth >= 992
    },
    isSidebarVisible () {
      if (this.isDesktop()) {
        if (this.layoutMode === "static") {
          return !this.staticMenuInactive
        } else if (this.layoutMode === "overlay") {
          return this.overlayMenuActive
        }
      }
      return true
    }
  },
  computed: {
    containerClass () {
      return ["layout-wrapper", {
        "layout-overlay": this.layoutMode === "overlay",
        "layout-static": this.layoutMode === "static",
        "layout-static-sidebar-inactive": this.staticMenuInactive && this.layoutMode === "static",
        "layout-overlay-sidebar-active": this.overlayMenuActive && this.layoutMode === "overlay",
        "layout-mobile-sidebar-active": this.mobileMenuActive,
        "p-input-filled": this.$primevue.config.inputStyle === "filled",
        "p-ripple-disabled": this.$primevue.config.ripple === false,
        "layout-theme-light": this.$appState.theme.startsWith("saga")
      }]
    },
    logo () {
      return (this.$appState.darkTheme) ? "images/logo-white.svg" : "images/logo-dark.svg"
    },
    direction () {
      for (const language in this.$languages) {
        if (this.$i18n.locale === language) {
          return this.$languages[language]
        }
      }
      return "rtl"
    },
    translationObject () {
      return {
        menu: [
          {
            label: "",
            items: [
              {
                label: this.$t("App.dashboard"), icon: "pi pi-fw pi-chart-bar", to: "/"
              },
              {
                label: this.$t("App.users"), icon: "pi pi-fw pi-user", to: "/users"
              },
              {
                label: this.$t("App.groups"), icon: "pi pi-fw pi-users", to: "/groups"
              },
              {
                label: this.$t("App.services"), icon: "pi pi-fw pi-sitemap", to: "/services"
              },
              {
                label: this.$t("App.roles"), icon: "pi pi-fw pi-tags", to: "/roles"
              },
              {
                label: this.$t("App.ticketing"), icon: "pi pi-fw pi-ticket", to: "/ticketing"
              },
              {
                label: this.$t("App.notifications"), icon: "pi pi-fw pi-bell", to: "/notifications"
              },
              {
                label: this.$t("App.reports"),
                icon: "pi pi-fw pi-credit-card",
                items: [
                  {
                    label: this.$t("App.audits"), icon: "pi pi-fw pi-search", to: "/audits"
                  },
                  {
                    label: this.$t("App.events"), icon: "pi pi-fw pi-calendar", to: "/events"
                  },
                  {
                    label: this.$t("App.reports"), icon: "pi pi-fw pi-file", to: "/reports"
                  },
                  {
                    label: this.$t("App.accessReports"), icon: "pi pi-fw pi-list", to: "/accessreports"
                  }
                ]
              },
              {
                label: this.$t("App.profile"), icon: "pi pi-fw pi-id-card", to: "/profile"
              },
              {
                label: this.$t("App.settings"), icon: "pi pi-fw pi-cog", to: "/settings"
              }
            ]
          }
        ]
      }
    }
  },
  beforeCreate () {
    document.title = "پارسو"
  },
  beforeUpdate () {
    if (this.mobileMenuActive) {
      this.addClass(document.body, "body-overflow-hidden")
    } else {
      this.removeClass(document.body, "body-overflow-hidden")
    }
  },
  components: {
    AppTopBar,
    AppMenu
    /* AppConfig,
    AppFooter, */
  }
}
</script>
