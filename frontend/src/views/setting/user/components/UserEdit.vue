<script setup lang="ts">
import { ref } from "vue"

import ModalDialog from "/@/components/ModalDialog.vue"
import { useForm } from "@alova/scene-vue"

import { GROUP_TYPE } from "/@/utils/constants"
import { list2SelectOption } from "/@/utils/list-2-select"

import { getAllUserGroupByType, getUserAllGroups, IGroupDTO } from "/@/apis/group"
import { IProject } from "/@/apis/project"
import { saveUserData, updateUserData, USER } from "/@/apis/user"
import { getGroupResource, IWorkspaceItem } from "/@/apis/workspace/index"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { FormInst, FormItemRule, FormRules, NForm, NFormItem, NInput, NSwitch, SelectOption } from "naive-ui"

const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const formRef = ref<FormInst | null>(null)
const type = ref<string>("Add")
const title = ref<string>("创建用户")
const limitOptionCount = ref(400)
const userGroup = ref<Array<IGroupDTO>>([])
const currentGroupWSIds = ref<Array<string>>([])
const currentWSGroupIndex = ref(-1)
const rules: FormRules = {
  username: { required: true, message: "名称不能为空", trigger: "blur" },
  nickname: { required: true, message: "昵称不能为空", trigger: "blur" },
  password: { required: true, message: "密码不能为空", trigger: "blur" },
  phone: {
    required: false,
    trigger: ["blur"],
    validator: (rule: FormItemRule, value: string) => {
      console.log(rule)
      if (value) {
        return /^[1]+[3-9]+\d{9}$/.test(value)
      }
    },
  },
  email: {
    required: true,
    trigger: ["blur"],
    validator: (rule: FormItemRule, value: string) => {
      console.log(rule)
      if (value) {
        return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value)
      }
    },
  },
}
const emits = defineEmits(["refresh"])
const {
  //   loading: submiting,
  send: submit,
  // 响应式的表单数据，内容由initialForm决定
  form: formData,
  onSuccess,
  onError: saveUserDataError,
} = useForm(
  (formData: USER) => {
    return formData.id ? updateUserData(formData) : saveUserData(formData)
  },
  {
    // 初始化表单数据
    initialForm: {
      id: undefined,
      username: "",
      nickname: "",
      password: "",
      status: true,
      email: "",
      phone: "",
      groups: [],
    },
    // 设置这个参数为true即可在提交完成后自动重置表单数据
    resetAfterSubmiting: true,
  },
)
const handleSave = () => {
  formRef.value?.validate((error) => {
    if (!error) {
      console.log(`output->ormData.value`, formData.value)
      if (formData.value.id) {
        window.$dialog.warning({
          title: "提示",
          content: "确定要修改吗？",
          onPositiveClick: () => {
            submit(formData.value)
          },
        })
      } else {
        submit(formData.value)
      }
    }
  })
}
onSuccess(() => {
  modalDialog.value?.closeModal()
  window.$message.success("success")
  emits("refresh")
})
saveUserDataError((error) => {
  window.$message.error(error.error.message)
})
// 获取group数据
const { send: loadGroup, onSuccess: loadGroupSuccess } = useRequest((val) => getAllUserGroupByType(val), {
  immediate: false,
})
loadGroupSuccess((resp) => {
  userGroup.value = resp.data
})
// getUserAllGroups
const { send: loadUserGroup, onSuccess: loadUserGroupSuccess } = useRequest((val) => getUserAllGroups(val), {
  immediate: false,
})
loadUserGroupSuccess((resp) => {
  const _data = resp.data
  formData.value.groups = _data
  for (let group of formData.value.groups) {
    handleWorkspaceOption(group, group.workspaces)
    handleProjectOption(group, group.projects)
  }
})
const handleWorkspaceOption = (group: IGroupDTO, workspaces?: IWorkspaceItem[]) => {
  if (!workspaces) {
    return
  }
  group.showSearchGetMore = workspaces.length > limitOptionCount.value
  const options = workspaces.slice(0, limitOptionCount.value)
  if (!group.ids || group.ids.length === 0) {
    return
  }
  for (let id of group.ids) {
    let index = options.findIndex((o) => o.id === id)
    if (index <= -1) {
      let obj = workspaces.find((d) => d.id === id)
      if (obj) {
        options?.unshift(obj)
      }
    }
  }
  group.workspaceOptions = list2SelectOption(options)
}
const handleProjectOption = (group: IGroupDTO, projects?: IProject[]) => {
  if (!projects) {
    return
  }
  group.showSearchGetMore = projects.length > limitOptionCount.value
  const options = projects.slice(0, limitOptionCount.value)
  if (!group.ids || group.ids.length === 0) {
    return
  }
  for (let id of group.ids) {
    let index = options.findIndex((o) => o.id === id)
    if (index <= -1) {
      let obj = projects.find((d) => d.id === id)
      if (obj) {
        options?.unshift(obj)
      }
    }
  }
  group.projectOptions = list2SelectOption(options)
}
const open = (_type: string, _title: string, val?: USER) => {
  type.value = _type ? _type : type.value
  title.value = _title ? _title : title.value
  if (val && _type === "Edit") {
    loadUserGroup(val.id)
    formData.value = Object.assign({}, val)
  }

  loadGroup({ type: GROUP_TYPE.SYSTEM })
  modalDialog.value?.showModal()
}
const getLabel = (index: number) => {
  let a = index + 1
  return i18n.t("commons.group") + a
}
const activeGroup = (group: IGroupDTO) => {
  const tmp = userGroup.value.filter((ug) => {
    if (!group.selects) {
      return true
    }
    let sign = true
    for (let groupSelect of group.selects) {
      if (ug.code + "+" + ug.type === groupSelect) {
        sign = false
        break
      }
    }
    return sign
  })
  const options: SelectOption[] = []

  for (let index = 0; index < tmp.length; index++) {
    const item = tmp[index]
    const option: SelectOption = {}
    option.label = item.name
    option.value = item.code + "+" + item.type
    option.index = index
    options.push(option)
  }
  return options
}
const { send: loadGroupRes } = useRequest((id, type) => getGroupResource(id, type), {
  immediate: false,
})

