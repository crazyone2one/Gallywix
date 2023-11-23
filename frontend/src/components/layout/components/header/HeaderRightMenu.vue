<script setup lang="ts">
import { computed, onMounted, ref } from "vue"
import { useRouter } from "vue-router"

import { useAuthStore } from "/@/store/auth-store"

import { loadProjectOption } from "/@/apis/project"
import { switchUserRole } from "/@/apis/user"
import { loadOptionList } from "/@/apis/workspace"
import { useRequest } from "alova"
import { NButton, NPopselect, NSpace, NText, SelectOption } from "naive-ui"

const router = useRouter()

const store = useAuthStore()
const organizationValue = ref("")
const workspaceValue = ref("")
const currrentOrganizationName = ref("选择组织")
const currrentWorkspaceName = ref("选择工作空间")
const organizationList = ref<Array<SelectOption>>([])
const workspaceList = ref<Array<SelectOption>>([])
const workspaceIds = ref<Array<string>>([])
const currentUser = computed(() => store.userInfo)
/**
 * 初始化项目/工作空间数据
 */
const { send: sendList } = useRequest(loadProjectOption(), { immediate: false })
const { send: sendWorkspace } = useRequest(loadOptionList(), {
  immediate: false,
})
// 利用send函数返回的promise对象
const initMenuData = async () => {
  const [workspaceResponse, projectResponse] = await Promise.all([
    sendWorkspace(),
    sendList(),
  ])
  // 并行请求完成，继续处理业务...
  workspaceList.value = workspaceResponse

  workspaceIds.value = workspaceResponse.map(
    (item) => item.id as unknown as string,
  )
  organizationList.value = projectResponse
  const org = organizationList.value.find(
    (item) => item.id === currentUser.value.lastOrganizationId,
  )
  if (org) {
    currrentOrganizationName.value = org.label as string
    organizationValue.value = org.value as string
  }
  const workspace = workspaceList.value.find(
    (item) => item.value === currentUser.value.lastWorkspaceId,
  )
  if (workspace) {
    currrentWorkspaceName.value = workspace.label as string
    workspaceValue.value = workspace.value as string
  }
}
const { send, onSuccess } = useRequest(
  (sign, sourceId) => switchUserRole(sign, sourceId),
  { immediate: false },
)
const changeOrg = (value: string) => {
  const orgId = value
  const sign = "organization"
  send(sign, orgId)
}
const changeWs = (value: string) => {
  const sign = "workspace"
  const workspaceId = value
  send(sign, workspaceId)
}
onSuccess((resp) => {
  store.userInfo = resp.data
  window.location.reload()
})
const handleCreateProject = () => {
  router.push({ path: "/project/create" })
}
onMounted(() => {
  initMenuData()
})
</script>
<template>
  <n-space class="items-center mr-2">
    <n-popselect
      v-model:value="organizationValue"
      :options="organizationList"
      scrollable
      @update:value="changeOrg">
      <n-text>【组织】{{ currrentOrganizationName }} </n-text>
      <template #action>
        <n-button @click="handleCreateProject"> 创建项目 </n-button>
      </template>
    </n-popselect>
    <n-popselect
      v-model:value="workspaceValue"
      :options="workspaceList"
      scrollable
      size="small"
      @update:value="changeWs">
      <n-text>【工作空间】{{ currrentWorkspaceName }}</n-text>
    </n-popselect>
  </n-space>
</template>

<style></style>
