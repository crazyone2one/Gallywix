<script setup lang="ts">
import { ref } from "vue"

import GaUpload from "/@/components/GaUpload.vue"

import { NButton } from "naive-ui"

const gaUpload = ref<InstanceType<typeof GaUpload> | null>(null)
const openUpload = () => gaUpload.value?.open()
const debounceClick = () => window.$message.info("debounceClick")
</script>
<template>
  upload
  <h1>{{ $t("home.home") }}</h1>
  <n-button @click="openUpload"> 上传 </n-button>
  <n-button>无v-permission</n-button>
  <n-button v-permissions="['ADMIN']"> 有v-permission </n-button>
  <n-button v-permissions="['AD1MIN']"> 错误v-permission </n-button>
  <n-button v-debounce="debounceClick"> 防抖测试 </n-button>
  <ga-upload ref="gaUpload" />
</template>

<style></style>
