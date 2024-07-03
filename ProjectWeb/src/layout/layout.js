

// 持久化配置

import _ from "lodash";
import {computed, reactive, ref} from "vue";
import {isEmpty} from "@/utils/commons.js";
import {addRouteTag} from "@/layout/tags/tag.js";
import {colorDarken, colorIsDark, colorLighten} from "@/utils/color.js";

// 重新加载当前路由
export const reloadCurrentRoute = ref(null);

// 缓存的路由
export const includeRoutes = ref([]);

// 排除的路由
export const excludeRoute = ref(['message']);

export const defaultTheme = {
    global: {
        primary: "#16aad8",
        info: "#909399",
        success: "#67C23A",
        warning: "#E6A23C",
        error: "#F56C6C"
    },
    header: {
        bgColor: "hsl(160,10%,95%)",    // 背景色
        bgColor1: "hsl(160,10%,80%)",   // 鼠标悬停在按钮上的背景色
        fontColor: "#000",              // 字体颜色
        nprogressColor: '#16aad8'       // 顶部导航进度条颜色
    },
    tag: {
        bgColor: "hsl(200,57%,50%)",    // 标签被选中时的颜色
        bgColor1: "hsl(200,57%,65%)",   // 标签选中时鼠标移入的背景色
        bgColor2: "hsl(200,57%,80%)",   // 标签选中时鼠标移入图标的背景色
        activeFontColor:"#fff",         //标签激活时的文字颜色
        fontColor: "#fff"        // 字体颜色
    },
    aside:{
        bgColor: "#E5E5E5", //侧边栏的背景色
        fontColor: "#171616",   //侧边栏文字的颜色
        fontActiveColor: "#00AC9B"  //侧边栏文字激活时的颜色
    }
};

//窗口的全局设置
export const windowConfig = reactive({
    isPc: true,       // 根据终端窗口大小(1024px)判断当前环境是否是pc端，如果为true，sidebar将以drawer形式展示
    breakpoint: "",   // 窗口断点，这里仅划分三档：xs(windows<=600) sm(600<windows<=1024) lg(windows>1024)
    showSidebarDrawer: false,
    showNav: true,
    dialogWidth: computed(() => {
        switch (windowConfig.breakpoint) {
            case "lg":
                return "50%";
            case "sm":
                return "80%";
            case "xs":
                return "96%";
        }
    })
});

//全局设置
export const persistentConfig = reactive({
    routeTags: [],                // 路由标签集合
    closeWaterMark:false,          //关闭水印
    isCollapse: false,            // 菜单栏是否缩放
    openKeepalive: true,          //是否开启缓存
    defaultPageSize:25,           // 表格默认分页大小
    drawerPosition:"rtl",         //抽屉的位置
    notiPosition:"top-right",     //通知的位置
    scroll: {                     // 全局滚动条
        openCustom: true,           // 开启自定义
        size: "small"               // 尺寸
    },
    //主题的颜色设置
    theme: {
        global: _.cloneDeep(defaultTheme.global),
        header: _.cloneDeep(defaultTheme.header),
        tag: _.cloneDeep(defaultTheme.tag),
        aside: _.cloneDeep(defaultTheme.aside)
    },
});

// 侧边栏宽度
export const sidebarWidth = computed(() => {
    if (windowConfig.isPc) {
        return persistentConfig.isCollapse ? 64 : 200;
    } else {
        return 0;
    }
});

// 初始化配置
export function initPersistentConfig(config) {
    if (!isEmpty(config.routeTags)) {
        config.routeTags.forEach(item => {
            addRouteTag(item);
        });
    }
    if (!isEmpty(config.isCollapse)) {
        persistentConfig.isCollapse = config.isCollapse;
    }
    if (!isEmpty(config.defaultPageSize)) {
        persistentConfig.defaultPageSize = config.defaultPageSize;
    }
    if (!isEmpty(config.scroll)) {
        persistentConfig.scroll = config.scroll;
    }
    persistentConfig.closeWaterMark = config.closeWaterMark;

    if (!_.isEmpty(config.theme)) {
        Object.keys(config.theme).forEach(key => {
            persistentConfig.theme[key] = config.theme[key];
        });
    }
}

export function updatePrimaryTheme(value) {
    setGlobalTheme(value, "primary");
}

export function updateInfoTheme(value) {
    setGlobalTheme(value, "info");
}

export function updateSuccessTheme(value) {
    setGlobalTheme(value, "success");
}

export function updateWarningTheme(value) {
    setGlobalTheme(value, "warning");
}

export function updateErrorTheme(value) {
    setGlobalTheme(value, "error");
}

//更新element主题色的设置
function setGlobalTheme(value, type) {
    persistentConfig.theme.global[type] = value;
    document.documentElement.style.setProperty(`--el-color-${type}`, value);
    document.documentElement.style.setProperty(`--text-${type}`,
        colorIsDark(value) ? "#fff" : "#606266");
    document.documentElement.style.setProperty(`--border-${type}`,
        colorIsDark(value) ? colorLighten(value, 15) : colorDarken(value, 15));
    for (let i = 1; i <= 2; i++) {
        document.documentElement.style.setProperty(`--color-${type}-${i}`,
            colorIsDark(value) ? colorLighten(value, 10 * i) : colorDarken(value, 10 * i));
    }
}

//设置页面加载进度条的颜色
export function setNprogressColor(value) {
    persistentConfig.theme.header.nprogressColor = value;
    document.documentElement.style.setProperty(`--nprogress-color`, value);
}