<script setup lang="ts">
import { ref } from "vue"

import FieldTemplateEdit from "../components/FieldTemplateEdit.vue"
import FormRichTextItem from "../components/FormRichTextItem.vue"
import StepChangeItem from "../components/StepChangeItem.vue"
import TestCaseStepItem from "../components/TestCaseStepItem.vue"

import { ITestCaseTemplate } from "/@/apis/template/test-case-template"

const showDialog = ref(false)
const fieldTemplateEdit = ref<InstanceType<typeof FieldTemplateEdit> | null>(null)
const open = () => {
  // showDialog.value = true
  fieldTemplateEdit.value?.open()
}
const form = ref({
  stepModel: "STEP",
  steps: [
    {
      num: 1,
      desc: "",
      result: "",
    },
  ],
  type: "functional",
} as ITestCaseTemplate)
const handleUpdateShowDialog = (value: boolean) => {
  showDialog.value = value
}
defineExpose({ open })
</script>
<template>
  <field-template-edit
    ref="fieldTemplateEdit"
    :visible="showDialog"
    scene="TEST_CASE"
    :form="form"
    @update:visible="handleUpdateShowDialog">
    <template #default>
      <n-form-item :label="$t('test_track.case.name')" path="caseName">
        <n-input v-model:value="form.caseName" maxlength="64" show-count />
      </n-form-item>
      <form-rich-text-item prop="prerequisite" :data="form" :label="$t('test_track.case.prerequisite')" />
      <step-change-item :form="form" />
      <test-case-step-item v-if="form.stepModel === 'STEP'" :form="form" />
      <form-rich-text-item
        v-if="form.stepModel === 'TEXT'"
        :label="$t('test_track.case.step_desc')"
        :data="form"
        prop="stepDescription" />
      <form-rich-text-item
        v-if="form.stepModel === 'TEXT'"
        :label="$t('test_track.case.expected_results')"
        :data="form"
        prop="expectedResult" />
      <form-rich-text-item
        v-if="form.stepModel === 'TEXT'"
        :label="$t('test_track.case.actual_result')"
        :data="form"
        prop="actualResult" />
    </template>
  </field-template-edit>
</template>
<style scoped></style>
