<script setup lang="ts">
import Setting from "/@/router/modules/setting"

import { ElIcon, ElMenu, ElMenuItem, ElSubMenu } from "element-plus"

interface IMenu {
  title: string
  index: string
  permissions: Array<string>
  roles: Array<string>
}
const systemPermission: Array<string> = [
  "SYSTEM_USER:READ",
  "SYSTEM_ORGANIZATION:READ",
  "SYSTEM_GROUP:READ",
  "SYSTEM_WORKSPACE:READ",
  "SYSTEM_TEST_POOL:READ",
  "SYSTEM_SETTING:READ",
  "SYSTEM_QUOTA:READ",
  "SYSTEM_AUTH:READ",
]
const workspacePermission: Array<string> = [
  "WORKSPACE_USER:READ",
  "WORKSPACE_SERVICE:READ",
  "WORKSPACE_PROJECT_MANAGER:READ",
  "WORKSPACE_PROJECT_ENVIRONMENT:READ",
  "WORKSPACE_OPERATING_LOG:READ",
]
const getMenus = (group: string) => {
  let menus: Array<IMenu> = []
  if (Setting.children) {
    for (let child of Setting.children) {
      if (child.meta![group] === true) {
        let menu: IMenu = { index: Setting.path + "/" + child.path } as IMenu
        menu.title = child.meta?.title as string
        menu.roles = child.meta?.roles as Array<string>
        menu.permissions = child.meta?.permissions as Array<string>
        // menu.valid = child.meta.valid
        menus.push(menu)
      }
    }
  }
  return menus
}
const systems = getMenus("system")
const workspaces = getMenus("workspace")
</script>
<template>
  <el-menu default-active="2" class="el-menu-vertical-demo" unique-opened router>
    <el-sub-menu v-permission="systemPermission" index="1">
      <template #title>
        <el-icon><span class="i-tabler:address-book" /></el-icon>
        <span>{{ $t("commons.system") }}</span>
      </template>
      <el-menu-item
        v-for="menu in systems"
        :key="menu.index"
        v-permission="menu.permissions"
        :index="menu.index"
        class="setting-item">
        {{ $t(menu.title) }}
      </el-menu-item>
    </el-sub-menu>
    <el-sub-menu v-permission="workspacePermission" index="2">
      <template #title>
        <el-icon><span class="i-tabler:align-box-center-stretch" /></el-icon>
        <span> {{ $t("commons.workspace") }}</span>
      </template>
      <el-menu-item
        v-for="menu in workspaces"
        :key="menu.index"
        v-permission="menu.permissions"
        :index="menu.index"
        class="setting-item">
        {{ $t(menu.title) }}
      </el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>

<style scoped></style>
