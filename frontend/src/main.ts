import { createApp } from "vue"

import App from "./App.vue"

import "./style.css"
import "virtual:uno.css"
import debounce from "./directives/debounce"
import permissions from "./directives/permissions"
import i18n from "./i18n"
import naive from "./naive"
import router from "./router"
import pinia from "./store"

const app = createApp(App)
app.use(router).use(pinia).use(i18n).use(naive)
//defining the directive
app.directive("debounce", debounce).directive("permissions", permissions)
app.mount("#app")
