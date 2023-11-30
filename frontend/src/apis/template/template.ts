import alovaInst from ".."
import { ICustomFieldOption } from "./custom-field"

export interface ICustomFieldTemp {
  id: string | undefined
  name: string
  scene: string
  type: string
  remark: string
  system: boolean
  global: boolean
  projectId: string
  options?: ICustomFieldOption[] | string
  required?: boolean
  disabled?: boolean
  fieldId?: string
  defaultValue?: []
  [key: string]: any
}

export const getCustomFieldDefault = (param: { projectId: string; scene: string }) =>
  alovaInst.Post<Array<ICustomFieldTemp>>(`/custom/field/default`, param)
export const getCustomFieldTemplates = (param: { templateId: string }) =>
  alovaInst.Post(`/custom/field/template/list`, param)
