import router from "@/router/index.js";
import { nextTick, reactive, ref, unref } from "vue";
import { isEmpty } from "@/utils/commons";
import {persistentConfig, reloadCurrentRoute, sidebarWidth} from "@/layout/layout.js";

export const scrollbarRef = ref(null); // 滚动条dom

export const tagMenu = reactive({
    selectIndex: -1,                // 右键选中的下标
    seclectTag: "",                 // 右键选中的标签
    showMenu: false,                // 是否打开菜单
    menuLeft: 0,                   // 菜单定位
    menuTop: 0
});

/**
 * 处理鼠标滚轮滚动
 * @param e  滚动事件
 */
export function handleScroll(e) {
    // 如果滚轮向下滚动，e.wheelDelta为负数，则滚动条向右移动，否则向左移动
    const wheelDelta = e.wheelDelta || -e.deltaY * 40
	scrollbarRef.value.setScrollLeft(scrollbarRef.value.wrapRef.scrollLeft - wheelDelta)
}

/**
 * 根据激活的路由标签所处位置自动滚动到目标标签
 * @param activeTag    激活的标签
 */
export function moveToTarget(activeTag) {
    const scrollWrapper = unref(scrollbarRef);
    const containerWidth = scrollWrapper.wrapRef.offsetWidth;      // 获取滚动容器可视宽度
    const targetLeftPosition = activeTag.getBoundingClientRect().left - sidebarWidth.value; // 跳转目标距离屏幕左边的距离
    if (targetLeftPosition < containerWidth / 3) {    // 如果目标位置距离左边小于可视宽度的3分之一，则触发左滚动，若大于3分之二，则触发右滚动，处于中间则不滚动
        scrollWrapper.wrapRef.scrollLeft = activeTag.offsetLeft - 4; // 先跳转到指定目标，此时激活标签处于第一个位置
        scrollWrapper.wrapRef.scrollLeft -= scrollWrapper.wrapRef.offsetWidth / 3; // 在当前基础上在向左滚动3分之一，增加左边可见度
    } else if (targetLeftPosition > containerWidth / 3 * 2) {
        scrollWrapper.wrapRef.scrollLeft = activeTag.offsetLeft - containerWidth / 3; // 同理，触发向右滚动也需要向左滚动3分之一，增加左边可见度
    }
}

/**
 * 新增路由标签
 * @param routeTag
 */
export function addRouteTag(routeTag) {
    if (!isEmpty(routeTag.title) && !isEmpty(routeTag.path) && !isEmpty(routeTag.componentName)) {
        let flag = true;                                               // 控制是否添加新的路由标签
        persistentConfig.routeTags.forEach((item, index) => {
            if (!isEmpty(item) && item.title === routeTag.title) {       // 如果标题相等，则将flag设置为false，禁止新增
                flag = false;
                if (item.path !== routeTag.path) {                 // 如果跳转path不相等，则替换当前标签的path
                    persistentConfig.routeTags[index] = routeTag;
                }
            }
        });
        if (flag) {                                         // 新增标签
            persistentConfig.routeTags.push({
                title: routeTag.title,
                path: routeTag.path,
                componentName: routeTag.componentName
            });
        }
    }
}

/**
 * 关闭标签
 * @param index       当前下标
 * @param currentPath 当前路由path
 */
export function closeTag(index, currentPath) {
    const temp = persistentConfig.routeTags;
    // 如果关闭的标签是当前激活的标签，那么将跳转到后一个路由，后一个路由没有则跳转到前一个路由，前后都没有则跳转首页
    if (temp[index].path === currentPath) {
        if (temp[index + 1] !== undefined) {
            router.push(temp[index + 1].path).then(() => {
                persistentConfig.routeTags.splice(index, 1);
            });
            return;
        }
        if (temp[index - 1] !== undefined) {
            router.push(temp[index - 1].path).then(() => {
                persistentConfig.routeTags.splice(index, 1);
            });
            return;
        }
    }
    persistentConfig.routeTags.splice(index, 1);
    if (persistentConfig.routeTags.length === 0) {
        router.push("/admin/home").then(() => {
            addRouteTag({
                title: "首页",
                path: "/admin/home",
                componentName: "Home"
            });
        });
    }
}

/**
 * 关闭除选中的tag外所有tags
 */
export function closeOtherTag() {
    if (persistentConfig.routeTags.length > 1) {
        const temp = Object.assign({}, persistentConfig.routeTags[tagMenu.selectIndex]);
        persistentConfig.routeTags.splice(0, persistentConfig.routeTags.length);
        router.push(temp.path).then(() => {
            addRouteTag({
                title: temp.title,
                path: temp.path,
                componentName: temp.componentName
            });
        });
    }
}

// 重新加载页面
export function reloadPage() {
    reloadCurrentRoute.value = true;
    nextTick(() => {
        reloadCurrentRoute.value = null;
    }).then(() => {
    });
}
