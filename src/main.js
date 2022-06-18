import "primevue/resources/primevue.min.css"
import "primeflex/primeflex.css"
import "primeicons/primeicons.css"
import "prismjs/themes/prism-coy.css"
import "./assets/styles/layout.scss"
import "@fortawesome/fontawesome-free/css/all.css"
import "boxicons/css/boxicons.min.css"
import "./assets/styles/iziToast.min.css"
import "persian-datepicker/dist/css/persian-datepicker.css"

import ParssoConfigs from "./Configs.json"
import ParssoMessages from "./Messages.json"

import { createApp, reactive } from "vue"
import { createStore } from "vuex"
import App from "./App.vue"
import router from "./router"
import { createI18n } from "vue-i18n"
import axios from "axios"
import VueAxios from "vue-axios"
import jQuery from "jquery"
import persianDate from "persian-date"
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
import InputText from "primevue/inputtext"
import InputNumber from "primevue/inputnumber"
import Dropdown from "primevue/dropdown"
import Paginator from "primevue/paginator"
import TabView from "primevue/tabview"
import TabPanel from "primevue/tabpanel"
import ProgressSpinner from "primevue/progressspinner"
import ToggleButton from "primevue/togglebutton"
import MultiSelect from "primevue/multiselect"
import Textarea from "primevue/textarea"
import Fieldset from "primevue/fieldset"
import Password from "primevue/password"
import Divider from "primevue/divider"
import Checkbox from "primevue/checkbox"
import BlockUI from "primevue/blockui"
import Tag from "primevue/tag"
import OverlayPanel from "primevue/overlaypanel"
import Listbox from "primevue/listbox"
import PickList from "primevue/picklist"
import Steps from "primevue/steps"
import Dialog from "primevue/dialog"
import InputSwitch from "primevue/inputswitch"
import Editor from "primevue/editor"
import FileUpload from "primevue/fileupload"
import RadioButton from "primevue/radiobutton"
import SelectButton from "primevue/selectbutton"
import * as XLSX from "xlsx"
import { jsPDF } from "jspdf"
import copyText from "@meforma/vue-copy-to-clipboard"

window.$ = jQuery
window.jQuery = jQuery
window.persianDate = persianDate
window.XLSX = XLSX
window.jsPDF = jsPDF

const i18n = createI18n({
  locale: "Fa",
  messages: ParssoMessages
})

const store = createStore({
  state () {
    return {
      languages: ParssoConfigs.languages,
      version: ParssoConfigs.app.version,
      appName: ParssoConfigs.app.Fa,
      clientName: ParssoConfigs.client.Fa,
      direction: ParssoConfigs.languages.Fa,
      reverseDirection: ParssoConfigs.reverseDirection.Fa,
      accessLevel: 0, // 0 for not logged in, 1 for "USER" and "PRESENTER", 2 for "SUPPORTER", 3 for "ADMIN", 4 for "SUPERUSER"
      userId: "",
      displayName: "",
      persianFont: ParssoConfigs.font.Fa,
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
    languageChange (state, language) {
      state.appName = ParssoConfigs.app[language]
      state.clientName = ParssoConfigs.client[language]
      state.direction = ParssoConfigs.languages[language]
      state.reverseDirection = ParssoConfigs.reverseDirection[language]
      state.persianFont = ParssoConfigs.font[language]
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
      axios({
        url: "/api/user",
        method: "GET"
      }).then((res) => {
        if (res.data.data.role === "SUPERUSER") {
          state.accessLevel = 4
        } else if (res.data.data.role === "ADMIN") {
          state.accessLevel = 3
        } else if (res.data.data.role === "SUPPORTER") {
          state.accessLevel = 2
        } else if (res.data.data.role === "USER" || res.data.data.role === "PRESENTER") {
          state.accessLevel = 1
        } else {
          state.accessLevel = 0
        }
        state.userId = res.data.data._id
        state.displayName = res.data.data.displayName
        runApp()
      }).catch(() => {
        state.accessLevel = 0
        runApp()
      })
    }
  }
})

store.commit("setAccessLevel")

function runApp () {
  router.beforeEach(function (to, from, next) {
    window.scrollTo(0, 0)
    if (to.meta.requiresAccessLevel === 4) {
      if (store.state.accessLevel >= 4) {
        next()
      } else {
        next({ path: "/403" })
      }
    } else if (to.meta.requiresAccessLevel === 2) {
      if (store.state.accessLevel >= 2) {
        next()
      } else {
        next({ path: "/403" })
      }
    } else if (to.meta.requiresAccessLevel === 1) {
      if (store.state.accessLevel >= 1) {
        next()
      } else {
        next({ path: "/403" })
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
  app.use(copyText)

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
  app.component("InputText", InputText)
  app.component("InputNumber", InputNumber)
  app.component("Dropdown", Dropdown)
  app.component("Paginator", Paginator)
  app.component("TabView", TabView)
  app.component("TabPanel", TabPanel)
  app.component("ProgressSpinner", ProgressSpinner)
  app.component("ToggleButton", ToggleButton)
  app.component("MultiSelect", MultiSelect)
  app.component("Textarea", Textarea)
  app.component("Fieldset", Fieldset)
  app.component("Password", Password)
  app.component("Divider", Divider)
  app.component("Checkbox", Checkbox)
  app.component("BlockUI", BlockUI)
  app.component("Tag", Tag)
  app.component("OverlayPanel", OverlayPanel)
  app.component("Listbox", Listbox)
  app.component("PickList", PickList)
  app.component("Dialog", Dialog)
  app.component("Steps", Steps)
  app.component("InputSwitch", InputSwitch)
  app.component("Editor", Editor)
  app.component("FileUpload", FileUpload)
  app.component("RadioButton", RadioButton)
  app.component("SelectButton", SelectButton)

  app.mount("#app")
}
