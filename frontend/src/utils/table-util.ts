import { ICustomFieldTemp } from "/@/apis/template/template"
export const getCustomFieldsKeys = (customFields: Array<ICustomFieldTemp>) => {
  const keys = new Set()
  customFields.forEach((item) => {
    if (item.key) {
      keys.add(item.key)
    }
  })
  return keys
}

export const generateTableHeaderKey = (keys: Set<string>) => {
  const customFieldKeys = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
  for (let i = 0; i < customFieldKeys.length; i++) {
    const key = customFieldKeys[i]
    if (keys.has(key)) {
      continue
    }
    keys.add(key)
    return key
  }
  return ""
}
