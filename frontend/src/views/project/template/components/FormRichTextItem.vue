<script setup lang="ts">
import GaMarkDown from "/@/components/GaMarkDown.vue"

import { ITestCaseTemplate } from "/@/apis/template/test-case-template"
interface IPorps {
  label: string
  prop: string
  data: ITestCaseTemplate
}
defineProps<IPorps>()
</script>
<template>
  <n-form-item :label="label" :path="prop">
    <ga-mark-down :prop="prop" :data="data" />
  </n-form-item>
</template>
<style scoped></style>
