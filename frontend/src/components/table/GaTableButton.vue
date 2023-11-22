<script setup lang="ts">
interface IProps {
  content: string
  size?: "tiny" | "small" | "medium" | "large"
  icon?: string
  type?:
    | "default"
    | "tertiary"
    | "primary"
    | "success"
    | "info"
    | "warning"
    | "error"
}
withDefaults(defineProps<IProps>(), {
  size: "small",
  icon: "i-tabler:question-circle",
  type: "primary",
})
</script>
<template>
  <n-button icon-placement="left" secondary :size="size" :type="type">
    <template #icon>
      <n-icon>
        <span :class="icon" />
      </n-icon>
    </template>
    {{ content }}
  </n-button>
</template>

<style scoped></style>
