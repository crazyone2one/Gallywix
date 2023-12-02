<script setup lang="ts">
import { h, onMounted, ref } from "vue"

import EditUserPassword from "./components/EditUserPassword.vue"
import UserEdit from "./components/UserEdit.vue"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaButton from "/@/components/GaButton.vue"
import GaRolesTag from "/@/components/GaRolesTag.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"

import { ITableDataInfo, PageReq } from "/@/apis/interface"
import { loadUserData, specialGetUserGroup, updateUserData, USER } from "/@/apis/user"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns, NDataTable, NSkeleton, NSwitch } from "naive-ui"

const userEdit = ref<InstanceType<typeof UserEdit> | null>(null)
const editUserPassword = ref<InstanceType<typeof EditUserPassword> | null>(null)
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
    render(row) {
      return h(GaRolesTag, { roles: row.groups ? row.groups : [] }, {})
    },
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
          editPermission: ["SYSTEM_USER:READ+EDIT"],
          deletePermission: ["SYSTEM_USER:READ+DELETE"],
          onEditClick: () => handleEdit(rowData),
        },
        {
          behind: () => {
            return h(
              GaButton,
              {
                isPop: true,
                text: true,
                isIcon: true,
                iconClass: "i-tabler:password-user",
                popText: i18n.t("member.edit_password"),
                permission: ["SYSTEM_USER:READ+EDIT_PASSWORD"],
                onExec: () => editPassword(rowData),
              },
              {},
            )
          },
        },
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
const { loading, send } = useRequest(loadUserData(condition.value), {
  immediate: false,
})
const { send: loadUserGroup } = useRequest((id) => specialGetUserGroup(id), {
  immediate: false,
})
// onSuccess((resp) => {
//   loading.value = false
//   tableInfo.value.data = resp.data.records
// })
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
const editPassword = (val: USER) => {
  editUserPassword.value?.open(val)
}
onMounted(() => {
  send().then((resp) => {
    loading.value = false
    tableInfo.value.data = resp.records
    tableInfo.value.data.forEach((item) => {
      loadUserGroup(item.id).then((result) => {
        let data = result
        let groups = data.groups
        item.groups = groups
      })
    })
  })
})
</script>
<template>
  <base-card>
    <template #header>
      <base-search
        :condition="condition"
        :create-tip="$t('user.create')"
        :create-permission="['SYSTEM_USER:READ+CREATE']"
        @create="handleAdd" />
    </template>
    <template #content>
      <n-skeleton v-if="loading" :sharp="false" height="550px" />
      <n-data-table v-else :columns="columns" :data="tableInfo.data" :row-key="rowKey" />
    </template>
  </base-card>
  <user-edit ref="userEdit" @refresh="send()" />
  <edit-user-password ref="editUserPassword" />
</template>

<style></style>
