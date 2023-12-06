import { createApp } from "vue"

import App from "./App.vue"
import * as ElementPlusIconsVue from "@element-plus/icons-vue"

// import ElementPlus from "element-plus"
import { setupElementPlus } from "/@/plugins/ele-plus"

import "./style.css"
import "virtual:uno.css"
import "element-plus/dist/index.css"
import debounce from "./directives/debounce"
import permissions from "./directives/permissions"
import i18n from "./i18n"
import router from "./router"
import pinia from "./store"

const app = createApp(App)
app.use(router).use(pinia).use(i18n)
setupElementPlus(app)
// app.use(ElementPlus)
//defining the directive
app.directive("debounce", debounce)
app.directive("permission", permissions)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.mount("#app")
