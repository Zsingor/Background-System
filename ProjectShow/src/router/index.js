import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import Home from '@/views/home/index.vue'
import Animate from '@/views/animate/index.vue'

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
    {
        path: '/animate',
        name: 'animate',
        component: Animate
    },
]


const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
