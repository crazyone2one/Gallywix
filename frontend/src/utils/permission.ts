import { getCurrentProjectID, getCurrentUser, getCurrentWorkspaceId } from "./token"

export const hasPermission = (permission: string): boolean => {
  const user = getCurrentUser()
  if (!user || !user.groups) {
    return false
  }
  const index = user.groups.findIndex((g) => g.id === "super_group")
  if (index !== -1) {
    return true
  }
  user.userGroups?.forEach((ug) => {
    user.groupPermissions?.forEach((gp) => {
      if (gp.group.id === ug.groupId) {
        ug.userGroupPermissions = gp.userGroupPermissions
        ug.group = gp.group
      }
    })
  })
  const currentProjectPermissions = user.userGroups
    ?.filter((ug) => ug.group && ug.group.type === "PROJECT")
    .filter((ug) => ug.sourceId === getCurrentProjectID())
    .flatMap((ug) => ug.userGroupPermissions)
    .map((g) => g?.permissionId)
    .reduce((total, current) => {
      total.add(current)
      return total
    }, new Set())
  if (currentProjectPermissions) {
    for (const p of currentProjectPermissions) {
      if (p === permission) {
        return true
      }
    }
  }

  const currentWorkspacePermissions = user.userGroups
    ?.filter((ug) => ug.group && ug.group.type === "WORKSPACE")
    .filter((ug) => ug.sourceId === getCurrentWorkspaceId())
    .flatMap((ug) => ug.userGroupPermissions)
    .map((g) => g?.permissionId)
    .reduce((total, current) => {
      total.add(current)
      return total
    }, new Set())
  if (currentWorkspacePermissions) {
    for (const p of currentWorkspacePermissions) {
      if (p === permission) {
        return true
      }
    }
  }

  const systemPermissions = user.userGroups
    ?.filter((gp) => gp.group && gp.group.type === "SYSTEM")
    .filter((ug) => ug.sourceId === "system" || ug.sourceId === "adminSourceId")
    .flatMap((ug) => ug.userGroupPermissions)
    .map((g) => g?.permissionId)
    .reduce((total, current) => {
      total.add(current)
      return total
    }, new Set())

  if (systemPermissions) {
    for (const p of systemPermissions) {
      if (p === permission) {
        return true
      }
    }
  }

  return false
}

export const hasPermissions = (...permissions: Array<string>): boolean => {
  for (const p of permissions) {
    if (hasPermission(p)) {
      return true
    }
  }
  return false
}
