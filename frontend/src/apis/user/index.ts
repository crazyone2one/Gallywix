import { IPage, PageReq } from "../interface"
import alovaInstance from ".."
export interface USER {
  id: number | null
  username: string
  nickname: string
  password: string
  status: boolean
  email: string
  phone: string
  createTime?: number
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
export const updateUserData = (param: USER) => alovaInstance.Put(`/system/user/update`, param)
/**
 * delete user by id
 * @param param user id
 * @returns
 */
export const deleteUserData = (param: string) => alovaInstance.Delete(`/system/user/remove/${param}`)

export const logOut = () => alovaInstance.Post("/auth/logout")
