<script setup lang="ts">
import { computed } from "vue"

import { ICustomFieldOption } from "/@/apis/template/custom-field"
import { ICustomFieldTemp } from "/@/apis/template/template"
import { ITestCaseTemplate } from "/@/apis/template/test-case-template"

interface IProps {
  data: ICustomFieldTemp
  prop: string
  isTemplateEdit: boolean
  disabled?: boolean
  form?: ITestCaseTemplate
}
const emits = defineEmits(["update:data"])
const props = withDefaults(defineProps<IProps>(), {
  disabled: false,
  form: () => {
    return {} as ITestCaseTemplate
  },
})
const form = computed({
  get: () => props.data,
  set: (val) => {
    emits("update:data", val)
  },
})
const handleChange = () => {
  console.log(`output->props.form`,props.form)
  console.log(`output->data.name`, props.data.name)
}
</script>
<template>
  <span>
    <n-select
      v-if="data.type === 'select' || data.type === 'multipleSelect'"
      v-model:value="form[prop]"
      filterable
      clearable
      :disabled="disabled"
      :multiple="data.type === 'multipleSelect'"
      :placeholder="$t('commons.default')"
      :options="data.options as ICustomFieldOption[]"
      @update:value="handleChange" />
    <n-input
      v-else-if="data.type === 'textarea'"
      v-model:value="form[prop]"
      type="textarea"
      :disabled="disabled"
      :placeholder="$t('commons.input_content')" />
    <n-input v-else v-model:value="form[prop]" :disabled="disabled" :placeholder="$t('commons.input_content')" />
  </span>
</template>
<style scoped></style>
