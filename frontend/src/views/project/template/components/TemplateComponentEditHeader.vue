<script setup lang="ts">
import { ITestCaseTemplate } from "/@/apis/template/test-case-template"

interface IProps {
  template?: ITestCaseTemplate
  prop?: string
  showEdit?: boolean
}
withDefaults(defineProps<IProps>(), {
  template: () => ({}) as ITestCaseTemplate,
  prop: "name",
  showEdit: true,
})
const emits = defineEmits(["handle:cancel", "handle:save"])
const handleCancel = () => emits("handle:cancel")
const handleSave = () => emits("handle:save")
</script>
<template>
  <n-space justify="end" class="head-bar">
    <span class="title">{{ template[prop] }} </span>
    <span>
      <n-button secondary size="small" @click="handleCancel">{{ $t("test_track.return") }}</n-button>
      <n-button type="primary" size="small" @click="handleSave">{{ $t("test_track.save") }}</n-button>
    </span>
  </n-space>
</template>

<style scoped>
.head-bar {
  background: white;
  height: 40px;
  line-height: 40px;
  padding: 0 10px;
  /* border: 1px solid #ebeef5; */
  box-shadow:
    0 0 2px 0 rgba(31, 31, 31, 0.15),
    0 1px 2px 0 rgba(31, 31, 31, 0.15);
}
</style>
