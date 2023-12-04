import VueHook from "alova/vue"

// import { useAuthStore } from "../store/auth-store"
import { useUserStore } from "/@/store/modules/user"

// import { i18n } from "../i18n"
import { createAlova } from "alova"
import GlobalFetch from "alova/GlobalFetch"
import { ElLoading, ElMessage, ElNotification } from "element-plus"

let loading: { close(): void }
// For Make Log on Develop Mode
const logOnDev = (message: string) => {
  if (import.meta.env.MODE === "development") {
    console.log(message)
  }
}
// 1. 创建alova实例
const alovaInstance = createAlova({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  // VueHook用于创建ref状态，包括请求状态loading、响应数据data、请求错误对象error等
  statesHook: VueHook,
  // 请求适配器，推荐使用fetch请求适配器
  requestAdapter: GlobalFetch(),
  timeout: 50000,
  //   设置全局请求拦截器
  beforeRequest(method) {
    loading = ElLoading.service({
      lock: true,
      text: "Loading...",
      spinner: "el-icon-loading",
      background: "rgba(122, 122, 122, 0.8)",
    })
    // 添加token到请求头
    const { getStatus } = useUserStore()
    const accessToken = getStatus().ACCESS_TOKEN
    if (accessToken) {
      method.config.headers.Authorization = `Bearer ${accessToken}`
    }
    method.config.headers["Content-type"] = "application/json;charset=utf-8"
  },
  // 使用数组的两个项，分别指定请求成功的拦截器和请求失败的拦截器
  responded: {
    // 请求成功的拦截器
    // 当使用GlobalFetch请求适配器时，第一个参数接收Response对象
    // 第二个参数为当前请求的method实例，你可以用它同步请求前后的配置信息
    onSuccess: async (response, method) => {
      loading.close()
      let title = "请求失败"
      if (response.status !== 200) {
        switch (response.status) {
          case 405:
            title = response.statusText
            break
          case 500:
            title = "Internal Server Error"
            break
          default:
            break
        }
        ElMessage.error(title)
        logOnDev(`🚀 [API] ${method.url}  | Response ${response.status}`)
        throw new Error(response.statusText)
      }
      const json = await response.json()
      if (json.code === 401) {
        title = "身份认证失败"
        if ("/auth/authenticate" === method.url) {
          ElMessage.error(json.message)
          throw new Error(json.message)
        } else {
          const { getStatus, logout } = useUserStore()
          if (getStatus().ACCESS_TOKEN) {
            logout()
          }
          ElNotification({
            title,
            message: json.message,
            type: "error",
          })
        }
        // token失效
        return false
      } else if (json.code !== 200) {
        // 抛出错误或返回reject状态的Promise实例时，此请求将抛出错误
        ElMessage.error(json.message)
        throw new Error(json.message)
      } else {
        // 解析的响应数据将传给method实例的transformData钩子函数，这些函数将在后续讲解
        return json.data
      }
    },

    // 请求失败的拦截器
    // 请求错误时将会进入该拦截器。
    // 第二个参数为当前请求的method实例，你可以用它同步请求前后的配置信息
    onError: (err) => {
      loading.close()
      console.log("请求失败，错误信息为:" + err)
    },
  },
})
export default alovaInstance
