import {reactive, ref} from "vue";
import {includeRoutes, persistentConfig} from "@/layout/layout.js";
import router from "@/router/index.js";
import _, {isEmpty} from "lodash";
import websocket from "@/utils/WebSocket.js";

export const routeMenus = ref([]);            // 路由菜单集合

// 用户初始信息
export const userModel = {
    user_id:"",
    user_name: "",
    token:"",
    menuList: []
};

export const userInfo = reactive({
    baseInfo: Object.assign({}, userModel),   // 用户基本信息
    routeNames: [],                                 // 用户所拥有的路由名字，当退出登录时根据这些名字删除路由
    user_menus:[], //用户拥有的菜单，用于路由搜索
    unread_count:0 //用户的未读信息
});

//退出登录
export const logout=()=>{
    localStorage.removeItem("User_Info")
    localStorage.removeItem("Global_Config")
    persistentConfig.routeTags = []
    userInfo.user_menus=[]
    skipLogin()
    websocket.Close()
}

//跳转登录页
export const skipLogin=()=>{
    router.push("/login").then(() => {
        removeUserLoginInfo();
    });
}

// 移除用户登录信息
export const removeUserLoginInfo=()=>{
    // 删除vue-router内存中的路由信息
    userInfo.routeNames.forEach(name => {
        router.removeRoute(name);
        _.remove(includeRoutes.value, name);
    });
    userInfo.baseInfo = Object.assign(userModel);
    userInfo.routeNames = [];
}