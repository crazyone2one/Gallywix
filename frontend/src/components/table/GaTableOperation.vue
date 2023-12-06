<script setup lang="ts">
import GaTableOperatorButton from "./GaTableOperatorButton.vue"

import { i18n } from "/@/i18n"
interface IProps {
  isShow: boolean
  tip1?: string
  tip2?: string
  editPermission?: Array<string>
  deletePermission?: Array<string>
  showEdit?: boolean
  showDelete?: boolean
}
withDefaults(defineProps<IProps>(), {
  showEdit: true,
  showDelete: true,
  tip1: () => i18n.t("commons.edit"),
  tip2: () => i18n.t("commons.delete"),

  editPermission: () => [],
  deletePermission: () => [],
})
const emits = defineEmits(["editClick", "deleteClick"])
</script>
<template>
  <span>
    <slot name="front" />
    <ga-table-operator-button
      v-if="showEdit"
      v-permission="editPermission"
      :tip="tip1"
      icon="el-icon-edit"
      :disabled="isShow"
      @exec="emits('editClick')" />
    <slot name="middle" />
    <ga-table-operator-button
      v-if="showDelete"
      v-permission="deletePermission"
      :tip="tip2"
      icon="el-icon-delete"
      :disabled="isShow"
      type="danger"
      @exec="emits('deleteClick')" />
    <slot name="behind" />
  </span>
</template>

<style></style>
