import { RouteRecordRaw, createRouter, createWebHashHistory } from 'vue-router'
const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        component: () => import(`/@/components/layout/BaseLayout.vue`),
    },
    // {
    //     path: '/login',
    //     name: 'login',
    //     component: () => import(`/@/views/login/index.vue`),
    //     meta: {
    //         title: '登录页',
    //         requiresAuth: false,
    //     },
    // },
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