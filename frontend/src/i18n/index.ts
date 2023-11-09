import { createI18n } from "vue-i18n"
import en_US from "./locales/en_US"
import zh_CN from "./locales/zh_CN"

const i18n = createI18n({
  // something vue-i18n options here ...
  legacy: false,
  globalInjection: true,
  messages: {
    ["zh-cn"]: {
      ...zh_CN,
    },
    ["en"]: {
      ...en_US,
    },
  },
  locale: "zh-cn", // set locale
  fallbackLocale: "en", // set fallback locale
})

export default i18n
