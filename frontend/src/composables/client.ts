import { ref, onMounted, nextTick } from "vue"

/**
 * 获取页面高度
 * @returns 页面高度
 */
export const useClientHeight = () => {
  const height = ref(window.innerHeight)
  onMounted(() => {
    nextTick(() => {
      // 监听窗口大小变化，主要监听高度的变化
      window.addEventListener("resize", () => {
        height.value = document.documentElement.clientHeight
      })
    })
  })
  return { height }
}
