import { useUserStore } from "../store/modules/user-store"

import { IUSerDtoItem } from "../apis/interface"

export const getCurrentUser = (): IUSerDtoItem => {
  const store = useUserStore()
  return store.userInfo
}

export const getCurrentUserId = (): string | undefined => {
  return getCurrentUser().id
}

export const getCurrentWorkspaceId = (): string => {
  const workspaceId = localStorage.getItem("workspace_id")
  if (workspaceId) {
    return workspaceId
  }
  return getCurrentUser().lastWorkspaceId
}

export const getCurrentProjectId = (): string => {
  const projectId = localStorage.getItem("project_id")
  if (projectId) {
    return projectId
  }
  return getCurrentUser().lastProjectId
}
