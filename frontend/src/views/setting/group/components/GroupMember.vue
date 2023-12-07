<script setup lang="ts">
import { computed, ref } from "vue"

import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import ModalDialog from "/@/components/ModalDialog.vue"
import { useForm } from "@alova/scene-vue"

import { GROUP_PROJECT, GROUP_SYSTEM, GROUP_WORKSPACE } from "/@/utils/constants"
import { list2SelectOption, userList2SelectOption } from "/@/utils/list-2-select"

import { addUser2Group, getUserGroup, IGroupDTO, IUser2Group } from "/@/apis/group"
import { ICustomGroup, ITableDataInfo, PageReq } from "/@/apis/interface"
import { IProject } from "/@/apis/project"
import { getUserList, USER } from "/@/apis/user"
import { getGroupResource, IWorkspaceItem } from "/@/apis/workspace"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns, FormInst, SelectOption } from "naive-ui"
const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const memberVisible = ref<InstanceType<typeof ModalDialog> | null>(null)
const title = ref("")
const initUserGroupUrl = ref("/user/group/user/")
const initUserUrl = ref("/user/list")
const usersCopy = ref<Array<USER>>([])
const users = ref<Array<USER>>([])
const group = ref<IGroupDTO>({} as IGroupDTO)
const limitOptionCount = ref(400)
const showUserSearchGetMore = ref(false)
const showResourceSearchGerMore = ref(false)
const condition = ref<PageReq>({
  name: "",
  pageNumber: 1,
  pageSize: 10,
})
const tableInfo = ref<ITableDataInfo<USER[]>>({
  data: [],
  total: 0,
})
const rowKey = (row: USER) => row.id as unknown as string
const columns: DataTableColumns<USER> = [
  {
    type: "selection",
    align: "center",
  },
  {
    title: i18n.t("commons.username"),
    key: "username",
    align: "center",
  },
  {
    title: i18n.t("commons.email"),
    key: "email",
    align: "center",
  },
  {
    title: i18n.t("commons.phone"),
    key: "phone",
    align: "center",
  },

  {
    title: i18n.t("commons.operating"),
    key: "operator",
    width: 200,
    fixed: "right",
    align: "center",
    // render(rowData) {
    //   return h(
    //     GaTableOperation,
    //     {
    //       deleteTip: i18n.t("commons.remove"),
    //       onEditClick: () => handleEditMember(rowData),
    //       onDeleteClick: () => handleDeleteMember(rowData),
    //     },
    //     {},
    //   )
    // },
  },
]
const formRef = ref<FormInst | null>(null)
const memberFormRules = {}
const userList = ref<Array<SelectOption>>([])
const emits = defineEmits(["refresh"])
const showTypeLabel = computed(() => {
  return group.value.type !== GROUP_SYSTEM
})
const typeLabel = computed(() => {
  const type = group.value.type
  if (type === GROUP_WORKSPACE) {
    return i18n.t("group.belong_workspace")
  }
  if (type === GROUP_PROJECT) {
    return i18n.t("group.belong_project")
  }
  return ""
})
const openModal = (_group: IGroupDTO, _initUserGroupUrl?: string, _initUserUrl?: string) => {
  initUserGroupUrl.value = _initUserGroupUrl ? _initUserGroupUrl : "/system/group/user"
  initUserUrl.value = _initUserUrl ? _initUserUrl : "/user/list"
  group.value = _group
  modalDialog.value?.showModal()
  init()
}
const { send: loadUser } = useRequest((val) => getUserGroup(val), {
  immediate: false,
})
const init = () => {
  condition.value.userGroupId = group.value.code
  loadUser(condition.value).then((res) => {
    tableInfo.value.data = res.records
    tableInfo.value.total = res.totalRow
  })
}
// get user
const { send: loadUserList } = useRequest(getUserList(), { immediate: false })
// get group resource
const { send: loadGroupResource } = useRequest((v1, v2) => getGroupResource(v1, v2), {
  immediate: false,
})
const _sourceData = ref<Array<SelectOption>>([])
const sourceData = ref<Array<IWorkspaceItem> | Array<IProject> | undefined>([])
const sourceDataCopy = ref<Array<IWorkspaceItem> | Array<IProject> | undefined>([])
const getResource = () => {
  loadGroupResource(group.value.code, group.value.type).then((res) => {
    let data = res
    if (data) {
      _setResource(group.value.type, data)
      handleResourceOption(sourceData.value)
    }
  })
}
const handleResourceOption = (resources: Array<IWorkspaceItem> | Array<IProject> | undefined) => {
  if (!resources) {
    return
  }
  showResourceSearchGerMore.value = resources.length > limitOptionCount.value
  sourceData.value = resources.slice(0, limitOptionCount.value)
  _sourceData.value = list2SelectOption(sourceData.value)
  if (!formData.value.sourceIds || formData.value.sourceIds.length === 0) {
    return
  }
  _handleSelectOption(formData.value.sourceIds, sourceData.value, sourceDataCopy.value)
}
const _setResource = (type: string, data: ICustomGroup) => {
  switch (type) {
    case GROUP_WORKSPACE: {
      sourceData.value = data.workspaces
      sourceDataCopy.value = data.workspaces
      break
    }
    case GROUP_PROJECT: {
      sourceData.value = data.projects
      sourceDataCopy.value = data.projects
      break
    }
    default:
      break
  }
}
const addMemberBtn = () => {
  title.value = i18n.t("member.create")
  loadUserList().then((res) => {
    handleUserOption(res)
    usersCopy.value = res
  })
  getResource()
  memberVisible.value?.showModal()
}
const handleUserOption = (_users: USER[]) => {
  if (!_users) {
    return
  }
  showUserSearchGetMore.value = _users.length > limitOptionCount.value
  users.value = _users.slice(0, limitOptionCount.value)
  userList.value = userList2SelectOption(users.value)
  if (!formData.value.userIds || formData.value.userIds.length === 0) {
    return
  }
  _handleSelectOption(formData.value.userIds, users.value, usersCopy.value)
}
const _handleSelectOption = (
  ids: Array<string>,
  options: Array<USER> | Array<IWorkspaceItem> | Array<IProject>,
  origins: Array<USER> | Array<IWorkspaceItem> | Array<IProject>,
) => {
  for (let id of ids) {
    let index = options.findIndex((o) => o.id === id)
    if (index <= -1) {
      let obj: USER | IWorkspaceItem | IProject | undefined = origins.find((d) => d.id === id)
      if (obj) {
        options.unshift(obj)
      }
    }
  }
  console.log(`output->typeof options`, typeof options)
  userList.value = userList2SelectOption(options)
}