const getResource = (value: string, option: SelectOption) => {
  if (!value) {
    return
  }
  let id = value.split("+")[0]
  let type = value.split("+")[1]
  const index = option.index as number
  if (formData.value.groups[index].ids) {
    if (index > 0 && formData.value.groups[index].ids.length > 0) {
      return
    }
  }
  let isHaveWorkspace = false
  if (type === GROUP_TYPE.PROJECT) {
    if (formData.value.groups) {
      for (let i = 0; i < formData.value.groups?.length; i++) {
        let _type = formData.value.groups[i].type.split("+")[1]
        if (_type === GROUP_TYPE.WORKSPACE) {
          isHaveWorkspace = true
          break
        }
      }
    }
  } else if (type === GROUP_TYPE.WORKSPACE) {
    isHaveWorkspace = true
  }
  loadGroupRes(id, type).then((resp) => {
    _setResource(resp, index, type)
    if (id === "super_group") {
      return
    }
    if (isHaveWorkspace === false) {
      addWorkspaceGroup(id, index)
    }
  })
}
const _setResource = (data: IGroupDTO, index: number, type: string) => {
  switch (type) {
    case GROUP_TYPE.WORKSPACE: {
      const _group = formData.value.groups[index]
      _group.workspaces = data.workspaces
      handleWorkspaceOption(_group, data.workspaces)
      break
    }
    case GROUP_TYPE.PROJECT: {
      const _group = formData.value.groups[index]
      _group.projects = data.projects
      handleProjectOption(_group, data.projects)
      break
    }
    default:
      break
  }
}

