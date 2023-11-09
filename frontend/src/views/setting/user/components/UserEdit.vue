<script setup lang="ts">
import { ref } from "vue"
import { FormInst, FormItemRule, FormRules, NForm, NFormItem, NInput, NSwitch } from "naive-ui"
import { useForm } from "@alova/scene-vue"
import ModalDialog from "/@/components/ModalDialog.vue"
import { USER, saveUserData, updateUserData } from "/@/apis/user"

const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const formRef = ref<FormInst | null>(null)
const rules: FormRules = {
  username: { required: true, message: "名称不能为空", trigger: "blur" },
  nickname: { required: true, message: "昵称不能为空", trigger: "blur" },
  password: { required: true, message: "密码不能为空", trigger: "blur" },
  phone: {
    required: false,
    trigger: ["blur"],
    validator: (rule: FormItemRule, value: string) => {
      console.log(rule)
      if (value) {
        return /^[1]+[3-9]+\d{9}$/.test(value)
      }
    },
  },
  email: {
    required: true,
    trigger: ["blur"],
    validator: (rule: FormItemRule, value: string) => {
      console.log(rule)
      if (value) {
        return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value)
      }
    },
  },
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
    return formData.id ? updateUserData(formData) : saveUserData(formData)
  },
  {
    // 初始化表单数据
    initialForm: {
      id: null,
      username: "",
      nickname: "",
      password: "",
      status: true,
      email: "",
      phone: "",
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
const open = (val?: USER) => {
  if (val) {
    Object.assign(formData.value, val)
    window.$message.info(val.username)
  }
  modalDialog.value?.showModal()
}
defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog" title="创建用户" modal-with="50%" @confirm="handleSave">
    <template #content>
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging">
        <n-form-item label="用户名" path="username">
          <n-input v-model:value="formData.username" />
        </n-form-item>
        <n-form-item label="昵称" path="nickname">
          <n-input v-model:value="formData.nickname" />
        </n-form-item>
        <n-form-item label="邮箱" path="email">
          <n-input v-model:value="formData.email" />
        </n-form-item>
        <n-form-item label="电话" path="phone">
          <n-input v-model:value="formData.phone" />
        </n-form-item>
        <n-form-item label="启用">
          <n-switch v-model:value="formData.status" />
        </n-form-item>
      </n-form>
    </template>
  </modal-dialog>
</template>

<style></style>
