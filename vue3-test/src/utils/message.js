//消息工具类

import {ElMessage} from "element-plus";


/**
 * 基于element ui通用的消息提示函数
 * @param text
 * @param type
 */
export function message(text, type) {
    ElMessage({
        message: text,
        type: type || "success",
        duration: 2000,
        showClose: true
    });
}