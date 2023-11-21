import { USER } from "../apis/user"
import { SelectOption } from "naive-ui"

export const list2SelectOption = (val: Array<any>) => {
  const result: SelectOption[] = []
  val.forEach((item) => {
    const option: SelectOption = {}
    option.label = item.name
    option.value = item.id
    result.push(option)
  })
  return result
}

export const userList2SelectOption = (val: Array<USER>) => {
  const result: SelectOption[] = []
  val.forEach((item) => {
    const option: SelectOption = {}
    option.label = item.username
    option.value = item.id
    result.push(option)
  })
  return result
}
