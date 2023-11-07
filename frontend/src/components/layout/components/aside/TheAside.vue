<script setup lang="ts">
import { ref, h } from "vue"
import { NLayoutSider, MenuOption, NMenu } from "naive-ui"
import { RouterLink } from "vue-router"

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
            { to: { name: "workspace" } },
            { default: () => "工作空间" },
          ),
        key: "workspace",
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
        key: "user",
      },
    ],
  },
]
</script>
<template>
  <n-layout-sider
    content-style="padding: 12px;"
    :native-scrollbar="false"
    bordered>
    <n-menu v-model:value="activeKey" :options="menuOptions" accordion />
  </n-layout-sider>
</template>

<style></style>
