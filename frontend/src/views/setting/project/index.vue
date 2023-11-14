<script setup lang="ts">
import { ref, onMounted, h } from "vue"
import { NSkeleton, DataTableColumns, NDataTable } from "naive-ui"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import ProjectEdit from "./components/ProjectEdit.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"
import { ITableDataInfo, PageReq } from "/@/apis/interface"
import { useRequest } from "alova"
import { PROJECT, loadData } from "/@/apis/project"
import { useRoute, useRouter } from "vue-router"

const route = useRoute()
const router = useRouter()
const projectEdit = ref<InstanceType<typeof ProjectEdit> | null>(null)
const columns: DataTableColumns<PROJECT> = [
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
    title: "创建时间",
    key: "createTime",
    align: "center",
  },
  {
    title: "操作",
    key: "operator",
    width: 200,
    fixed: "right",
    align: "center",
    render(rowData) {
      return h(GaTableOperation, { onEditClick: () => handleEdit(rowData) }, {})
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
  projectEdit.value?.open()
}
const handleEdit = (rowData: PROJECT) => {
  projectEdit.value?.open(rowData)
}

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
      <base-search :condition="condition" popover-text="添加项目" @create="handleAdd"></base-search>
    </template>
    <template #content>
      <n-skeleton v-if="loading" :sharp="false" height="550px"> </n-skeleton>
      <n-data-table v-else :columns="columns" :data="tableInfo.data" :row-key="rowKey" />
    </template>
  </base-card>
  <project-edit ref="projectEdit" @refresh="send()" />
</template>

<style></style>
