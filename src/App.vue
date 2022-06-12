<template>
  <div :class="containerClass" @click="onWrapperClick" :dir="$store.state.direction">
    <AppTopBar @menu-toggle="onMenuToggle" />
    <div class="layout-sidebar" @click="onSidebarClick" :dir="$store.state.direction">
      <AppUserInfo v-if="$store.state.accessLevel > 0" />
      <AppMenu :model="menuObject.menu" @menuitem-click="onMenuItemClick" />
      <p :style="'font-family: ' + this.$store.state.persianFont" class="varsion">{{ $store.state.version }}</p>
    </div>

    <div class="layout-main-container" :dir="$store.state.direction">
        <div class="layout-main" :dir="$store.state.direction">
            <router-view />
        </div>
    </div>
  </div>
</template>

<script>
import AppTopBar from "@/components/AppTopbar.vue"
import AppMenu from "@/components/AppMenu.vue"
import AppUserInfo from "@/components/AppUserInfo.vue"

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
    this.$primevue.config.locale.startsWith = this.$t("startsWith")
    this.$primevue.config.locale.contains = this.$t("contains")
    this.$primevue.config.locale.notContains = this.$t("notContains")
    this.$primevue.config.locale.endsWith = this.$t("endsWith")
    this.$primevue.config.locale.equals = this.$t("equals")
    this.$primevue.config.locale.notEquals = this.$t("notEquals")
    this.$primevue.config.locale.clear = this.$t("clear")
    this.$primevue.config.locale.apply = this.$t("apply")
    this.$primevue.config.locale.matchAll = this.$t("matchAll")
    this.$primevue.config.locale.matchAny = this.$t("matchAny")
    this.$primevue.config.locale.addRule = this.$t("addFilter")
    this.$primevue.config.locale.removeRule = this.$t("removeFilter")
    this.$primevue.config.locale.weak = ""
    this.$primevue.config.locale.medium = ""
    this.$primevue.config.locale.strong = ""
    this.$primevue.config.locale.passwordPrompt = this.$t("passwordPrompt")
    this.$primevue.config.locale.emptyFilterMessage = this.$t("emptyFilterMessage")
    this.$primevue.config.locale.emptyMessage = this.$t("emptyMessage")
    this.$primevue.config.locale.passwordPrompt = ""
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
                  label: this.$t("dashboard"), icon: "bx bx-fw bxs-dashboard", to: "/"
                },
                {
                  label: this.$t("users"), icon: "bx bx-fw bxs-user", to: "/users"
                },
                {
                  label: this.$t("groups"), icon: "bx bx-fw bxs-group", to: "/groups"
                },
                {
                  label: this.$t("services"), icon: "bx bx-fw bxs-server", to: "/services"
                },
                {
                  label: this.$t("roles"), icon: "bx bx-fw bxs-tag", to: "/roles"
                },
                {
                  label: this.$t("ticketing"), icon: "bx bx-fw bxs-envelope", to: "/ticketing"
                },
                {
                  label: this.$t("notifications"), icon: "bx bx-fw bxs-bell", to: "/notifications"
                },
                {
                  label: this.$t("devices"), icon: "bx bx-fw bxs-devices", to: "/devices"
                },
                {
                  label: this.$t("logs"),
                  icon: "bx bx-fw bxs-bar-chart-square",
                  items: [
                    {
                      label: this.$t("audits"), icon: "bx bx-fw bxs-search", to: "/audits"
                    },
                    {
                      label: this.$t("events"), icon: "bx bx-fw bxs-calendar", to: "/events"
                    },
                    {
                      label: this.$t("reports"), icon: "bx bx-fw bxs-file-blank", to: "/reports"
                    },
                    {
                      label: this.$t("accessReports"), icon: "bx bx-fw bxs-pie-chart-alt-2", to: "/accessreports"
                    }
                  ]
                },
                {
                  label: this.$t("profile"), icon: "bx bx-fw bxs-id-card", to: "/profile"
                },
                {
                  label: this.$t("settings"), icon: "bx bx-fw bxs-cog", to: "/settings"
                },
                {
                  label: this.$t("privacy"), icon: "bx bx-fw bxs-lock-alt", to: "/privacy", target: "_blank"
                },
                {
                  label: this.$t("guide"), icon: "bx bx-fw bxs-info-circle", url: "/Parsso-User-Guide.pdf", target: "_blank"
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
                  label: this.$t("dashboard"), icon: "bx bx-fw bxs-dashboard", to: "/"
                },
                {
                  label: this.$t("users"), icon: "bx bx-fw bxs-user", to: "/users"
                },
                {
                  label: this.$t("groups"), icon: "bx bx-fw bxs-group", to: "/groups"
                },
                {
                  label: this.$t("services"), icon: "bx bx-fw bxs-server", to: "/services"
                },
                {
                  label: this.$t("ticketing"), icon: "bx bx-fw bxs-envelope", to: "/ticketing"
                },
                {
                  label: this.$t("notifications"), icon: "bx bx-fw bxs-bell", to: "/notifications"
                },
                {
                  label: this.$t("devices"), icon: "bx bx-fw bxs-devices", to: "/devices"
                },
                {
                  label: this.$t("logs"),
                  icon: "bx bx-fw bxs-bar-chart-square",
                  items: [
                    {
                      label: this.$t("audits"), icon: "bx bx-fw bxs-search", to: "/audits"
                    },
                    {
                      label: this.$t("events"), icon: "bx bx-fw bxs-calendar", to: "/events"
                    },
                    {
                      label: this.$t("reports"), icon: "bx bx-fw bxs-file-blank", to: "/reports"
                    },
                    {
                      label: this.$t("accessReports"), icon: "bx bx-fw bxs-pie-chart-alt-2", to: "/accessreports"
                    }
                  ]
                },
                {
                  label: this.$t("profile"), icon: "bx bx-fw bxs-id-card", to: "/profile"
                },
                {
                  label: this.$t("privacy"), icon: "bx bx-fw bxs-lock-alt", to: "/privacy", target: "_blank"
                },
                {
                  label: this.$t("guide"), icon: "bx bx-fw bxs-info-circle", url: "/Parsso-User-Guide.pdf", target: "_blank"
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
                  label: this.$t("dashboard"), icon: "bx bx-fw bxs-dashboard", to: "/"
                },
                {
                  label: this.$t("ticketing"), icon: "bx bx-fw bxs-envelope", to: "/ticketing"
                },
                {
                  label: this.$t("logs"),
                  icon: "bx bx-fw bxs-bar-chart-square",
                  items: [
                    {
                      label: this.$t("audits"), icon: "bx bx-fw bxs-search", to: "/audits"
                    },
                    {
                      label: this.$t("events"), icon: "bx bx-fw bxs-calendar", to: "/events"
                    },
                    {
                      label: this.$t("reports"), icon: "bx bx-fw bxs-file-blank", to: "/reports"
                    }
                  ]
                },
                {
                  label: this.$t("profile"), icon: "bx bx-fw bxs-id-card", to: "/profile"
                },
                {
                  label: this.$t("privacy"), icon: "bx bx-fw bxs-lock-alt", to: "/privacy", target: "_blank"
                },
                {
                  label: this.$t("guide"), icon: "bx bx-fw bxs-info-circle", url: "/Parsso-User-Guide.pdf", target: "_blank"
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
                  label: this.$t("resetPassword"), icon: "bx bx-fw bxs-key", to: "/resetpassword"
                },
                {
                  label: this.$t("privacy"), icon: "bx bx-fw bxs-lock-alt", to: "/privacy", target: "_blank"
                },
                {
                  label: this.$t("guide"), icon: "bx bx-fw bxs-info-circle", url: "/Parsso-User-Guide.pdf", target: "_blank"
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
