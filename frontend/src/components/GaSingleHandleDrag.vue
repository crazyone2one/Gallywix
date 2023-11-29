<script setup lang="ts">
import { computed, ref } from "vue"

import { ICustomFieldOption } from "../apis/template/custom-field"
import { type UseDraggableReturn, VueDraggable } from "vue-draggable-plus"

interface IProp {
  options?: Array<ICustomFieldOption>
  isKv: boolean
  disable?: boolean
}
const props = withDefaults(defineProps<IProp>(), {
  options: () => [
    {
      label: "Joao",
      value: "Joao",
      system: false,
    },
  ],
  disable: false,
})
const el = ref<UseDraggableReturn>()
const editIndex = ref(-1)
const emit = defineEmits(["update:options"])
const list = computed({
  get: () => props.options,
  set: (val) => {
    emit("update:options", val)
  },
})
const onStart = () => {
  console.log("start")
}

const onUpdate = () => {
  console.log("update")
}
const generateUUID = () => {
  let d = new Date().getTime(),
    d2 = (typeof performance !== "undefined" && performance.now && performance.now() * 1000) || 0
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, (c) => {
    let r = Math.random() * 16
    if (d > 0) {
      r = (d + r) % 16 | 0
      d = Math.floor(d / 16)
    } else {
      r = (d2 + r) % 16 | 0
      d2 = Math.floor(d2 / 16)
    }
    return (c == "x" ? r : (r & 0x7) | 0x8).toString(16)
  })
}
const add = () => {
  const item: ICustomFieldOption = {
    label: "",
    value: "",
  }
  if (!props.isKv) {
    item.value = generateUUID().substring(0, 8)
  }
  while (typeof item.value === "number") {
    item.value = generateUUID().substring(0, 8)
  }
  list.value.push(item)
  editIndex.value = list.value.length - 1
}
const handleEdit = (val: ICustomFieldOption, idx: number) => {
  if (props.disable) {
    return
  }
  if (!val.system) {
    editIndex.value = idx
  }
}
const handleTextEdit = () => {
  if (!props.isKv) {
    editIndex.value = -1
  }
}
const handleValueEdit = (val: ICustomFieldOption) => {
  if (val.label && val.value) {
    editIndex.value = -1
  }
}
</script>
<template>
  <div>
    <n-button size="tiny" type="info" dashed text :disabled="disable" @click="add">
      {{ $t("custom_field.add_option") }}
    </n-button>
    <VueDraggable ref="el" v-model="list" @start="onStart" @update="onUpdate">
      <div v-for="(item, idx) in list" :key="idx">
        <n-icon size="15">
          <span class="i-tabler:baseline-density-small" />
        </n-icon>
        <n-input
          v-if="editIndex === idx"
          v-model:value="item.label"
          type="text"
          size="tiny"
          class="text-item"
          :placeholder="$t('custom_field.field_text')"
          @blur="handleTextEdit" />
        <span v-else class="text-item">
          <span v-if="item.system">{{ $t(item.label) }}</span>
          <span v-else> {{ item.label }}</span>
        </span>
        <n-input
          v-if="editIndex === idx && isKv"
          v-model:value="item.value"
          type="text"
          size="tiny"
          class="text-item"
          :placeholder="$t('custom_field.field_value')"
          @blur="handleValueEdit(item)" />
        <span v-else-if="isKv" class="text-item">
          <span>
            {{ (item.value && isKv ? "(" : "") + item.value + (item.value && isKv ? ")" : "") }}
          </span>
        </span>
        <n-button
          text
          :disabled="item.system && true"
          style="margin-left: 20px; margin-right: 6px"
          @click="handleEdit(item, idx)">
          <n-icon>
            <span class="i-tabler:edit" />
          </n-icon>
        </n-button>
        <n-button text :disabled="item.system && false" style="margin-right: 6px">
          <n-icon>
            <span class="i-tabler:trash" />
          </n-icon>
        </n-button>
      </div>
    </VueDraggable>
  </div>
</template>

<style scoped>
.text-item {
  margin: 5px;
}
</style>
