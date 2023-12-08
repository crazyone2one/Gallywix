<script setup lang="ts">
import { ref } from "vue"

import ModalDialog from "/@/components/ModalDialog.vue"
import { useForm } from "@alova/scene-vue"

import { createWorkspace, IWorkspaceItem } from "/@/apis/workspace"
import { i18n } from "/@/i18n"
import { FormInst, FormRules } from "naive-ui"

const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const formRef = ref<FormInst | null>(null)
const emits = defineEmits(["refresh"])
const rules: FormRules = {
  name: [{ required: true, message: i18n.t("workspace.input_name"), trigger: "blur" }],
  description: {
    max: 50,
    message: i18n.t("commons.input_limit", [0, 50]),
    trigger: "blur",
  },
}
const open = (val?: IWorkspaceItem) => {
  if (val) {
    formData.value = Object.assign({}, val)
  }
  modalDialog.value?.showModal()
}
const {
  loading: submiting,
  send: submit,
  // 响应式的表单数据，内容由initialForm决定
  form: formData,
} = useForm((formData) => createWorkspace(formData), {
  // 初始化表单数据
  initialForm: {
    id: undefined,
    name: "",
    description: "",
  } as unknown as IWorkspaceItem,
  // 设置这个参数为true即可在提交完成后自动重置表单数据
  resetAfterSubmiting: true,
  immediate: false,
})
const handleSave = () => {
  formRef.value?.validate((error) => {
    if (!error) {
      submit(formData.value).then(() => {
        modalDialog.value?.closeModal()
        window.$message.success("创建成功")
        emits("refresh")
      })
    }
  })
}
defineExpose({ open })
</script>
<template>
  <n-spin :show="submiting">
    <modal-dialog ref="modalDialog" :title="$t('workspace.create')" @confirm="handleSave">
      <template #content>
        <n-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-placement="left"
          require-mark-placement="right-hanging">
          <n-form-item :label="$t('commons.name')" path="name">
            <n-input v-model:value="formData.name" />
          </n-form-item>
          <n-form-item :label="$t('commons.description')" path="description">
            <n-input v-model:value="formData.description" type="textarea" />
          </n-form-item>
        </n-form>
      </template>
    </modal-dialog>
  </n-spin>
</template>
<style></style>
