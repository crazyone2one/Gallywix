<script setup lang="ts">
import { computed, h } from "vue"

import GaCustomFiledComponent from "../GaCustomFiledComponent.vue"

import { SYSTEM_FIELD_NAME_MAP } from "/@/utils/table-constants"

import { ICustomFieldTemp } from "/@/apis/template/template"
import { ITestCaseTemplate } from "/@/apis/template/test-case-template"
import { i18n } from "/@/i18n"
import { DataTableColumns, NCheckbox } from "naive-ui"

interface IProps {
  tableData?: Array<ICustomFieldTemp>
  scene: string
  platform?: string
  templateContainIds: Set<string> | undefined
  form?: ITestCaseTemplate
}
const props = withDefaults(defineProps<IProps>(), {
  tableData: () => [],
  platform: "",
  form: () => {
    return {} as ITestCaseTemplate
  },
})
const columns: DataTableColumns<ICustomFieldTemp> = [
  {
    title: i18n.t("commons.name"),
    key: "name",
    render(rowData) {
      return h("span", null, {
        default: () => (rowData.system ? i18n.t(systemNameMap.value[rowData.name]) : rowData.name),
      })
    },
  },
  {
    title: i18n.t("commons.default"),
    key: "type",
    align: "center",
    width: 200,
    render(rowData) {
      return h(
        GaCustomFiledComponent,
        { data: rowData, prop: "defaultValue", isTemplateEdit: true, form: props.form },
        {},
      )
    },
  },
  {
    title: i18n.t("api_test.definition.document.table_coloum.is_required"),
    key: "type",
    align: "center",
    render(rowData) {
      return h(NCheckbox, { disabled: rowData.disabled, checked: rowData.required }, {})
    },
  },
  {
    title: i18n.t("custom_field.system_field"),
    key: "system",
    width: 80,
    align: "center",
    render(rowData) {
      return h("span", null, {
        default: () => (rowData.system ? i18n.t("commons.yes") : i18n.t("commons.no")),
      })
    },
  },
  {
    title: i18n.t("commons.remark"),
    key: "remark",
    width: 80,
  },
]
const systemNameMap = computed(() => {
  return SYSTEM_FIELD_NAME_MAP
})
</script>
<template>
  <n-data-table :columns="columns" :data="tableData" />
</template>
<style scoped></style>
