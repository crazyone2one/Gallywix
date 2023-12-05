import alovaInstance from ".."
import { IUSerDto } from "../interface"

export interface ILoginParam {
  username: string
  password: string
}
export interface ILoginResponse {
  access_token: string
  refresh_token: string
  userId: string
  // roles: Array<string>
  user: IUSerDto
}
/**
 * 登录
 * @param param 登录参数
 * @returns
 */
export const loginFunction = (param: ILoginParam) => alovaInstance.Post<ILoginResponse>(`/auth/authenticate`, param)
export const logOutFunction = () => alovaInstance.Post("/auth/logout")
