<script setup lang="ts">
import { h, ref } from "vue"
import { RouterLink } from "vue-router"

import i18n from "/@/i18n"
import { MenuOption, NMenu } from "naive-ui"

const activeKey = ref<string | null>(null)
const menuOptions: MenuOption[] = [
  {
    label: "系统配置",
    key: "pinball-1973",
    children: [
      {
        label: () =>
          h(
            RouterLink,
            { to: { name: "user" } },
            { default: () => "用户管理" },
          ),
        key: "user",
      },
      {
        label: () =>
          h(
            RouterLink,
            { to: { name: "group" } },
            { default: () => i18n.global.t("group.group_permission") },
          ),
        key: "user",
      },
      {
        label: () =>
          h(
            RouterLink,
            { to: { name: "workspace" } },
            { default: () => "工作空间" },
          ),
        key: "workspace",
      },
      {
        label: () =>
          h(
            RouterLink,
            { to: { name: "organization" } },
            { default: () => "organization" },
          ),
        key: "organization",
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
  {
    label: () =>
      h(
        RouterLink,
        { to: { path: "/project/all" } },
        { default: () => "项目" },
      ),
    key: "project",
  },
  {
    label: () =>
      h(
        RouterLink,
        { to: { path: "/createTest" } },
        { default: () => "创建测试" },
      ),
    key: "createTest",
  },
]
</script>
<template>
  <n-menu v-model:value="activeKey" mode="horizontal" :options="menuOptions" />
</template>

<style></style>
