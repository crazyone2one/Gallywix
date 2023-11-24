import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router"

import { useAuthStore } from "../store/auth-store"

import { i18n } from "../i18n"
import Project from "./modules/project"
import Setting from "./modules/setting"
// 路由信息
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    redirect: "/workstation",
    component: () => import(`/@/components/layout/BaseLayout.vue`),
    children: [
      {
        path: "/workstation",
        name: "workstation",
        component: () => import(`/@/views/home/HomeView.vue`),
        meta: { title: "commons.my_workstation", requiresAuth: true },
      },
      ...Setting,
      ...Project,
      {
        path: "/demo/upload",
        name: "upload",
        component: () => import(`/@/views/demo/upload/index.vue`),
        meta: { title: "上传", roles: ["admin"] },
      },

      {
        path: "/createTest",
        component: () => import(`/@/views/test-plan/index.vue`),
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

router.beforeEach((to, _from, next) => {
  const store = useAuthStore()
  const isAuthenticated = store.accessToken || ""
  if (to.path === "/login") {
    isAuthenticated ? next("/") : next()
  } else {
    if (!isAuthenticated) {
      store.restAuthStore()
      next(
        `/login?redirect=${to.path}&params=${JSON.stringify(
          to.query ? to.query : to.params,
        )}`,
      )
    } else if (isAuthenticated && to.path === "login") {
      next("/")
    } else {
      next()
    }
  }
})
router.afterEach((to) => {
  // TODO: title from sfc custom block?
  // const current = to.matched[to.matched.length - 1].components.default
  // const title = current.title ?? current.name
  const items = [import.meta.env.VITE_APP_TITLE]
  to.meta.title != null && items.unshift(i18n.t(to.meta.title as string))
  document.title = items.join(" | ")
})

export default router
