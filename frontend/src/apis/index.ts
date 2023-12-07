import VueHook from "alova/vue"

import { useUserStore } from "/@/store/modules/user-store"

import { i18n } from "../i18n"
import { createAlova } from "alova"
import GlobalFetch from "alova/GlobalFetch"
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
    // 添加token到请求头
    const accessToken = useUserStore().accessToken
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
      logOnDev(`🚀 [API] ${method.url}  | Response ${response.status}`)
      if (response.status === 405) {
        window.$message.error(response.statusText)
        // throw new Error(response.statusText)
      }
      if (response.status === 500) {
        window.$message.error("服务器内部错误")
        // throw new Error(response.statusText)
      }
      const json = await response.json()
      if (json.code === 401) {
        if ("/auth/authenticate" === method.url) {
          // window.$message.error(json.message)
          throw new Error(json.message)
        } else {
          window.$dialog.warning({
            title: json.message,
            maskClosable: false,
            content: i18n.t("commons.tips"),
            positiveText: "确定",
            negativeText: "不确定",
            onPositiveClick() {
              useUserStore().$reset()
              window.location.reload()
            },
          })
        }
        // token失效
        return false
      } else if (json.code !== 200) {
        // 账号/密码不正确
        // 403 无访问权限
        // 抛出错误或返回reject状态的Promise实例时，此请求将抛出错误
        window.$message.error(json.message)
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
      console.log("请求失败，错误信息为:" + err)
    },
  },
})
export default alovaInstance
