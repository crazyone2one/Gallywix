import { list2SelectOption } from "/@/utils/list-2-select"

import alovaInstance from ".."
import { ICustomGroup, IPage, PageReq } from "../interface"
import { USER } from "../user"
import { SelectOption } from "naive-ui"

export interface IWorkspace {
  id: string | undefined
  name: string
  description: string
  organizationId?: string
  memberSize?: number
}

export const loadWorkspaceData = (param: PageReq) => alovaInstance.Post<IPage<IWorkspace[]>>(`/workspace/page`, param)

export const createWorkspace = (param: IWorkspace) => alovaInstance.Post<IWorkspace>(`/workspace/save`, param)
export const loadList = () => alovaInstance.Get<IWorkspace[]>(`/workspace/list`)
/**
 * 查询workspace数据
 * @returns select option
 */
export const loadOptionList = () =>
  alovaInstance.Get<SelectOption[]>(`/workspace/list`, {
    transformData(data) {
      return list2SelectOption(data as Array<IWorkspace>)
    },
  })
/**
 * 删除workspace
 * @param id workspace id
 */
export const delWorkspaceSpecial = (id: string) => alovaInstance.Delete(`/workspace/remove/${id}`)

export const getGroupResource = (groupId: string, groupType: string) =>
  alovaInstance.Get<ICustomGroup>(`/workspace/list/resource/${groupId}/${groupType}`)

export const switchWorkspace = (workspaceId?: string) =>
  alovaInstance.Get<USER>(`/system/user/switch/source/workspace/${workspaceId}`)
