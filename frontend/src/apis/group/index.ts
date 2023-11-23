import alovaInstance from ".."
import { IPage, PageReq } from "../interface"
import { IProject } from "../project"
import { IWorkspace } from "../workspace"
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

export const getAllUserGroupByType = (param: { type: string }) =>
  alovaInstance.Post<Array<IGroupDTO>>(`/system/group/list-by-type`, param)

export const getUserAllGroups = (userId: string) =>
  alovaInstance.Get<
    Array<{
      ids: Array<string>
      type: string
      workspaces?: Array<IWorkspace>
      projects?: Array<IProject>
    }>
  >(`/system/group/all/${userId}`)

export const getGroupsByType = (param: {
  resourceId: string
  projectId: string
  type: string
}) => alovaInstance.Post<Array<IGroupDTO>>(`/system/group/list`, param)
