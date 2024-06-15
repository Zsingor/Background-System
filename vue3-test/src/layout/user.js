import {reactive, ref} from "vue";

export const routeMenus = ref([]);            // 路由菜单集合

// 用户初始信息
export const userModel = {
    user_name: "",
    token:"",
    menuList: []
};

export const userInfo = reactive({
    baseInfo: Object.assign({}, userModel),   // 用户基本信息
    routeNames: []                                   // 用户所拥有的路由名字，当退出登录时根据这些名字删除路由
});