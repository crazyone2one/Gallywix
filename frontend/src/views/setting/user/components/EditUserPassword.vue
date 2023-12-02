<script setup lang="ts">
import { ref } from "vue"

import ModalDialog from "/@/components/ModalDialog.vue"
import { useForm } from "@alova/scene-vue"

import { specialModifyPassword, USER } from "/@/apis/user"
import { i18n } from "/@/i18n"
import { FormInst } from "naive-ui"

const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const formRef = ref<FormInst | null>(null)

const rules = {
  newPassword: [
    { required: true, message: i18n.t("user.input_password"), trigger: "blur" },
    {
      required: true,
      pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,30}$/,
      message: i18n.t("member.password_format_is_incorrect"),
      trigger: "blur",
    },
  ],
  confirmpassword: [
    {
      required: true,
      pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,30}$/,
      message: i18n.t("member.password_format_is_incorrect"),
      trigger: "blur",
    },
    // { trigger: ["blur", "change"], validator: validateConfirmPwd },
  ],
}
const changePasswordUser = ref<string | undefined>("")
// const ruleForm = ref<USER>({ newpassword: "", confirmpassword: "", id: "" } as USER)

const open = (val: USER) => {
  changePasswordUser.value = val.id
  ruleForm.value = Object.assign({}, val)
  modalDialog.value?.showModal()
}
const {
  loading: submiting,
  send: submit,
  form: ruleForm,
} = useForm((val) => specialModifyPassword(val), {
  immediate: false, // 初始化表单数据
  initialForm: {
    id: "",
    newPassword: "",
    confirmpassword: "",
  } as USER,
})
const handleSave = () => {
  formRef.value?.validate((e) => {
    if (!e) {
      submit(ruleForm.value).then(() => {
        window.$message.success(i18n.t("commons.modify_success"))
        modalDialog.value?.toggleModal()
      })
    }
  })
}
defineExpose({ open })
</script>
<template>
  <n-spin :show="submiting">
    <modal-dialog ref="modalDialog" :title="$t('member.edit_password')" @confirm="handleSave">
      <template #content>
        <n-form
          ref="formRef"
          :model="ruleForm"
          :rules="rules"
          label-placement="left"
          label-width="auto"
          require-mark-placement="right-hanging">
          <n-form-item :label="$t('member.new_password')" path="newPassword">
            <n-input v-model:value="ruleForm.newPassword" type="password" show-password-on="mousedown" />
          </n-form-item>
          <n-form-item :label="$t('member.repeat_password')" path="confirmpassword">
            <n-input v-model:value="ruleForm.confirmpassword" type="password" show-password-on="mousedown" />
          </n-form-item>
        </n-form>
      </template>
    </modal-dialog>
  </n-spin>
</template>
<style scoped></style>
