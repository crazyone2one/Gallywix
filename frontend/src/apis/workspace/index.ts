import { list2SelectOption } from "/@/utils/list-2-select"

import alovaInstance from ".."
import { IGroupDTO } from "../group"
import { IPage, PageReq } from "../interface"
import { USER } from "../user"
import { SelectOption } from "naive-ui"

export interface IWorkspaceItem {
  id: string
  name: string
  description: string
  organizationId?: string
  memberSize?: number
}

export const loadWorkspaceData = (param: PageReq) =>
  alovaInstance.Post<IPage<IWorkspaceItem[]>>(`/workspace/page`, param)

export const createWorkspace = (param: IWorkspaceItem) => alovaInstance.Post<IWorkspaceItem>(`/workspace/save`, param)
export const loadList = () => alovaInstance.Get<IWorkspaceItem[]>(`/workspace/list`)
/**
 * 查询workspace数据
 * @returns select option
 */
export const loadOptionList = () =>
  alovaInstance.Get<SelectOption[]>(`/workspace/list`, {
    transformData(data) {
      return list2SelectOption(data as Array<IWorkspaceItem>)
    },
  })
/**
 * 删除workspace
 * @param id workspace id
 */
export const delWorkspaceSpecial = (id: string) => alovaInstance.Delete(`/workspace/remove/${id}`)

export const getGroupResource = (groupId: string, groupType: string) =>
  alovaInstance.Get<IGroupDTO>(`/workspace/list/resource/${groupId}/${groupType}`)

export const switchWorkspace = (workspaceId?: string) =>
  alovaInstance.Get<USER>(`/system/user/switch/source/workspace/${workspaceId}`)
