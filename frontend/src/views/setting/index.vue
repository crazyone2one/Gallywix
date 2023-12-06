<script setup lang="ts">
import { computed } from "vue"

import SettingMenu from "./components/SettingMenu.vue"
import GaAsideContainer from "/@/components/GaAsideContainer.vue"
import GaContainer from "/@/components/GaContainer.vue"
import GaMainContainer from "/@/components/GaMainContainer.vue"

import { GROUP_SYSTEM } from "/@/utils/constants"
import { hasPermissions } from "/@/utils/permission"
import { getCurrentUser } from "/@/utils/token"

const isSystemGroup = computed(() => {
  const user = getCurrentUser()
  if (user && user.groups) {
    let group = user.groups.filter((gp) => gp && gp.type === GROUP_SYSTEM)
    return group.length > 0
  }
  return false
})
const enableAsideHidden = () => {
  let systemPermission = [
    "SYSTEM_USER:READ",
    "SYSTEM_ORGANIZATION:READ",
    "SYSTEM_GROUP:READ",
    "SYSTEM_WORKSPACE:READ",
    "SYSTEM_TEST_POOL:READ",
    "SYSTEM_SETTING:READ",
    "SYSTEM_QUOTA:READ",
    "SYSTEM_AUTH:READ",
  ]
  let workspacePermission = [
    "WORKSPACE_USER:READ",
    "WORKSPACE_SERVICE:READ",
    "WORKSPACE_PROJECT_MANAGER:READ",
    "WORKSPACE_PROJECT_ENVIRONMENT:READ",
    "WORKSPACE_OPERATING_LOG:READ",
  ]
  return hasPermissions(...systemPermission) || hasPermissions(...workspacePermission)
}
</script>
<template>
  <ga-container :is-show-warning="isSystemGroup">
    <ga-aside-container
      page-key="SETTING"
      :enable-aside-hidden="enableAsideHidden()"
      :width="enableAsideHidden() ? '300px' : '0px'">
      <setting-menu />
    </ga-aside-container>
    <ga-main-container>
      <router-view v-slot="{ Component }">
        <keep-alive>
          <component :is="Component" />
        </keep-alive>
      </router-view>
    </ga-main-container>
  </ga-container>
</template>

<style></style>
