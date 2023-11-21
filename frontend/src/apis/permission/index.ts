import alovaInstance from ".."
import { IGroupDTO } from "../group"

export interface IUserGroupPermission {
  id: string | undefined
  groupId: string
  permissionId: string
  moduleId: string
}
export interface IPermissionDTO {
  id: string
  name: string
  resourceId: string
  checked: boolean
  license: boolean
}
export interface IResource {
  id: string
  name: string
  license: boolean
  global: boolean
}
export interface IGroupResourceDTO {
  group: IGroupDTO
  type: string
  userGroupPermissions: Array<IUserGroupPermission>
  permissions: Array<IPermissionDTO>
  resource: IResource
}
export interface IGroupPermissionDTO {
  permissions: Array<IGroupResourceDTO>
}

export const getUserGroupPermission = (param: IGroupDTO) =>
  alovaInstance.Post<IGroupPermissionDTO>(`/user/group/permission/list`, param)
