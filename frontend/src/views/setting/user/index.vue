<script setup lang="ts">
import { h, onMounted, ref } from "vue"

import UserEdit from "./components/UserEdit.vue"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"

import { ITableDataInfo, PageReq } from "/@/apis/interface"
import { loadUserData, updateUserData, USER } from "/@/apis/user"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns, NDataTable, NSkeleton, NSwitch } from "naive-ui"

const userEdit = ref<InstanceType<typeof UserEdit> | null>(null)
const columns: DataTableColumns<USER> = [
  {
    type: "selection",
    align: "center",
  },
  {
    title: i18n.t("commons.name"),
    key: "username",
    align: "center",
  },
  {
    title: i18n.t("commons.group"),
    key: "x",
  },
  {
    title: i18n.t("commons.email"),
    key: "email",
    align: "center",
  },
  {
    title: "电话",
    key: "phone",
    align: "center",
  },
  {
    title: i18n.t("commons.status"),
    key: "userStatus",
    align: "center",
    render(rowData) {
      return h(
        NSwitch,
        {
          value: rowData.status,
          onUpdateValue: () => handleChangeStatus(rowData),
        },
        {},
      )
    },
  },
  {
    title: i18n.t("commons.create_time"),
    key: "createTime",
    align: "center",
  },
  {
    title: i18n.t("user.source"),
    key: "source",
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
          editTip: i18n.t("commons.edit") + "(有bug，慎用)",
          onEditClick: () => handleEdit(rowData),
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
const tableInfo = ref<ITableDataInfo<USER[]>>({
  data: [],
  total: 0,
})
const rowKey = (row: USER) => row.id as unknown as string
const { loading, send, onSuccess } = useRequest(loadUserData(condition.value), {
  immediate: false,
})
onSuccess((resp) => {
  loading.value = false
  tableInfo.value.data = resp.data.records
})
const handleAdd = () => {
  userEdit.value?.open("Add", i18n.t("user.create"))
}
const handleEdit = (rowData: USER) => {
  userEdit.value?.open("Edit", i18n.t("user.modify"), rowData)
}
/**
 * 更新用户状态
 * @param rowData user
 */
const handleChangeStatus = (rowData: USER) => {
  rowData.status = !rowData.status
  useRequest(updateUserData(rowData))
    .send()
    .then(() => {
      send()
      window.$message.success("update user status success")
    })
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
        :create-tip="$t('user.create')"
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
  <user-edit ref="userEdit" @refresh="send()" />
</template>

<style></style>
