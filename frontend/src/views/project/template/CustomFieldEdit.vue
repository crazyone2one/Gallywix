<script setup lang="ts">
import { computed, ref } from "vue"

import GaSingleHandleDrag from "/@/components/GaSingleHandleDrag.vue"
import ModalDialog from "/@/components/ModalDialog.vue"
import { useForm } from "@alova/scene-vue"

import { useAuthStore } from "/@/store/auth-store"

import { CUSTOM_FIELD_TYPE_OPTION, SYSTEM_FIELD_NAME_MAP } from "/@/utils/table-constants"

import { ICustomField, ICustomFieldOption, saveCustomField, updateCustomField } from "/@/apis/template/custom-field"
import { i18n } from "/@/i18n"
import { FormInst } from "naive-ui"

const props = withDefaults(defineProps<{ scene?: string }>(), { scene: "" })

const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const formRef = ref<FormInst | null>(null)
const title = ref(i18n.t("custom_field.create"))
const emits = defineEmits(["refresh"])
const isSystem = computed(() => {
  return formData.value.system
})
const systemNameMap = computed(() => {
  return SYSTEM_FIELD_NAME_MAP
})
const fieldTypeOptions = computed(() => {
  return CUSTOM_FIELD_TYPE_OPTION
})
const isTemplateEdit = computed(() => {
  return !!props.scene
})
const showOptions = computed(() => {
  return ["select", "multipleSelect", "radio", "checkbox"].indexOf(formData.value.type) > -1
})
const sceneOptions = [
  { value: "TEST_CASE", label: i18n.t("workspace.case_template_manage") },
  { value: "ISSUE", label: i18n.t("workspace.issue_template_manage") },
  { value: "API", label: i18n.t("workspace.api_template_manage") },
]
const rules = {
  name: [
    {
      required: true,
      message: i18n.t("test_track.case.input_name"),
      trigger: "blur",
    },
    {
      max: 64,
      message: i18n.t("test_track.length_less_than") + "64",
      trigger: "blur",
    },
  ],
  scene: [{ required: true, trigger: "change" }],
  type: [{ required: true, trigger: "change" }],
}
const {
  //   loading: submiting,
  send: submit,
  // 响应式的表单数据，内容由initialForm决定
  form: formData,
  onSuccess,
} = useForm(
  (formData: ICustomField) => {
    return formData.id ? updateCustomField(formData) : saveCustomField(formData)
  },
  {
    // 初始化表单数据
    initialForm: {
      id: undefined,
      name: "",
      type: "input",
      scene: "TEST_CASE",
      remark: "",
      system: false,
      options: [],
      global: true,
      projectId: "",
    },
    // 设置这个参数为true即可在提交完成后自动重置表单数据
    resetAfterSubmiting: true,
    immediate: false,
  },
)
const open = (_title: string, val?: ICustomField) => {
  title.value = _title
  if (val) {
    Object.assign(formData.value, val)
    if (!(val.options instanceof Array)) {
      formData.value.options = val.options ? JSON.parse(val.options) : []
    }
  } else {
    if (isTemplateEdit.value) {
      formData.value.scene = props.scene
    }
  }
  modalDialog.value?.showModal()
}
const handleSave = () => {
  formRef.value?.validate((e) => {
    if (!e) {
      let param = {} as ICustomField
      if (formData.value.name.indexOf(".") > -1) {
        window.$message.error("名称不能包含'.'号")
        return
      }
      if (formData.value.name === i18n.t("custom_field.case_priority")) {
        if (formData.value.options) {
          for (let i = 0; i < formData.value.options.length; i++) {
            const _tmp = formData.value.options[i] as ICustomFieldOption
            if (_tmp.label !== "P" + i) {
              window.$message.warning(i18n.t("custom_field.case_priority_option_check_error") + i)
              return
            }
          }
        }
      }
      Object.assign(param, formData.value)
      param.projectId = useAuthStore().project_id as string
      if (["select", "multipleSelect", "radio", "checkbox"].indexOf(param.type) > -1) {
        if (param.options) {
          if (param.options.length < 1) {
            window.$message.warning(i18n.t("custom_field.option_check"))
            return
          }
          for (const item of param.options as Array<ICustomFieldOption>) {
            if (!item.label || !item.value) {
              window.$message.warning(i18n.t("custom_field.option_value_check"))
              return
            }
          }
        }
      }
      param.options = JSON.stringify(formData.value.options)
      console.log(`output->formData.value`, param)
      submit(param)
    }
  })
}
onSuccess(() => {
  modalDialog.value?.toggleModal()
  window.$message.success(i18n.t("commons.save_success"))
  emits("refresh")
})
defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog" :title="title" modal-with="600px" @confirm="handleSave">
    <template #content>
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging">
        <n-form-item :label="$t('custom_field.field_name')" path="name">
          <n-input v-if="isSystem" :value="$t(systemNameMap[formData.name])" :disabled="isSystem" />
          <n-input v-else v-model:value="formData.name" />
        </n-form-item>
        <n-form-item :label="$t('custom_field.field_remark')" path="remark">
          <n-input v-model:value="formData.remark" type="textarea" />
        </n-form-item>
        <n-form-item :label="$t('custom_field.scene')" path="type">
          <n-select
            v-model:value="formData.scene"
            filterable
            :disabled="isSystem || isTemplateEdit"
            :placeholder="$t('custom_field.scene')"
            :options="sceneOptions" />
        </n-form-item>
        <n-form-item :label="$t('custom_field.scene')" path="type">
          <n-select
            v-model:value="formData.type"
            filterable
            :disabled="isSystem"
            :placeholder="$t('custom_field.field_type')"
            :options="fieldTypeOptions" />
        </n-form-item>
        <n-form-item v-if="showOptions" :label="$t('custom_field.field_option')" path="options">
          <ga-single-handle-drag
            :is-kv="formData.scene === 'ISSUE'"
            :options="formData.options as Array<ICustomFieldOption>" />
        </n-form-item>
      </n-form>
    </template>
  </modal-dialog>
</template>

<style></style>
