<script setup lang="ts">
import { computed } from "vue"
import { NInput, NButton, NPopover, NIcon } from "naive-ui"
import { PageReq } from "../apis/interface"

interface IProps {
  condition: PageReq
  showCreate?: boolean
  popoverText?: string
  searchContent?: string
}
const props = withDefaults(defineProps<IProps>(), {
  showCreate: true,
  popoverText: "",
  searchContent: () => "根据名称搜索",
})
const emits = defineEmits(["update:condition", "search", "create"])
const condition = computed({
  get: () => props.condition,
  set: (val) => {
    emits("update:condition", val)
  },
})
</script>

<template>
  <div class="flex justify-center">
    <div class="-mb-5">
      <n-popover trigger="hover">
        <template #trigger>
          <n-button
            text
            type="primary"
            style="font-size: 20px"
            @click="emits('create')">
            <n-icon>
              <span class="i-tabler:circle-plus" />
            </n-icon>
          </n-button>
        </template>
        <span>{{ popoverText }}</span>
      </n-popover>
      <slot name="button"></slot>
    </div>
    <div class="ml-auto">
      <div>
        <n-input
          v-model:value="condition.name"
          type="text"
          size="small"
          :placeholder="searchContent"
          clearable
          @blur="emits('search')" />
      </div>
      <div>
        <slot name="otherSearchComp" />
      </div>

      <slot name="after" />
    </div>
  </div>
</template>

<style></style>
