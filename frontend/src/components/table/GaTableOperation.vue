<script setup lang="ts">
import GaTableOperatorButton from "./GaTableOperatorButton.vue"

import { i18n } from "/@/i18n"
import { NSpace } from "naive-ui"
interface PROPS {
  showEdit?: boolean
  showDelete?: boolean
  editTip?: string
  deleteTip?: string
  type?: "default" | "tertiary" | "primary" | "success" | "info" | "warning" | "error"
  editPermission?: Array<string>
  deletePermission?: Array<string>
  isShow?: boolean
}
withDefaults(defineProps<PROPS>(), {
  showEdit: true,
  showDelete: true,
  editTip: () => i18n.t("commons.edit"),
  deleteTip: () => i18n.t("commons.delete"),
  type: "warning",
  editPermission: () => [],
  deletePermission: () => [],
  isShow: false,
})
const emits = defineEmits(["editClick", "deleteClick"])
</script>
<template>
  <n-space justify="center">
    <slot name="front" />
    <ga-table-operator-button
      v-if="showEdit"
      icon="i-tabler:edit"
      :type="type"
      :tip="editTip"
      :permission="editPermission"
      :disabled="isShow"
      @exec="emits('editClick')" />
    <slot name="middle" />
    <ga-table-operator-button
      v-if="showDelete"
      icon="i-tabler:trash"
      type="error"
      :tip="deleteTip"
      :permission="deletePermission"
      :disabled="isShow"
      @exec="emits('deleteClick')" />
    <slot name="behind" />
  </n-space>
</template>

<style></style>
