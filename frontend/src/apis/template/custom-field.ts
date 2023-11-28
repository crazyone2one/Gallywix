import alovaInst from ".."
import { PageReq } from "../interface"
export interface ICustomField {
  id: string | undefined
  name: string
  scene: string
  type: string
  remark: string
  system: boolean
  global: boolean
}
/**
 * 列表数据查询
 * @param param 查询参数
 * @returns
 */
export const getCustomFieldPages = (param: PageReq) =>
  alovaInst.Post(`/custom/field/page`, param)