const addWorkspaceGroup = (id: string, index: number) => {
  let isHaveWorkSpace
  formData.value.groups.forEach((item) => {
    if (item.type === "ws_member+WORKSPACE") {
      isHaveWorkSpace = true
    }
  })
  if (isHaveWorkSpace) {
    return
  }
  loadGroupRes(id, GROUP_TYPE.WORKSPACE).then((resp) => {
    let data = resp
    if (data) {
      let roleInfo: {
        selects: Array<string>
        type: string
        ids: Array<string>
      } = { selects: [], type: "", ids: [] }
      roleInfo.selects = []
      roleInfo.type = "ws_member+WORKSPACE"
      let ids = formData.value.groups.map((r) => r.type)
      ids.forEach((id) => {
        roleInfo.selects.push(id)
      })
      if (currentGroupWSIds.value.length > 0) {
        roleInfo.ids = []
        currentGroupWSIds.value.forEach((item) => {
          roleInfo.ids.push(item)
        })
      } else {
        roleInfo.ids = []
      }
      formData.value.groups.push(roleInfo as IGroupDTO)
      currentWSGroupIndex.value = index + 1
      _setResource(data, index + 1, GROUP_TYPE.WORKSPACE)
    }
  })
}
const removeGroup = (item: IGroupDTO) => {
  if (formData.value.groups.length === 1) {
    window.$message.info(i18n.t("system_user.remove_group_tip"))
    return
  }
  let index = formData.value.groups.indexOf(item)
  let isRemove = checkRemove(item, index)
  if (!isRemove) {
    return
  }
  if (item.type) {
    let _type = item.type.split("+")[1]
    if (_type === GROUP_TYPE.WORKSPACE) {
      currentWSGroupIndex.value = -1
    } else {
      if (currentWSGroupIndex.value > index) {
        currentWSGroupIndex.value = currentWSGroupIndex.value - 1
      }
    }
  }
}
const checkRemove = (item: IGroupDTO, index: number | undefined) => {
  if (!item.type) {
    return true
  }
  let type = item.type.split("+")[1]
  if (type === GROUP_TYPE.WORKSPACE) {
    let isHaveWorkspace = 0
    let isHaveProject = 0
    const _group = formData.value.groups
    if (_group) {
      for (let i = 0; i < _group.length; i++) {
        if (index === i) {
          continue
        }
        let group = _group[i]
        if (!group.type) {
          continue
        }
        let _type = group.type.split("+")[1]
        if (_type === GROUP_TYPE.WORKSPACE) {
          isHaveWorkspace += 1
        }
        if (_type === GROUP_TYPE.PROJECT) {
          isHaveProject += 1
        }
      }
      if (isHaveWorkspace === 0 && isHaveProject > 0) {
        window.$message.warning(i18n.t("commons.not_eligible_for_deletion"))
        return false
      } else {
        currentGroupWSIds.value = []
      }
    }
  }
  return true
}
const groupType = (idType: string) => {
  if (!idType) {
    return
  }
  return idType.split("+")[1]
}
const updateWorkSpace = (value: string, option: SelectOption) => {
  let _type = value.split("+")[1]
  console.log(`output->option`, option)
  console.log(`output->_type`, _type)
}
const setWorkSpaceIds = (value: string, option: SelectOption) => {
  console.log(`output->option`, option)
  console.log(`output->value`, value)
}
defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog" :title="title" modal-with="50%" @confirm="handleSave">
    <template #content>
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging">
        <n-form-item :label="$t('commons.username')" path="username">
          <n-input v-model:value="formData.username" />
        </n-form-item>
        <n-form-item label="昵称" path="nickname">
          <n-input v-model:value="formData.nickname" />
        </n-form-item>
        <n-form-item :label="$t('commons.email')" path="email">
          <n-input v-model:value="formData.email" />
        </n-form-item>
        <n-form-item :label="$t('commons.phone')" path="phone">
          <n-input v-model:value="formData.phone" />
        </n-form-item>
        <n-form-item v-if="type === 'Add'" :label="$t('commons.password')" path="password">
          <n-input
            v-model:value="formData.password"
            :placeholder="$t('user.input_password')"
            type="password"
            show-password-on="mousedown" />
        </n-form-item>
        <div v-for="(group, index) in formData.groups" :key="index">
          <n-form-item :label="getLabel(index)">
            <n-select v-model:value="group.type" :options="activeGroup(group)" @update:value="getResource" />
            <n-button style="margin-left: 20px" @click="removeGroup(group)">
              {{ $t("commons.delete") }}
            </n-button>
          </n-form-item>
          <div v-if="groupType(group.type) === GROUP_TYPE.WORKSPACE">
            <n-form-item :label="$t('commons.workspace')">
              <n-select
                v-model:value="group.ids"
                :options="group.workspaceOptions"
                multiple
                :placeholder="$t('system_user.search_get_more_tip')"
                @update:value="updateWorkSpace" />
            </n-form-item>
          </div>
          <div v-if="groupType(group.type) === GROUP_TYPE.PROJECT">
            <n-form-item :label="$t('commons.project')">
              <n-select
                v-model:value="group.ids"
                :options="group.projectOptions"
                multiple
                @update:value="setWorkSpaceIds" />
            </n-form-item>
          </div>
        </div>
        <n-form-item label="启用">
          <n-switch v-model:value="formData.status" />
        </n-form-item>
      </n-form>
    </template>
  </modal-dialog>
</template>

<style></style>
