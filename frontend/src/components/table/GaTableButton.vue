<script setup lang="ts">
interface IProps {
  content: string
  size?: "large" | "default" | "small"
  icon?: string
  type?: "primary" | "success" | "warning" | "danger" | "info"
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
