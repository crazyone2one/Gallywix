<script setup lang="ts">
interface IProps {
  size?: "tiny" | "small" | "medium" | "large"
  type?: "default" | "tertiary" | "primary" | "success" | "info" | "warning" | "error"
  disabled?: boolean
  circle?: boolean
  icon?: string
  tip?: string
  permission?: Array<string>
}

withDefaults(defineProps<IProps>(), {
  size: "small",
  type: "primary",
  disabled: false,
  circle: false,
  icon: "i-tabler:question-circle",

  tip: "ops",
  permission: () => [],
})
const emits = defineEmits(["exec"])
</script>

<template>
  <n-popover trigger="hover">
    <template #trigger>
      <n-button
        v-permissions="permission"
        :size="size"
        :type="type"
        :disabled="disabled"
        :circle="circle"
        :style="type"
        @click="emits('exec')">
        <n-icon>
          <span :class="icon" />
        </n-icon>
      </n-button>
    </template>
    <span>{{ tip }}</span>
  </n-popover>
</template>

<style scoped></style>
