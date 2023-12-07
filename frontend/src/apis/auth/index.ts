import alovaInstance from ".."
import { IUSerDtoItem } from "../interface"

export interface ILoginParamItem {
  username: string
  password: string
}
export interface ILoginRespItem {
  access_token: string
  refresh_token: string
  userId: string
  roles: Array<string>
  user: IUSerDtoItem
}
/**
 * 登录
 * @param param 登录参数
 * @returns
 */
export const loginFunction = (param: ILoginParamItem) => alovaInstance.Post<ILoginRespItem>(`/auth/authenticate`, param)
export const logOutFunction = () => alovaInstance.Post("/auth/logout")
