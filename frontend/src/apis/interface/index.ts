import { IGroupDTO } from "../group"
import { IGroupResourceDTO, IUserGroupPermission } from "../permission"
import { IProject } from "../project"
import { IWorkspace } from "../workspace"

/**
 * 通用返回结果（根据后端定义的返回类型更改）
 */
export interface GenericResponse {
  code: number
  message: string
  status: boolean
}
// mybatis-flex分页查询返回类型
export interface IPage<T> {
  empty?: boolean
  pageNumber: number
  pageSize: number
  totalPage: number
  totalRow: number
  records: Array<T>
}
/**
 * 分页结果类型
 */
export interface PageResponse<T> extends GenericResponse {
  data: IPage<T>
}
/**
 * 查询参数类型
 */
export interface PageReq {
  name?: string
  pageNumber: number
  pageSize: number
  ids?: Array<number | string>
  projectId?: string
  filters?: {
    scene?: string[]
    type?: string[]
    status?: string[]
    state?: string[]
  }
  [key: string]: string | number | Array<number> | unknown | []
}

export interface IPaginationData<T> {
  data?: Array<T>
  total: number
}

export interface ICustomGroup {
  ids: Array<string>
  type: string
  groupId: string
  workspaces?: Array<IWorkspace>
  projects?: Array<IProject>
  showSearchGetMore?: boolean
  workspaceOptions?: Array<object>
  projectOptions?: Array<object>
  selects?: Array<string> | undefined
  userGroupPermissions?: Array<IUserGroupPermission>
  group?: IGroupDTO
  sourceId?: string
}

export interface IUserGroup {
  id: string
  userId: string
  groupId: string
}

export interface IUSerDto {
  id: string | undefined
  username: string
  nickname: string
  password: string
  status: boolean
  email: string
  phone: string
  createTime?: number
  lastProjectId: string
  lastWorkspaceId: string
  userGroups?: Array<ICustomGroup>
  groupPermissions?: Array<IGroupResourceDTO>
  groups?: Array<IGroupDTO>
}
