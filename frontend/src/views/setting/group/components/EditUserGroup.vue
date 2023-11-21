<script setup lang="ts">
import { ref } from "vue"

import ModalDialog from "/@/components/ModalDialog.vue"
import { useForm } from "@alova/scene-vue"

import { GROUP_SYSTEM } from "/@/utils/constants"

import { IGroupDTO, saveGroup, updateGroup } from "/@/apis/group"
import { loadProjectOption } from "/@/apis/project"
import { loadOptionList } from "/@/apis/workspace"
import i18n from "/@/i18n"
import { useRequest } from "alova"
import { FormInst, FormRules, NForm, NFormItem, SelectOption } from "naive-ui"

const formRef = ref<FormInst | null>(null)
const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const title = ref(i18n.global.t("group.create"))
const dialogType = ref("")
const show = ref(true)
const isSystem = ref(false)
const workspaces = ref<SelectOption[]>([])
const showLabel = ref("")
const emits = defineEmits(["refresh"])
const rules: FormRules = {
  name: [
    {
      required: true,
      message: i18n.global.t("commons.input_name"),
      trigger: "blur",
    },
    {
      min: 2,
      max: 50,
      message: "长度在 2 到 50 个字符",
      trigger: "blur",
    },
  ],
  code: [
    {
      required: true,
      message: i18n.global.t("commons.encode"),
      trigger: "blur",
    },
    {
      min: 2,
      max: 50,
      message: "长度在 2 到 50 个字符",
      trigger: "blur",
    },
  ],
  description: {
    min: 2,
    max: 90,
    message: i18n.global.t("commons.input_limit", [2, 90]),
    trigger: "blur",
  },
  scopeId: {
    required: true,
    message: i18n.global.t("group.select_belong_organization"),
    trigger: ["blur", "change"],
  },
  type: {
    required: true,
    message: i18n.global.t("group.select_type"),
    trigger: ["blur", "change"],
  },
}
const {
  //   loading: submiting,
  send: submit,
  // 响应式的表单数据，内容由initialForm决定
  form: formData,
  onSuccess,
  onError,
} = useForm(
  (formData: IGroupDTO) => {
    return formData.id ? updateGroup(formData) : saveGroup(formData)
  },
  {
    // 初始化表单数据
    initialForm: {
      id: undefined,
      name: "",
      code: "",
      description: "",
      system: true,
      global: false,
      type: "",
      scopeId: "",
    },
    // 设置这个参数为true即可在提交完成后自动重置表单数据
    resetAfterSubmiting: true,
  },
)
const options = [
  { label: i18n.global.t("group.system"), value: "SYSTEM" },
  { label: i18n.global.t("group.workspace"), value: "WORKSPACE" },
  { label: i18n.global.t("group.project"), value: "PROJECT" },
]
// 加载workspace数据
const { send: getWorkspace, onSuccess: loadWs } = useRequest(loadOptionList(), {
  immediate: false,
})
loadWs((resp) => {
  const _data = resp.data
  workspaces.value = _data
  const name = _data.find((item) => (item.id = formData.value.scopeId))
  showLabel.value = name
    ? i18n.global.t("project.owning_workspace")
    : i18n.global.t("project.owning_project")
})
// 加载project数据
const { send: getProject, onSuccess: loadProject } = useRequest(
  loadProjectOption(),
  {
    immediate: false,
  },
)
loadProject((resp) => {
  const _data = resp.data
  workspaces.value = workspaces.value.concat(_data)
})
// 打开弹框
const open = (type: string, tip: string, val?: IGroupDTO) => {
  showLabel.value = ""
  title.value = tip
  isSystem.value = false
  show.value = true
  dialogType.value = type
  formData.value = Object.assign({}, val)
  // if (val) {
  //   formData.value = Object.assign({}, val)
  // }
  if (type !== "create") {
    if (formData.value.type === GROUP_SYSTEM) {
      formData.value.global = true
      show.value = false
    } else {
      formData.value.global = formData.value.scopeId === "global"
      show.value = !formData.value.global
    }
  }
  getWorkspace()
  if (type === "edit") {
    getProject()
  }
  modalDialog.value?.showModal()
}
// 保存数据
const handleSave = () => {
  formRef.value?.validate((error) => {
    if (error) {
      return false
    } else {
      submit()
    }
  })
}
onSuccess(() => {
  modalDialog.value?.closeModal()
  window.$message.success(i18n.global.t("commons.save_success"))
  emits("refresh")
})
onError((val) => {
  window.$message.error(val.error.message)
})
const handleChange = (value: boolean) => {
  show.value = isSystem.value ? false : !value
}
defineExpose({ open })
</script>
<template>
  <modal-dialog ref="modalDialog" :title="title" @confirm="handleSave">
    <template #content>
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="left"
        size="small"
        require-mark-placement="right-hanging">
        <n-form-item :label="$t('commons.name')" path="name">
          <n-input v-model:value="formData.name" />
        </n-form-item>
        <n-form-item :label="$t('commons.encode')" path="code">
          <n-input v-model:value="formData.code" />
        </n-form-item>
        <n-form-item :label="$t('group.type')" path="type">
          <n-select v-model:value="formData.type" :options="options" />
        </n-form-item>
        <n-form-item :label="$t('group.description')" path="description">
          <n-input v-model:value="formData.description" type="textarea" />
        </n-form-item>
        <n-form-item :label="$t('group.global_group')">
          <n-switch
            v-model:value="formData.global"
            :disabled="dialogType === 'edit' || formData.type === 'SYSTEM'"
            @update:value="handleChange(formData.global)" />
        </n-form-item>
        <n-form-item v-if="show" :label="showLabel" path="scopeId">
          <n-select
            v-model:value="formData.scopeId"
            :options="workspaces"
            :placeholder="$t('project.please_choose_workspace')"
            :disabled="dialogType === 'edit'"
            filterable
            clearable />
        </n-form-item>
      </n-form>
    </template>
  </modal-dialog>
</template>
<style></style>
