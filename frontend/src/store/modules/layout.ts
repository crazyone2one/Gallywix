import { reactive } from "vue"

import { IMenubarStatus, ISetting, IStatus } from "/@/types/store/layout"

import { getLocal } from "/@/utils/tools"

import { defineStore } from "pinia"
const settings = getLocal<ISetting>("setting")
const { ACCESS_TOKEN } = getLocal<IStatus>("token")

export const useLayoutStore = defineStore(
  "layout",
  () => {
    const menubar = reactive({
      status: document.body.offsetWidth < 768 ? IMenubarStatus.PHN : IMenubarStatus.PCE,
      menuList: [],
      isPhone: document.body.offsetWidth < 768,
    })

    const setting = reactive({
      theme: settings.theme !== undefined ? settings.theme : 0,
      showTags: settings.showTags !== undefined ? settings.showTags : true,
      color: {
        primary: settings.color !== undefined ? settings.color.primary : "#409eff",
      },
      usePinyinSearch: settings.usePinyinSearch !== undefined ? settings.usePinyinSearch : false,
      mode: settings.mode || "vertical",
    })
    const status = reactive({
      isLoading: false,
      ACCESS_TOKEN: ACCESS_TOKEN || "",
    })
    const changeCollapsed = (): void => {
      menubar.status = menubar.isPhone
        ? menubar.status === IMenubarStatus.PHN
          ? IMenubarStatus.PHE
          : IMenubarStatus.PHN
        : menubar.status === IMenubarStatus.PCN
        ? IMenubarStatus.PCE
        : IMenubarStatus.PCN
    }
    const changeDeviceWidth = (): void => {
      menubar.isPhone = document.body.offsetWidth < 768
      menubar.status = menubar.isPhone ? IMenubarStatus.PHN : IMenubarStatus.PCE
    }
    return { menubar, setting, status, changeCollapsed, changeDeviceWidth }
  },
  { persist: true },
)
