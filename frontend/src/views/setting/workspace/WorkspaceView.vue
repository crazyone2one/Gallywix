<script setup lang="ts">
import { onMounted,ref } from "vue"

import WorkspaceEdit from "./components/WorkspaceEdit.vue"
import BaseCard from "/@/components/BaseCard.vue"

import { ITableDataInfo, PageReq } from "/@/apis/interface"
import { loadWorkspaceData,WORKSPACE } from "/@/apis/workspace"
import { useRequest } from "alova"
import { DataTableColumns, NButton, NDataTable,NSkeleton } from "naive-ui"

const workspaceEdit = ref<InstanceType<typeof WorkspaceEdit> | null>(null)
const columns: DataTableColumns<WORKSPACE> = [
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
    align: "center",
  },
]
const condition = ref<PageReq>({
  name: "",
  ids: [],
  pageNumber: 1,
  pageSize: 10,
})
const tableInfo = ref<ITableDataInfo<WORKSPACE[]>>({
  data: [],
  total: 0,
})
const rowKey = (row: WORKSPACE) => row.id as unknown as string
const handleAdd = () => {
  workspaceEdit.value?.open()
}
const { loading, send, onSuccess } = useRequest(
  loadWorkspaceData(condition.value),
  {
    immediate: false,
  },
)
onMounted(() => {
  send()
})
onSuccess((resp) => {
  loading.value = false
  tableInfo.value.data = resp.data.records
})
</script>
<template>
  <base-card>
    <template #header>
      <n-button @click="handleAdd">
        workspace
      </n-button>
    </template>
    <template #content>
      <n-skeleton v-if="loading" :sharp="false" height="550px" />
      <n-data-table
        v-else
        :columns="columns"
        :data="tableInfo.data"
        :row-key="rowKey"
      />
    </template>
  </base-card>
  <workspace-edit ref="workspaceEdit" />
</template>

<style></style>
