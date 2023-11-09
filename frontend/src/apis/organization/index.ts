import { IPage, PageReq } from "../interface"
import alovaInstance from ".."
export interface ORGANIZATION {
  id: number | null
  name: string
  description: string
  createTime?: number
}
export const loadTableData = (param: PageReq) =>
  alovaInstance.Post<IPage<ORGANIZATION[]>>(`/system/organization/page`, param)
/**
 * save organization
 * @param param organization
 * @returns
 */
export const saveData = (param: ORGANIZATION) => alovaInstance.Post<ORGANIZATION>("/system/organization/save", param)
/**
 * edit organization
 * @param param organization
 * @returns
 */
export const updateData = (param: ORGANIZATION) => alovaInstance.Put(`/system/organization/update`, param)
/**
 * delete organization by id
 * @param param organization id
 * @returns
 */
export const deleteData = (param: string) => alovaInstance.Delete(`/system/organization/remove/${param}`)
