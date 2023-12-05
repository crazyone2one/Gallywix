<script setup lang="ts">
import { ElButton, ElResult } from "element-plus"
</script>
<template>
  <el-result icon="warning" title="403 禁止访问" sub-title="总有些门是对你关闭的">
    <template #extra>
      <el-button type="primary">Back</el-button>
    </template>
  </el-result>
</template>

<style></style>
