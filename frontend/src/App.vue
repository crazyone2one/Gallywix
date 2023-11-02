<script setup lang="ts">
import {
  darkTheme,
  useOsTheme,
  zhCN,
  dateZhCN,
  NConfigProvider,
  NGlobalStyle,
  NLoadingBarProvider,
  NDialogProvider,
  NNotificationProvider,
  NMessageProvider,
} from "naive-ui"
import TheView from "./components/TheView.vue"
import { computed } from "vue"
const osTheme = useOsTheme()
const theme = computed(() => (osTheme.value === "dark" ? darkTheme : null))
</script>

<template>
  <n-config-provider :theme="theme" :locale="zhCN" :date-locale="dateZhCN">
    <n-global-style />
    <n-loading-bar-provider>
      <n-dialog-provider>
        <n-notification-provider>
          <n-message-provider>
            <the-view />
            <slot />
          </n-message-provider>
        </n-notification-provider>
      </n-dialog-provider>
    </n-loading-bar-provider>
  </n-config-provider>
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>
