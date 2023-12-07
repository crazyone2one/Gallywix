<script setup lang="ts">
import { computed, nextTick, ref } from "vue"

import CustomFieldFormList from "./CustomFieldFormList.vue"
import TemplateComponentEditHeader from "./TemplateComponentEditHeader.vue"
import GaFormDivider from "/@/components/GaFormDivider.vue"
import { useForm } from "@alova/scene-vue"

import { useUserStore } from "/@/store/modules/user-store"

import { generateTableHeaderKey, getCustomFieldsKeys } from "/@/utils/table-util"

import { getCustomFieldDefault, getCustomFieldTemplates, ICustomFieldTemp } from "/@/apis/template/template"
import { handleResourceSave, handleResourceUpdate, ITestCaseTemplate } from "/@/apis/template/test-case-template"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { FormInst, NDrawer, NDrawerContent } from "naive-ui"

interface IProps {
  visible?: boolean
  scene: string
  form?: ITestCaseTemplate
}
const props = withDefaults(defineProps<IProps>(), {
  visible: false,
  form: () => {
    return {} as ITestCaseTemplate
  },
})
const relateFields = ref<Array<ICustomFieldTemp>>([])
const templateContainIds = ref<Set<string>>(new Set())
const isSystem = computed(() => {
  return props.form.system
})
const formRef = ref<FormInst | null>(null)
const emits = defineEmits(["update:visible", "update:form"])
const active = computed(() => {
  return props.visible
})
const _form = computed({
  get: () => props.form,
  set: (val) => {
    emits("update:form", val)
  },
})
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
}
const { send: loadDefaultCustomField } = useRequest((val) => getCustomFieldDefault(val), { immediate: false })
const { send: loadCustomFieldTemplate } = useRequest((val) => getCustomFieldTemplates(val), { immediate: false })
const getRelateFields = () => {
  let condition = { templateId: "" }
  condition.templateId = _form.value.id as string
  if (_form.value.id) {
    loadCustomFieldTemplate(condition).then((res) => {
      console.log(`output->res`, res)
      // todo
    })
  } else {
    appendDefaultFiled()
  }
}

const appendDefaultFiled = () => {
  let condition = {
    projectId: useUserStore().project_id,
    scene: props.scene,
  }
  loadDefaultCustomField(condition).then((res) => {
    const data = res
    data.forEach((item) => {
      if (item.name === "用例等级" && item.system && item.scene === "TEST_CASE") {
        item.required = true
        item.disabled = true
      }
      if (item.id) {
        templateContainIds.value.add(item.id)
      }
      item.fieldId = item.id
      item.id = undefined
      if (item.type === "checkbox") {
        item.defaultValue = []
      }
    })
    relateFields.value.push(...data)
  })
}
const open = () => {
  nextTick(() => {
    relateFields.value = []
    templateContainIds.value = new Set()
    getRelateFields()
    emits("update:visible", true)
  })
}
const handleClose = () => {
  emits("update:visible", false)
}
const {
  //   loading: submiting,
  send: submit,
  // 响应式的表单数据，内容由initialForm决定
  form: param,
  onSuccess,
  onError,
} = useForm(
  (formData: ITestCaseTemplate) => {
    return formData.id ? handleResourceUpdate(formData) : handleResourceSave(formData)
  },
  {
    // 初始化表单数据
    initialForm: {
      id: undefined,
      name: "",
      type: "functional",
      stepModel: "STEP",
      steps: [
        {
          num: 1,
          desc: "",
          result: "",
        },
      ],
    },
    // 设置这个参数为true即可在提交完成后自动重置表单数据
    resetAfterSubmiting: true,
    immediate: false,
  },
)
const handleSave = () => {
  formRef.value?.validate((e) => {
    if (!e) {
      // let param = {} as ITestCaseTemplate
      Object.assign(param.value, _form.value)
      if (_form.value.steps) {
        param.value.steps = _form.value.steps
      }
      // param.options = JSON.stringify(_form.value.options)
      param.value.projectId = useUserStore().project_id as string
      const customFields = relateFields.value
      if (customFields) {
        let keys = getCustomFieldsKeys(customFields)
        customFields.forEach((item) => {
          if (!item.key) {
            item.key = generateTableHeaderKey(keys as Set<string>)
          }
          // item.defaultValue = JSON.stringify(item.defaultValue)
        })
      }
      param.value.customFields = customFields
      submit(param.value)
    }
  })
}
onSuccess(() => {
  window.$message.success(i18n.t("commons.save_success"))
  emits("update:visible", false)
})
onError((r) => {
  window.$message.error(r.error.message)
})
defineExpose({ open })
</script>

<template>
  <n-drawer v-model:show="active" width="100%">
    <n-drawer-content :native-scrollbar="false">
      <template #header>
        <template-component-edit-header :template="_form" @handle:cancel="handleClose" @handle:save="handleSave" />
      </template>
      <template #default>
        <ga-form-divider :title="$t('test_track.plan_view.base_info')" />
        <n-form
          ref="formRef"
          :model="_form"
          :rules="rules"
          label-placement="left"
          label-width="auto"
          size="small"
          require-mark-placement="right-hanging">
          <n-form-item :label="$t('commons.template_name')" path="name">
            <n-input v-model:value="_form.name" :disabled="isSystem" />
          </n-form-item>
          <slot name="base"></slot>
          <n-form-item :label="$t('commons.description')" path="description">
            <n-input
              v-model:value="_form.description"
              type="textarea"
              :autosize="{
                minRows: 3,
                maxRows: 4,
              }"
              maxlength="255"
              show-count />
          </n-form-item>
          <ga-form-divider :title="$t('custom_field.template_setting')" />
          <slot name="info"></slot>
          <slot></slot>
          <n-form-item :label="$t('table.selected_fields')" class="mt-7">
            <n-button type="primary">{{ $t("custom_field.add_field") }}</n-button>
            <n-button type="primary">{{ $t("custom_field.custom_field_setting") }}</n-button>
          </n-form-item>
          <n-form-item>
            <custom-field-form-list
              :table-data="relateFields"
              :scene="scene"
              :template-contain-ids="templateContainIds"
              :form="_form" />
          </n-form-item>
        </n-form>
      </template>
      <template #footer>
        <n-button>Footer</n-button>
      </template>
    </n-drawer-content>
  </n-drawer>
</template>

<style scoped></style>
