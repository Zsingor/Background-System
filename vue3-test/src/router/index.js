import {createRouter, createWebHistory} from 'vue-router'
import login from '@/views/loginview/index.vue'
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
]


const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router