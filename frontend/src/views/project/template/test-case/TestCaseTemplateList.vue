<script setup lang="ts">
import { h, onMounted, ref } from "vue"

import TestCaseTemplateEdit from "./TestCaseTemplateEdit.vue"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"

import { useAuthStore } from "/@/store/auth-store"

import { ITableDataInfo, PageReq } from "/@/apis/interface"
import { getCaseFieldTemplatePages, ITestCaseTemplate } from "/@/apis/template/test-case-template"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns } from "naive-ui"

const testCaseTemplateEdit = ref<InstanceType<typeof TestCaseTemplateEdit> | null>(null)
const condition = ref<PageReq>({
  name: "",
  ids: [],
  pageNumber: 1,
  pageSize: 10,
})
const caseTypeMap: { [key: string]: string } = {
  functional: i18n.t("api_test.home_page.failed_case_list.table_value.case_type.functional"),
}
const tableInfo = ref<ITableDataInfo<ITestCaseTemplate[]>>({
  data: [],
  total: 0,
})
const rowKey = (row: ITestCaseTemplate) => row.id as unknown as string
const columns: DataTableColumns<ITestCaseTemplate> = [
  {
    type: "selection",
    align: "center",
  },
  {
    title: i18n.t("commons.name"),
    key: "name",
    align: "center",
    render(row) {
      return h(
        "span",
        {},
        {
          default: () => (row.system ? row.name + "(" + i18n.t("custom_field.default_template") + ")" : row.name),
        },
      )
    },
  },
  {
    title: i18n.t("api_test.home_page.failed_case_list.table_coloum.case_type"),
    key: "type",
    align: "center",
    render(row) {
      return h(
        "span",
        {},
        {
          default: () => caseTypeMap[row.type],
        },
      )
    },
  },
  {
    title: i18n.t("custom_field.system_template"),
    key: "system",
    align: "center",
    render(row) {
      return h(
        "span",
        {},
        {
          default: () => (row.system ? i18n.t("commons.yes") : i18n.t("commons.no")),
        },
      )
    },
  },
  {
    title: i18n.t("commons.description"),
    key: "description",
    align: "center",
  },
  {
    title: i18n.t("commons.create_time"),
    key: "createTime",
    align: "center",
  },
  {
    title: i18n.t("commons.update_time"),
    key: "updateTime",
    align: "center",
  },
  {
    title: i18n.t("commons.operating"),
    key: "operator",
    width: 200,
    fixed: "right",
    align: "center",
    render(rowData) {
      return h(
        GaTableOperation,
        {
          // onEditClick: () => handleEdit(rowData),
          // onDeleteClick: () => handleDelete(rowData),
        },
        {},
      )
    },
  },
]
const { loading, send } = useRequest(getCaseFieldTemplatePages(condition.value), {
  immediate: false,
})
const handleCreate = () => {
  testCaseTemplateEdit.value?.open()
}
onMounted(() => {
  condition.value.projectId = useAuthStore().project_id
  send().then((res) => {
    tableInfo.value.data = res.records
    tableInfo.value.total = res.totalRow
  })
})
</script>
<template>
  <base-card>
    <template #header>
      <base-search :condition="condition" :create-tip="$t('custom_field.template_create')" @create="handleCreate" />
    </template>
    <template #content>
      <n-skeleton v-if="loading" :sharp="false" height="550px" />
      <n-data-table v-else :columns="columns" :data="tableInfo.data" :row-key="rowKey" />
    </template>
  </base-card>
  <test-case-template-edit ref="testCaseTemplateEdit" />
</template>
<style scoped></style>
