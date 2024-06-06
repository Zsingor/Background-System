import request from "@/request/index.js";

// 获取用户信息
// 登录
export function getmessage() {
    return request({
        method: 'get',
        url: '/dialog/test'
    })
}