<script setup lang="ts">
import GaButton from "/@/components/GaButton.vue"

import { i18n } from "/@/i18n"
import { NSpace } from "naive-ui"
interface PROPS {
  showEdit?: boolean
  showDelete?: boolean
  editTip?: string
  deleteTip?: string
  type?:
    | "default"
    | "tertiary"
    | "primary"
    | "success"
    | "info"
    | "warning"
    | "error"
  // editPermission?: Array<string>
  // deletePermission?: Array<string>
}
withDefaults(defineProps<PROPS>(), {
  showEdit: true,
  showDelete: true,
  editTip: () => i18n.t("commons.edit"),
  deleteTip: () => i18n.t("commons.delete"),
  type: "warning",
  // editPermission: () => [],
  // deletePermission: () => [],
})
const emits = defineEmits(["editClick", "deleteClick"])
</script>
<template>
  <n-space justify="center">
    <slot name="front" />
    <ga-button
      v-if="showEdit"
      :text="true"
      :is-icon="true"
      icon-class="i-tabler:edit"
      :type="type"
      :is-pop="true"
      :pop-text="editTip"
      @exec="emits('editClick')" />
    <slot name="middle" />
    <ga-button
      v-if="showDelete"
      :text="true"
      :is-icon="true"
      icon-class="i-tabler:trash"
      type="error"
      :is-pop="true"
      :pop-text="deleteTip"
      @exec="emits('deleteClick')" />
    <slot name="behind" />
  </n-space>
</template>

<style></style>
