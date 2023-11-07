<script setup lang="ts">
import { NLayoutHeader, NMenu, MenuOption } from "naive-ui"
import { ref } from "vue"
import logo from "./../../../../assets/387941.svg"
import UserInfo from "./UserInfo.vue"

const activeKey = ref<string | null>(null)
const menuOptions: MenuOption[] = [
  {
    label: "1973年的弹珠玩具",
    key: "pinball-1973",

    disabled: true,
    children: [
      {
        label: "鼠",
        key: "rat",
      },
    ],
  },
  {
    label: "寻羊冒险记",
    key: "a-wild-sheep-chase",

    disabled: true,
  },
  {
    label: "舞，舞，舞",
    key: "dance-dance-dance",

    children: [
      {
        type: "group",
        label: "人物",
        key: "people",
        children: [
          {
            label: "叙事者",
            key: "narrator",
          },
          {
            label: "羊男",
            key: "sheep-man",
          },
        ],
      },
      {
        label: "饮品",
        key: "beverage",

        children: [
          {
            label: "威士忌",
            key: "whisky",
          },
        ],
      },
      {
        label: "食物",
        key: "food",
        children: [
          {
            label: "三明治",
            key: "sandwich",
          },
        ],
      },
      {
        label: "过去增多，未来减少",
        key: "the-past-increases-the-future-recedes",
      },
    ],
  },
]
</script>
<template>
  <n-layout-header
    class="header-layout"
    style="
      --side-padding: 32px;
      grid-template-columns: calc(272px - var(--side-padding)) 1fr auto;
    "
    bordered>
    <div class="ui-logo">
      <img class="ui-img" :src="logo" />
      <span>Gallywix</span>
    </div>
    <div style="display: flex; align-items: center">
      <n-menu
        v-model:value="activeKey"
        mode="horizontal"
        :options="menuOptions" />
    </div>
    <div>
      <user-info />
    </div>
  </n-layout-header>
</template>

<style scoped>
.header-layout {
  height: 64px;
  padding: 24px;
  display: grid;
  align-items: center;
  grid-template-columns: calc(64px-1px);
  padding: 0 32px;
  .ui-logo {
    align-items: center;
  }
}
.ui-logo {
  cursor: pointer;
  display: flex;
  font-size: 18px;
  .ui-img {
    height: 32px;
    margin-right: 12px;
    width: 32px;
  }
}
</style>
