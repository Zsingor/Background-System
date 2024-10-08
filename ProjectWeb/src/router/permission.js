import router from "@/router/index.js"
import nprogress from "@/plugins/nprogress.js";

const whiteList = ['/login']

// 导航守卫
// 使用 router.beforeEach 注册一个全局前置守卫，判断用户是否登陆
router.beforeEach((to, from, next) => {
    nprogress.start()
    //白名单
    if (whiteList.includes((to.path))) {
        next();
    }
    let hasList = router.getRoutes()
    if (hasList.some(item => item.path === to.path)) {
        next();
    }
    else {
        next("/404");
    }
});

router.afterEach(() => {
    nprogress.done();
});