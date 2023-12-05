import { reactive, ref } from "vue"

import { IStatus } from "/@/types/store/layout"

import { getLocal, setLocal } from "/@/utils/tools"

import { ILoginParam, loginFunction } from "/@/apis/auth"
import router from "/@/router"
import { defineStore } from "pinia"

const { ACCESS_TOKEN } = getLocal<IStatus>("token")

export const useUserStore = defineStore(
  "user",
  () => {
    const userInfo = reactive({
      name: "",
      role: [],
    })

    const status = ref<IStatus>({
      isLoading: false,
      ACCESS_TOKEN: ACCESS_TOKEN || "",
    })
    const setToken = (token: string): void => {
      status.value.ACCESS_TOKEN = token
      setLocal("token", status, 1000 * 60 * 60)
    }
    const login = (param: ILoginParam) => {
      const route = router.currentRoute
      loginFunction(param)
        .send()
        .then((res) => {
          const { access_token } = res
          setToken(access_token)
          // 路由跳转
          if (route.value.query?.redirect) {
            router.push({
              path: <string>route.value.query?.redirect,
              query: route.value.query?.params
                ? Object.keys(<string>route.value.query?.params).length > 0
                  ? JSON.parse(<string>route.value.query?.params)
                  : ""
                : "",
            })
          } else {
            router.push("/")
          }
        })
        .catch((error) => {
          console.log(`output->error`, error)
        })
    }
    const logout = (): void => {
      status.value.ACCESS_TOKEN = ""
      localStorage.removeItem("token")
      //   history.go(0)
    }
    const getStatus = (): IStatus => {
      return status.value
    }
    return { menubar, userInfo, status, setToken, logout, getStatus, login }
  },
  { persist: true },
)
