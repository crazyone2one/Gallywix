import alovaInst from ".."
import { ICustomFieldTemp } from "./template"
// import { IPage, PageReq } from "../interface"
export interface ITestCaseStep {
  num: number
  desc: string
  result: string
}
export interface ITestCaseTemplate {
  id: string | undefined
  name: string
  description: string
  type: string
  caseName: string
  system: boolean
  global: boolean
  prerequisite: string
  stepDescription: string
  expectedResult: string
  actualResult: string
  projectId: string
  stepModel: string
  steps: Array<ITestCaseStep>
  platform: string
  customFields?: Array<ICustomFieldTemp>
  [key: string]: string | number | boolean | Array<ITestCaseStep> | Array<ICustomFieldTemp> | undefined
}
/**
 * 列表数据查询
 * @param param 查询参数
 * @returns
 */
export const handleResourceSave = (param: ITestCaseTemplate) =>
  alovaInst.Post<string>(`/field/template/case/save`, param)
export const handleResourceUpdate = (param: ITestCaseTemplate) =>
  alovaInst.Put<string>(`/field/template/case/update`, param)