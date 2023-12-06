<script setup lang="ts">
import { computed } from "vue"

import GaTableSearchBar from "./GaTableSearchBar.vue"

import { PageReq } from "../apis/interface"
import { i18n } from "../i18n"

interface IProps {
  condition: PageReq
  baseSearchWidth?: number
  showBaseSearch?: boolean
  baseSearchTip?: string
}
const props = withDefaults(defineProps<IProps>(), {
  showBaseSearch: true,
  baseSearchWidth: 240,
  baseSearchTip: () => i18n.t("commons.search_by_name_or_id"),
})
const emits = defineEmits(["update:condition", "search"])
const condition = computed({
  get: () => props.condition,
  set: (val) => {
    emits("update:condition", val)
  },
})
</script>
<template>
  <div style="float: right">
    <ga-table-search-bar
      v-if="showBaseSearch"
      :condition="condition"
      :style="{ width: baseSearchWidth + 'px' }"
      :tip="baseSearchTip"
      @search="emits('search')" />
  </div>
</template>

<style scoped></style>
