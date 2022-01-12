<template>
  <div :class="containerClass" @click="onWrapperClick" :dir="$store.state.direction">
    <AppTopBar @menu-toggle="onMenuToggle" />
    <div class="layout-sidebar" @click="onSidebarClick" :dir="$store.state.direction">
      <AppUserInfo v-if="$store.state.accessLevel > 0" />
      <AppMenu :model="menuObject.menu" @menuitem-click="onMenuItemClick" />
      <p :style="'font-family: ' + this.$store.state.farsiFont" class="varsion">{{ $store.state.version }}</p>
    </div>

    <div class="layout-main-container" :dir="$store.state.direction">
        <div class="layout-main" :dir="$store.state.direction">
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
import AppUserInfo from "@/components/AppUserInfo.vue"
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
  beforeCreate () {
    document.title = this.$store.state.appName
    this.$store.commit("setAccessLevel")
  },
  beforeUpdate () {
    if (this.mobileMenuActive) {
      this.addClass(document.body, "body-overflow-hidden")
    } else {
      this.removeClass(document.body, "body-overflow-hidden")
    }
  },
  methods: {
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
    menuObject () {
      if (this.$store.state.accessLevel === 4) {
        return {
          menu: [
            {
              label: "",
              items: [
                {
                  label: this.$t("dashboard"), icon: "pi pi-fw pi-th-large", to: "/"
                },
                {
                  label: this.$t("users"), icon: "pi pi-fw pi-user", to: "/users"
                },
                {
                  label: this.$t("groups"), icon: "pi pi-fw pi-users", to: "/groups"
                },
                {
                  label: this.$t("services"), icon: "fa fa-fw fa-server", to: "/services"
                },
                {
                  label: this.$t("roles"), icon: "pi pi-fw pi-tags", to: "/roles"
                },
                {
                  label: this.$t("ticketing"), icon: "pi pi-fw pi-ticket", to: "/ticketing"
                },
                {
                  label: this.$t("notifications"), icon: "pi pi-fw pi-bell", to: "/notifications"
                },
                {
                  label: this.$t("reports"),
                  icon: "pi pi-fw pi-chart-bar",
                  items: [
                    {
                      label: this.$t("audits"), icon: "pi pi-fw pi-search", to: "/audits"
                    },
                    {
                      label: this.$t("events"), icon: "pi pi-fw pi-calendar", to: "/events"
                    },
                    {
                      label: this.$t("reports"), icon: "pi pi-fw pi-file", to: "/reports"
                    },
                    {
                      label: this.$t("accessReports"), icon: "pi pi-fw pi-list", to: "/accessreports"
                    }
                  ]
                },
                {
                  label: this.$t("profile"), icon: "pi pi-fw pi-id-card", to: "/profile"
                },
                {
                  label: this.$t("settings"), icon: "pi pi-fw pi-cog", to: "/settings"
                },
                {
                  label: this.$t("privacy"), icon: "pi pi-fw pi-lock", to: "/privacy"
                },
                {
                  label: this.$t("guide"), icon: "pi pi-fw pi-info-circle", url: "/Parsso-User-Guide.pdf", target: "_blank"
                }
              ]
            }
          ]
        }
      } else if (this.$store.state.accessLevel === 3 || this.$store.state.accessLevel === 2) {
        return {
          menu: [
            {
              label: "",
              items: [
                {
                  label: this.$t("dashboard"), icon: "pi pi-fw pi-th-large", to: "/"
                },
                {
                  label: this.$t("users"), icon: "pi pi-fw pi-user", to: "/users"
                },
                {
                  label: this.$t("groups"), icon: "pi pi-fw pi-users", to: "/groups"
                },
                {
                  label: this.$t("services"), icon: "fa fa-fw fa-server", to: "/services"
                },
                {
                  label: this.$t("ticketing"), icon: "pi pi-fw pi-ticket", to: "/ticketing"
                },
                {
                  label: this.$t("notifications"), icon: "pi pi-fw pi-bell", to: "/notifications"
                },
                {
                  label: this.$t("reports"),
                  icon: "pi pi-fw pi-chart-bar",
                  items: [
                    {
                      label: this.$t("audits"), icon: "pi pi-fw pi-search", to: "/audits"
                    },
                    {
                      label: this.$t("events"), icon: "pi pi-fw pi-calendar", to: "/events"
                    },
                    {
                      label: this.$t("reports"), icon: "pi pi-fw pi-file", to: "/reports"
                    },
                    {
                      label: this.$t("accessReports"), icon: "pi pi-fw pi-list", to: "/accessreports"
                    }
                  ]
                },
                {
                  label: this.$t("profile"), icon: "pi pi-fw pi-id-card", to: "/profile"
                },
                {
                  label: this.$t("privacy"), icon: "pi pi-fw pi-lock", to: "/privacy"
                },
                {
                  label: this.$t("guide"), icon: "pi pi-fw pi-info-circle", url: "/Parsso-User-Guide.pdf", target: "_blank"
                }
              ]
            }
          ]
        }
      } else if (this.$store.state.accessLevel === 1) {
        return {
          menu: [
            {
              label: "",
              items: [
                {
                  label: this.$t("dashboard"), icon: "pi pi-fw pi-th-large", to: "/"
                },
                {
                  label: this.$t("ticketing"), icon: "pi pi-fw pi-ticket", to: "/ticketing"
                },
                {
                  label: this.$t("reports"),
                  icon: "pi pi-fw pi-chart-bar",
                  items: [
                    {
                      label: this.$t("audits"), icon: "pi pi-fw pi-search", to: "/audits"
                    },
                    {
                      label: this.$t("events"), icon: "pi pi-fw pi-calendar", to: "/events"
                    },
                    {
                      label: this.$t("reports"), icon: "pi pi-fw pi-file", to: "/reports"
                    }
                  ]
                },
                {
                  label: this.$t("profile"), icon: "pi pi-fw pi-id-card", to: "/profile"
                },
                {
                  label: this.$t("privacy"), icon: "pi pi-fw pi-lock", to: "/privacy"
                },
                {
                  label: this.$t("guide"), icon: "pi pi-fw pi-info-circle", url: "/Parsso-User-Guide.pdf", target: "_blank"
                }
              ]
            }
          ]
        }
      } else {
        return {
          menu: [
            {
              label: "",
              items: [
                {
                  label: this.$t("resetPassword"), icon: "pi pi-fw pi-key", to: "/resetpassword"
                },
                {
                  label: this.$t("privacy"), icon: "pi pi-fw pi-lock", to: "/privacy"
                },
                {
                  label: this.$t("guide"), icon: "pi pi-fw pi-info-circle", url: "/Parsso-User-Guide.pdf", target: "_blank"
                }
              ]
            }
          ]
        }
      }
    }
  },
  components: {
    AppTopBar,
    AppMenu,
    AppUserInfo
    /* AppConfig,
    AppFooter, */
  }
}
</script>

<style>
.varsion {
  max-height: 1.5rem;
  text-align: center;
  position: absolute;
  bottom: 0;
  right: 0;
  left: 0;
  margin: 0;
  padding: 0;
}
.p-paginator {
  direction: initial;
}
.p-dataview-layout-options {
  direction: initial;
}
.p-paginator-page {
  font-family: "IRANSansWeb-PersianNumbers";
}
.p-dropdown-label {
  font-family: "IRANSansWeb-PersianNumbers";
}
.p-dropdown-item {
  font-family: "IRANSansWeb-PersianNumbers";
}
.p-badge  {
  font-family: "IRANSansWeb-PersianNumbers";
}
.service-notification-button {
  font-family: "IRANSansWeb-PersianNumbers";
}
.iziToast-texts {
  font-family: "IRANSansWeb-PersianNumbers";
}
</style>
