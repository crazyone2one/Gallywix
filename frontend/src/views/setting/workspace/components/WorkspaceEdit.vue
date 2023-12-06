<script setup lang="ts">
import { ref } from "vue"

import ModalDialog from "/@/components/ModalDialog.vue"
import { useForm } from "@alova/scene-vue"

import { createWorkspace, IWorkspace } from "/@/apis/workspace"
import { i18n } from "/@/i18n"
import { ElForm, ElFormItem, ElInput, ElMessage, FormInstance } from "element-plus"

const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const formRef = ref<FormInstance>()
const emits = defineEmits(["refresh"])
const rules = {
  name: [{ required: true, message: i18n.t("workspace.input_name"), trigger: "blur" }],
  description: {
    max: 50,
    message: i18n.t("commons.input_limit", [0, 50]),
    trigger: "blur",
  },
}
const open = (val?: IWorkspace) => {
  if (val) {
    Object.assign(formData.value, val)
  }
  modalDialog.value?.showModal()
}
const {
  //   loading: submiting,
  send: submit,
  // 响应式的表单数据，内容由initialForm决定
  form: formData,
} = useForm(
  (formData) => {
    return createWorkspace(formData)
  },
  {
    // 初始化表单数据
    initialForm: {
      id: undefined,
      name: "",
      description: "",
    },
    // 设置这个参数为true即可在提交完成后自动重置表单数据
    resetAfterSubmiting: true,
  },
)
const handleSave = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      submit().then(() => {
        formEl.resetFields()
        modalDialog.value?.hideModal()
        ElMessage.success(i18n.t("commons.save_success"))
        emits("refresh")
      })
    } else {
      console.log("error submit!", fields)
    }
  })
}

defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog" :title="$t('workspace.create')" width="30%" @confirm="handleSave(formRef)">
    <template #content>
      <el-form ref="formRef" :model="formData" :rules="rules" label-position="right" label-width="100px">
        <el-form-item :label="$t('commons.name')" prop="name">
          <el-input v-model="formData.name" autocomplete="off" class="form-input" show-word-limit maxlength="100" />
        </el-form-item>
        <el-form-item :label="$t('commons.description')" prop="description">
          <el-input v-model="formData.description" type="textarea" class="form-input"></el-input>
        </el-form-item>
      </el-form>
    </template>
  </modal-dialog>
</template>
<style scoped>
.form-input {
  width: 80%;
}
</style>
