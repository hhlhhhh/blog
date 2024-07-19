import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';
import Home from '../views/home.vue';
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        redirect: '/user-table',
    },
    {
        path: '/',
        name: 'Home',
        component: Home,
        redirect: '/user-table',
        children: [            
            {
                path: '/user-table',
                name: 'UserTable',
                meta: {
                    title: '用户管理'
                },
                component: () => import('../views/UserTable.vue'),
            }, 
            {
                path: '/comment',
                name: 'Comment',
                meta: {
                    title: '评论管理'
                },
                component: () => import('../views/CommentTable.vue'),
            }, 
            {
                path: '/article',
                name: 'Article',
                meta: {
                    title: '文章管理'
                },
                component: () => import('../views/ArticleTable.vue'),
            }, 
            {
                path: '/tag',
                name: 'Tag',
                meta: {
                    title: '标签管理'
                },
                component: () => import('../views/TagTable.vue'),
            }, 
            {
                path: '/channel',
                name: 'Channel',
                meta: {
                    title: '专栏管理'
                },
                component: () => import('../views/ChannelTable.vue'),
            }, 
            {
                path: '/friend_link',
                name: 'FriendLink',
                meta: {
                    title: '友情链接管理'
                },
                component: () => import('../views/FriendLinkTable.vue'),
            }, 
            {
                path: '/notice',
                name: 'Notice',
                meta: {
                    title: '公告管理'
                },
                component: () => import('../views/NoticeTable.vue'),
            }
        ],
    },
    {
        path: '/login',
        name: 'Login',
        meta: {
            title: '登录',
        },
        component: () => import(/* webpackChunkName: "login" */ '../views/login.vue'),
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    NProgress.start();
    const token = localStorage.getItem('auth');
    if (!token && to.path !== '/login') {
        next('/login');
    } else {
        next();
    }
});

router.afterEach(() => {
    NProgress.done()
})

export default router;
