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
      <el-menu-item
        v-permission="[
          'PROJECT_TRACK_HOME:READ',
          'PROJECT_TRACK_CASE:READ',
          'PROJECT_TRACK_PLAN:READ',
          'PROJECT_TRACK_REVIEW:READ',
          'PROJECT_TRACK_ISSUE:READ',
          'PROJECT_TRACK_REPORT:READ',
        ]"
        index="/track"
        disabled>
        <el-icon><span class="i-tabler:checkup-list" /></el-icon>
        <span>{{ $t("test_track.test_track") }}</span>
      </el-menu-item>
      <el-menu-item
        v-permission="[
          'PROJECT_API_HOME:READ',
          'PROJECT_API_DEFINITION:READ',
          'PROJECT_API_SCENARIO:READ',
          'PROJECT_API_REPORT:READ',
        ]"
        index="/api"
        disabled>
        <el-icon><span class="i-my-local-api" /></el-icon>
        <span>{{ $t("commons.api") }}</span>
      </el-menu-item>
      <el-menu-item
        v-permission="['PROJECT_UI_SCENARIO:READ', 'PROJECT_UI_REPORT:READ', 'PROJECT_UI_ELEMENT:READ']"
        index="4"
        disabled>
        <el-icon><span class="i-my-local-settings" /></el-icon>
        <span>{{ $t("commons.ui") }}</span>
      </el-menu-item>
      <el-menu-item
        v-permission="[
          'PROJECT_PERFORMANCE_HOME:READ',
          'PROJECT_PERFORMANCE_TEST:READ',
          'PROJECT_PERFORMANCE_REPORT:READ',
        ]"
        index="/performance"
        disabled>
        <el-icon><span class="i-my-local-performance" /></el-icon>
        <span>{{ $t("commons.performance") }}</span>
      </el-menu-item>
      <el-menu-item
        v-permission="[
          'PROJECT_REPORT_ANALYSIS:READ',
          'PROJECT_ENTERPRISE_REPORT:READ+EXPORT',
          'PROJECT_ENTERPRISE_REPORT:READ+CREATE',
          'PROJECT_ENTERPRISE_REPORT:READ+DELETE',
          'PROJECT_ENTERPRISE_REPORT:READ+COPY',
          'PROJECT_ENTERPRISE_REPORT:READ+SCHEDULE',
          'PROJECT_ENTERPRISE_REPORT:READ+EDIT',
        ]"
        index="/report"
        disabled>
        <el-icon><span class="i-tabler:report" /></el-icon>
        <span>{{ $t("commons.report_statistics.title") }}</span>
      </el-menu-item>
      <el-menu-item
        v-permission="[
          'PROJECT_USER:READ',
          'PROJECT_MANAGER:READ',
          'PROJECT_GROUP:READ',
          'PROJECT_FILE:READ',
          'PROJECT_VERSION:READ',
          'PROJECT_APP_MANAGER:READ+EDIT',
          'PROJECT_ENVIRONMENT:READ',
          'PROJECT_OPERATING_LOG:READ',
          'PROJECT_FILE:READ+JAR',
          'PROJECT_FILE:READ+FILE',
          'PROJECT_CUSTOM_CODE:READ',
          'PROJECT_ERROR_REPORT_LIBRARY:READ',
          'PROJECT_TEMPLATE:READ',
          'PROJECT_MESSAGE:READ',
        ]"
        index="/project"
        disabled>
        <el-icon><span class="i-my-local-project" /></el-icon>
        <span>{{ $t("commons.project_setting") }}</span>
      </el-menu-item>
      <el-menu-item
        v-permission="[
          'SYSTEM_USER:READ',
          'SYSTEM_WORKSPACE:READ',
          'SYSTEM_GROUP:READ',
          'SYSTEM_TEST_POOL:READ',
          'SYSTEM_SETTING:READ',
          'SYSTEM_AUTH:READ',
          'SYSTEM_QUOTA:READ',
          'SYSTEM_OPERATING_LOG:READ',
          'WORKSPACE_SERVICE:READ',
          'WORKSPACE_USER:READ',
          'WORKSPACE_PROJECT_MANAGER:READ',
          'WORKSPACE_PROJECT_ENVIRONMENT:READ',
          'WORKSPACE_OPERATING_LOG:READ',
        ]"
        index="/setting"
        disabled>
        <el-icon><span class="i-my-local-settings" /></el-icon>
        <span>{{ $t("commons.system_setting") }}</span>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<style></style>
