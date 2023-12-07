<script setup lang="ts">
import { ref } from "vue"

import GaTipButton from "../GaTipButton.vue"

import { DropdownOption } from "naive-ui"

interface IProps {
  tip: string
  disabled?: boolean
  isMoreOperate?: boolean
  isTextButton?: boolean
  isDivButton?: boolean
  icon?: string
  childOperate?: Array<DropdownOption>
  permission?: Array<string>
}
withDefaults(defineProps<IProps>(), {
  isMoreOperate: false,
  isTextButton: false,
  isDivButton: false,
  icon: "i-tabler:question-circle",
  disabled: false,
  childOperate: () => [],
  permission: () => [],
})
const isReadOnly = ref(false)
// const isHover = ref(false)
const emits = defineEmits(["exec"])
</script>
<template>
  <n-tooltip v-if="isDivButton" trigger="hover">
    <template #trigger>
      <n-button size="small" circle :disabled="isReadOnly" @click="emits('exec')">
        <div style="transform: scale(0.8)">
          <span style="margin-left: -4px; line-height: 27px">{{ tip }}</span>
        </div>
      </n-button>
    </template>
    {{ tip }}
  </n-tooltip>
  <n-button v-else-if="isTextButton" size="small" text :disabled="isReadOnly" @click="emits('exec')">
    <div style="transform: scale(0.8)">
      <span style="margin-left: -4px; line-height: 27px">{{ tip }}</span>
    </div>
  </n-button>
  <n-dropdown v-else-if="isMoreOperate" trigger="hover" size="small" :options="childOperate">
    <n-button>找个地方休息</n-button>
  </n-dropdown>
  <ga-tip-button
    v-else
    :tip="tip"
    :icon="icon"
    :disabled="disabled || isReadOnly"
    size="tiny"
    :permission="permission"
    @exec="emits('exec')" />
</template>

<style scoped></style>
