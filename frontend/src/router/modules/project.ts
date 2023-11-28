import { RouteRecordRaw } from "vue-router"
const Project: Array<RouteRecordRaw> = [
  {
    path: "/project",
    name: "project-manage",
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
      {
        path: "/project/template",
        name: "template",
        component: () =>
          import(`/@/views/project/template/TemplateSetting.vue`),
        meta: {
          title: "workspace.template_manage",
        },
      },
    ],
  },
]
export default Project
