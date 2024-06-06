//颜色工具类

/**
 * 判断是否 十六进制颜色值.
 * 输入形式可为 #fff000 #f00
 */
export function isHexColor(color) {
    const reg = /^#([0-9a-fA-F]{3}|[0-9a-fA-f]{6})$/;
    return reg.test(color);
}

/**
 * 将16进制颜色值转换成rgb
 */
export function hexToRGB(hex) {
    let sHex = hex.toLowerCase();
    if (isHexColor(hex)) {
        if (sHex.length === 4) {
            let sColorNew = "#";
            for (let i = 1; i < 4; i += 1) {
                sColorNew += sHex.slice(i, i + 1).concat(sHex.slice(i, i + 1));
            }
            sHex = sColorNew;
        }
        const sColorChange = [];
        for (let i = 1; i < 7; i += 2) {
            sColorChange.push(parseInt("0x" + sHex.slice(i, i + 2)));
        }
        return "RGB(" + sColorChange.join(",") + ")";
    }
    return sHex;
}

/**
 * 判断颜色是否为深色
 */
export function colorIsDark(color) {
    if (!isHexColor(color)) return;
    const [r, g, b] = hexToRGB(color)
        .replace(/(?:\(|\)|rgb|RGB)*/g, "")
        .split(",")
        .map((item) => Number(item));
    return r * 0.299 + g * 0.578 + b * 0.114 < 192;
}

/**
 * 根据百分比将一个颜色变暗
 * @param {string} color   16进制颜色
 * @param {number} amount 变暗百分比，范围 0-100
 * @returns {string}
 */
export function colorDarken(color, amount) {
    color = color.indexOf("#") >= 0 ? color.substring(1, color.length) : color;
    amount = Math.trunc((255 * amount) / 100);
    return `#${subtractLight(color.substring(0, 2), amount)}${subtractLight(
        color.substring(2, 4),
        amount
    )}${subtractLight(color.substring(4, 6), amount)}`;
}

/**
 * 根据百分比将一个颜色变亮
 * @param {string} color      16进制颜色
 * @param {number} amount    变亮百分比，范围 0-100
 * @returns {string}
 */
export function colorLighten(color, amount) {
    color = color.indexOf("#") >= 0 ? color.substring(1, color.length) : color;
    amount = Math.trunc((255 * amount) / 100);
    return `#${addLight(color.substring(0, 2), amount)}${addLight(
        color.substring(2, 4),
        amount
    )}${addLight(color.substring(4, 6), amount)}`;
}

/**
 * 按指示的百分比减去十六进制颜色的 R、G 或 B
 * @param {string} color 要更改的颜色
 * @param {number} amount 更改颜色的量
 * @returns {string} 更改后的颜色值
 */
function subtractLight(color, amount) {
    const cc = parseInt(color, 16) - amount;
    const c = cc < 0 ? 0 : cc;
    return c.toString(16).length > 1 ? c.toString(16) : `0${c.toString(16)}`;
}

/**
 * 将传递的百分比求和为十六进制颜色的 R、G 或 B
 * @param {string} color
 * @param {number} amount
 */
function addLight(color, amount) {
    const cc = parseInt(color, 16) + amount;
    const c = cc > 255 ? 255 : cc;
    return c.toString(16).length > 1 ? c.toString(16) : `0${c.toString(16)}`;
}