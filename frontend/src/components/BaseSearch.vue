<script setup lang="ts">
import { computed } from "vue"

import GaTableButton from "./table/GaTableButton.vue"

import { PageReq } from "../apis/interface"
import { i18n } from "../i18n"

interface IProps {
  condition: PageReq
  showCreate?: boolean
  showImport?: boolean
  showRun?: boolean
  createTip?: string
  importTip?: string
  runTip?: string
  popoverText?: string
  searchContent?: string
  createPermission?: Array<string>
  uploadPermission?: Array<string>
}
const props = withDefaults(defineProps<IProps>(), {
  showCreate: true,
  showImport: false,
  showRun: false,
  popoverText: "",
  createTip: i18n.t("commons.create"),
  importTip: i18n.t("commons.import"),
  runTip: i18n.t("commons.run"),
  searchContent: () => i18n.t("commons.search_by_name_or_id"),
  createPermission: () => [],
  uploadPermission: () => [],
})
const emits = defineEmits([
  "update:condition",
  "search",
  "create",
  "importData",
  "runTest",
])
const condition = computed({
  get: () => props.condition,
  set: (val) => {
    emits("update:condition", val)
  },
})
</script>

<template>
  <div class="flex justify-center">
    <div class="-mb-5">
      <ga-table-button
        v-if="showCreate"
        v-permissions="createPermission"
        :content="createTip"
        icon="i-tabler:circle-plus"
        @click="emits('create')" />
      <ga-table-button
        v-if="showImport"
        v-permissions="uploadPermission"
        :content="importTip"
        icon="i-tabler:cloud-upload"
        @click="emits('importData')" />
      <ga-table-button
        v-if="showRun"
        :content="runTip"
        icon="i-tabler:play"
        @click="emits('runTest')" />
      <slot name="button"></slot>
    </div>
    <div class="ml-auto">
      <slot name="searchBarBefore"></slot>
      <div>
        <n-input
          v-model:value="condition.name"
          type="text"
          size="small"
          :placeholder="searchContent"
          clearable
          @blur="emits('search')" />
      </div>
      <div>
        <slot name="searchBarAfter" />
      </div>

      <slot name="after" />
    </div>
  </div>
</template>

<style></style>
