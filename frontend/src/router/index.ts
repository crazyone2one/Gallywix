import { RouteRecordRaw, createRouter, createWebHashHistory } from "vue-router"
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    redirect: "/dashboard",
    component: () => import(`/@/components/layout/BaseLayout.vue`),
    children: [
      {
        path: "/dashboard",
        name: "dashboard",
        component: () => import(`/@/views/home/HomeView.vue`),
        meta: { title: "首页", requiresAuth: true },
      },
      {
        path: "/workspace",
        name: "workspace",
        component: () => import(`/@/views/setting/workspace/WorkspaceView.vue`),
        meta: { title: "工作空间管理", requiresAuth: true },
      },
    ],
  },
  {
    path: "/login",
    name: "login",
    component: () => import(`/@/views/login/LoginView.vue`),
    meta: {
      title: "登录页",
      requiresAuth: false,
    },
  },
]

const router = createRouter({
  history: createWebHashHistory(), // HTML5 History モード
  routes,
  scrollBehavior() {
    // always scroll to top
    return { top: 0 }
  },
})

export default router
