<script setup lang="ts">
import { h, ref } from "vue"

import AddMember from "./AddMember.vue"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import ModalDialog from "/@/components/ModalDialog.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"

import { ITableDataInfo, PageReq } from "/@/apis/interface"
import { getWorkspaceMemberListSpecial, USER } from "/@/apis/user"
import { IWorkspace } from "/@/apis/workspace"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns, NDataTable } from "naive-ui"
const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const addMember = ref<InstanceType<typeof AddMember> | null>(null)
const condition = ref<PageReq>({
  name: "",
  workspaceId: "",
  pageNumber: 1,
  pageSize: 10,
})
const columns: DataTableColumns<USER> = [
  {
    type: "selection",
    align: "center",
  },
  {
    title: i18n.t("commons.username"),
    key: "username",
    align: "center",
  },
  {
    title: i18n.t("commons.email"),
    key: "email",
    align: "center",
  },
  {
    title: i18n.t("commons.phone"),
    key: "phone",
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
          deleteTip: i18n.t("commons.remove"),
          onEditClick: () => handleEditMember(rowData),
          onDeleteClick: () => handleDeleteMember(rowData),
        },
        {},
      )
    },
  },
]
const tableInfo = ref<ITableDataInfo<USER[]>>({
  data: [],
  total: 0,
})
const rowKey = (row: USER) => row.id as unknown as string
const { send: loadWsMember, onSuccess: loadWsMemberSucccess } = useRequest(
  (val) => getWorkspaceMemberListSpecial(val),
  {
    immediate: false,
  },
)
const open = (val: IWorkspace) => {
  modalDialog.value?.showModal()
  condition.value.workspaceId = val.id
  loadWsMember(condition.value)
}
loadWsMemberSucccess((resp) => {
  tableInfo.value.data = resp.data.records
  tableInfo.value.total = resp.data.totalRow
})
const handleEditMember = (val: USER) => {
  console.log(val)
}
const handleDeleteMember = (val: USER) => {
  console.log(val)
}
const handleOpenAdd = () => {
  addMember.value?.open()
}
defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog" modal-width="60%">
    <template #content>
      <base-card>
        <template #header>
          <base-search
            :condition="condition"
            :popover-text="$t('member.create')"
            @create="handleOpenAdd" />
        </template>
        <template #content>
          <n-data-table
            :columns="columns"
            :data="tableInfo.data"
            :row-key="rowKey" />
        </template>
      </base-card>
    </template>
  </modal-dialog>
  <add-member ref="addMember" />
</template>

<style></style>
