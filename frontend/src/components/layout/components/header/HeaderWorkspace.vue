<script setup lang="ts">
import { onMounted, ref, watch } from "vue"

import { useAuthStore } from "/@/store/auth-store"

import { loadOptionList, switchWorkspace } from "/@/apis/workspace/index"
import { useRequest } from "alova"
import { NPopselect, SelectOption } from "naive-ui"

const authStore = useAuthStore()
const currentWorkspaceName = ref(authStore.workspace_name)
const workspaceListOptions = ref<Array<SelectOption>>([])

const workspaceId = ref(authStore.workspace_id)
const { send } = useRequest(loadOptionList(), { immediate: false })
const { send: change } = useRequest((val) => switchWorkspace(val), { immediate: false })
const changeWs = (value: string) => {
  let _workspaceId = value
  if (!_workspaceId || authStore.workspace_id === _workspaceId) {
    return false
  }
  _changeWs(_workspaceId)
}
const _changeWs = (_workspaceId?: string) => {
  console.log(`output->value`, _workspaceId)
  if (_workspaceId) {
    change(_workspaceId).then((res) => {
      const { lastProjectId, lastWorkspaceId } = res
      authStore.project_id = lastProjectId
      authStore.workspace_id = lastWorkspaceId
      window.location.reload()
    })
  }
}
onMounted(() => {
  send().then((res) => {
    workspaceListOptions.value = res
    const workspace = res.filter((r) => r.value === authStore.workspace_id)
    if (workspace.length > 0) {
      currentWorkspaceName.value = workspace[0].label as string
      workspaceListOptions.value = res.filter((r) => r.value !== authStore.workspace_id)
      workspaceListOptions.value.unshift(workspace[0])
    } else {
      currentWorkspaceName.value = res[0].label as string
      _changeWs(res[0].value as string)
    }
  })
})
watch(
  () => currentWorkspaceName.value,
  (newValue) => {
    authStore.workspace_name = newValue
  },
)
</script>
<template>
  <div
    v-permissions="[
      'PROJECT_TRACK_CASE:READ',
      'PROJECT_TRACK_PLAN:READ',
      'PROJECT_TRACK_REVIEW:READ',
      'PROJECT_API_DEFINITION:READ',
      'PROJECT_API_SCENARIO:READ',
      'PROJECT_API_REPORT:READ',
      'PROJECT_USER:READ',
      'PROJECT_ENVIRONMENT:READ',
      'PROJECT_FILE:READ+JAR',
      'PROJECT_FILE:READ+FILE',
      'PROJECT_OPERATING_LOG:READ',
      'PROJECT_CUSTOM_CODE:READ',
      'PROJECT_PERFORMANCE_TEST:READ',
      'PROJECT_PERFORMANCE_REPORT:READ',
      'WORKSPACE_USER:READ',
    ]">
    <n-popselect
      v-model:value="workspaceId"
      :options="workspaceListOptions"
      scrollable
      size="small"
      @update:value="changeWs">
      <span>【工作空间】{{ currentWorkspaceName }}</span>
    </n-popselect>
  </div>
</template>
<style scoped></style>
