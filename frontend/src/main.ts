import { createApp } from "vue"
import "./style.css"
import App from "./App.vue"
import router from "./router"
import pinia from "./store"
import "virtual:uno.css"

const app = createApp(App)
app.use(router).use(pinia)
app.mount("#app")
