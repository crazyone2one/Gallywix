<script setup lang="ts">
import { ref, onMounted, h } from "vue"
import { NSkeleton, DataTableColumns, NDataTable } from "naive-ui"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"
import OrganizationEdit from "./components/OrganizationEdit.vue"
import { ITableDataInfo, PageReq } from "/@/apis/interface"
import { useRequest } from "alova"
import { ORGANIZATION, loadTableData } from "/@/apis/organization"

const organizationEdit = ref<InstanceType<typeof OrganizationEdit> | null>(null)
const columns: DataTableColumns<ORGANIZATION> = [
  {
    type: "selection",
    align: "center",
  },
  {
    title: "名称",
    key: "name",
    align: "center",
  },
  {
    title: "描述",
    key: "description",
  },

  {
    title: "创建时间",
    key: "createTime",
    align: "center",
  },
  {
    title: "操作",
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
const tableInfo = ref<ITableDataInfo<ORGANIZATION[]>>({
  data: [],
  total: 0,
})
const rowKey = (row: ORGANIZATION) => row.id as unknown as string
const { loading, send, onSuccess } = useRequest(loadTableData(condition.value), {
  immediate: false,
})
onSuccess((resp) => {
  loading.value = false
  tableInfo.value.data = resp.data.records
})
const handleAdd = () => {
  organizationEdit.value?.open()
}
const handleEdit = (rowData: ORGANIZATION) => {
  organizationEdit.value?.open(rowData)
}

onMounted(() => {
  send()
})
</script>
<template>
  <base-card>
    <template #header>
      <base-search :condition="condition" popover-text="添加用户" @create="handleAdd"></base-search>
    </template>
    <template #content>
      <n-skeleton v-if="loading" :sharp="false" height="550px"> </n-skeleton>
      <n-data-table v-else :columns="columns" :data="tableInfo.data" :row-key="rowKey" />
    </template>
  </base-card>
  <organization-edit ref="organizationEdit" @refresh="send()" />
</template>

<style></style>
