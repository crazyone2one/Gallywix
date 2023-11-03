<script setup lang="ts">
import { NResult, NButton } from "naive-ui"
import BaseCard from "/@/components/BaseCard.vue"
import WorkspaceEdit from "./components/WorkspaceEdit.vue"
import { ref } from "vue"

const workspaceEdit = ref<InstanceType<typeof WorkspaceEdit> | null>(null)
const handleAdd = () => {
  workspaceEdit.value?.open()
}
</script>
<template>
  <base-card>
    <template #header>
      <n-button @click="handleAdd">workspace</n-button>
    </template>
    <template #content>
      <n-result
        status="403"
        title="403 禁止访问"
        description="总有些门是对你关闭的">
        <template #footer>
          <n-button>workspace</n-button>
        </template>
      </n-result>
    </template>
  </base-card>
  <workspace-edit ref="workspaceEdit" />
</template>

<style></style>
