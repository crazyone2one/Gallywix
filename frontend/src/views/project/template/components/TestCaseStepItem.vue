<script setup lang="ts">
import { computed, defineComponent, h, nextTick, onMounted, ref } from "vue"

import { renderIcon } from "/@/utils/common"

import { ITestCaseStep, ITestCaseTemplate } from "/@/apis/template/test-case-template"
import { i18n } from "/@/i18n"
import { DataTableColumns, NButton, NInput } from "naive-ui"

interface IPorps {
  form: ITestCaseTemplate
  readOnly?: boolean
}
const props = withDefaults(defineProps<IPorps>(), {
  readOnly: false,
})
const emits = defineEmits(["update:form"])
const plus = computed({
  get: () => props.form,
  set: (val) => {
    emits("update:form", val)
  },
})
const ShowOrEdit = defineComponent({
  props: {
    value: [String, Number],
    onUpdateValue: [Function, Array],
  },
  setup(props) {
    const isEdit = ref(false)
    const inputRef = ref<InstanceType<typeof NInput> | null>(null)
    const inputValue = ref(props.value)
    function handleOnClick() {
      isEdit.value = true
      nextTick(() => {
        inputRef.value?.focus()
      })
    }
    function handleChange() {
      props.onUpdateValue(inputValue.value)
      isEdit.value = false
    }
    return () =>
      h(
        "div",
        {
          style: "min-height: 22px",
          onClick: handleOnClick,
        },
        isEdit.value
          ? h(NInput, {
              ref: inputRef,
              value: inputValue.value,
              onUpdateValue: (v) => {
                inputValue.value = v
              },
              onChange: handleChange,
              onBlur: handleChange,
            })
          : props.value,
      )
  },
})
const columns: DataTableColumns<ITestCaseStep> = [
  {
    title: i18n.t("test_track.case.number"),
    key: "num",
  },
  {
    title: i18n.t("test_track.case.step_desc"),
    key: "desc",
    render(rowData) {
      return h(
        ShowOrEdit,
        {
          value: rowData.desc,
          onUpdateValue(v: string) {
            rowData.desc = v
          },
        },
        {},
      )
    },
  },
  {
    title: i18n.t("test_track.case.expected_results"),
    key: "result",
    render(rowData) {
      return h(
        ShowOrEdit,
        {
          value: rowData.result,
          onUpdateValue(v: string) {
            rowData.result = v
          },
        },
        {},
      )
    },
  },
  {
    title: i18n.t("commons.operating"),
    key: "x",
    render(rowData, rowIndex) {
      return [
        h(
          NButton,
          {
            type: "primary",
            circle: true,
            size: "tiny",
            renderIcon: () => renderIcon("i-tabler:plus"),
            onClick: () => handleAddStep(rowIndex, rowData),
          },
          {},
        ),
        h(
          NButton,
          {
            type: "success",
            circle: true,
            size: "tiny",
            renderIcon: () => renderIcon("i-tabler:copy"),
            onClick: () => handleCopyStep(rowIndex, rowData),
          },
          {},
        ),
        h(
          NButton,
          {
            type: "error",
            circle: true,
            size: "tiny",
            renderIcon: () => renderIcon("i-tabler:trash"),
            onClick: () => handleDeleteStep(rowIndex, rowData),
          },
          {},
        ),
      ]
    },
  },
]
const handleAddStep = (index: number, data: ITestCaseStep) => {
  let step = {} as ITestCaseStep
  step.num = data.num + 1
  step.desc = ""
  step.result = ""
  plus.value.steps.forEach((step) => {
    if (step.num > data.num) {
      step.num++
    }
  })
  plus.value.steps.splice(index + 1, 0, step)
}
const handleCopyStep = (index: number, data: ITestCaseStep) => {
  let step = {} as ITestCaseStep
  step.num = data.num + 1
  step.desc = data.desc
  step.result = data.result
  plus.value.steps.forEach((step) => {
    if (step.num > data.num) {
      step.num++
    }
  })
  plus.value.steps.splice(index + 1, 0, step)
}
const handleDeleteStep = (index: number, data: ITestCaseStep) => {
  plus.value.steps.splice(index, 1)
  plus.value.steps.forEach((step) => {
    if (step.num > data.num) {
      step.num--
    }
  })
}
onMounted(() => {})
</script>
<template>
  <n-form-item prop="steps">
    <n-data-table :columns="columns" :data="plus.steps" size="small" />
  </n-form-item>
</template>

<style scoped></style>
