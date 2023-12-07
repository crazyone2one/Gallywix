import VueHook from "alova/vue"

import { useUserStore } from "../store/modules/user-store"

import { xhrRequestAdapter } from "@alova/adapter-xhr"
import { createAlova } from "alova"
const logOnDev = (message: string) => {
  if (import.meta.env.MODE === "development") {
    console.log(message)
  }
}
// 1. 创建alova实例
const alovaInst = createAlova({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  // VueHook用于创建ref状态，包括请求状态loading、响应数据data、请求错误对象error等
  statesHook: VueHook,
  // 请求适配器，推荐使用fetch请求适配器
  requestAdapter: xhrRequestAdapter(),
  timeout: 50000,
  //   设置全局请求拦截器
  beforeRequest(method) {
    // 添加token到请求头
    const accessToken = useUserStore().accessToken
    if (accessToken) {
      method.config.headers.Authorization = `Bearer ${accessToken}`
    }
  },
  // 使用数组的两个项，分别指定请求成功的拦截器和请求失败的拦截器
  responded: {
    // 请求成功的拦截器
    // 当使用GlobalFetch请求适配器时，第一个参数接收Response对象
    // 第二个参数为当前请求的method实例，你可以用它同步请求前后的配置信息
    onSuccess: async (response, method) => {
      logOnDev(`🚀 [API] ${method.url}  | Response ${response.status}`)
      return response.data
    },

    // 请求失败的拦截器
    // 请求错误时将会进入该拦截器。
    // 第二个参数为当前请求的method实例，你可以用它同步请求前后的配置信息
    onError: (err) => {
      console.log("请求失败，错误信息为:" + err)
    },
  },
})
/**
 * 通用上传方法
 * @param file 上传的文件
 * @param url upload接口地址
 */
export const uploadFile = (file: File, url: string) => {
  const formData = new FormData()
  formData.append("file", file as File)
  return alovaInst.Post(`${url}`, formData, {
    // 开启上传进度
    enableUpload: true,
  })
}
export const downloadFile = (url: string) =>
  alovaInst.Get(`${url}`, {
    // 开启下载进度
    enableDownload: true,
    responseType: "blob",
  })
export default alovaInst
