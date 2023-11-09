<script setup lang="ts">
import { NSpace } from "naive-ui"
import GaButton from "/@/components/GaButton.vue"
interface PROPS {
  showEdit?: boolean
  showDelete?: boolean
  editTip?: string
  deleteTip?: string
}
withDefaults(defineProps<PROPS>(), {
  showEdit: true,
  showDelete: true,
  editTip: () => "编辑",
  deleteTip: () => "删除",
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
