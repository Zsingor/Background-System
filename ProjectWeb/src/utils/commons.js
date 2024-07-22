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

/* 获取浏览器信息：类型和版本号 */
export function getExplorerInfo() {
    let explorerInfo = null;                                                 // 浏览器信息对象
    const explorer = navigator.userAgent;                                    // 获取浏览器信息
    const explorerMatch = explorer.match(/Chrome|Firefox|Trident|Version/);  // 解析浏览器类型
    if (isEmpty(explorerMatch)) {                                           // 若解析成功则开始解析版本号
        console.error("未知的浏览器内核：" + explorer);
    } else {
        switch (explorerMatch[0]) {
            case "Chrome":
                explorerInfo = {
                    type: "Chrome",
                    version: explorer.match(/Chrome\/(\d+)/)[1]
                };
                break;
            case "Firefox":
                explorerInfo = {
                    type: "Firefox",
                    version: explorer.match(/Firefox\/(\d+)/)[1]
                };
                break;
            case "Trident": {
                const IEVersion = explorer.match(/MSIE (\d+)/);
                if (IEVersion === null) {
                    explorerInfo = {
                        type: "IE",
                        version: 11
                    };
                } else {
                    explorerInfo = {
                        type: "IE",
                        version: IEVersion[1]
                    };
                }
            }
                break;
            case "Version":
                explorerInfo = {
                    type: "Iphone",
                    version: explorer.match(/Version\/(\d+)/)[1]
                };
        }
        explorerInfo.version = Number(explorerInfo.version);                    // 将版本信息转化成Number类型
    }
    return explorerInfo;
}

//canvas指纹追踪技术(编码生成)
export const uuid=()=>{
    const canvas = document.createElement('canvas')
    const ctx = canvas.getContext('2d');
    const txt='backstage management system'
    ctx.fillText(txt,20,20)
    return canvas.toDataURL()
}

/**
 * 数组并集,只支持一维数组
 * @param {Array} arrOne
 * @param {Array} arrTwo
 */
export const arrAndSet = (arrOne, arrTwo) => {
    return arrOne.concat(arrTwo.filter(v => !arrOne.includes(v)))
}

/**
 * 数组交集,只支持一维数组
 * @param {Array} arrOne
 * @param {Array} arrTwo
 */
export const arrIntersection = (arrOne, arrTwo) => {
    return arrOne.filter(v => arrTwo.includes(v))
}

/**
 * 数组差集,只支持一维数组
 * @param {Array} arrOne
 * @param {Array} arrTwo
 * eg: [1, 2, 3] [2, 4, 5] 差集为[1,3,4,5]
 */
export const arrDifference = (arrOne, arrTwo) => {
    return arrOne.concat(arrTwo).filter(v => !arrOne.includes(v) || !arrTwo.includes(v))
}

/**
 * 数组去重
 * @param {Array} arr  数组
 */
export const arrRemoveRepeat = arr => {
    return Array.from(new Set(arr))
}

/**
 * 数组排序
 * @param {Array} arr  数组
 * @param {Boolean} ascendFlag   升序,默认为 true
 */
export const arrOrderAscend = (arr, ascendFlag=true) => {
    return arr.sort((a, b) => {
        return ascendFlag ? a - b : b - a
    })
}

/**
 * 去掉字符左右空格
 * @param {String} str 字符
 */
export const strTrimLeftOrRight = str => {
    return str.replace(/(^\s*)|(\s*$)/g, "")
}
