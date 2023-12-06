<script setup lang="ts">
import { computed } from "vue"

import { PageReq } from "../apis/interface"
import { i18n } from "../i18n"
import { ElIcon, ElInput } from "element-plus"

interface IProps {
  condition: PageReq
  tip?: string
}
const props = withDefaults(defineProps<IProps>(), {
  tip: () => i18n.t("commons.search_by_name"),
})
const emits = defineEmits(["update:condition", "search"])
const condition = computed({
  get: () => props.condition,
  set: (val) => {
    emits("update:condition", val)
  },
})
const search = (): void => emits("search")
</script>

<template>
  <el-input
    v-model="condition.name"
    class="search"
    type="text"
    size="small"
    :placeholder="tip"
    maxlength="60"
    clearable
    @change="search">
    <template #prefix>
      <el-icon class="el-input__icon"><span class="i-tabler:search" /></el-icon>
    </template>
  </el-input>
</template>

<style></style>
