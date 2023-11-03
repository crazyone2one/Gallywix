<script setup lang="ts">
import { NCard } from "naive-ui"
interface Props {
  bordered?: boolean
  embedded?: boolean
  hoverable?: boolean
  size?: "small" | "medium" | "large" | "huge"
  title?: string
}
withDefaults(defineProps<Props>(), {
  bordered: false,
  embedded: true,
  hoverable: true,
  size: "medium",
  title: "",
})
</script>

<template>
  <n-card
    :title="title"
    :embedded="embedded"
    :bordered="bordered"
    :hoverable="hoverable"
    :size="size"
    :segmented="{
      content: true,
      footer: 'soft',
    }">
    <template #header>
      <slot name="header"></slot>
    </template>
    <slot name="content"></slot>
    <template #footer> <slot name="footer"></slot> </template>
    <template #action> <slot name="action"></slot> </template>
  </n-card>
</template>

<style></style>
