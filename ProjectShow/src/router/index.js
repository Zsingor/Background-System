import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import Home from '@/views/home/index.vue'

export const routes = [
    {
        path: '/',
        redirect: '/home',
    },
    {
        path: '/home',
        name: 'home',
        component: Home
    },
]


const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
