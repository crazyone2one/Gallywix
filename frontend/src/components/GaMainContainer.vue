<script setup lang="ts">
import { ElMain } from "element-plus"
</script>
<template>
  <el-main class="ms-main-container">
    <slot></slot>
  </el-main>
</template>

<style scoped>
.ms-main-container {
  padding: 5px 10px;
}
</style>
