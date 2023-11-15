<script setup lang="ts">
import BaseCard from "/@/components/BaseCard.vue"
import { NTabs, NTabPane } from "naive-ui"
</script>
<template>
  <base-card>
    <template #header> </template>
    <template #content>
      <n-tabs type="line" animated>
        <n-tab-pane name="oasis" tab="Oasis"> Wonderwall </n-tab-pane>
        <n-tab-pane name="the beatles" tab="the Beatles"> Hey Jude </n-tab-pane>
        <n-tab-pane name="jay chou" tab="周杰伦"> 七里香 </n-tab-pane>
      </n-tabs>
    </template>
  </base-card>
</template>

<style></style>
