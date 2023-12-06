<script setup lang="ts">
import { ref } from "vue"

import { i18n } from "../i18n"
import { ElButton, ElDialog } from "element-plus"

interface Props {
  title?: string
  isShow?: boolean // 是否展示底部操作按钮
  saveAsEditTip?: string
  modalWidth?: string
}
withDefaults(defineProps<Props>(), {
  title: i18n.t("commons.operating"), // modal title
  isShow: false, // show footer button
  saveAsEditTip: "",
  modalWidth: "", // modal width
})
const visible = ref(false)
const emits = defineEmits(["confirm", "cancel", "saveAsEdit", "closeModal"])
const toggleModal = () => {
  visible.value = !visible.value
  return Promise.resolve(visible.value)
}
// * show modal
const showModal = () => {
  visible.value = true
  return Promise.resolve(true)
}
// * close modal
const hideModal = () => {
  visible.value = false
  return Promise.resolve(false)
}
const onCancel = (): void => {
  visible.value = false
  emits("cancel")
}
defineExpose({ toggleModal, showModal, hideModal })
</script>
<template>
  <el-dialog v-model="visible" :title="title" :close-on-click-modal="false" @close="onCancel">
    <slot name="content" />
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="hideModal">{{ $t("commons.cancel") }}</el-button>
        <el-button type="primary" @click="emits('confirm')"> {{ $t("commons.confirm") }} </el-button>
        <el-button v-if="isShow" v-prevent-re-click type="primary" @click="emits('saveAsEdit')" @keydown.enter.prevent>
          {{ saveAsEditTip }}
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped></style>
