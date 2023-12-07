import { DirectiveBinding } from "vue"

import { hasPermissions } from "../utils/permission"

interface ElType extends HTMLElement {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  __handleClick__: () => any
}

const checkPermission = (el: ElType, binding: DirectiveBinding) => {
  const { value } = binding
  if (value && value instanceof Array && value.length > 0) {
    const _hasPermission = hasPermissions(...value)
    if (!_hasPermission) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  } else {
    throw new Error(`need a array like ['role1', 'role2']`)
  }
}
export default {
  mounted(el: ElType, binding: DirectiveBinding) {
    checkPermission(el, binding)
  },
  updated(el: ElType, binding: DirectiveBinding) {
    checkPermission(el, binding)
  },
}
