<script setup lang="ts">
import HeaderMenu from "./HeaderMenu.vue"
import HeaderRightMenu from "./HeaderRightMenu.vue"
import UserInfo from "./UserInfo.vue"

import logo from "./../../../../assets/387941.svg"

import { NLayoutHeader } from "naive-ui"
</script>
<template>
  <n-layout-header
    class="header-layout"
    style="
      --side-padding: 32px;
      grid-template-columns: calc(272px - var(--side-padding)) 1fr auto;
    "
    bordered>
    <div class="ui-logo">
      <img class="ui-img" :src="logo" />
      <span>Gallywix</span>
    </div>
    <div style="display: flex; align-items: center">
      <header-menu />
    </div>
    <div class="flex items-center">
      <header-right-menu />
      <user-info />
    </div>
  </n-layout-header>
</template>

<style scoped>
.header-layout {
  height: 64px;
  padding: 24px;
  display: grid;
  align-items: center;
  grid-template-columns: calc(64px-1px);
  padding: 0 32px;
  .ui-logo {
    align-items: center;
  }
}
.ui-logo {
  cursor: pointer;
  display: flex;
  font-size: 18px;
  .ui-img {
    height: 32px;
    margin-right: 12px;
    width: 32px;
  }
}
</style>
