<script setup lang="ts">
import { IGroupDTO } from "../apis/group"
import { NTag } from "naive-ui"

interface IProps {
  roles: Array<IGroupDTO>
  type?: "default" | "primary" | "info" | "success" | "warning" | "error"
  size?: "small" | "medium" | "large"
  bordered?: boolean
}
withDefaults(defineProps<IProps>(), {
  type: "primary",
  size: "small",
  bordered: false,
})
</script>
<template>
  <n-space>
    <n-tag
      v-for="(role, index) in roles"
      :key="index"
      :type="type"
      :size="size"
      :bordered="bordered">
      {{ role.name }}
    </n-tag>
  </n-space>
</template>

<style></style>
