import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router"

import { useUserStore } from "../store/modules/user"

import { i18n } from "../i18n"
// 路由信息
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    redirect: "/dashboard",
    component: () => import(`/@/components/layout/BaseLayout.vue`),
    children: [{ path: "/dashboard", name: "dashboard", component: () => import(`/@/views/home/HomeView.vue`) }],
  },
  {
    path: "/login",
    name: "login",
    component: () => import(`/@/views/login/index.vue`),
    meta: {
      title: "commons.login",
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
  const { status, logout } = useUserStore()
  const isAuthenticated = status.ACCESS_TOKEN || ""
  if (to.path === "/login") {
    isAuthenticated ? next("/") : next()
  } else {
    if (!isAuthenticated) {
      logout()
      next(`/login?redirect=${to.path}&params=${JSON.stringify(to.query ? to.query : to.params)}`)
    } else if (isAuthenticated && to.path === "login") {
      next("/")
    } else {
      next()
    }
  }
})
router.afterEach((to) => {
  const items = [import.meta.env.VITE_APP_TITLE]
  to.meta.title != null && items.unshift(i18n.t(to.meta.title as string))
  document.title = items.join(" | ")
})

export default router
