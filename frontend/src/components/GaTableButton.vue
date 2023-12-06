<script setup lang="ts">
import { ElButton } from "element-plus"

interface PROPS {
  size?: "large" | "default" | "small"
  type?: "primary" | "success" | "warning" | "danger" | "info"
  content: string
  icon?: string
  disabled?: boolean
}

withDefaults(defineProps<PROPS>(), {
  size: "small",
  type: "primary",
  icon: "i-tabler:question-circle",
  disabled: false,
})
const emits = defineEmits(["exec"])
</script>

<template>
  <el-button :disabled="disabled" plain :type="type" :size="size" @click="emits('exec')">
    <span :class="icon" />
    {{ content }}
  </el-button>
</template>

<style scoped></style>
