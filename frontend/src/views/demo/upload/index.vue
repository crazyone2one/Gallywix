<script setup lang="ts">
import { ref } from "vue"

import GaUpload from "/@/components/GaUpload.vue"

import { list2SelectOption } from "/@/utils/list-2-select"

import { SelectOption } from "naive-ui"
import { NButton } from "naive-ui"

const result = ref<SelectOption[]>([])
const gaUpload = ref<InstanceType<typeof GaUpload> | null>(null)
const openUpload = () => gaUpload.value?.open()
const debounceClick = () => window.$message.info("debounceClick")

const test1 = () => {
  const _data = [
    { id: "1", name: "test" },
    { id: "2", name: "test2" },
  ]
  result.value = list2SelectOption(_data)
}
</script>
<template>
  upload

  <n-button @click="openUpload"> 上传 </n-button>
  <n-button>无v-permission</n-button>
  <n-button v-permissions="['ADMIN']" @click="test1"> 有v-permission </n-button>
  <n-button v-permissions="['AD1MIN']"> 错误v-permission </n-button>
  <n-button v-debounce="debounceClick"> 防抖测试 </n-button>
  <ga-upload ref="gaUpload" />
</template>

<style></style>
