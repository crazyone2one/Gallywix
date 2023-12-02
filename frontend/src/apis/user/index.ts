import alovaInstance from ".."
import { IGroupDTO } from "../group"
import { ICustomGroup, IPage, IUserGroupPermission, PageReq } from "../interface"

export interface USER {
  id: string | undefined
  username: string
  nickname: string
  password: string
  status: boolean
  email: string
  phone: string
  createTime?: number
  userGroups: Array<ICustomGroup>
  groups: Array<IGroupDTO>
  lastProjectId: string
  lastWorkspaceId: string
  groupPermissions: Array<{ group: IGroupDTO }>
  newPassword?: string
  confirmpassword?: string
}
export const loadUserData = (param: PageReq) => alovaInstance.Post<IPage<USER[]>>(`/system/user/page`, param)
/**
 * save user
 * @param param user
 * @returns
 */
export const saveUserData = (param: USER) => alovaInstance.Post<USER>("/system/user/save", param)
/**
 * edit user
 * @param param user
 * @returns
 */
export const updateUserData = (param: USER) => alovaInstance.Put<USER>(`/system/user/update`, param)
/**
 * delete user by id
 * @param param user id
 * @returns
 */
export const deleteUserData = (param: string) => alovaInstance.Delete(`/system/user/remove/${param}`)

export const logOut = () => alovaInstance.Post("/auth/logout")

export const getUserInfo = (id: string) => alovaInstance.Get(`/system/user/getInfo${id}`)

export const switchUserRole = (sign: string, sourceId: string) =>
  alovaInstance.Post(`/system/user//switch/source/${sign}/${sourceId}`)

/**
 * 获取workspace关联的用户数据
 * @param param 查询参数
 */
export const getWorkspaceMemberListSpecial = (param: PageReq) =>
  alovaInstance.Post<IPage<USER[]>>(`/system/user/special/ws/member/page`, param)

export const getUserList = () => alovaInstance.Get<USER[]>(`/system/user/list`)

export const specialGetUserGroup = (userId: string) =>
  alovaInstance.Get<IUserGroupPermission>(`/system/user/special/user/group/${userId}`)

export const addWorkspaceMemberSpecial = (param: {
  groupIds: Array<string>
  userIds: Array<string>
  workspaceId: string
}) => alovaInstance.Post(`/system/group/special/ws/member/add`, param)

export const specialModifyPassword = (user: USER) => alovaInstance.Post(`/system/user/special/password`, user)
