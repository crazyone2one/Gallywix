<script setup lang="ts">
import { ref } from "vue"

import ModalDialog from "/@/components/ModalDialog.vue"

import { useAuthStore } from "/@/store/auth-store"

import {
  list2SelectOption,
  userList2SelectOption,
} from "/@/utils/list-2-select"

import { getGroupsByType } from "/@/apis/group"
import { addWorkspaceMemberSpecial, getUserList, USER } from "/@/apis/user"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { FormInst, FormRules, SelectOption } from "naive-ui"

interface IProp {
  groupType?: string
  groupScopeId?: string
  projectId?: string
  userResourceUrl?: string
}
const props = withDefaults(defineProps<IProp>(), {
  groupType: "",
  groupScopeId: "",
  projectId: "",
  userResourceUrl: "/system/user/list",
})

const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const formRef = ref<FormInst | null>(null)
const model = ref<{
  userIds: Array<string>
  groupIds: Array<string>
  groups: Array<SelectOption>
}>({
  userIds: [],
  groupIds: [],
  groups: [],
})
const rules: FormRules = {
  userIds: {
    type: "array",
    required: false,
    trigger: ["blur", "change"],
    message: i18n.t("member.please_choose_member"),
  },
  groupIds: {
    type: "array",
    required: false,
    message: i18n.t("group.please_select_group"),
    trigger: ["blur", "change"],
  },
}
const limitOptionCount = ref(400)
const showUserSearchGetMore = ref(false)
const userList = ref<Array<SelectOption>>([])
const userListCopy = ref<Array<SelectOption>>([])
// load user list
const { send: loadUserList } = useRequest(getUserList(), { immediate: false })
// getGroupsByType
const { send: loadGroupsByType } = useRequest((val) => getGroupsByType(val), {
  immediate: false,
})
const handleUserOption = (users: Array<USER>) => {
  if (!users) {
    return
  }
  showUserSearchGetMore.value = users.length > limitOptionCount.value
  userList.value = userList2SelectOption(users.slice(0, limitOptionCount.value))
  if (!model.value.userIds || model.value.userIds.length == 0) {
    return
  }
  _handleSelectOption(model.value.userIds, userList.value)
}
const _handleSelectOption = (
  userIds: Array<string>,
  options: Array<SelectOption>,
) => {
  for (let id of userIds) {
    let index = options.findIndex((o) => o.key === id)
    if (index <= -1) {
      let obj = userListCopy.value.find((o) => o.key === id)
      if (obj) {
        options.unshift(obj)
      }
    }
  }
}
const open = () => {
  modalDialog.value?.showModal()
  loadUserList().then((resp) => {
    handleUserOption(resp)
    userListCopy.value = userList2SelectOption(resp)
  })
  let param = {
    type: props.groupType,
    resourceId: props.groupScopeId,
    projectId: "",
  }
  if (props.groupType === "PROJECT") {
    param.projectId = props.projectId || (useAuthStore().project_id as string)
  }
  loadGroupsByType(param).then((resp) => {
    model.value.groups = list2SelectOption(resp)
  })
}
const { send: addWsMember } = useRequest(
  (val) => addWorkspaceMemberSpecial(val),
  {
    immediate: false,
  },
)
const handleSave = () => {
  let param = {
    userIds: model.value.userIds,
    groupIds: model.value.groupIds,
    workspaceId: props.groupScopeId,
  }
  addWsMember(param)
    .then(() => {
      modalDialog.value?.toggleModal()
    })
    .catch((err) => {
      window.$message.error(err.message)
    })
}
defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog" @confirm="handleSave">
    <template #content>
      <n-form
        ref="formRef"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        size="small"
        require-mark-placement="right-hanging">
        <n-form-item :label="$t('commons.member')" path="userIds">
          <n-select
            v-model:value="model.userIds"
            :options="userList"
            multiple
            filterable
            :placeholder="$t('member.please_choose_member')" />
        </n-form-item>
        <n-form-item :label="$t('commons.group')" path="groupIds">
          <n-select
            v-model:value="model.groupIds"
            multiple
            filterable
            :placeholder="$t('group.please_select_group')"
            :options="model.groups" />
        </n-form-item>
      </n-form>
    </template>
  </modal-dialog>
</template>

<style></style>
