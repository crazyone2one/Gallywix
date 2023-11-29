import { ref } from "vue"

import { defineStore } from "pinia"

export const useAuthStore = defineStore(
  "auth",
  () => {
    const accessToken = ref<string>("")
    const refreshToken = ref<string>("")
    const userId = ref<string | null>()
    const workspace_id = ref<string | undefined>()
    const project_id = ref<string | undefined>()
    const roles = ref<Array<string>>([])
    const userInfo = ref()
    /**
     * 重置token信息
     */
    const restAuthStore = (): void => {
      accessToken.value = ""
      refreshToken.value = ""
      userId.value = null
      roles.value = []
      userInfo.value = {}
      workspace_id.value = undefined
      project_id.value = undefined
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
    }
  },
  { persist: true },
)
