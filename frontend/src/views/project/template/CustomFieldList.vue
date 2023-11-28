<script setup lang="ts">
import { h, onMounted, ref } from "vue"

import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"

import { useAuthStore } from "/@/store/auth-store"

import { ITableDataInfo, PageReq } from "/@/apis/interface"
import {
  getCustomFieldPages,
  ICustomField,
} from "/@/apis/template/custom-field"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns } from "naive-ui"
const condition = ref<PageReq>({
  name: "",
  ids: [],
  pageNumber: 1,
  pageSize: 10,
})
const tableInfo = ref<ITableDataInfo<ICustomField[]>>({
  data: [],
  total: 0,
})
const rowKey = (row: ICustomField) => row.id as unknown as string
const columns: DataTableColumns<ICustomField> = [
  {
    type: "selection",
    align: "center",
  },
  {
    title: i18n.t("custom_field.field_name"),
    key: "name",
    align: "center",
  },
  {
    title: i18n.t("custom_field.scene"),
    key: "scene",
    align: "center",
  },
  {
    title: i18n.t("custom_field.field_type"),
    key: "type",
    align: "center",
  },
  {
    title: i18n.t("custom_field.system_field"),
    key: "system",
    align: "center",
  },
  {
    title: i18n.t("custom_field.field_remark"),
    key: "remark",
    align: "center",
  },
  {
    title: i18n.t("commons.create_time"),
    key: "createTime",
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
          type: "default",

          onEditClick: () => handleEdit(rowData),
        },
        {},
      )
    },
  },
]
const { loading, send: loadTableData } = useRequest(
  (val) => getCustomFieldPages(val),
  {
    immediate: false,
  },
)
const handleEdit = (rowData: ICustomField) => {
  console.log(`output->`, rowData)
}
onMounted(() => {
  condition.value.projectId = useAuthStore().project_id
  loadTableData(condition.value).then((res) => console.log(`output->res`, res))
})
</script>
<template>
  <base-card>
    <template #header>
      <base-search
        :condition="condition"
        :create-tip="$t('custom_field.create')" />
    </template>
    <template #content>
      <n-skeleton v-if="loading" :sharp="false" height="550px" />
      <n-data-table
        v-else
        :columns="columns"
        :data="tableInfo.data"
        :row-key="rowKey" />
    </template>
  </base-card>
</template>

<style scoped></style>
