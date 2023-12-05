import { useUserStore } from "../store/modules/user"

import { IUSerDto } from "../apis/interface"

export const getCurrentUser = (): IUSerDto => {
  const store = useUserStore()
  return store.userInfo
}
export const getCurrentUserId = (): string => {
  return getCurrentUser().id as string
}

export const getCurrentWorkspaceId = (): string => {
  const workspaceId = sessionStorage.getItem("workspace_id")
  if (workspaceId) {
    return workspaceId
  }
  return getCurrentUser().lastWorkspaceId
}

export const getCurrentProjectID = (): string => {
  const workspaceId = sessionStorage.getItem("project_id")
  if (workspaceId) {
    return workspaceId
  }
  return getCurrentUser().lastProjectId
}
