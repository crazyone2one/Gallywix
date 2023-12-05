import { ref } from "vue"

import { IStatus } from "/@/types/store/layout"

import { getLocal, setLocal } from "/@/utils/tools"

import { ILoginParam, ILoginResponse, loginFunction, logOutFunction } from "/@/apis/auth"
import { IUSerDto } from "/@/apis/interface"
import router from "/@/router"
import { defineStore } from "pinia"

const { ACCESS_TOKEN } = getLocal<IStatus>("status")

export const useUserStore = defineStore(
  "user",
  () => {
    const userInfo = ref<IUSerDto>({} as IUSerDto)
    const workspace_id = ref<string>()
    const workspace_name = ref<string>()
    const project_id = ref<string>()
    const project_name = ref<string>()
    const status = ref<IStatus>({
      isLoading: false,
      ACCESS_TOKEN: ACCESS_TOKEN || "",
    })
    const setToken = (token: string): void => {
      status.value.ACCESS_TOKEN = token
      setLocal("token", status, 1000 * 60 * 60)
    }
    /**
     * 登录信息保存 cookie
     * @param res
     */
    const saveSessionStorage = (res: ILoginResponse) => {
      const { user } = res
      userInfo.value = user
      // 校验权限
      user.userGroups?.forEach((ug) => {
        user.groupPermissions?.forEach((gp) => {
          if (gp.group.id === ug.groupId) {
            ug.userGroupPermissions = gp.userGroupPermissions
            ug.group = gp.group
          }
        })
      })
      // 检查当前项目有没有权限
      const currentProjectId = project_id.value
      if (!currentProjectId) {
        project_id.value = user.lastProjectId
      } else {
        const v = user.userGroups
          ?.filter((ug) => ug.group && ug.group.type === "PROJECT")
          .filter((ug) => ug.sourceId === currentProjectId)
        const index = user.groups?.findIndex((g) => g.id === "super_group")
        if (v?.length === 0 && index === -1) {
          project_id.value = user.lastProjectId
        }
      }
      if (!workspace_id.value) {
        workspace_id.value = user.lastWorkspaceId
      }
    }
    /**
     * 登录方法
     * @param param 登录参数
     */
    const login = (param: ILoginParam) => {
      const route = router.currentRoute
      loginFunction(param)
        .send()
        .then((res) => {
          const { access_token } = res
          setToken(access_token)
          saveSessionStorage(res)
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
      const route = router.currentRoute
      logOutFunction()
        .send()
        .then(() => {
          $reset()
          // window.location.reload()
          // (`/login?redirect=${to.path}&params=${JSON.stringify(to.query ? to.query : to.params)}`)
          router.push(
            `/login?redirect=${route.value.path}&params=${JSON.stringify(
              route.value.query ? route.value.query : route.value.params,
            )}`,
          )
        })
    }
    const $reset = (): void => {
      status.value.ACCESS_TOKEN = ""
      workspace_id.value = ""
      project_id.value = ""
      workspace_name.value = ""
      project_name.value = ""
      userInfo.value = {} as IUSerDto
    }
    const getStatus = (): IStatus => {
      return status.value
    }
    return {
      menubar,
      userInfo,
      status,
      workspace_id,
      workspace_name,
      project_id,
      project_name,
      setToken,
      logout,
      getStatus,
      login,
      $reset,
    }
  },
  { persist: true },
)
