import { list2SelectOption } from "/@/utils/list-2-select"

import alovaInstance from ".."
import { IPage, PageReq } from "../interface"
import { SelectOption } from "naive-ui"

export interface IProject {
  id: string | undefined
  name: string
  description: string
  workspaceId: string | undefined
  memberSize?: number
}
export const loadData = (param: PageReq) =>
  alovaInstance.Post<IPage<IProject[]>>(`/system/project/page`, param)
export const loadProjectList = () =>
  alovaInstance.Get<IProject[]>(`/system/project/list`)
/**
 * 查询项目数据
 * @returns select option
 */
export const loadProjectOption = () =>
  alovaInstance.Get<SelectOption[]>(`/system/project/list`, {
    transformData(data) {
      return list2SelectOption(data as Array<IProject>)
    },
  })

export const getUserProjectList = (param: {
  userId: string
  workspaceId: string
}) =>
  alovaInstance.Post<SelectOption[]>(`/system/project/list/related`, param, {
    transformData(data) {
      return list2SelectOption(data as Array<IProject>)
    },
  })
/**
 * save project
 * @param param project
 * @returns
 */
export const saveData = (param: IProject) =>
  alovaInstance.Post<IProject>("/system/project/save", param)
/**
 * edit project
 * @param param project
 * @returns
 */
export const updateData = (param: IProject) =>
  alovaInstance.Put(`/system/project/update`, param)
/**
 * delete project by id
 * @param param project id
 * @returns
 */
export const deleteData = (param: string) =>
  alovaInstance.Delete(`/system/project/remove/${param}`)
