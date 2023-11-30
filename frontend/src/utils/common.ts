import { h } from "vue"

import { NIcon } from "naive-ui"

/**
 * 渲染图标
 * @param icon 图标class
 */
export const renderIcon = (icon: string) => {
  return h(NIcon, null, { default: () => h("i", { class: icon }) })
}
