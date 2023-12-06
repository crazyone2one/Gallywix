<script setup lang="ts">
import { computed, onMounted, ref } from "vue"

import WorkspaceEdit from "./components/WorkspaceEdit.vue"
import GaTableHeader from "/@/components/table/GaTableHeader.vue"
import { usePagination } from "@alova/scene-vue"

import { hasPermission } from "/@/utils/permission"

import { PageReq } from "/@/apis/interface"
import { IWorkspace, loadWorkspaceData } from "/@/apis/workspace"
import { ElCard, ElLink, ElTable, ElTableColumn } from "element-plus"

const condition = ref<PageReq>({
  name: "",
  ids: [],
  pageNumber: 1,
  pageSize: 10,
})
const workspaceEdit = ref<InstanceType<typeof WorkspaceEdit> | null>(null)
const { send, data: workspaces } = usePagination(
  (page, pageSize) => loadWorkspaceData(page, pageSize, condition.value),
  {
    immediate: false,
    initialData: { total: 0, data: [] },
    initialPage: 1, // 初始页码，默认为1
    initialPageSize: 10, // 初始每页数据条数，默认为10
    // watchingStates: [condition.value],
    // debounce: [800],
    preloadPreviousPage: true, // 预加载上一页数据
    preloadNextPage: true, // 预加载下一页数据
    total: (response) => response.totalRow,
    data: (response) => response.records,
  },
)
const disabledEditWorkspaceMember = computed(() => {
  return !hasPermission("SYSTEM_WORKSPACE:READ+EDIT")
})
const cellClick = (val: IWorkspace) => {
  workspaceEdit.value?.open(val)
}
const create = () => {
  workspaceEdit.value?.open()
}
onMounted(() => {
  send()
})
</script>
<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <ga-table-header
          :condition="condition"
          :create-permission="['SYSTEM_WORKSPACE:READ+CREATE']"
          :create-tip="$t('workspace.create')"
          :title="$t('commons.workspace')"
          @create="create" />
      </div>
    </template>
    <el-table :data="workspaces" style="width: 100%">
      <el-table-column show-overflow-tooltip prop="name" :label="$t('commons.name')" min-width="100" />
      <el-table-column show-overflow-tooltip prop="description" :label="$t('commons.description')" min-width="100" />
      <el-table-column :label="$t('commons.member')" width="150">
        <template #default="scope">
          <el-link
            type="primary"
            class="member-size"
            :disabled="disabledEditWorkspaceMember"
            @click="cellClick(scope.row)">
            {{ scope.row.memberSize }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.operating')" min-width="100"></el-table-column>
    </el-table>
  </el-card>
  <workspace-edit ref="workspaceEdit" @refresh="send" />
</template>

<style scoped></style>
