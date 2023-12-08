<script setup lang="ts">
import { h, ref } from "vue"

import AddMember from "./AddMember.vue"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaRolesTag from "/@/components/GaRolesTag.vue"
import ModalDialog from "/@/components/ModalDialog.vue"
import GaPagination from "/@/components/table/GaPagination.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"
import { usePagination } from "@alova/scene-vue"

import { GROUP_TYPE } from "/@/utils/constants"

import { IGroupDTO } from "/@/apis/group"
import { PageReq } from "/@/apis/interface"
import { getWorkspaceMemberListSpecial, USER } from "/@/apis/user"
import { IWorkspaceItem } from "/@/apis/workspace"
import { i18n } from "/@/i18n"
import { DataTableColumns, NDataTable } from "naive-ui"
const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const addMember = ref<InstanceType<typeof AddMember> | null>(null)
const groupScopeId = ref("")
const condition = ref<PageReq>({
  name: "",
  workspaceId: "",
  pageNumber: 1,
  pageSize: 8,
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
    title: i18n.t("commons.group"),
    key: "groups",
    render(rowData) {
      return h(GaRolesTag, { roles: rowData.groupList as IGroupDTO[] })
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
          deleteTip: i18n.t("commons.remove"),
          onEditClick: () => handleEditMember(rowData),
          onDeleteClick: () => handleDeleteMember(rowData),
          editPermission: ["WORKSPACE_USER:READ+EDIT"],
          deletePermission: ["WORKSPACE_USER:READ+DELETE"],
        },
        {},
      )
    },
  },
]
const rowKey = (row: USER) => row.id as unknown as string
const {
  // 加载状态
  loading,
  // 列表数据
  data,
  // 是否为最后一页
  // 下拉加载时可通过此参数判断是否还需要加载
  // isLastPage,
  // 当前页码，改变此页码将自动触发请求
  page,
  // 每页数据条数
  pageSize,
  // 总数据量
  total,
  send: loadWsMember,
} = usePagination(
  // Method实例获取函数，它将接收page和pageSize，并返回一个Method实例
  (page, pageSize) => getWorkspaceMemberListSpecial(page, pageSize, condition.value),
  {
    // 请求前的初始数据（接口返回的数据格式）
    initialData: {
      total: 0,
      data: [],
    },
    initialPage: 1, // 初始页码，默认为1
    initialPageSize: 10, // 初始每页数据条数，默认为10
    immediate: false,
    total: (response) => response.totalRow,
    data: (response) => response.records,
  },
)

const open = (val: IWorkspaceItem) => {
  modalDialog.value?.showModal()
  condition.value.workspaceId = val.id
  /**
   * 加载工作空间相关联的用户数据
   */
  loadWsMember()
  groupScopeId.value = val.id as string
}

const handleEditMember = (val: USER) => {
  console.log(val)
}
const handleDeleteMember = (val: USER) => {
  console.log(val)
}
const handleOpenAdd = () => {
  addMember.value?.open()
}
const handleUpdatePage = (_page: number) => (page.value = _page)
const handleUpdatePageSize = (_pageSize: number) => (pageSize.value = _pageSize)
defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog" modal-width="60%" :title="$t('commons.member')" :show-footer="false">
    <template #content>
      <n-spin :show="loading">
        <base-card>
          <template #header>
            <base-search
              :condition="condition"
              :create-tip="$t('member.create')"
              :create-permission="['WORKSPACE_USER:READ+CREATE']"
              @create="handleOpenAdd" />
          </template>
          <template #content>
            <n-data-table :columns="columns" :data="data" :row-key="rowKey" />
            <ga-pagination
              :total="total"
              :page="page"
              :page-size="pageSize"
              @update:page="handleUpdatePage"
              @update:page-size="handleUpdatePageSize" />
          </template>
        </base-card>
      </n-spin>
    </template>
  </modal-dialog>
  <add-member ref="addMember" :group-type="GROUP_TYPE.WORKSPACE" :group-scope-id="groupScopeId" />
</template>

<style></style>
