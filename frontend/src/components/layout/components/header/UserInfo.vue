<script setup lang="ts">
import { h } from "vue"

import { useUserStore } from "/@/store/modules/user-store"

import { useDate } from "/@/composables/use-date"
import { NAvatar, NButton, NDropdown, NText } from "naive-ui"

const { logout } = useUserStore()
const { year, week } = useDate()
function renderCustomHeader() {
  return h(
    "div",
    {
      style: "display: flex; align-items: center; padding: 8px 12px;",
    },
    [
      h(NAvatar, {
        round: true,
        style: "margin-right: 12px;",
        src: "https://07akioni.oss-cn-beijing.aliyuncs.com/demo1.JPG",
      }),
      h("div", null, [
        h("div", null, [h(NText, { depth: 2 }, { default: () => "打工仔" })]),
        h("div", { style: "font-size: 12px;" }, [
          h(NText, { depth: 3 }, { default: () => "毫无疑问，你是办公室里最亮的星" }),
        ]),
      ]),
    ],
  )
}
const options = [
  {
    key: "header",
    type: "render",
    render: renderCustomHeader,
  },
  {
    key: "header-divider",
    type: "divider",
  },
  {
    label: "处理群消息 342 条",
    key: "stmt1",
  },
  {
    label: "被 @ 58 次",
    key: "stmt2",
  },
  {
    label: "退出系统",
    key: "stmt3",
  },
]

const handleSelect = (key: string | number) => {
  if (key === "stmt3") {
    window.$dialog.warning({
      title: "",
      maskClosable: false,
      content: () => "是否退出系统",
      positiveText: "确定",
      negativeText: "不确定",
      onPositiveClick() {
        logout()
      },
    })
  }
}
</script>
<template>
  <n-dropdown trigger="hover" :options="options" @select="handleSelect">
    <n-button>{{ year }}年 第{{ week }}周</n-button>
  </n-dropdown>
</template>

<style></style>
