import { defineStore } from "pinia"
import { ref } from "vue"

export const useAuthStore = defineStore(
  "auth",
  () => {
    const accessToken = ref<string>("")
    const refreshToken = ref<string>("")
    const userId = ref<number | null>()
    const roles = ref<Array<string>>()

    /**
     * 重置token信息
     */
    const restAuthStore = (): void => {
      accessToken.value = ""
      refreshToken.value = ""
      userId.value = null
      roles.value = []
    }

    return { accessToken, refreshToken, userId, roles, restAuthStore }
  },
  { persist: true },
)
