<script setup lang="ts">
import { computed, h, ref } from "vue"

import GroupPermission from "./GroupPermission.vue"
import ModalDialog from "/@/components/ModalDialog.vue"

import { GROUP_TYPE, SUPER_GROUP } from "/@/utils/constants"
import { USER_GROUP_SCOPE } from "/@/utils/table-constants"

import { IGroupDTO } from "/@/apis/group"
import { getUserGroupPermission, IGroupResourceDTO } from "/@/apis/permission"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns, NCheckbox } from "naive-ui"

const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)

const title = ref<string>(i18n.t("group.set_permission"))
const readOnly = ref<boolean>(false)
const group = ref<IGroupDTO>({} as IGroupDTO)
const userGroupType = computed(() => {
  return USER_GROUP_SCOPE
})
const isReadOnly = (data: IGroupResourceDTO) => {
  if (group.value.id === SUPER_GROUP) {
    return true
  }
  const isDefaultSystemGroup =
    group.value.id === "admin" && data.resource.id === "SYSTEM_GROUP"
  return readOnly.value || isDefaultSystemGroup
}
const columns: DataTableColumns<IGroupResourceDTO> = [
  {
    type: "selection",
    align: "center",
  },
  {
    title: i18n.t("permission.common.first_level_menu"),
    key: "type",
    align: "center",
    render(rowData) {
      return h(
        "span",
        {},
        {
          default: () => {
            if (rowData.type !== GROUP_TYPE.PROJECT) {
              return userGroupType.value[rowData.type]
                ? i18n.t(userGroupType.value[rowData.type])
                : i18n.t(rowData.type)
            } else {
              return "2"
            }
          },
        },
      )
    },
  },
  {
    title: i18n.t("permission.common.second_level_menu"),
    key: "resource",
    align: "center",
    render(rowData) {
      return h("span", {}, i18n.t(rowData.resource.name))
    },
  },

  {
    title: i18n.t("group.permission"),
    key: "permissions",
    render(row) {
      return h(
        GroupPermission,
        {
          permissions: row.permissions,
          readOnly: readOnly.value,
          group: group.value,
        },
        {},
      )
    },
  },
  {
    title: i18n.t("group.check_all"),
    key: "check",
    align: "center",
    render(rowData) {
      return h(NCheckbox, { disabled: isReadOnly(rowData) }, {})
    },
  },
]
const tableInfo = ref<IGroupResourceDTO[]>()
const rowKey = (row: IGroupResourceDTO) => row.resource.name

const { send: loadPersission, onSuccess: loadPersissionSuccess } = useRequest(
  (val) => getUserGroupPermission(val),
  {
    immediate: false,
  },
)
loadPersissionSuccess((resp) => {
  tableInfo.value = resp.data.permissions
})
const handleSave = () => {}
const open = (row: IGroupDTO, _readOnly?: boolean, _title?: string) => {
  readOnly.value = _readOnly || false
  title.value = _title || i18n.t("group.set_permission")
  group.value = Object.assign({}, row)
  modalDialog.value?.showModal()
  loadPersission(group.value)
}
defineExpose({ open })
</script>
<template>
  <modal-dialog
    ref="modalDialog"
    :title="title"
    modal-width="85%"
    @confirm="handleSave">
    <template #content>
      <n-data-table :columns="columns" :data="tableInfo" :row-key="rowKey" />
    </template>
  </modal-dialog>
</template>

<style scoped></style>
