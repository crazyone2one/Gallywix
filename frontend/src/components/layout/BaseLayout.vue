<script setup lang="ts">
import { NLayout, NLayoutFooter, NButton } from "naive-ui"
import { useClientHeight } from "/@/composables/client"
import TheHeader from "/@/components/layout/components/header/TheHeader.vue"
// import TheAside from "/@/components/layout/components/aside/TheAside.vue"
const { height } = useClientHeight()
const handleTest = () => {
  window.$message.info("test")
}
</script>

<template>
  <n-layout :style="{ height: height + 'px' }">
    <the-header />
    <n-layout position="absolute" style="top: 64px; bottom: 64px" has-sider>
      <!-- <the-aside /> -->
      <n-layout content-style="padding: 24px;" :native-scrollbar="false">
        <router-view />
      </n-layout>
    </n-layout>
    <n-layout-footer position="absolute" style="height: 64px; padding: 24px" bordered>
      <n-button size="small" @click="handleTest">城府路</n-button>
    </n-layout-footer>
  </n-layout>
</template>

<style scoped></style>
