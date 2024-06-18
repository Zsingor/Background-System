import {isReactive, isRef, toRaw, unref} from "vue";
import router from "@/router/index.js";
import {routeMenus, userInfo} from "@/layout/user.js";
import layoutRoutes from "@/router/routes/LayoutRoutes.js";
import {isEmpty} from "@/utils/commons.js";

// 获取原生路由数组
function getSourceRouteJson(userRouteJson) {
    let routeJson;
    if (isReactive(userRouteJson)) {
        routeJson = Object.assign([], toRaw(userRouteJson));
    } else if (isRef(userRouteJson)) {
        routeJson = Object.assign([], unref(userRouteJson));
    } else {
        routeJson = Object.assign([], userRouteJson);
    }
    return routeJson;
}

/**
 * 根据后台传递的json动态添加路由和创建菜单
 * @param routeJson
 */
export function createRouteAndMenu(routeJson) {
    if(!isEmpty(routeJson))
    {
        const routes = createRoutes(routeJson);
        routes.forEach(route => {
            // 往layout路由中动态添加路由
            router.addRoute("admin", route);
            userInfo.routeNames.push(route.name);
        });
        routeMenus.value=createLayoutRouteMenu(layoutRoutes).concat(createMenus(routeJson));
    }
    else
    {
        routeMenus.value=createLayoutRouteMenu(layoutRoutes)
    }
}

/**
 * 根据后台路由json创建路由
 * @param userRouteJson
 */
export function createRoutes(userRouteJson) {
    const routeJson = getSourceRouteJson(userRouteJson);
    const routes = [];
    routeJson.forEach(item => {
        if(item.children && item.children.length > 0){
            item.children.forEach(r => {
                let routepath=`@/views${r.path}/index.vue`
                routes.push({
                    path: `/admin${item.path}${r.path}`,
                    meta: {title: r.title},
                    name: r.name,
                    component: () => import(/*@vite-ignore*/"../views"+item.path+r.path+"/index.vue")
                });
            });
        }
        else
        {
            routes.push({
                path: `/admin${item.path}`,
                name: item.name,
                meta: {title: item.title},
                component: () => import(/*@vite-ignore*/"../views"+item.path+"/index.vue")
            })
        }

    });
    return routes;
}

/**
 * 根据后台路由json创建菜单
 * @param userRouteJson
 */
export function createMenus(userRouteJson) {
    const routeJson = getSourceRouteJson(userRouteJson);
    const routeMenus = [];
    routeJson.forEach(item => {
        const routeMenu = {
            title: item.title,
            path: item.path,
            children: []
        };
        if (!isEmpty(item.icon)) {
            routeMenu.icon = item.icon;
        }
        if (!isEmpty(item.children)) {
            item.children.forEach(child => {
                const childMenu = {
                    title: child.title,
                    path: routeMenu.path + child.path,
                    children: []
                };
                if (!isEmpty(child.icon)) {
                    childMenu.icon = child.icon;
                }
                routeMenu.children.push(childMenu);
            });
        }
        routeMenus.push(routeMenu);
    });
    return routeMenus;
}

/**
 * 根据layout静态路由创建菜单
 * @param layoutRoute
 */
export function createLayoutRouteMenu(layoutRoute) {
    const routeMenus = [];
    layoutRoute.children.forEach(route => {
        if (verifyRoute(route)) {
            const routeMenu = {
                title: route.meta.title,
                path: "/"+route.path,
                children: []
            };
            if (!isEmpty(route.icon)) {
                routeMenu.icon = route.icon;
            }
            if (!isEmpty(route.children)) {
                route.children.forEach(child => {
                    const childMenu = {
                        title: child.meta.title,
                        path: routeMenu.path+'/'+ child.path,
                        children: []
                    };
                    if (!isEmpty(child.icon)) {
                        childMenu.icon = child.icon;
                    }
                    routeMenu.children.push(childMenu);
                });
            }
            routeMenus.push(routeMenu);
        }
    });
    return routeMenus;
}

export function verifyRoute(route) {
    if (route.meta.hidden !== undefined && route.meta.hidden === true) {
        return false;
    } else {
        if (route.meta.title === undefined && route.meta.title === "") {
            console.error("路由没有设置title属性，无法创建菜单");
            return false;
        } else {
            return true;
        }
    }
}

//
// /**
//  * 验证用户角色是否匹配路由
//  * @param route
//  * @returns {boolean}
//  */
// export function verifyRouteRole(route) {
//     if (route.meta.role === undefined) {
//         return true;
//     } else if (userInfo.baseInfo.user_role === "admin") {
//         return true;
//     } else {
//         return userInfo.baseInfo.user_role === route.meta.role;
//     }
// }