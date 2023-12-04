/**
 * 设置全局语言
 * @param local 语言类型
 */
export const setHtmlLang = (local: "zh-CN" | "en") => {
  document.querySelector("html")?.setAttribute("lang", local)
}
