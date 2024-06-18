import home from "@/views/home/index.vue";
import message from "@/views/message/index.vue"


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
        {
            path: 'message',
            title:"我的信息",
            meta:{
                title:"我的信息",
                hidden:true
            },
            name: 'message',
            icon: "Message",
            component: message
        }
    ]
};

export default layoutRoutes;