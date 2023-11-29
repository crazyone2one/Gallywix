import alovaInst from ".."
import { IPage, PageReq } from "../interface"
import { SelectOption } from "naive-ui"

export interface ICustomFieldOption extends SelectOption {
  label: string
  value: string
  system?: boolean
}
export interface ICustomField {
  id: string | undefined
  name: string
  scene: string
  type: string
  remark: string
  system: boolean
  global: boolean
  projectId: string
  options?: ICustomFieldOption[] | string
}
/**
 * 列表数据查询
 * @param param 查询参数
 * @returns
 */
export const getCustomFieldPages = (param: PageReq) =>
  alovaInst.Post<IPage<Array<ICustomField>>>(`/custom/field/page`, param)

export const saveCustomField = (param: ICustomField) => alovaInst.Post<string>(`/custom/field/save`, param)
export const updateCustomField = (param: ICustomField) => alovaInst.Put<string>(`/custom/field/update`, param)
