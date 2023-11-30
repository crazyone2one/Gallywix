<script setup lang="ts">
import { ref } from "vue"

import TestCaseTemplateEdit from "./TestCaseTemplateEdit.vue"

const testCaseTemplateEdit = ref<InstanceType<typeof TestCaseTemplateEdit> | null>(null)
const handleCreate = () => {
  testCaseTemplateEdit.value?.open()
}
</script>
<template>
  <div>
    <n-button @click="handleCreate">x</n-button>
  </div>
  <test-case-template-edit ref="testCaseTemplateEdit" />
</template>
<style scoped></style>
