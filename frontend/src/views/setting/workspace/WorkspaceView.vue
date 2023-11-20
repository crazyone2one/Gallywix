<script setup lang="ts">
import { h, onMounted, ref } from "vue"

import WorkspaceEdit from "./components/WorkspaceEdit.vue"
import WorkspaceMember from "./components/WorkspaceMember.vue"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaDeleteConfirm from "/@/components/GaDeleteConfirm.vue"
import GaPagination from "/@/components/table/GaPagination.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"

import { ITableDataInfo, PageReq } from "/@/apis/interface"
import { getWorkspaceMemberListSpecial } from "/@/apis/user"
import {
  delWorkspaceSpecial,
  loadWorkspaceData,
  WORKSPACE,
} from "/@/apis/workspace"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns, NButton, NDataTable, NSkeleton } from "naive-ui"

const workspaceEdit = ref<InstanceType<typeof WorkspaceEdit> | null>(null)
const deleteConfirm = ref<InstanceType<typeof GaDeleteConfirm> | null>(null)
const workspaceMember = ref<InstanceType<typeof WorkspaceMember> | null>(null)
const columns: DataTableColumns<WORKSPACE> = [
  {
    type: "selection",
    align: "center",
  },
  {
    title: i18n.t("commons.name"),
    key: "name",
    align: "center",
  },
  {
    title: i18n.t("commons.description"),
    key: "description",
    align: "center",
  },
  {
    title: i18n.t("commons.member"),
    key: "memberSize",
    align: "center",
    render(rowData) {
      return h(
        NButton,
        { text: true, type: "primary", onClick: () => cellClick(rowData) },
        {
          default: () => rowData.memberSize,
        },
      )
    },
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
const wsMemberCondition = ref<PageReq>({
  name: "",
  workspaceId: "",
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
// 删除功能
const handleDelete = (val: WORKSPACE) => {
  deleteConfirm.value?.open(val)
}
const _deleteWorkspace = (val: WORKSPACE) => {
  window.$dialog.warning({
    title: "",
    maskClosable: false,
    content: i18n.t("workspace.delete_confirm"),
    positiveText: i18n.t("commons.confirm"),
    negativeText: i18n.t("commons.cancel"),
    onPositiveClick: () => {
      deleteWs(val.id)
    },
    onNegativeClick: () => {
      window.$message.info(i18n.t("commons.delete_cancelled"))
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
// 单击成员事件
const { send: loadWsMember, onSuccess: loadWsMemberSucccess } = useRequest(
  (val) => getWorkspaceMemberListSpecial(val),
  {
    immediate: false,
  },
)
const cellClick = (val: WORKSPACE) => {
  wsMemberCondition.value.workspaceId = val.id
  workspaceMember.value?.open(val)
  loadWsMember(wsMemberCondition.value)
}
loadWsMemberSucccess((resp) => {
  console.log(resp)
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
      <ga-pagination :condition="condition" />
    </template>
  </base-card>
  <workspace-edit ref="workspaceEdit" @refresh="send()" />
  <ga-delete-confirm ref="deleteConfirm" @delete="_deleteWorkspace" />
  <workspace-member ref="workspaceMember" />
</template>

<style></style>
