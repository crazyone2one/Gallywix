<script setup lang="ts">
import { ref } from "vue"

import ModalDialog from "/@/components/ModalDialog.vue"
import { useForm } from "@alova/scene-vue"

import { useAuthStore } from "/@/store/auth-store"

import { PROJECT, saveData } from "/@/apis/project"
import { i18n } from "/@/i18n"
import { FormInst, FormRules, NForm, NFormItem, NInput } from "naive-ui"

const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const formRef = ref<FormInst | null>(null)
const title = ref(i18n.t("project.create"))
const emits = defineEmits(["refresh"])
const rules: FormRules = {
  name: [
    { required: true, message: "请输入工作空间名称", trigger: "blur" },
    {
      min: 2,
      max: 50,
      message: "长度在 2 到 50 个字符",
      trigger: "blur",
    },
  ],
  description: {
    max: 250,
    message: "长度在 2 到 250 个字符",
    trigger: "blur",
  },
}
const open = (val?: PROJECT) => {
  if (val) {
    window.$message.info(val.name)
  }
  modalDialog.value?.showModal()
}
const {
  //   loading: submiting,
  send: submit,
  // 响应式的表单数据，内容由initialForm决定
  form: formData,
  onSuccess,
  onError,
} = useForm(
  (formData) => {
    return saveData(formData)
  },
  {
    // 初始化表单数据
    initialForm: {
      id: undefined,
      name: "",
      description: "",
      workspaceId: "",
    },
    // 设置这个参数为true即可在提交完成后自动重置表单数据
    resetAfterSubmiting: true,
  },
)
const handleSave = () => {
  formRef.value?.validate((error) => {
    if (!error) {
      formData.value.workspaceId = useAuthStore().workspace_id as string
      submit(formData.value)
    }
  })
}
onSuccess(() => {
  modalDialog.value?.closeModal()
  window.$message.success("创建成功")
  emits("refresh")
})
onError((val) => {
  window.$message.error(val.error.message)
})
defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog" :title="title" @confirm="handleSave">
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
</template>
<style></style>
