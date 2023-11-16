<script setup lang="ts">
import TestPlanPressureConfig from "./components/PressureConfig.vue"
import BaseCard from "/@/components/BaseCard.vue"

import { NTabPane, NTabs } from "naive-ui"
</script>
<template>
  <base-card>
    <template #header />
    <template #content>
      <n-tabs type="line" animated>
        <n-tab-pane name="oasis" tab="基础配置"> Wonderwall </n-tab-pane>
        <n-tab-pane name="the beatles" tab="压力配置">
          <test-plan-pressure-config />
        </n-tab-pane>
        <n-tab-pane name="jay chou" tab="高级配置"> 七里香 </n-tab-pane>
      </n-tabs>
    </template>
  </base-card>
</template>

<style></style>
