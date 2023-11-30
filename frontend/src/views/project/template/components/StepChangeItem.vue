<script setup lang="ts">
import { computed, onMounted } from "vue"

import { ITestCaseTemplate } from "/@/apis/template/test-case-template"
import { i18n } from "/@/i18n"
import { NPopselect, SelectOption } from "naive-ui"

interface IPorps {
  form: ITestCaseTemplate
  disable?: boolean
}
const props = withDefaults(defineProps<IPorps>(), {
  disable: false,
})
const emits = defineEmits(["update:form"])
const plus = computed({
  get: () => props.form,
  set: (val) => {
    emits("update:form", val)
  },
})
const options: Array<SelectOption> = [
  {
    label: i18n.t("test_track.case.step_describe"),
    value: "STEP",
    disabled: props.disable,
  },
  {
    label: i18n.t("test_track.case.text_describe"),
    value: "TEXT",
    disabled: props.disable,
  },
]
onMounted(() => {
  if (!plus.value.stepModel) {
    console.log(`output->plus.value`, plus.value)
    plus.value.stepModel = "STEP"
  }
})
</script>
<template>
  <n-form-item
    :label="plus.stepModel === 'STEP' ? $t('test_track.case.step_describe') : $t('test_track.case.text_describe')">
    <n-popselect v-if="!disable" v-model:value="plus.stepModel" :options="options" trigger="click">
      <span class="el-dropdown-link">
        {{ $t("test_track.case.change_type") }} <i class="i-tabler:arrow-big-down-lines-filled" />
      </span>
    </n-popselect>
  </n-form-item>
</template>

<style scoped></style>
