import "primevue/resources/primevue.min.css"
import "primeflex/primeflex.css"
import "primeicons/primeicons.css"
import "prismjs/themes/prism-coy.css"
import "./assets/styles/layout.scss"
import "@fortawesome/fontawesome-free/css/all.css"
import "./assets/styles/iziToast.min.css"

import ParssoConfigs from "./Configs.json"
import ParssoMessages from "./Messages.json"

import { createApp, reactive } from "vue"
import { createStore } from "vuex"
import App from "./App.vue"
import router from "./router"
import { createI18n } from "vue-i18n"
import axios from "axios"
import VueAxios from "vue-axios"
import PrimeVue from "primevue/config"
import Ripple from "primevue/ripple"
import StyleClass from "primevue/styleclass"
import Tooltip from "primevue/tooltip"
import Avatar from "primevue/avatar"
import Button from "primevue/button"
import SpeedDial from "primevue/speeddial"
import Chart from "primevue/chart"
import DataView from "primevue/dataview"
import DataViewLayoutOptions from "primevue/dataviewlayoutoptions"
import Badge from "primevue/badge"
import Toolbar from "primevue/toolbar"
import DataTable from "primevue/datatable"
import Column from "primevue/column"

const i18n = createI18n({
  locale: "Fa",
  messages: ParssoMessages
})

const store = createStore({
  state () {
    return {
      languages: ParssoConfigs.languages,
      version: ParssoConfigs.app.version,
      lastError: 403,
      appName: ParssoConfigs.app.Fa,
      clientName: ParssoConfigs.client.Fa,
      direction: ParssoConfigs.languages.Fa,
      accessLevel: 0, // 0 for not logged in, 1 for "USER" and "PRESENTER", 2 for "SUPPORTER", 3 for "ADMIN", 4 for "SUPERUSER"
      userId: "",
      displayName: "",
      farsiFont: ParssoConfigs.font.Fa,
      alignRTL: ParssoConfigs.alignRTL.Fa,
      alignLTR: ParssoConfigs.alignLTR.Fa,
      translations: {},
      translationsSwitch: {
        Fa: {},
        En: {}
      }
    }
  },
  mutations: {
    errorChange (state, error) {
      state.lastError = error
    },
    languageChange (state, language) {
      state.appName = ParssoConfigs.app[language]
      state.clientName = ParssoConfigs.client[language]
      state.direction = ParssoConfigs.languages[language]
      state.farsiFont = ParssoConfigs.font[language]
      state.alignRTL = ParssoConfigs.alignRTL[language]
      state.alignLTR = ParssoConfigs.alignLTR[language]
      for (const translation in state.translations) {
        if (language === "Fa") {
          state.translations[translation] = state.translationsSwitch.Fa[translation]
        } else if (language === "En") {
          state.translations[translation] = state.translationsSwitch.En[translation]
        }
      }
    },
    setAccessLevel (state) {
      /* axios.get("https://parsso2.razi.ac.ir/api/users")
        .then((res) => {
          if (res.data.role === "SUPERUSER") {
            state.accessLevel = 4
          } else if (res.data.role === "ADMIN") {
            state.accessLevel = 3
          } else if (res.data.role === "SUPPORTER") {
            state.accessLevel = 2
          } else if (res.data.role === "USER" || res.data.role === "PRESENTER") {
            state.accessLevel = 1
          } else {
            state.accessLevel = 0
          }
          state.userId = res.data.userId
          state.displayName = res.data.displayName
        })
        .catch(() => {
          state.accessLevel = 0
        }) */
      state.accessLevel = 4
      state.userId = "admin"
      state.displayName = "مدیر ویژه"
    }
  }
})

router.beforeEach(function (to, from, next) {
  window.scrollTo(0, 0)
  if (to.meta.requiresAccessLevel === 4) {
    if (store.state.accessLevel >= 4) {
      next()
    } else {
      store.commit("errorChange", 403)
      next({ path: "/error" })
    }
  } else if (to.meta.requiresAccessLevel === 2) {
    if (store.state.accessLevel >= 2) {
      next()
    } else {
      store.commit("errorChange", 403)
      next({ path: "/error" })
    }
  } else if (to.meta.requiresAccessLevel === 1) {
    if (store.state.accessLevel >= 1) {
      next()
    } else {
      store.commit("errorChange", 403)
      next({ path: "/error" })
    }
  } else {
    next()
  }
})

const app = createApp(App)

app.config.globalProperties.$appState = reactive({ theme: ParssoConfigs.app.defaultTheme, darkTheme: false })

app.use(store)
app.use(router)
app.use(i18n)
app.use(PrimeVue, { ripple: true, inputStyle: "outlined" })
app.use(VueAxios, axios)

app.directive("ripple", Ripple)
app.directive("styleclass", StyleClass)
app.directive("tooltip", Tooltip)

app.component("AppAvatar", Avatar)
app.component("Button", Button)
app.component("SpeedDial", SpeedDial)
app.component("Chart", Chart)
app.component("DataView", DataView)
app.component("DataViewLayoutOptions", DataViewLayoutOptions)
app.component("Badge", Badge)
app.component("Toolbar", Toolbar)
app.component("DataTable", DataTable)
app.component("Column", Column)

app.mount("#app")
