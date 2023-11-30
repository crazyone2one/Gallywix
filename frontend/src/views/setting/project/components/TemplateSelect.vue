<script setup lang="ts">
import { computed, onMounted, ref } from "vue"

import { list2SelectOption } from "/@/utils/list-2-select"

import { IProject } from "/@/apis/project"
import { getFieldTemplateCaseOption } from "/@/apis/template/test-case-template"
import { useRequest } from "alova"
import { SelectOption } from "naive-ui"

interface IProps {
  projectId: string | undefined
  scene: string
  data: IProject
  prop: string
}
const props = withDefaults(defineProps<IProps>(), {})
const templateOptions = ref<Array<SelectOption>>([])
const emits = defineEmits(["update:data"])
const _form = computed({
  get: () => props.data,
  set: (val) => {
    emits("update:data", val)
  },
})
const { send: sendTemplateCaseOption } = useRequest((val) => getFieldTemplateCaseOption(val), { immediate: false })
const getTemplateOptions = async () => {
  const projectId = props.projectId || ""
  let promise
  if (props.scene === "ISSUE") {
    // 获取issue模板
  } else if (props.scene === "API") {
    // 获取接口模板
  } else {
    promise = sendTemplateCaseOption(projectId)
  }
  promise?.then((res) => {
    templateOptions.value = list2SelectOption(res)
  })
}
onMounted(() => {
  getTemplateOptions()
})
</script>
<template>
  <n-select v-model:value="_form[prop]" :options="templateOptions" filterable clearable />
</template>
<style scoped></style>
