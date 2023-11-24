<script setup lang="ts">
import { h, ref, watchEffect } from "vue"
import { RouterLink, useRoute } from "vue-router"

import { i18n } from "/@/i18n"
import { MenuOption, NIcon, NMenu } from "naive-ui"

const activeKey = ref<string | null>(null)
const expandedKeys = ref<string[]>([])
const route = useRoute()
// 菜单数据
const menuOptions: MenuOption[] = [
  {
    label: () =>
      h(
        RouterLink,
        { to: { path: "/workstation" } },
        { default: () => i18n.t("commons.my_workstation") },
      ),
    icon: () =>
      h(NIcon, null, {
        default: () => h("span", { class: "i-my-local-workspace" }),
      }),
    key: "my_workstation",
  },
  {
    label: i18n.t("test_track.test_track"),
    key: "test_track",
    disabled: true,
    icon: () =>
      h(NIcon, null, {
        default: () => h("span", { class: "i-tabler:clipboard-list" }),
      }),
  },
  {
    label: i18n.t("commons.api"),
    key: "api",
    disabled: true,
    icon: () =>
      h(NIcon, null, {
        default: () => h("span", { class: "i-my-local-api" }),
      }),
  },
  {
    label: i18n.t("commons.performance"),
    key: "performance",
    disabled: true,
    icon: () =>
      h(NIcon, null, {
        default: () => h("span", { class: "i-my-local-performance" }),
      }),
  },
  {
    label: i18n.t("commons.report_statistics.title"),
    key: "report-statistics",
    disabled: true,
    icon: () =>
      h(NIcon, null, {
        default: () => h("span", { class: "i-tabler:report" }),
      }),
  },
  {
    // label: i18n.t("commons.project_setting"),
    label: () =>
      h(
        RouterLink,
        { to: { path: "/project" } },
        { default: () => i18n.t("commons.project_setting") },
      ),
    key: "project-setting",
    icon: () =>
      h(NIcon, null, {
        default: () => h("span", { class: "i-tabler:folder-cog" }),
      }),
  },
  {
    label: i18n.t("commons.system_setting"),
    key: "pinball-1973",
    icon: () =>
      h(NIcon, null, {
        default: () => h("span", { class: "i-my-local-settings" }),
      }),
    children: [
      {
        type: "group",
        label: i18n.t("commons.system"),
        key: "people",
        children: [
          {
            label: () =>
              h(
                RouterLink,
                { to: { name: "user" } },
                { default: () => i18n.t("commons.user") },
              ),
            key: "user",
          },
          {
            label: () =>
              h(
                RouterLink,
                { to: { name: "workspace" } },
                { default: () => i18n.t("commons.workspace") },
              ),
            key: "workspace",
          },
          {
            label: () =>
              h(
                RouterLink,
                { to: { name: "usergroup" } },
                { default: () => i18n.t("group.group_permission") },
              ),
            key: "group",
          },
        ],
      },
      {
        type: "group",
        label: i18n.t("commons.workspace"),
        key: "people",
        children: [
          {
            label: () =>
              h(
                RouterLink,
                { to: { path: "/project/all" } },
                { default: () => i18n.t("project.manager") },
              ),
            key: "project",
          },
        ],
      },
    ],
  },
  {
    label: "demo",
    key: "demo",
    children: [
      {
        label: () =>
          h(
            RouterLink,
            { to: { name: "upload" } },
            { default: () => "upload" },
          ),
        key: "demo1",
      },
    ],
  },

  // {
  //   label: () =>
  //     h(
  //       RouterLink,
  //       { to: { path: "/createTest" } },
  //       { default: () => "创建测试" },
  //     ),
  //   key: "createTest",
  // },
]
const routeMatched = (menu: MenuOption): boolean => {
  return (
    route.name === menu.key &&
    (menu.params == null ||
      JSON.stringify(route.params) === JSON.stringify(menu.params))
  )
}
const matchExpanded = (items: MenuOption[]): boolean => {
  let matched = false
  for (const item of items) {
    if (item.children != null) {
      matchExpanded(item.children) &&
        expandedKeys.value.push(item.key as string)
    }
    if (routeMatched(item)) {
      activeKey.value = item.key as string
      matched = true
    }
  }
  return matched
}
watchEffect(() => menuOptions.length > 0 && matchExpanded(menuOptions))
</script>
<template>
  <n-menu v-model:value="activeKey" mode="horizontal" :options="menuOptions" />
</template>

<style></style>
