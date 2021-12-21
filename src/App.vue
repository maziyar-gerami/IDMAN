<template>
  <div :class="containerClass" @click="onWrapperClick">
    <AppTopBar @menu-toggle="onMenuToggle" />
    <div class="layout-sidebar" @click="onSidebarClick">
        <AppMenu :model="menu" @menuitem-click="onMenuItemClick" />
    </div>

    <div class="layout-main-container">
        <div class="layout-main">
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

<!-- <template>
  <div id="nav">
    <router-link to="/accessreports">AccessReports</router-link> |
    <router-link to="/audits">Audits</router-link> |
    <router-link to="/changepassword">ChangePassword</router-link> |
    <router-link to="/">Dashboard</router-link> |
    <router-link to="/error">Error</router-link> |
    <router-link to="/events">Events</router-link> |
    <router-link to="/groups">Groups</router-link> |
    <router-link to="/notifications">Notifications</router-link> |
    <router-link to="/profile">Profile</router-link> |
    <router-link to="/reports">Reports</router-link> |
    <router-link to="/resetpassword">ResetPassword</router-link> |
    <router-link to="/roles">Roles</router-link> |
    <router-link to="/services">Services</router-link> |
    <router-link to="/settings">Settings</router-link> |
    <router-link to="/ticketing">Ticketing</router-link> |
    <router-link to="/users">Users</router-link>
  </div>
  <router-view/>
</template> -->

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
      mobileMenuActive: false,
      menu: [
        {
          label: "",
          items: [
            {
              label: "Dashboard", icon: "pi pi-fw pi-id-card", to: "/"
            },
            {
              label: "Users", icon: "pi pi-fw pi-check-square", to: "/users"
            },
            {
              label: "Groups", icon: "pi pi-fw pi-bookmark", to: "/groups"
            },
            {
              label: "Services", icon: "pi pi-fw pi-exclamation-circle", to: "/services"
            },
            {
              label: "Roles", icon: "pi pi-fw pi-tablet", to: "/roles"
            },
            {
              label: "Ticketing", icon: "pi pi-fw pi-table", to: "/ticketing"
            },
            {
              label: "Notifications", icon: "pi pi-fw pi-list", to: "/notifications"
            },
            {
              label: "Reports",
              icon: "pi pi-fw pi-share-alt",
              items: [
                {
                  label: "Audits", icon: "pi pi-fw pi-share-alt", to: "/audits"
                },
                {
                  label: "Events", icon: "pi pi-fw pi-share-alt", to: "/events"
                },
                {
                  label: "Reports", icon: "pi pi-fw pi-share-alt", to: "/reports"
                },
                {
                  label: "Access Reports", icon: "pi pi-fw pi-share-alt", to: "/accessreports"
                }
              ]
            },
            {
              label: "Profile", icon: "pi pi-fw pi-chart-bar", to: "/profile"
            },
            {
              label: "Settings", icon: "pi pi-fw pi-circle-off", to: "/settings"
            }
          ]
        }
      ]
    }
  },
  watch: {
    /* $route () {
      this.menuActive = false
      this.$toast.removeAllGroups()
    } */
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
    }
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
