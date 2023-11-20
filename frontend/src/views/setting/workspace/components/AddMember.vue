<script setup lang="ts">
import { ref } from "vue"

import ModalDialog from "/@/components/ModalDialog.vue"

import { userList2SelectOption } from "/@/utils/list-2-select"

import { getUserList, USER } from "/@/apis/user"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { FormInst, FormRules, SelectOption } from "naive-ui"

interface IProp {
  groupType?: string
  groupScopeId?: string
  projectId?: string
  userResourceUrl?: string
}
withDefaults(defineProps<IProp>(), {
  groupType: "",
  groupScopeId: "",
  projectId: "",
  userResourceUrl: "/system/user/list",
})

const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const formRef = ref<FormInst | null>(null)
const model = ref({
  userIds: [],
  groupIds: [],
  groups: [],
})
const rules: FormRules = {
  userIds: {
    type: "array",
    required: true,
    message: i18n.t("member.please_choose_member"),
    trigger: ["blur"],
  },
  groupIds: {
    type: "array",
    required: true,
    message: i18n.t("group.please_select_group"),
    trigger: ["blur", "change"],
  },
}
const limitOptionCount = ref(400)
const showUserSearchGetMore = ref(false)
const userList = ref<Array<SelectOption>>([])
const userListCopy = ref<Array<SelectOption>>([])
// load user list
const { send: loadUserList, onSuccess: loadUserSuccess } = useRequest(
  getUserList(),
  { immediate: false },
)
loadUserSuccess((resp) => {
  //   userList.value = userList2SelectOption(resp.data)
  handleUserOption(resp.data)
  userListCopy.value = userList2SelectOption(resp.data)
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
  loadUserList()
}

defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog">
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
            multiple
            filterable
            :placeholder="$t('member.please_choose_member')"
            :options="userList" />
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
