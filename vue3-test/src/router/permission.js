import router from "@/router/index.js"
import {userInfo} from "@/layout/user.js";

const whiteList=['/login']

// 导航守卫
// 使用 router.beforeEach 注册一个全局前置守卫，判断用户是否登陆
router.beforeEach((to, from, next) => {
    //白名单
    if(whiteList.includes((to.path)))
    {
        next();
    }
    // userInfo.baseInfo=JSON.parse(localStorage.getItem("User_Info"));
    // if(!userInfo.baseInfo)
    // {
    //     if (to.path !== '/login') {
    //         next('/login');
    //     } else {
    //         next();
    //     }
    //     return;
    // }
    // let token=userInfo.baseInfo.token
    let hasList=router.getRoutes()
    if(hasList.some(item => item.path === to.path)){
        next();
    }
    else {
        next("/login");
    }
});