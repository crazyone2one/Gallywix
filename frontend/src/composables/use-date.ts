import { nextTick, onMounted, ref } from "vue"

export const useDate = () => {
  const date = new Date()
  const year = ref(date.getFullYear())
  const month = ref(date.getMonth())
  const week = ref()
  onMounted(() => {
    nextTick(() => {
      const beginDate = new Date(year.value, 0, 1) //本年的第一天
      //星期从0-6,0代表星期天，6代表星期六
      let endWeek = date.getDay()
      if (endWeek == 0) endWeek = 7
      var beginWeek = beginDate.getDay()
      if (beginWeek == 0) beginWeek = 7
      //计算两个日期的天数差
      var millisDiff = date.getTime() - beginDate.getTime()
      var dayDiff = Math.floor((millisDiff + (beginWeek - endWeek) * (24 * 60 * 60 * 1000)) / 86400000)
      week.value = Math.ceil(dayDiff / 7)
    })
  })
  return { year, week, month }
}
