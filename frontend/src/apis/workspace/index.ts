import { PageResponse, PageReq } from "../interface"
import alovaInstance from ".."

export interface WORKSPACE {
  id: null
  name: string
  description: string
  organizationId?: string
}

export const loadWorkspaceData = (param: PageReq) =>
  alovaInstance.Post(``, param)

export const createWorkspace = (param: WORKSPACE) =>
  alovaInstance.Post<WORKSPACE>(`/workspace/save`, param)