const {
  //   loading: submiting,
  send: submit,
  // 响应式的表单数据，内容由initialForm决定
  form: formData,
  onSuccess,
  onError,
} = useForm(
  (formData: IUser2Group) => {
    return addUser2Group(formData)
  },
  {
    // 初始化表单数据
    initialForm: {
      userIds: [],
      sourceIds: [],
      groupId: "",
    },
    // 设置这个参数为true即可在提交完成后自动重置表单数据
    resetAfterSubmiting: true,
  },
)
const handleSave = () => {
  formRef.value?.validate((error) => {
    formData.value.groupId = group.value.id as string
    if (!error) {
      submit(formData.value)
    } else {
      return false
    }
  })
}
onSuccess(() => {
  window.$message.success(i18n.t("commons.save_success"))
  init()
  emits("refresh")
  memberVisible.value?.toggleModal()
})
defineExpose({ openModal })
</script>
<template>
  <modal-dialog ref="modalDialog" :title="title" modal-width="65%">
    <template #content>
      <base-card>
        <template #header>
          <base-search :condition="condition" :create-tip="$t('member.create')" @create="addMemberBtn" />
        </template>
        <template #content>
          <n-data-table :data="tableInfo.data" :columns="columns" :row-key="rowKey" />
        </template>
      </base-card>
    </template>
  </modal-dialog>
  <modal-dialog ref="memberVisible" :title="title" modal-width="45%" @confirm="handleSave">
    <template #content>
      <n-form
        ref="formRef"
        :model="formData"
        :rules="memberFormRules"
        label-placement="left"
        label-width="auto"
        size="small"
        require-mark-placement="right-hanging">
        <n-form-item :label="$t('commons.member')" path="userIds">
          <n-select
            v-model:value="formData.userIds"
            :options="userList"
            multiple
            filterable
            :placeholder="$t('member.please_choose_member')" />
        </n-form-item>
        <n-form-item v-if="showTypeLabel" :label="typeLabel" path="sourceIds">
          <n-select
            v-model:value="formData.sourceIds"
            :options="_sourceData"
            multiple
            filterable
            :placeholder="$t('member.please_choose_member')" />
        </n-form-item>
      </n-form>
    </template>
  </modal-dialog>
</template>

<style scoped></style>
