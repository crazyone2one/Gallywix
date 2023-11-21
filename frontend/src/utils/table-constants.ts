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
