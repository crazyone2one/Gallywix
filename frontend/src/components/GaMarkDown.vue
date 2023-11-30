<script setup lang="ts">
import { computed } from "vue"

import "md-editor-v3/lib/style.css"
import { ITestCaseTemplate } from "../apis/template/test-case-template"
import { MdEditor } from "md-editor-v3"
interface IPorps {
  customMinHeight?: string | number
  data: ITestCaseTemplate
  prop: string
}
const emits = defineEmits(["update:data"])
const props = withDefaults(defineProps<IPorps>(), {
  customMinHeight: 15,
})

const plus = computed({
  get: () => props.data,
  set: (val) => {
    emits("update:data", val)
  },
})
</script>
<template>
  <MdEditor v-model="plus[prop]" :style="{ 'min-height': customMinHeight + 'px', 'min-width': '100px' }" />
</template>

<style scoped></style>
