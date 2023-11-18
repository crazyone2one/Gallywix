<script setup lang="ts">
import { computed } from "vue"

import { PageReq } from "/@/apis/interface"
import { NPagination } from "naive-ui"

interface IProps {
  condition: PageReq
  itemCount?: number
}
const props = withDefaults(defineProps<IProps>(), {
  itemCount: 0,
})
const emits = defineEmits(["update:condition", "updatePage", "updatePageSize"])
const condition = computed({
  get: () => props.condition,
  set: (val) => {
    emits("update:condition", val)
  },
})
// * 翻页
const handleUpdatePage = (page: number) => {
  emits("updatePage", page)
}
// * 改变每页显示数量
const handleUpdatePageSize = (pageSize: number) => {
  emits("updatePageSize", pageSize)
}
</script>
<template>
  <div mt-5>
    <n-pagination
      v-model:page="condition.pageNumber"
      v-model:page-size="condition.pageSize"
      :page-sizes="[10, 20, 30, 40]"
      show-size-picker
      :item-count="itemCount"
      class="justify-end"
      @update-page="handleUpdatePage"
      @update-page-size="handleUpdatePageSize" />
  </div>
</template>

<style></style>
