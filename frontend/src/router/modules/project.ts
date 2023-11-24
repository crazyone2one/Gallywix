import { RouteRecordRaw } from "vue-router"
const Project: Array<RouteRecordRaw> = [
  {
    path: "/project",
    name: "project",
    redirect: "/project/home",
    component: () => import(`/@/views/project/ProjectSetting.vue`),
    meta: {
      title: "commons.project_setting",
    },
    children: [
      {
        path: "/project/home",
        name: "project-home",
        component: () => import(`/@/views/project/home/ProjectHome.vue`),
        meta: {
          title: "project.info",
        },
      },
    ],
  },
]
export default Project
