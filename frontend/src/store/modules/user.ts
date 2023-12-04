import { reactive, ref } from "vue"

import { IStatus } from "/@/types/store/layout"

import { getLocal, setLocal } from "/@/utils/tools"

import { ILoginParam, loginFunction } from "/@/apis/auth"
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
      loginFunction(param)
        .send()
        .then((res) => {
          console.log(`output->res`, res)
        })
        .catch((error) => {
          console.log(`output->error`, error)
        })
    }
    const logout = (): void => {
      status.value.ACCESS_TOKEN = ""
      localStorage.removeItem("token")
      history.go(0)
    }
    const getStatus = (): IStatus => {
      return status.value
    }
    return { menubar, userInfo, status, setToken, logout, getStatus, login }
  },
  { persist: true },
)
