<script setup lang="ts">
import { computed, nextTick, onMounted, ref } from "vue"

import { uuid } from "../utils/tools"

import { ElAside } from "element-plus"

interface Iprops {
  enableAsideHidden?: boolean
  width?: string
  minWidth?: number | null
  maxWidth?: number | null
  height?: number | null
  minHeight?: number | null
  enableAutoHeight?: boolean
  defaultHiddenBottomTop?: number
  draggable?: boolean
  pageKey?: string
}
const props = withDefaults(defineProps<Iprops>(), {
  enableAsideHidden: true,
  width: "300px",
  minWidth: null,
  maxWidth: null,
  height: null,
  minHeight: null,
  enableAutoHeight: false,
  defaultHiddenBottomTop: 0,
  draggable: true,
  pageKey: "",
})
const asideHidden = ref(false)
const id = ref("")
const hiddenBottomTop = ref("")
const containerCalHeight = computed(() => {
  return props.height ? props.height - 30 + "px" : props.enableAutoHeight ? null : "calc(100vh - 62px)"
})
const setHiddenBottomTop = () => {
  if (props.defaultHiddenBottomTop) {
    hiddenBottomTop.value = props.defaultHiddenBottomTop + "px"
  } else {
    const e = document.getElementById(id.value)
    if (!e) return
    // 默认在 3/1 的位置
    hiddenBottomTop.value = e.clientHeight / 3 + "px"
  }
}
onMounted(() => {
  id.value = uuid()
  nextTick(() => {
    setHiddenBottomTop()
  })
})
</script>
<template>
  <el-aside
    :id="id"
    :width="asideHidden ? '0' : props.width"
    class="ms-aside-container"
    :style="{ 'min-width': minWidth, 'max-width': maxWidth, 'min-height': minHeight }">
    <div
      v-if="enableAsideHidden"
      class="hiddenBottom"
      :style="{ top: hiddenBottomTop ? hiddenBottomTop : 0 }"
      @click="asideHidden = !asideHidden">
      <span v-if="!asideHidden" class="i-tabler:arrow-left" />
      <span v-if="asideHidden" class="i-tabler:arrow-right" />
    </div>
    <div style="overflow: scroll" class="ms-aside-node-tree" :style="{ height: containerCalHeight }">
      <slot></slot>
    </div>
    <!-- <ms-horizontal-drag-bar v-if="draggable" /> -->
  </el-aside>
</template>

<style scoped>
.ms-aside-container {
  border: 1px solid #e6e6e6;
  padding: 10px;
  border-radius: 2px;
  box-sizing: border-box;
  background-color: #fff;
  /*height: calc(100vh - 80px);*/
  border-right: 0px;
  position: relative;
  overflow: visible;
}

.hiddenBottom {
  width: 8px;
  height: 50px;
  /*top: calc((100vh - 80px)/3);*/
  right: -10px;
  /*top: 0;*/
  line-height: 50px;
  border-radius: 0 15px 15px 0;
  background-color: #acb7c1;
  display: inline-block;
  position: absolute;
  cursor: pointer;
  opacity: 0.4;
  font-size: 2px;
  margin-left: 1px;
}

.hiddenBottom i {
  margin-left: -2px;
}

.hiddenBottom:hover {
  background-color: #783887;
  opacity: 0.8;
  width: 12px;
}

.hiddenBottom:hover i {
  margin-left: 0;
  color: white;
}
</style>
