//常见公共工具类

import { dayjs } from "element-plus";
import { customRef, onUnmounted, ref } from "vue";
import SparkMD5 from 'spark-md5'


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

/**
 * 图片转base64
 * @param {*} file 
 * @returns 
 */
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

/**
 * 获取浏览器信息：类型和版本号
 * @returns {Object}
 */
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

/**
 * 编码生成浏览器指纹
 * @returns {string}
 */
export const getuuid = () => {
    const spark = new SparkMD5();
    const canvas = document.createElement('canvas')
    const ctx = canvas.getContext('2d');
    const txt = 'backstage management system'
    ctx.fillText(txt, 20, 20)
    spark.append(canvas.toDataURL())
    return spark.end()
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
export const arrOrderAscend = (arr, ascendFlag = true) => {
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

/**
 * 并发请求处理函数
 * @param {Array} tsakList 任务列表
 * @param {Number} maxNum 最大并发数
 * @returns {Promise<Array>}
 */
export const concurTask = (tsakList, maxNum) => {
    return new Promise((resolve) => {
        if (tsakList.length === 0) {
            resolve([])
        }
        //记录当前请求的下标
        let index = 0
        //用于记录已完成的请求数
        let count = 0
        //用于存储异步函数的返回值
        const result = []

        async function doTask() {
            let current = index
            const task = tsakList[index]
            index++
            try {
                let res = await task()
                result[current] = res
            }
            catch (err) {
                result[current] = err
            }
            finally {
                count++
                if (count === tsakList.length) {
                    resolve(result)
                }
                if (index < tsakList.length) {
                    doTask()
                }
            }
        }

        for (let i = 0; i < Math.min(tsakList.length, maxNum); i++) {
            doTask()
        }
    })
}

/**
 * 支持防抖的响应式变量
 * @param {*} value 要添加防抖的响应式数据
 * @param {number} duration 持续时间
 * @returns 
 */
export const debounceRef = (value, duration = 1000) => {
    let timer
    return customRef((track, trigger) => {
        return {
            get() {
                //收集依赖
                track()
                return value
            },
            set(val) {
                clearTimeout(timer)
                timer = setTimeout(() => {
                    //派发更新
                    trigger()
                    value = val
                }, duration);

            }
        }
    })
}

/**
 * 大数相加
 * @param {string} x 
 * @param {string} y 
 */
export const plusBigNum = (x, y) => {
    let result = ""
    let maxlen = Math.max(x.length, y.length)
    x = x.padStart(maxlen, '0')
    y = y.padStart(maxlen, '0')
    //进位
    let carry = 0
    for (let i = maxlen - 1; i >= 0; i--) {
        const sum = Number(x[i]) + Number(y[i]) + carry
        carry = Math.floor(sum / 10)
        result = (sum % 10) + result
    }
    if (carry > 0) {
        result = carry + result
    }
    return result
}

/**
 * 借助defer来优化前端渲染时间
 * @param {number} maxCount 最大计时数
 * @returns
 */
export const useDefer = (maxCount = 60) => {
    const frameCount = ref(0)
    let currentId
    function updateFrameCount() {
        currentId = requestAnimationFrame(() => {
            frameCount.value++
            if (frameCount.value >= maxCount) {
                return
            }
            updateFrameCount()
        })
    }
    updateFrameCount()
    onUnmounted(() => {
        cancelAnimationFrame(currentId)
    })
    //n标识多少帧之后开始渲染
    return function defer(n) {
        return frameCount.value >= n
    }
}

/**
 * 深度克隆函数
 * @param {*} value 
 * @returns 
 */
export const deepClone = (value) => {
    //使用WeakMap以便js进行内存回收
    const cache = new WeakMap()
    function _deepClone(value) {
        if (value === null || typeof value !== 'object') {
            return value
        }
        if (cache.has(value)) {
            return cache.get(value)
        }
        const result = Array.isArray(value) ? [] : {}
        cache.set(value, result)
        for (let key in value) {
            if (value.hasOwnProperty(key)) {
                result[key] = _deepClone(value[key])
            }
        }
        return result
    }
    return _deepClone(value)
}

//特殊字符串类
export class specialStr {
    constructor(value) {
        this.str = value
    }

    //获取字符串的码点数量
    pointLength() {
        let len = 0
        for (let i = 0; i < this.str.length;) {
            const codePoint = this.str.codePointAt(i)
            i += codePoint > 65535 ? 2 : 1
            len++
        }
        return len
    }

    //获取字符串对应下标的码点
    pointAt(index) {
        //码元的下标
        let curIndex = 0
        for (let i = 0; i < this.str.length;) {
            const codePoint = this.str.codePointAt(i)
            if (curIndex === index) {
                return String.fromCodePoint(codePoint)
            }
            i += codePoint > 65535 ? 2 : 1
            curIndex++
        }
        return undefined
    }

    //根据码点的范围截取字符串
    sliceByPoint(start, end = this.pointLength()) {
        let res = ""
        for (let i = start; i < end; i++) {
            res += this.pointAt(i)
        }
        return res
    }
}