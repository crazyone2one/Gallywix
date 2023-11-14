import { IPage, PageReq } from "../interface"
import alovaInstance from ".."
import { SelectOption } from "naive-ui"
export interface PROJECT {
  id: string | null
  name: string
  description: string
  workspaceId?: string
}
export const loadData = (param: PageReq) => alovaInstance.Post<IPage<PROJECT[]>>(`/system/project/page`, param)
export const loadProjectList = () => alovaInstance.Get<PROJECT[]>(`/system/project/list`)
export const loadProjectOption = () =>
  alovaInstance.Get<SelectOption[]>(`/system/project/list`, {
    transformData(data) {
      const options: SelectOption[] = []
      const _data = data as PROJECT[]
      _data.forEach((item) => {
        const option: SelectOption = {}
        option.label = item.name
        option.value = item.id as string
        options.push(option)
      })
      return options
    },
  })
/**
 * save project
 * @param param project
 * @returns
 */
export const saveData = (param: PROJECT) => alovaInstance.Post<PROJECT>("/system/project/save", param)
/**
 * edit project
 * @param param project
 * @returns
 */
export const updateData = (param: PROJECT) => alovaInstance.Put(`/system/project/update`, param)
/**
 * delete project by id
 * @param param project id
 * @returns
 */
export const deleteData = (param: string) => alovaInstance.Delete(`/system/project/remove/${param}`)
