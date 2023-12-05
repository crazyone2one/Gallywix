import { RouteRecordRaw } from "vue-router"
const Setting: Array<RouteRecordRaw> = [
  {
    path: "/setting",
    name: "Setting",
    component: () => import(`/@/views/setting/index.vue`),
    meta: {
      title: "commons.user",
      permissions: ["SYSTEM_USER:READ"],
      system: true,
    },
  },
]
export default Setting
