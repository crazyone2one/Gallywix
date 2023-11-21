import alovaInstance from ".."
import { IPage, PageReq } from "../interface"
export interface IGroupDTO {
  id: string | undefined
  name: string
  code: string
  description: string
  system: boolean
  global: boolean
  type: string
  scopeId: string
  scopeName?: string
  createTime?: number
  creator?: string
  memberSize?: number
}
export const loadTableData = (param: PageReq) =>
  alovaInstance.Post<IPage<IGroupDTO[]>>(`/system/group/page`, param)

export const saveGroup = (param: IGroupDTO) =>
  alovaInstance.Post<IGroupDTO>("/system/group/save", param)

export const updateGroup = (param: IGroupDTO) =>
  alovaInstance.Put<IGroupDTO>("/system/group/update", param)
