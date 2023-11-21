<script setup lang="ts">
import { SUPER_GROUP } from "/@/utils/constants"

import { IGroupDTO } from "/@/apis/group"
import { IPermissionDTO } from "/@/apis/permission"

interface IProps {
  permissions?: Array<IPermissionDTO>
  group?: IGroupDTO
  readOnly?: boolean
}
const props = withDefaults(defineProps<IProps>(), {
  permissions: () => [],
  group: () => {
    return {} as IGroupDTO
  },
  readOnly: false,
})
// const emits = defineEmits(["update"])
const isReadOnly = (val: IPermissionDTO) => {
  // 禁止取消系统管理员用户组权限
  if (props.group.id === SUPER_GROUP) {
    return true
  }
  // 禁止取消系统管理员用户组和超级管理员用户组的读取和设置权限
  const isSystemGroupPermission =
    val.id === "SYSTEM_GROUP:READ" ||
    val.id === "SYSTEM_GROUP:READ+SETTING_PERMISSION"
  const isDefaultSystemGroup =
    props.group.id === "admin" && isSystemGroupPermission
  return props.readOnly || isDefaultSystemGroup
}
const handleUpdateValue = (value: boolean) => {
  console.log(`output->value`, value)
}
</script>
<template>
  <n-space item-style="display: flex;" align="center">
    <n-checkbox
      v-for="(permission, index) in permissions"
      :key="index"
      v-model:checked="permission['checked']"
      :disabled="isReadOnly(permission)"
      @update:value="handleUpdateValue">
      {{ $t(permission.name) }}
    </n-checkbox>
  </n-space>
</template>

<style scoped></style>
