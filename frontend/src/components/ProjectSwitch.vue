<script setup lang="ts">
import { ref } from "vue"

import { NPopselect } from "naive-ui"

const value = ref("")
const currentProject = ref("xxxx")
const items = ref([])
</script>
<template>
  <n-popselect v-model:value="value" :options="items" trigger="click">
    <n-tooltip trigger="hover">
      <template #trigger>
        <span class="project-name">
          {{ $t("commons.project") }}: {{ currentProject }}
        </span>
      </template>
      {{ currentProject }}
    </n-tooltip>
    <template #empty>
      {{ $t("project.no_data") }}
    </template>
    <template #action>
      <n-space vertical class="ml-2">
        <n-input
          v-model:value="value"
          type="text"
          :placeholder="$t('project.search_by_name')"
          size="small" />
        <n-space>
          <n-button size="tiny" disabled>
            <template #icon>
              <n-icon>
                <span class="i-tabler:plus" />
              </n-icon>
            </template>
            {{ $t("project.create") }}
          </n-button>
          <n-button size="tiny" disabled>
            <template #icon>
              <n-icon>
                <span class="i-tabler:list" />
              </n-icon>
            </template>
            {{ $t("commons.show_all") }}
          </n-button>
        </n-space>
      </n-space>
    </template>
  </n-popselect>
</template>

<style scoped>
.project-name {
  display: inline-block;
  width: 160px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
