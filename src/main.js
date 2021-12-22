import "primevue/resources/primevue.min.css"
import "primeflex/primeflex.css"
import "primeicons/primeicons.css"
import "prismjs/themes/prism-coy.css"
import "./assets/styles/layout.scss"

import ParssoConfigs from "./Configs.json"
import ParssoMessages from "./Messages.json"

import { createApp, reactive } from "vue"
import App from "./App.vue"
import router from "./router"
import { createI18n } from "vue-i18n"
import PrimeVue from "primevue/config"
import Ripple from "primevue/ripple"
import StyleClass from "primevue/styleclass"

router.beforeEach(function (to, from, next) {
  window.scrollTo(0, 0)
  next()
})

export const i18n = createI18n({
  locale: "Fa",
  messages: ParssoMessages
})

const app = createApp(App)

app.config.globalProperties.$appNameFa = ParssoConfigs.appNameFa
app.config.globalProperties.$appNameEn = ParssoConfigs.appNameEn
app.config.globalProperties.$clientNameFa = ParssoConfigs.clientNameFa
app.config.globalProperties.$clientNameEn = ParssoConfigs.clientNameEn
app.config.globalProperties.$language = ParssoConfigs.defaultLanguage.lang
app.config.globalProperties.$direction = ParssoConfigs.defaultLanguage.dir
app.config.globalProperties.$version = ParssoConfigs.version
app.config.globalProperties.$appState = reactive({ theme: ParssoConfigs.defaultTheme, darkTheme: false })

app.use(router)
app.use(i18n)
app.use(PrimeVue, { ripple: true, inputStyle: "outlined" })

app.directive("ripple", Ripple)
app.directive("styleclass", StyleClass)

app.mount("#app")
