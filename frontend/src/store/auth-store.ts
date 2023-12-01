import { ref } from "vue"

import { USER } from "../apis/user"
import { defineStore } from "pinia"

export const useAuthStore = defineStore(
  "auth",
  () => {
    const accessToken = ref<string>("")
    const refreshToken = ref<string>("")
    const userId = ref<string | null>()
    const workspace_id = ref<string | undefined>()
    const workspace_name = ref<string>()
    const project_id = ref<string | undefined>()
    const roles = ref<Array<string>>([])
    const userInfo = ref<USER>({} as USER)
    /**
     * 重置token信息
     */
    const restAuthStore = (): void => {
      accessToken.value = ""
      refreshToken.value = ""
      userId.value = null
      roles.value = []
      userInfo.value = {} as USER
      workspace_id.value = undefined
      project_id.value = undefined
      workspace_name.value = ""
    }
    const saveSessionStorage = () => {
      const currentProjectId = project_id.value
      if (!currentProjectId) {
        project_id.value = userInfo.value.lastProjectId
      }
      if (!workspace_id.value) {
        workspace_id.value = userInfo.value.lastWorkspaceId
      }
    }
    return {
      accessToken,
      refreshToken,
      userId,
      roles,
      restAuthStore,
      userInfo,
      saveSessionStorage,
      project_id,
      workspace_id,
      workspace_name,
    }
  },
  { persist: true },
)
