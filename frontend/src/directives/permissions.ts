import { DirectiveBinding } from "vue"

import { useAuthStore } from "/@/store/auth-store"
interface ElType extends HTMLElement {
  __handleClick__: () => any
}

const checkPermission = (el: ElType, binding: DirectiveBinding) => {
  const { value } = binding
  const store = useAuthStore()
  const roles = store.roles
  if (value && value instanceof Array) {
    if (value.length > 0) {
      const elRoles = value

      const hasPermission = roles.some((role) => {
        return elRoles.includes(role)
      })

      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
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
