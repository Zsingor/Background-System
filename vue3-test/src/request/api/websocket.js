// 获取用户信息
import request from "@/request/index.js";

export const getUserService = () => request.get('/webSocket/getUser')

// 发送指定消息
export const sendMessageToService = ({ username, message }) => {
    const params = {
        username,
        message
    }
    return request.get('/webSocket/sendMessageTo', { params })
}

// 发送全部消息
export const sendMessageAll = (message) => {
    const params = {
        message
    }
    return request.get('/webSocket/sendMessageAll', { params })
}