import alovaInstance from ".."
import { IGroupDTO } from "../group"
import { IPage, PageReq } from "../interface"
import { USER } from "../user"

export interface IWorkspace {
  id: string | undefined
  name: string
  description: string
  organizationId?: string
  memberSize?: number
}

export const loadWorkspaceData = (page: number, pageSize: number, param: PageReq) => {
  param.pageNumber = page
  param.pageSize = pageSize
  return alovaInstance.Post<IPage<IWorkspace>>(`/workspace/page`, param)
}

export const createWorkspace = (param: IWorkspace) => alovaInstance.Post<IWorkspace>(`/workspace/save`, param)
export const loadList = () => alovaInstance.Get<IWorkspace[]>(`/workspace/list`)
/**
 * 查询workspace数据
 * @returns select option
 */
export const loadOptionList = () => alovaInstance.Get(`/workspace/list`)
/**
 * 删除workspace
 * @param id workspace id
 */
export const delWorkspaceSpecial = (id: string) => alovaInstance.Delete(`/workspace/remove/${id}`)

export const getGroupResource = (groupId: string, groupType: string) =>
  alovaInstance.Get<IGroupDTO>(`/workspace/list/resource/${groupId}/${groupType}`)

export const switchWorkspace = (workspaceId?: string) =>
  alovaInstance.Get<USER>(`/system/user/switch/source/workspace/${workspaceId}`)
