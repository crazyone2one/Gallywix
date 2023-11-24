<script setup lang="ts">
import { h, ref } from "vue"
import { RouterLink } from "vue-router"

import ProjectSwitch from "/@/components/ProjectSwitch.vue"

import { i18n } from "/@/i18n"
import { NGi, NGrid } from "naive-ui"
import { MenuOption, NMenu } from "naive-ui"
const activeKey = ref<string | null>(null)
const menuOptions: MenuOption[] = [
  {
    label: () =>
      h(
        RouterLink,
        { to: { path: "/project/home" } },
        { default: () => i18n.t("project.info") },
      ),

    key: "project.info",
  },
  {
    label: i18n.t("project.member"),
    key: "project.member",
    disabled: true,
  },
  {
    label: i18n.t("project.group_permission"),
    key: "project.group_permission",
    disabled: true,
  },
  {
    label: i18n.t("project.env"),
    key: "project.env",
    disabled: true,
  },
  {
    label: i18n.t("project.file_manage"),
    key: "project.file_manage",
    disabled: true,
  },
  {
    label: i18n.t("error_report_library.name"),
    key: "error_report_library.name",
    disabled: true,
  },
  {
    label: i18n.t("workspace.template_manage"),
    key: "workspace.template_manage",
    disabled: true,
  },
  {
    label: i18n.t("organization.message_settings"),
    key: "organization.message_settings",
    disabled: true,
  },
  {
    label: i18n.t("project.log"),
    key: "project.log",
    disabled: true,
  },
  {
    label: i18n.t("project.version_manage"),
    key: "project.version_manage",
    disabled: true,
  },
  {
    label: i18n.t("project.app_manage"),
    key: "project.app_manage",
    disabled: true,
  },
  {
    label: i18n.t("project.code_segment.code_segment"),
    key: "project.code_segment.code_segment",
    disabled: true,
  },
]
</script>
<template>
  <n-grid :cols="1" :collapsed-rows="2">
    <n-gi class="-mt-4">
      <n-space class="light-green">
        <project-switch />
        <n-menu
          v-model:value="activeKey"
          mode="horizontal"
          :options="menuOptions" />
      </n-space>
    </n-gi>
    <n-gi>
      <router-view />
    </n-gi>
  </n-grid>
</template>

<style scoped>
.light-green {
  display: grid;
  align-items: center;
  /* height: 108px; */
  border-bottom: 1px solid #e6e6e6;
  background-color: #fff;
  /* background-color: rgba(0, 128, 0, 0.12); */
}
</style>
