<script setup lang="ts">
import { useLayoutStore } from "/@/store/modules/layout"

import { ElAside, ElIcon, ElMenu, ElMenuItem } from "element-plus"

const { sidebar } = useLayoutStore()
</script>
<template>
  <el-aside width="200px">
    <el-menu default-active="2" class="el-menu-vertical-demo" unique-opened :collapse="sidebar.collapse">
      <el-menu-item index="/workstation" disabled>
        <el-icon><span class="i-my-local-workspace" /></el-icon>
        <span>{{ $t("commons.my_workstation") }}</span>
      </el-menu-item>
      <el-menu-item index="/track" disabled>
        <el-icon><span class="i-tabler:checkup-list" /></el-icon>
        <span>{{ $t("test_track.test_track") }}</span>
      </el-menu-item>
      <el-menu-item index="/api" disabled>
        <el-icon><span class="i-my-local-api" /></el-icon>
        <span>{{ $t("commons.api") }}</span>
      </el-menu-item>
      <el-menu-item index="4" disabled>
        <el-icon><span class="i-my-local-settings" /></el-icon>
        <span>{{ $t("commons.ui") }}</span>
      </el-menu-item>
      <el-menu-item index="/performance" disabled>
        <el-icon><span class="i-my-local-performance" /></el-icon>
        <span>{{ $t("commons.performance") }}</span>
      </el-menu-item>
      <el-menu-item index="/report" disabled>
        <el-icon><span class="i-tabler:report" /></el-icon>
        <span>{{ $t("commons.report_statistics.title") }}</span>
      </el-menu-item>
      <el-menu-item index="/project" disabled>
        <el-icon><span class="i-my-local-project" /></el-icon>
        <span>{{ $t("commons.project_setting") }}</span>
      </el-menu-item>
      <el-menu-item index="/setting" disabled>
        <el-icon><span class="i-my-local-settings" /></el-icon>
        <span>{{ $t("commons.system_setting") }}</span>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<style></style>
