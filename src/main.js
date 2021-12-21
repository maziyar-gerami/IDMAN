import "primevue/resources/primevue.min.css"
import "primeflex/primeflex.css"
import "primeicons/primeicons.css"
import "prismjs/themes/prism-coy.css"
import "./assets/styles/layout.scss"

import { createApp, reactive } from "vue"
import App from "./App.vue"
import router from "./router"
import PrimeVue from "primevue/config"
import Ripple from "primevue/ripple"
import StyleClass from "primevue/styleclass"

router.beforeEach(function (to, from, next) {
  window.scrollTo(0, 0)
  next()
})

const app = createApp(App)

app.config.globalProperties.$appState = reactive({ theme: "lara-light-blue", darkTheme: false })

app.use(PrimeVue, { ripple: true, inputStyle: "outlined" })

app.use(router)

app.directive("ripple", Ripple)
app.directive("styleclass", StyleClass)

app.mount("#app")
