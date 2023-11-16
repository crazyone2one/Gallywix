<script setup lang="ts">
import { ref } from "vue"

import ModalDialog from "/@/components/ModalDialog.vue"
import { useForm } from "@alova/scene-vue"

import { ORGANIZATION, saveData, updateData } from "/@/apis/organization"
import { FormInst, FormRules, NForm, NFormItem, NInput } from "naive-ui"

const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const formRef = ref<FormInst | null>(null)
const rules: FormRules = {
  name: [
    { required: true, message: "请输入姓名", trigger: "blur" },
    { min: 2, max: 10, message: "长度在 2 到 10 个字符", trigger: "blur" },
    {
      required: true,
      pattern: /^[\u4e00-\u9fa5_a-zA-Z0-9.·-]+$/,
      message: "姓名不支持特殊字符",
      trigger: "blur",
    },
  ],
  description: { max: 60, message: "最大长度 60 个字符", trigger: "blur" },
}
const emits = defineEmits(["refresh"])
const {
  //   loading: submiting,
  send: submit,
  // 响应式的表单数据，内容由initialForm决定
  form: formData,
  onSuccess,
} = useForm(
  (formData) => {
    return formData.id ? updateData(formData) : saveData(formData)
  },
  {
    // 初始化表单数据
    initialForm: {
      id: null,
      name: "",
      description: "",
    },
    // 设置这个参数为true即可在提交完成后自动重置表单数据
    resetAfterSubmiting: true,
  },
)
const handleSave = () => {
  formRef.value?.validate((error) => {
    if (!error) {
      submit(formData.value)
    }
  })
}
onSuccess(() => {
  modalDialog.value?.closeModal()
  window.$message.success("success")
  emits("refresh")
})
const open = (val?: ORGANIZATION) => {
  if (val) {
    Object.assign(formData.value, val)
  }
  modalDialog.value?.showModal()
}
defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog" title="创建组织" modal-with="50%" @confirm="handleSave">
    <template #content>
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
      >
        <n-form-item label="名称" path="name">
          <n-input v-model:value="formData.name" />
        </n-form-item>
        <n-form-item label="描述" path="description">
          <n-input v-model:value="formData.description" />
        </n-form-item>
      </n-form>
    </template>
  </modal-dialog>
</template>

<style></style>
