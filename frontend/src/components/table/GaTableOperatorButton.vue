<script setup lang="ts">
import { ref } from "vue"

import { ElButton, ElDropdown, ElDropdownMenu, ElTooltip } from "element-plus"
interface IOperator {
  permissions: Array<string>
  isDisable: boolean
  isDivide: boolean
  isActive: boolean
  tip: string
  exec: (val: unknown) => void
}
interface IProps {
  tip: string
  disabled?: boolean
  isMoreOperate?: boolean
  isDivButton?: boolean
  isTextButton?: boolean
  icon?: string
  type?: string
  childOperate?: Array<IOperator>
  rowData?: Record<string, never>
}
const props = withDefaults(defineProps<IProps>(), {
  isMoreOperate: false,
  isDivButton: false,
  isTextButton: false,
  icon: "i-tabler:question-circle",
  disabled: false,
  type: "primary",
  childOperate: () => [],
  rowData: () => {
    return {}
  },
})
const isReadOnly = ref(false)
const isHover = ref(false)
const emits = defineEmits(["exec"])
const exec = (): void => {
  emits("exec")
}
const click = (btn: IOperator) => {
  if (btn.exec instanceof Function) {
    btn.exec(props.rowData)
  }
}
</script>
<template>
  <el-tooltip v-if="isDivButton" :content="tip" placement="bottom" :enterable="false" effect="dark">
    <el-button
      type="primary"
      :disabled="isReadOnly"
      circle
      style="color: white; padding: 0px 0.1px; width: 28px; height: 28px"
      size="small"
      @click="exec"
      @keydown.enter.prevent>
      <div style="transform: scale(0.8)">
        <span style="margin-left: -4px; line-height: 27px">{{ tip }}</span>
      </div>
    </el-button>
  </el-tooltip>
  <el-button
    v-else-if="isTextButton"
    type="text"
    class="text-btn"
    :disabled="isReadOnly"
    size="small"
    @keydown.enter.prevent
    @click="exec">
    {{ tip }}
  </el-button>
  <el-dropdown v-else-if="isMoreOperate" size="small" placement="bottom-end">
    <span class="el-dropdown-link">
      <el-button ref="moreBtn" type="primary" class="more-btn" :class="{ 'more-btn-hover': isHover }">
        <i class="el-icon-more"></i>
      </el-button>
    </span>
    <template #dropdown>
      <el-dropdown-menu class="more-operate-menu" @mouseenter="isHover = true" @mouseleave="isHover = false">
        <el-dropdown-item
          v-for="(operator, index) in childOperate"
          :key="index"
          v-permission="operator.permissions"
          :disabled="operator.isDisable"
          :divided="operator.isDivide"
          :class="{ active: operator.isActive }"
          @click.stop="click(operator)">
          {{ operator.tip }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<style scoped></style>
