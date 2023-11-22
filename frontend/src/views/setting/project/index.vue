<script setup lang="ts">
import { h, onMounted, ref } from "vue"
import { useRoute, useRouter } from "vue-router"

import ProjectEdit from "./components/ProjectEdit.vue"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaDeleteConfirm from "/@/components/GaDeleteConfirm.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"

import { useAuthStore } from "/@/store/auth-store"

import { ITableDataInfo, PageReq } from "/@/apis/interface"
import { deleteData, loadData, PROJECT } from "/@/apis/project"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns, NDataTable, NSkeleton } from "naive-ui"

const route = useRoute()
const router = useRouter()
const projectEdit = ref<InstanceType<typeof ProjectEdit> | null>(null)
const deleteConfirm = ref<InstanceType<typeof GaDeleteConfirm> | null>(null)
const columns: DataTableColumns<PROJECT> = [
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
  },
  {
    title: "创建时间",
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
const tableInfo = ref<ITableDataInfo<PROJECT[]>>({
  data: [],
  total: 0,
})
const rowKey = (row: PROJECT) => row.id as unknown as string
const { loading, send, onSuccess } = useRequest(loadData(condition.value), {
  immediate: false,
})
onSuccess((resp) => {
  loading.value = false
  tableInfo.value.data = resp.data.records
})
const handleAdd = () => {
  const workspaceId = useAuthStore().workspace_id
  if (!workspaceId) {
    window.$message.warning(i18n.t("project.please_choose_workspace"))
    return false
  }
  projectEdit.value?.open()
}
const handleEdit = (rowData: PROJECT) => {
  projectEdit.value?.open(rowData)
}

const { send: deleteProject, onSuccess: deleteSuccess } = useRequest(
  (wsId) => deleteData(wsId),
  {
    immediate: false,
  },
)
const handleDelete = (rowData: PROJECT) => {
  deleteConfirm.value?.open(rowData)
}
const _handleDelete = (val: PROJECT) => {
  window.$dialog.warning({
    title: "",
    maskClosable: false,
    content: i18n.t("project.delete_tip"),
    positiveText: i18n.t("commons.confirm"),
    negativeText: i18n.t("commons.cancel"),
    onPositiveClick: () => {
      deleteProject(val.id)
    },
  })
}
deleteSuccess(() => {
  send()
  // todo 删除项目后更新用户信息，若删除的是当前用户的项目，则更新当前用户的lastproject 为空
})
onMounted(() => {
  send()
  if (route.path.split("/")[2] === "create") {
    projectEdit.value?.open()
    router.push("/project/list")
  }
})
</script>
<template>
  <base-card>
    <template #header>
      <base-search
        :condition="condition"
        :popover-text="$t('project.create')"
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
  <project-edit ref="projectEdit" @refresh="send()" />
  <ga-delete-confirm ref="deleteConfirm" @delete="_handleDelete" />
</template>

<style></style>
