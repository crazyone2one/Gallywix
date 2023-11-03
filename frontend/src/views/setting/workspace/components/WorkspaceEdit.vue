<script setup lang="ts">
import ModalDialog from "/@/components/ModalDialog.vue"
import { WORKSPACE, createWorkspace } from "/@/apis/workspace"
import { ref } from "vue"
import { FormInst, FormRules, NForm, NFormItem, NInput } from "naive-ui"
import { useForm } from "@alova/scene-vue"

const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const formRef = ref<FormInst | null>(null)
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
const open = (val?: WORKSPACE) => {
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
} = useForm(
  (formData) => {
    return createWorkspace(formData)
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
  window.$message.success("创建成功")
})
defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog" title="创建工作空间" @confirm="handleSave">
    <template #content>
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="left"
        require-mark-placement="right-hanging">
        <n-form-item label="名称" path="name">
          <n-input v-model:value="formData.name" />
        </n-form-item>
        <n-form-item label="描述" path="description">
          <n-input v-model:value="formData.description" type="textarea" />
        </n-form-item>
      </n-form>
    </template>
  </modal-dialog>
</template>
<style></style>
