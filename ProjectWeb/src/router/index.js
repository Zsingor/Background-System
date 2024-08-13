import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import login from '@/views/login/index.vue'
import register from '@/views/register/index.vue'
import errorPage from '@/views/errorPage/index.vue'
import layoutRoutes from "@/router/routes/LayoutRoutes.js";


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
    {
        path: '/register',
        name: 'register',
        component: register
    },
    {
        path: '/404',
        name: 'errorPage',
        component: errorPage
    },
]


const router = createRouter({
    //history: createWebHashHistory(),
    history: createWebHistory(),
    routes
})

export default router
