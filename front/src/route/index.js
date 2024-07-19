import { createRouter,createWebHashHistory } from "vue-router";

let routes = [
    {
        path: '/',
        name: 'Main',
        component: () => import('@/views/Main.vue'),
        redirect: "/home",
        children: [
            {
                path: '/home',
                name: 'Home',
                component: () => import('@/views/Home.vue')
            },
            {
                path: '/detail/:id',
                name: 'ArticleDetail',
                component: () => import('@/views/ArticleDetail.vue')
            },
            {
                path: '/notice',
                name: 'Notice',
                component: () => import('@/views/NoticeView.vue')
            },
            {
                path: "/channel/:id",
                name: "Channel",
                component: () => import("@/views/ChannelView.vue")
            },
            {
                path: "/article",
                name: "Article",
                component: () => import("@/views/ArticleEdit.vue")
            }
        ]
    },
    {
        path: "/profile",
        name: "Profile",
        component: () => import("@/views/Profile.vue")
    },
    {
        path: "/other",
        name: "ViewOther",
        component: () => import("@/views/ViewOther.vue")
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/Login.vue')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/views/Register.vue')
    }
]

export default createRouter({
    history: createWebHashHistory(),
    routes
})