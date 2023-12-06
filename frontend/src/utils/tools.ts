// import { IMenubarList } from "../types/store/layout"
export interface ILocalStore {
  startTime: number
  expires: number
  [propName: string]: number
}
/**
 * 睡眠函数
 * @param time
 */
export const sleep = async (time: number): Promise<void> => {
  await new Promise((resolve) => {
    setTimeout(() => resolve, time)
  })
}

export const getLocal = <T>(name: string): T => {
  const item = localStorage.getItem(name)
  return JSON.parse(item !== null ? item : "{}")
}

export const setLocal = <T>(name: string, value: T, pExpires = 1000 * 60 * 60 * 24 * 365 * 100): void => {
  const d = value as ILocalStore
  d.startTime = Date.now()
  d.expires = pExpires
  localStorage.setItem(name, JSON.stringify(value))
}

export const uuid = (): string => {
  const buf = new Uint32Array(4)
  window.crypto.getRandomValues(buf)
  let idx = -1
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
    idx++
    const r = (buf[idx >> 3] >> ((idx % 8) * 4)) & 15
    const v = c == "x" ? r : (r & 0x3) | 0x8
    return v.toString(16)
  })
}
