import { IPage } from "../interface"
import alovaInstance from ".."

export interface WORKSPACE {
  id: null
  name: string
  description: string
  organizationId?: string
}

export const loadWorkspaceData = () =>
  alovaInstance.Get<IPage<WORKSPACE[]>>(`/workspace/page`)

export const createWorkspace = (param: WORKSPACE) =>
  alovaInstance.Post<WORKSPACE>(`/workspace/save`, param)
