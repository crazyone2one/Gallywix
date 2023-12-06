import { RouteRecordRaw } from "vue-router"
const Setting: RouteRecordRaw = {
  path: "/setting",
  name: "Setting",
  redirect: "/setting/dashboard",
  component: () => import(`/@/views/setting/index.vue`),
  children: [
    {
      path: "dashboard",
      name: "SettingHome",
      component: () => import(`/@/views/setting/components/SettingHome.vue`),
      meta: {
        title: "commons.system_setting",
      },
    },
    {
      path: "user",
      component: () => import(`/@/views/setting/user/index.vue`),
      meta: { system: true, title: "commons.user", permissions: ["SYSTEM_USER:READ"] },
    },
    {
      path: "systemworkspace",
      component: () => import(`/@/views/setting/workspace/index.vue`),
      meta: { system: true, title: "commons.workspace", permissions: ["SYSTEM_WORKSPACE:READ"] },
    },
    {
      path: "usergroup",
      component: () => import(`/@/views/setting/group/index.vue`),
      meta: {
        system: true,
        title: "group.group_permission",
        permissions: ["SYSTEM_GROUP:READ"],
      },
    },
    {
      path: "member",
      component: () => import(`/@/views/setting/member/index.vue`),
      meta: { workspace: true, title: "commons.member", permissions: ["WORKSPACE_USER:READ"] },
    },
    {
      path: 'project/:type',
      component: () => import(`/@/views/setting/project/index.vue`),
      meta: {workspace: true, title: 'project.manager', permissions: ['WORKSPACE_PROJECT_MANAGER:READ']}
    },
  ],
}

export default Setting
