import alovaInstance from ".."
import { IPage, PageReq } from "../interface"
import { IPermissionDTO } from "../permission"
import { IProject } from "../project"
import { USER } from "../user"
import { IWorkspaceItem } from "../workspace"
import { SelectOption } from "naive-ui"
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
  workspaces?: Array<IWorkspaceItem>
  projects?: Array<IProject>
  ids?: Array<string>
  workspaceOptions?: Array<SelectOption>
  projectOptions?: Array<SelectOption>
  selects?: Array<string>
  showSearchGetMore?: boolean
}
export interface IUser2Group {
  userIds: Array<string>
  sourceIds: Array<string>
  groupId: string
}
export interface IModifyPermission {
  userGroupId: string
  permissions: Array<IPermissionDTO>
}
export const loadTableData = (param: PageReq) => alovaInstance.Post<IPage<IGroupDTO[]>>(`/system/group/page`, param)

export const saveGroup = (param: IGroupDTO) => alovaInstance.Post<IGroupDTO>("/system/group/save", param)

export const updateGroup = (param: IGroupDTO) => alovaInstance.Put<IGroupDTO>("/system/group/update", param)

export const getAllUserGroupByType = (param: { type: string }) =>
  alovaInstance.Post<Array<IGroupDTO>>(`/system/group/list-by-type`, param)

export const getUserAllGroups = (userId: string) => alovaInstance.Get<Array<IGroupDTO>>(`/system/group/all/${userId}`)

export const getGroupsByType = (param: { resourceId: string; projectId: string; type: string }) =>
  alovaInstance.Post<Array<IGroupDTO>>(`/system/group/list`, param)

export const modifyUserGroupPermission = (param: IModifyPermission) =>
  alovaInstance.Post(`/system/group/permission/edit`, param)

export const getUserGroup = (param: { userGroupId: string }) =>
  alovaInstance.Post<IPage<USER[]>>(`/system/group/user`, param)

export const addUser2Group = (param: IUser2Group) => alovaInstance.Post("/system/group/add/member", param)
/**
 * 查询用户所属group数据
 * @param workspaceId workspace id
 * @param userId user id
 * @returns
 */
export const getWorkspaceMemberGroup = (workspaceId: string, userId: string) =>
  alovaInstance.Get<Array<IGroupDTO>>(`/system/group/list/ws/${workspaceId}/${userId}`)
