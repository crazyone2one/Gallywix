import alovaInst from ".."
import { IPage, PageReq } from "../interface"

export interface ICustomFieldOption {
  label: string
  value: string
  system: boolean
}
export interface ICustomField {
  id: string | undefined
  name: string
  scene: string
  type: string
  remark: string
  system: boolean
  global: boolean
  options?: ICustomFieldOption[]
}
/**
 * 列表数据查询
 * @param param 查询参数
 * @returns
 */
export const getCustomFieldPages = (param: PageReq) =>
  alovaInst.Post<IPage<Array<ICustomField>>>(`/custom/field/page`, param)
