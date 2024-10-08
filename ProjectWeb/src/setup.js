import {createRouteAndMenu} from "@/router/routeUtils.js";
import {isEmpty} from "@/utils/commons.js";
import {ref} from "vue";
import {
    initPersistentConfig,
    persistentConfig, setNprogressColor, updateErrorTheme,
    updateInfoTheme,
    updatePrimaryTheme,
    updateSuccessTheme, updateWarningTheme
} from "@/layout/layout.js";
import {userInfo} from "@/layout/user.js";

/**
 * 项目启动前进行初始化配置
 */
export function loadConfig() {
    // 1.初始化全局配置
    if (isEmpty(localStorage.getItem("Global_Config"))) {
        localStorage.setItem("Global_Config", JSON.stringify(persistentConfig));
    } else {
        initPersistentConfig(JSON.parse(localStorage.getItem("Global_Config")));
    }
    //更新颜色样式
    updatePrimaryTheme(persistentConfig.theme.global.primary)
    updateInfoTheme(persistentConfig.theme.global.info)
    updateSuccessTheme(persistentConfig.theme.global.success)
    updateErrorTheme(persistentConfig.theme.global.error)
    updateWarningTheme(persistentConfig.theme.global.warning)
    setNprogressColor(persistentConfig.theme.header.nprogressColor)
    // 2.初始化路由信息
    if (!isEmpty(localStorage.getItem("User_Info"))) {
        userInfo.baseInfo=JSON.parse(localStorage.getItem("User_Info"))
        if(!isEmpty(userInfo.baseInfo))
        {
            createRouteAndMenu(userInfo.baseInfo.menuList)
        }
    }
    else
    {
        createRouteAndMenu()
    }
}