import home from "@/views/home/index.vue";


const layoutRoutes = {
    path: '/admin',
    name: 'admin',
    redirect: '/admin/home',
    component: () => import('@/layout/index.vue'),
    children: [
        {
            path: 'home',
            title:"首页",
            meta:{
                title:"首页"
            },
            name: 'home',
            icon: "House",
            component: home
        },
    ]
};

export default layoutRoutes;