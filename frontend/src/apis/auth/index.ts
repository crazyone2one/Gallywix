import alovaInstance from ".."
import { GenericResponse } from "../interface"
export interface LOGIN {
  username: string
  password: string
}
export interface LoginResponse {
  access_token: string
  refresh_token: string
  user_id: number
  roles: Array<string>
}
/**
 * 登录
 * @param param0 登录参数
 * @returns
 */
export const loginFunction = (param: LOGIN) =>
  alovaInstance.Post<LoginResponse>(`/auth/authenticate`, param)
