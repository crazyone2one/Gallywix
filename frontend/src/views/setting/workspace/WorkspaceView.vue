<script setup lang="ts">
import { h, onMounted, ref } from "vue"

import WorkspaceEdit from "./components/WorkspaceEdit.vue"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"

import { ITableDataInfo, PageReq } from "/@/apis/interface"
import {
  delWorkspaceSpecial,
  loadWorkspaceData,
  WORKSPACE,
} from "/@/apis/workspace"
import i18n from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns, NDataTable, NSkeleton } from "naive-ui"

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
  {
    title: "操作",
    key: "operator",
    width: 200,
    fixed: "right",
    align: "center",
    render(rowData) {
      return h(
        GaTableOperation,
        {
          onEditClick: () => handleEdit(rowData),
          onDeleteClick: () => handleDelete(rowData),
        },
        {},
      )
    },
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
const handleEdit = (val: WORKSPACE) => {
  window.$message.info(val.name)
}
const { send: deleteWs, onSuccess: deleteWsSuccess } = useRequest(
  (wsId) => delWorkspaceSpecial(wsId),
  {
    immediate: false,
  },
)
const handleDelete = (val: WORKSPACE) => {
  window.$dialog.warning({
    title: "",
    maskClosable: false,
    content: i18n.global.t("workspace.delete_confirm"),
    positiveText: i18n.global.t("commons.confirm"),
    negativeText: i18n.global.t("commons.cancel"),
    onPositiveClick: () => {
      deleteWs(val.id)
    },
    onNegativeClick: () => {
      window.$message.info(i18n.global.t("commons.delete_cancelled"))
    },
  })
}
deleteWsSuccess(() => {
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
      <base-search
        :condition="condition"
        :popover-text="$t('workspace.create')"
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
  <workspace-edit ref="workspaceEdit" @refresh="send()" />
</template>

<style></style>
