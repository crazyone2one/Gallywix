import alovaInstance from ".."
import { IPage, PageReq } from "../interface"
export interface USER {
  id: string | undefined
  username: string
  nickname: string
  password: string
  status: boolean
  email: string
  phone: string
  createTime?: number
}
export const loadUserData = (param: PageReq) =>
  alovaInstance.Post<IPage<USER[]>>(`/system/user/page`, param)
/**
 * save user
 * @param param user
 * @returns
 */
export const saveUserData = (param: USER) =>
  alovaInstance.Post<USER>("/system/user/save", param)
/**
 * edit user
 * @param param user
 * @returns
 */
export const updateUserData = (param: USER) =>
  alovaInstance.Put(`/system/user/update`, param)
/**
 * delete user by id
 * @param param user id
 * @returns
 */
export const deleteUserData = (param: string) =>
  alovaInstance.Delete(`/system/user/remove/${param}`)

export const logOut = () => alovaInstance.Post("/auth/logout")

export const getUserInfo = (id: string) =>
  alovaInstance.Get(`/system/user/getInfo${id}`)

export const switchUserRole = (sign: string, sourceId: string) =>
  alovaInstance.Post(`/system/user//switch/source/${sign}/${sourceId}`)

/**
 * 获取workspace关联的用户数据
 * @param param 查询参数
 */
export const getWorkspaceMemberListSpecial = (param: PageReq) =>
  alovaInstance.Post<IPage<USER[]>>(
    `/system/user/special/ws/member/page`,
    param,
  )

export const getUserList = () => alovaInstance.Get<USER[]>(`/system/user/list`)
