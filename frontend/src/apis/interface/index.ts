import { IProject } from "../project"
import { IWorkspace } from "../workspace"
import { SelectOption } from "naive-ui"

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
  records: T
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

export interface ITableDataInfo<T> {
  data?: T
  total: number
}

export interface ICustomGroup {
  ids: Array<string>
  type: string
  workspaces?: Array<IWorkspace>
  projects?: Array<IProject>
  showSearchGetMore?: boolean
  workspaceOptions?: Array<SelectOption>
  projectOptions?: Array<SelectOption>
  selects?: Array<string> | undefined
}
