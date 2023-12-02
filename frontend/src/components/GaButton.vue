<script setup lang="ts">
import { NButton, NIcon, NPopover } from "naive-ui"

interface PROPS {
  size?: "tiny" | "small" | "medium" | "large"
  type?: "default" | "tertiary" | "primary" | "success" | "info" | "warning" | "error"
  text?: boolean
  circle?: boolean
  iconClass?: string
  isIcon?: boolean
  buttonText?: string
  isPop?: boolean
  popText?: string
  editPermission?: Array<string>
  deletePermission?: Array<string>
  permission?: Array<string>
}

withDefaults(defineProps<PROPS>(), {
  size: "small",
  type: "primary",
  text: false,
  circle: false,
  iconClass: "i-tabler:question-circle",
  isIcon: false,
  buttonText: "ops",
  isPop: false,
  popText: "ops",
  editPermission: () => [],
  deletePermission: () => [],
  permission: () => [],
})
const emits = defineEmits(["exec"])
</script>

<template>
  <n-popover v-if="isPop" trigger="hover">
    <template #trigger>
      <n-button
        v-permissions="permission"
        :size="size"
        :type="type"
        :text="text"
        :circle="circle"
        :style="type ? 'font-size:20px' : ''"
        @click="emits('exec')">
        <n-icon v-if="text && isIcon">
          <span :class="iconClass" />
        </n-icon>
        <span v-else>{{ buttonText }}</span>
      </n-button>
    </template>
    <span>{{ popText }}</span>
  </n-popover>
  <n-button
    v-else
    v-permissions="permission"
    :size="size"
    :type="type"
    :text="text"
    :circle="circle"
    :style="type ? 'font-size:20px' : ''"
    @click="emits('exec')">
    <n-icon v-if="text && isIcon">
      <span :class="iconClass" />
    </n-icon>
    <span v-else>{{ buttonText }}</span>
  </n-button>
</template>

<style scoped></style>
