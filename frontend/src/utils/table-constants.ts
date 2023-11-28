export const USER_GROUP_SCOPE: { [key: string]: string } = {
  SYSTEM: "group.system",
  WORKSPACE: "group.workspace",
  PROJECT: "group.project",
  PERSONAL: "group.personal",
}

export const CUSTOM_FIELD_TYPE_OPTION: Array<{
  label: string
  value: string
  hasOption?: boolean
}> = [
  { value: "input", label: "workspace.custom_filed.input" },
  { value: "textarea", label: "workspace.custom_filed.textarea" },
  { value: "select", label: "workspace.custom_filed.select", hasOption: true },
  {
    value: "multipleSelect",
    label: "workspace.custom_filed.multipleSelect",
    hasOption: true,
  },
  { value: "radio", label: "workspace.custom_filed.radio", hasOption: true },
  {
    value: "checkbox",
    label: "workspace.custom_filed.checkbox",
    hasOption: true,
  },
  { value: "member", label: "workspace.custom_filed.member", hasOption: true },
  {
    value: "multipleMember",
    label: "workspace.custom_filed.multipleMember",
    hasOption: true,
  },
  { value: "date", label: "workspace.custom_filed.date" },
  { value: "datetime", label: "workspace.custom_filed.datetime" },
  { value: "richText", label: "workspace.custom_filed.richText" },
  { value: "int", label: "workspace.custom_filed.int" },
  { value: "float", label: "workspace.custom_filed.float" },
  { value: "multipleInput", label: "workspace.custom_filed.multipleInput" },
]

export const FIELD_TYPE_MAP: { [key: string]: string } = {
  input: "workspace.custom_filed.input",
  textarea: "workspace.custom_filed.textarea",
  select: "workspace.custom_filed.select",
  multipleSelect: "workspace.custom_filed.multipleSelect",
  radio: "workspace.custom_filed.radio",
  checkbox: "workspace.custom_filed.checkbox",
  member: "workspace.custom_filed.member",
  multipleMember: "workspace.custom_filed.multipleMember",
  date: "workspace.custom_filed.date",
  datetime: "workspace.custom_filed.datetime",
  richText: "workspace.custom_filed.richText",
  int: "workspace.custom_filed.int",
  float: "workspace.custom_filed.float",
  multipleInput: "workspace.custom_filed.multipleInput",
}

export const SCENE_MAP: { [key: string]: string } = {
  ISSUE: "workspace.issue_template_manage",
  TEST_CASE: "workspace.case_template_manage",
  PLAN: "workstation.table_name.track_plan",
  API: "workspace.api_template_manage",
}

export const SYSTEM_FIELD_NAME_MAP: { [key: string]: string } = {
  //用例字段
  用例状态: "custom_field.case_status",
  责任人: "custom_field.case_maintainer",
  用例等级: "custom_field.case_priority",
  //缺陷字段
  创建人: "custom_field.issue_creator",
  处理人: "custom_field.issue_processor",
  状态: "custom_field.issue_status",
  严重程度: "custom_field.issue_severity",
  // 测试计划
  测试阶段: "test_track.plan.plan_stage",
}
