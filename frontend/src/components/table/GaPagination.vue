<script setup lang="ts">
import { NPagination } from "naive-ui"

interface IProps {
  page?: number
  total?: number
  pageSize?: number
}
withDefaults(defineProps<IProps>(), {
  total: 0,
  page: 1,
  pageSize: 10,
})
const emits = defineEmits(["update:page", "update:page-size"])

// * 翻页
const handleUpdatePage = (page: number) => {
  emits("update:page", page)
}
// * 改变每页显示数量
const handleUpdatePageSize = (pageSize: number) => {
  emits("update:page-size", pageSize)
}
</script>
<template>
  <div mt-5>
    <n-pagination
      :page="page"
      :page-size="pageSize"
      :page-sizes="[10, 20, 30, 40, 100]"
      show-size-picker
      :item-count="total"
      class="justify-end"
      @update-page="handleUpdatePage"
      @update-page-size="handleUpdatePageSize" />
  </div>
</template>

<style></style>
