//常见公共工具类

import { useRouter } from 'vue-router'
import {dayjs} from "element-plus";

const router = useRouter()


/**
 * 判断一个对象 || 数组 || 字符串(包括空格) 是否为空
 * @example null、undefined、'undefined'、[]、''、' '
 * @param obj
 * @return {boolean}
 */
export function isEmpty(obj) {
    if (obj === null || obj === undefined || obj === "undefined") {
        return true;
    } else if (Array.isArray(obj)) {
        return obj.length === 0;
    } else if (typeof obj === "string") {
        return obj.trim().length === 0;
    } else {
        return false;
    }
}

/**
 * 解析日期
 * @param date
 * @param format
 * @returns {string}
 */
export function parseDate(date, format) {
    if (isEmpty(date)) {
        return undefined;
    } else {
        format = format || "YYYY-MM-DD HH:mm:ss";
        return dayjs(date).format(format);
    }
}

// 图片转base64
export function blobToBase64(file) {
    return new Promise((resolve, reject) => {
        if (file) {
            const reader = new FileReader()
            reader.onload = function () {
                if (typeof reader.result === 'string') resolve(reader.result)
                else reject(new Error('图片转base64失败'))
            }
            reader.onerror = function () {
                reject(new Error('图片读取失败'))
            }
            reader.readAsDataURL(file)
        }
    })
}