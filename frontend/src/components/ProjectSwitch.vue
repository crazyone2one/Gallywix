<script setup lang="ts">
import { computed, onMounted, ref } from "vue"
import { useRoute, useRouter } from "vue-router"

import { useAuthStore } from "../store/auth-store"

import { getUserProjectList } from "../apis/project"
import { updateUserData } from "../apis/user"
import { i18n } from "../i18n"
import { useRequest } from "alova"
import { NPopselect, SelectOption } from "naive-ui"

const route = useRoute()
const store = useAuthStore()
const value = ref("")
const currentProject = ref("")
const router = useRouter()
const items = ref<Array<SelectOption>>([])
const searchArray = ref<Array<SelectOption>>()
const userId = computed(() => {
  return store.userId
})
// load project list
const { send } = useRequest((val) => getUserProjectList(val), {
  immediate: false,
})
const { send: updateUser } = useRequest((val) => updateUserData(val), {
  immediate: false,
})
const init = () => {
  let data = {
    userId: store.userId,
    workspaceId: route.params.workspaceId || store.workspace_id,
  }
  items.value = []
  searchArray.value = []
  send(data).then((resp) => {
    items.value = resp
    searchArray.value = resp
    let projectId = store.project_id
    if (projectId) {
      // 路由跳转的项目ID与当前项目ID不一致时, 切换项目(API跨工作空间的场景)
      if (
        route.fullPath.startsWith("/api") &&
        route.params.projectId &&
        route.params.projectId !== projectId &&
        route.params.projectId !== "all"
      ) {
        change(route.params.projectId)
      }
      // 保存的 projectId 在当前项目列表是否存在; 切换工作空间后
      if (searchArray.value.length > 0 && searchArray.value.map((p) => p.value).indexOf(projectId) === -1) {
        change(items.value[0].value as string)
      }
    } else {
      if (items.value.length > 0) {
        change(items.value[0].value as string)
      }
    }
    changeProjectName(projectId as string)
  })
}
const change = (projectId: string | string[]) => {
  let currentProjectId = store.project_id
  if (projectId === currentProjectId) {
    return
  }
  updateUser({ id: userId.value, lastProjectId: projectId }).then((resp) => {
    store.project_id = resp.lastProjectId
    changeProjectName(projectId as string)
  })
}
const changeProjectName = (projectId: string) => {
  if (projectId) {
    const project = searchArray.value?.find((p) => p.value === projectId)
    if (project) {
      currentProject.value = project.label as string
    }
  } else {
    currentProject.value = i18n.t("project.select")
  }
}
const handleCreateProject = () => {
  router.push({ path: "/project/create" })
}
const handleAllProject = () => {
  router.push({ path: "/project/:type" })
}
onMounted(() => {
  init()
})
</script>
<template>
  <n-popselect v-model:value="value" :options="items" trigger="click">
    <n-tooltip trigger="hover">
      <template #trigger>
        <span class="project-name"> {{ $t("commons.project") }}: {{ currentProject }} </span>
      </template>
      {{ currentProject }}
    </n-tooltip>
    <template #empty>
      {{ $t("project.no_data") }}
    </template>
    <template #action>
      <n-space vertical class="ml-2">
        <n-input v-model:value="value" type="text" :placeholder="$t('project.search_by_name')" size="small" />
        <n-space>
          <n-button v-permissions="['WORKSPACE_PROJECT_MANAGER:READ+CREATE']" size="tiny" @click="handleCreateProject">
            <template #icon>
              <n-icon>
                <span class="i-tabler:plus" />
              </n-icon>
            </template>
            {{ $t("project.create") }}
          </n-button>
          <n-button v-permissions="['WORKSPACE_PROJECT_MANAGER:READ']" size="tiny" @click="handleAllProject">
            <template #icon>
              <n-icon>
                <span class="i-tabler:list" />
              </n-icon>
            </template>
            {{ $t("commons.show_all") }}
          </n-button>
        </n-space>
      </n-space>
    </template>
  </n-popselect>
</template>

<style scoped>
.project-name {
  display: inline-block;
  width: 160px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
