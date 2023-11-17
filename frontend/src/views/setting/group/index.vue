<script setup lang="ts">
import { h, onMounted, ref } from "vue"

import EditUserGroup from "./components/EditUserGroup.vue"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"

import { IGroupDTO, loadTableData } from "/@/apis/group"
import { ITableDataInfo, PageReq } from "/@/apis/interface"
import i18n from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns, NDataTable, NSkeleton } from "naive-ui"

const editUserGroup = ref<InstanceType<typeof EditUserGroup> | null>(null)
const columns: DataTableColumns<IGroupDTO> = [
  {
    type: "selection",
    align: "center",
  },
  {
    title: i18n.global.t("commons.name"),
    key: "name",
    align: "center",
  },
  {
    title: i18n.global.t("commons.member"),
    key: "memberSize",
  },
  {
    title: i18n.global.t("group.scope"),
    key: "scopeName",
  },
  {
    title: i18n.global.t("commons.create_time"),
    key: "createTime",
    align: "center",
  },
  {
    title: i18n.global.t("commons.operating"),
    key: "operator",
    width: 200,
    fixed: "right",
    align: "center",
    render(rowData) {
      return h(GaTableOperation, { onEditClick: () => handleEdit(rowData) }, {})
    },
  },
]
const condition = ref<PageReq>({
  name: "",
  ids: [],
  pageNumber: 1,
  pageSize: 10,
})
const tableInfo = ref<ITableDataInfo<IGroupDTO[]>>({
  data: [],
  total: 0,
})
const rowKey = (row: IGroupDTO) => row.id as unknown as string
const { loading, send, onSuccess } = useRequest(
  loadTableData(condition.value),
  {
    immediate: false,
  },
)
onSuccess((resp) => {
  loading.value = false
  tableInfo.value.data = resp.data.records
})
const handleAdd = () => {
  editUserGroup.value?.open("create", i18n.global.t("group.create"))
}
onMounted(() => {
  send()
})
</script>
<template>
  <base-card>
    <template #header>
      <base-search
        :condition="condition"
        :popover-text="$t('group.create')"
        @create="handleAdd" />
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
  <edit-user-group ref="editUserGroup" @refresh="send" />
</template>

<style></style>
