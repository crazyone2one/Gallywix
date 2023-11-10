<script setup lang="ts">
import { ref } from "vue"
import { NButton } from "naive-ui"
import GaUpload from "/@/components/GaUpload.vue"

const gaUpload = ref<InstanceType<typeof GaUpload> | null>(null)
const openUpload = () => gaUpload.value?.open()
</script>
<template>
  upload
  <h1>{{ $t("home.home") }}</h1>
  <n-button @click="openUpload">上传</n-button>
  <ga-upload ref="gaUpload" />
</template>

<style></style>
