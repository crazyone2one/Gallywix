<script setup lang="ts">
import { computed, h, onMounted, ref } from "vue"

import EditPermission from "./components/EditPermission.vue"
import EditUserGroup from "./components/EditUserGroup.vue"
import GroupMember from "./components/GroupMember.vue"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"
import GaTableOperatorButton from "/@/components/table/GaTableOperatorButton.vue"

import { SUPER_GROUP } from "/@/utils/constants"
import { USER_GROUP_SCOPE } from "/@/utils/table-constants"

import { IGroupDTO, loadTableData } from "/@/apis/group"
import { ITableDataInfo, PageReq } from "/@/apis/interface"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns, NButton, NDataTable, NSkeleton } from "naive-ui"

const editUserGroup = ref<InstanceType<typeof EditUserGroup> | null>(null)
const editPermission = ref<InstanceType<typeof EditPermission> | null>(null)
const groupMember = ref<InstanceType<typeof GroupMember> | null>(null)
const userGroupType = computed(() => {
  return USER_GROUP_SCOPE
})
const columns: DataTableColumns<IGroupDTO> = [
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
    title: i18n.t("group.type"),
    key: "type",
    align: "center",
    render(row) {
      return h(
        "span",
        {},
        {
          default: () => (userGroupType.value[row.type] ? i18n.t(userGroupType.value[row.type]) : row.type),
        },
      )
    },
  },
  {
    title: i18n.t("commons.member"),
    key: "memberSize",
    align: "center",
    render(rowData) {
      return h(
        NButton,
        { text: true, type: "primary", onClick: () => memberClick(rowData) },
        {
          default: () => rowData.memberSize,
        },
      )
    },
  },
  {
    title: i18n.t("group.scope"),
    key: "scopeName",
    align: "center",
    render(row) {
      return h(
        "span",
        {},
        {
          default: () => {
            if (row.scopeId === "global") {
              return i18n.t("group.global")
            } else if (row.scopeId === "system") {
              return i18n.t("group.system")
            } else {
              return row.scopeName
            }
          },
        },
      )
    },
  },
  {
    title: i18n.t("group.description"),
    key: "description",
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
          isShow: rowData.code === SUPER_GROUP,
          onEditClick: () => handleEdit(rowData),
          editPermission: ["SYSTEM_GROUP:READ+EDIT"],
          deletePermission: ["SYSTEM_GROUP:READ+DELETE"],
        },
        {
          middle: () => {
            return h(
              GaTableOperatorButton,
              {
                tip: i18n.t("group.set_permission"),
                icon: "i-tabler:settings",
                permission: ["SYSTEM_GROUP:READ+SETTING_PERMISSION"],
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
const tableInfo = ref<ITableDataInfo<IGroupDTO[]>>({
  data: [],
  total: 0,
})
const rowKey = (row: IGroupDTO) => row.id as unknown as string
const { loading, send, onSuccess } = useRequest(loadTableData(condition.value), {
  immediate: false,
})
onSuccess((resp) => {
  loading.value = false
  tableInfo.value.data = resp.data.records
})
const handleAdd = () => {
  editUserGroup.value?.open("create", i18n.t("group.create"))
}
const handleEdit = (val: IGroupDTO) => {
  if (val.code === "admin") {
    window.$message.warning(i18n.t("group.admin_not_allow_edit"))
    return
  }
  editUserGroup.value?.open("edit", i18n.t("group.edit"), val)
}
const setPermission = (val: IGroupDTO) => {
  editPermission.value?.open(val)
}

const memberClick = (val: IGroupDTO) => {
  groupMember.value?.openModal(val)
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
        :create-tip="$t('group.create')"
        :create-permission="['SYSTEM_GROUP:READ+CREATE']"
        @create="handleAdd" />
    </template>
    <template #content>
      <n-skeleton v-if="loading" :sharp="false" height="550px" />
      <n-data-table v-else :columns="columns" :data="tableInfo.data" :row-key="rowKey" />
    </template>
  </base-card>
  <edit-user-group ref="editUserGroup" @refresh="send" />
  <edit-permission ref="editPermission" />
  <group-member ref="groupMember" @refresh="send" />
</template>

<style></style>
