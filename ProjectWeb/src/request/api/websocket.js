// 获取用户信息
import request from "@/request/index.js";

//获取在线的用户列表
export const getUserService = () => request.get('/webSocket/getUser')

//获取所有的用户信息
export const getAllUser=()=> request.get('/webSocket/getAllUser')

export const getMessage=(senderId,receiverId)=> {
    const params = {
        senderId,
        receiverId
    }
    return request.get('/message/getMessage',{params})
}

export const getConversation=(userId)=>{
    const params = {
        userId
    }
    return request.get('/webSocket/getConversations',{params})
}

// 发送指定消息
export const sendMessageToService = (userMessage) => {
    return request.post('/webSocket/sendMessageTo', userMessage )
}

// 发送全部消息
export const sendNotification = (notification) => {
    return request.post('/notification/send',  notification)
}