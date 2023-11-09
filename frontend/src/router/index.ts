import { RouteRecordRaw, createRouter, createWebHashHistory } from "vue-router"
import { useAuthStore } from "../store/auth-store"
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
        path: "/user",
        name: "user",
        component: () => import(`/@/views/setting/user/index.vue`),
        meta: { title: "用户管理", requiresAuth: true },
      },
      {
        path: "/workspace",
        name: "workspace",
        component: () => import(`/@/views/setting/workspace/WorkspaceView.vue`),
        meta: { title: "工作空间管理", requiresAuth: true },
      },
      {
        path: "/organization",
        name: "organization",
        component: () => import(`/@/views/setting/organization/index.vue`),
        meta: { title: "organization", requiresAuth: true },
      },
      {
        path: "/demo/upload",
        name: "upload",
        component: () => import(`/@/views/demo/upload/index.vue`),
        meta: { title: "上传" },
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
    !!isAuthenticated ? next("/") : next()
  } else {
    if (!isAuthenticated) {
      store.restAuthStore()
      next(`/login?redirect=${to.path}&params=${JSON.stringify(to.query ? to.query : to.params)}`)
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
  to.meta.title != null && items.unshift(to.meta.title)
  document.title = items.join(" | ")
})
export default router
