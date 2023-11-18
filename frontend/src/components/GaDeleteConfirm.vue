<script setup lang="ts">
import { ref } from "vue"

import ModalDialog from "./ModalDialog.vue"

import { i18n } from "../i18n"
import { NGrid, NGridItem, NInput } from "naive-ui"
interface IProps {
  title?: string
  withTip?: boolean
}
withDefaults(defineProps<IProps>(), {
  title: () => i18n.t("commons.title"), // modal title
  withTip: false,
})
const emits = defineEmits(["delete"])
const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const value = ref<string>("")
const record = ref<{ name: string }>({ name: "" })
const open = (val: { name: string }) => {
  record.value = val
  value.value = ""
  modalDialog.value?.showModal()
}
const handleConfirm = () => {
  if (value.value.trim() !== ("DELETE-" + record.value.name).trim()) {
    window.$message.warning(i18n.t("commons.incorrect_input"))
    return
  }
  emits("delete", record.value)
  modalDialog.value?.closeModal()
}
defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog" :title="title" @confirm="handleConfirm">
    <template #content>
      <n-grid :x-gap="12" :y-gap="8" :cols="1">
        <n-grid-item>
          <span>{{ $t("commons.delete_confirm") }}</span>
          <span class="delete-tip"> DELETE-{{ record.name }}</span>
        </n-grid-item>
        <n-grid-item v-if="withTip" class="tip">
          <span>
            <slot class="tip"></slot>
          </span>
        </n-grid-item>
        <n-grid-item>
          <n-input
            v-model:value="value"
            :placeholder="$t('commons.input_content')" />
        </n-grid-item>
      </n-grid>
    </template>
  </modal-dialog>
</template>

<style></style>
