import { list2SelectOption } from "/@/utils/list-2-select"

import alovaInstance from ".."
import { IPage, PageReq } from "../interface"
import { SelectOption } from "naive-ui"

export interface WORKSPACE {
  id: string | undefined
  name: string
  description: string
  organizationId?: string
  memberSize?: number
}

export const loadWorkspaceData = (param: PageReq) =>
  alovaInstance.Post<IPage<WORKSPACE[]>>(`/workspace/page`, param)

export const createWorkspace = (param: WORKSPACE) =>
  alovaInstance.Post<WORKSPACE>(`/workspace/save`, param)
export const loadList = () => alovaInstance.Get<WORKSPACE[]>(`/workspace/list`)
/**
 * 查询workspace数据
 * @returns select option
 */
export const loadOptionList = () =>
  alovaInstance.Get<SelectOption[]>(`/workspace/list`, {
    transformData(data) {
      return list2SelectOption(data as Array<WORKSPACE>)
    },
  })
/**
 * 删除workspace
 * @param id workspace id
 */
export const delWorkspaceSpecial = (id: string) =>
  alovaInstance.Delete(`/workspace/remove/${id}`)
