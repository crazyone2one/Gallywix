<script setup lang="ts">
import { computed } from "vue"

import GaSearch from "../GaSearch.vue"
import GaTableButton from "../GaTableButton.vue"

import { PageReq } from "/@/apis/interface"
import { i18n } from "/@/i18n"
import { ElRow } from "element-plus"
interface IProps {
  title?: string
  showCreate?: boolean
  showImport?: boolean
  showRun?: boolean
  createTip?: string
  importTip?: string
  createPermission?: Array<string>
  uploadPermission?: Array<string>
  tip?: string
  condition: PageReq
  haveSearch?: boolean
}
const props = withDefaults(defineProps<IProps>(), {
  title: "",
  showCreate: true,
  showImport: false,
  showRun: false,
  createTip: () => i18n.t("commons.create"),
  importTip: () => i18n.t("commons.import"),
  createPermission: () => [],
  uploadPermission: () => [],
  tip: () => i18n.t("commons.search_by_name"),
  haveSearch: true,
})
const emits = defineEmits(["update:condition", "search", "create", "importData", "runTest"])
const condition = computed({
  get: () => props.condition,
  set: (val) => {
    emits("update:condition", val)
  },
})
</script>
<template>
  <div>
    <el-row v-if="title" type="flex" class="table-title" justify="space-between" align="middle">
      <slot name="title">
        {{ title }}
      </slot>
    </el-row>
    <el-row type="flex" justify="space-between" align="middle">
      <span class="operate-button">
        <ga-table-button
          v-if="showCreate"
          v-permission="createPermission"
          icon="i-tabler:circle-plus"
          :content="createTip"
          @click="emits('create')" />
        <ga-table-button
          v-if="showImport"
          v-permission="uploadPermission"
          icon="el-icon-circle-plus-outline"
          :content="importTip"
          @click="emits('importData')" />
      </span>
      <span>
        <slot name="searchBarBefore"></slot>
        <ga-search
          :condition="condition"
          :base-search-tip="tip"
          :show-base-search="haveSearch"
          @search="emits('search')" />
      </span>
    </el-row>
  </div>
</template>

<style scoped>
.operate-button {
  margin-bottom: -5px;
}
.table-title {
  height: 40px;
  font-weight: bold;
  font-size: 18px;
}
</style>
