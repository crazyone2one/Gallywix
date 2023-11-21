import { list2SelectOption } from "/@/utils/list-2-select"

import alovaInstance from ".."
import { IPage, PageReq } from "../interface"
import { SelectOption } from "naive-ui"

export interface PROJECT {
  id: string | undefined
  name: string
  description: string
  workspaceId: string | undefined
  memberSize?: number
}
export const loadData = (param: PageReq) =>
  alovaInstance.Post<IPage<PROJECT[]>>(`/system/project/page`, param)
export const loadProjectList = () =>
  alovaInstance.Get<PROJECT[]>(`/system/project/list`)
/**
 * 查询项目数据
 * @returns select option
 */
export const loadProjectOption = () =>
  alovaInstance.Get<SelectOption[]>(`/system/project/list`, {
    transformData(data) {
      return list2SelectOption(data as Array<PROJECT>)
    },
  })
/**
 * save project
 * @param param project
 * @returns
 */
export const saveData = (param: PROJECT) =>
  alovaInstance.Post<PROJECT>("/system/project/save", param)
/**
 * edit project
 * @param param project
 * @returns
 */
export const updateData = (param: PROJECT) =>
  alovaInstance.Put(`/system/project/update`, param)
/**
 * delete project by id
 * @param param project id
 * @returns
 */
export const deleteData = (param: string) =>
  alovaInstance.Delete(`/system/project/remove/${param}`)
