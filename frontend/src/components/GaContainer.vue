<script setup lang="ts">
import { ElAlert, ElContainer } from "element-plus"
defineProps<{
  isShowWarning: boolean
}>()
</script>
<template>
  <div>
    <el-alert
      v-if="isShowWarning"
      :title="$t('license.count_warning')"
      type="warning"
      center
      :closable="false"
      show-icon>
    </el-alert>
    <el-container class="ms-container">
      <slot></slot>
    </el-container>
  </div>
</template>

<style scoped>
.ms-container :deep(span.title) {
  font-size: 16px;
  font-weight: 500;
  margin-top: 0;
  text-overflow: ellipsis;
  overflow: hidden;
  word-wrap: break-word;
  white-space: nowrap;
}
</style>
