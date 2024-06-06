import {createRouter, createWebHistory} from 'vue-router'
import login from '@/views/loginview/index.vue'
import home from '@/views/home/index.vue'
import layoutRoutes from "@/router/routes/LayoutRoutes.js";
import {userInfo} from "@/layout/user.js";
import {isEmpty} from "@/utils/commons.js";


export const routes = [
    layoutRoutes,
    {
        path: '/',
        redirect: '/admin/home',
    },
    {
        path: '/login',
        name: 'login',
        component: login
    },
]


const router = createRouter({
    history: createWebHistory(),
    routes
})

// 导航守卫
// 使用 router.beforeEach 注册一个全局前置守卫，判断用户是否登陆
router.beforeEach((to, from, next) => {
    // if (to.path === '/login') {
    //     next();
    // } else {
    //     userInfo.baseInfo = localStorage.getItem('User_Info');
    //     let token=userInfo.baseInfo.token
    //     if (isEmpty(token)) {
    //         next('/login');
    //     } else {
    //         next();
    //     }
    // }
    console.log("路由跳转")
    next()
});

export default router