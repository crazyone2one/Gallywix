<script setup lang="ts">
import GaButton from "/@/components/GaButton.vue"

import { i18n } from "/@/i18n"
import { NSpace } from "naive-ui"
interface PROPS {
  showEdit?: boolean
  showDelete?: boolean
  editTip?: string
  deleteTip?: string
}
withDefaults(defineProps<PROPS>(), {
  showEdit: true,
  showDelete: true,
  editTip: () => i18n.t("commons.edit"),
  deleteTip: () => i18n.t("commons.delete"),
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
      type="warning"
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
