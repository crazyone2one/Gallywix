import { PageResponse } from "../interface"
import alovaInstance from ".."
export interface IUser {
  id: number | null
  username: string
  nickname: string
  password: string
  userStatus: boolean
  email?: string
  phone?: string
}
