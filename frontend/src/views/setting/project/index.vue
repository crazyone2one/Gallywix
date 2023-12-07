<script setup lang="ts">
import { h, onMounted, ref } from "vue"
import { useRoute, useRouter } from "vue-router"

import ProjectEdit from "./components/ProjectEdit.vue"
import ProjectMember from "./components/ProjectMember.vue"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaDeleteConfirm from "/@/components/GaDeleteConfirm.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"

import { useUserStore } from "/@/store/modules/user-store"

import { ITableDataInfo, PageReq } from "/@/apis/interface"
import { deleteData, IProject, loadData } from "/@/apis/project"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns, NButton, NDataTable, NSkeleton } from "naive-ui"

const route = useRoute()
const router = useRouter()
const projectEdit = ref<InstanceType<typeof ProjectEdit> | null>(null)
const projectMember = ref<InstanceType<typeof ProjectMember> | null>(null)
const deleteConfirm = ref<InstanceType<typeof GaDeleteConfirm> | null>(null)
const columns: DataTableColumns<IProject> = [
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
  workspaceId: "",
})
const tableInfo = ref<ITableDataInfo<IProject[]>>({
  data: [],
  total: 0,
})
const rowKey = (row: IProject) => row.id as unknown as string
const { loading, send, onSuccess } = useRequest(loadData(condition.value), {
  immediate: false,
})
onSuccess((resp) => {
  loading.value = false
  tableInfo.value.data = resp.data.records
})
const handleAdd = () => {
  const workspaceId = useUserStore().workspace_id
  if (!workspaceId) {
    window.$message.warning(i18n.t("project.please_choose_workspace"))
    return false
  }
  projectEdit.value?.open()
}
const handleEdit = (rowData: IProject) => {
  projectEdit.value?.open(rowData)
}

const { send: deleteProject, onSuccess: deleteSuccess } = useRequest((wsId) => deleteData(wsId), {
  immediate: false,
})
const handleDelete = (rowData: IProject) => {
  deleteConfirm.value?.open(rowData)
}
const _handleDelete = (val: IProject) => {
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

const cellClick = (val: IProject) => {
  projectMember.value?.open(val)
}
onMounted(() => {
  condition.value.workspaceId = useUserStore().workspace_id
  send()
  const tmpPath = route.path.split("/")[2]
  if (tmpPath === "create") {
    projectEdit.value?.open()
    router.push("/project/list")
  } else {
    router.push("/project/list")
  }
})
</script>
<template>
  <base-card>
    <template #header>
      <base-search
        :condition="condition"
        :create-tip="$t('project.create')"
        :create-permission="['WORKSPACE_PROJECT_MANAGER:READ+CREATE']"
        @create="handleAdd" />
    </template>
    <template #content>
      <n-skeleton v-if="loading" :sharp="false" height="550px" />
      <n-data-table v-else :columns="columns" :data="tableInfo.data" :row-key="rowKey" />
    </template>
  </base-card>
  <project-edit ref="projectEdit" @refresh="send()" />
  <project-member ref="projectMember" />
  <ga-delete-confirm ref="deleteConfirm" @delete="_handleDelete" />
</template>

<style></style>
