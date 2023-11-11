import { createApp } from "vue"
import "./style.css"
import App from "./App.vue"
import router from "./router"
import pinia from "./store"
import i18n from "./i18n"
import "virtual:uno.css"
import debounce from "./directives/debounce"
import permissions from "./directives/permissions"

const app = createApp(App)
app.use(router).use(pinia).use(i18n)
//defining the directive
app.directive("debounce", debounce).directive("permissions", permissions)
app.mount("#app")
