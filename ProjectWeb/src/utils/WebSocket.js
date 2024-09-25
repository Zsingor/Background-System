// websocket工具类
import {onceMessage} from "@/utils/message.js";
import {isEmpty} from "@/utils/commons.js";
import {ElNotification} from "element-plus";
import {persistentConfig} from "@/layout/layout.js";
import router from "@/router/index.js";
import {nextTick} from "vue";

// WebSocket地址
const url = import.meta.env.VITE_WS_API;

// WebSocket实例
let ws

// 重连定时器实例
let reconnectTimer

// WebSocket重连开关
let isReconnecting = false

// WebSocket对象
const websocket = {
    // WebSocket建立连接
    Init(userid) {
        // 判断浏览器是否支持WebSocket
        if (!('WebSocket' in window)) {
            onceMessage.error("您的浏览器不支持 WebSocket")
            return
        }

        // 创建WebSocket实例
        ws = new WebSocket(url + userid)

        // 监听WebSocket连接
        ws.onopen = () => {
            //onceMessage.success("WebSocket连接成功")
        }

        // 监听WebSocket连接错误信息
        ws.onerror = (e) => {
            onceMessage.error('WebSocket传输发生错误')
            // 打开重连
            reconnect()
        }

        // 监听WebSocket接收消息
        ws.onmessage = (e) => {
            let message=JSON.parse(e.data)
            if(isEmpty(message.receiverId))
            {
                ElNotification({
                    title: message.title,
                    message: message.content,
                    duration:0,
                    position: persistentConfig.notiPosition,
                    type: 'success',
                })
                if (websocket.onNotificationCallback)
                {
                    websocket.onNotificationCallback(message)
                }
            }
            else
            {
                ElNotification({
                    title: message.senderName,
                    message: message.content,
                    duration:0,
                    position: persistentConfig.notiPosition,
                    type: 'info',
                    onClick(){
                        ElNotification.closeAll();
                        router.push({path:'/admin/message', query:{senderId:message.senderId}})
                    }
                })
                // 调用回调函数处理接收到的消息
                if (websocket.onMessageCallback) {
                    websocket.onMessageCallback(message)
                }
            }
        }
    },

    // WebSocket连接关闭方法
    Close() {
        // 关闭断开重连机制
        isReconnecting = true
        ws.close()
        //onceMessage.error('WebSocket断开连接')
    },

    // WebSocket发送信息方法
    Send(data) {
        // 处理发送数据JSON字符串
        const msg = JSON.stringify(data)
        // 发送消息给后端
        ws.send(msg)
    },

    // 暴露WebSocket实例，其他地方调用就调用这个
    getWebSocket() {
        return ws
    },

    // 新增回调函数用于处理收到的消息
    onMessageCallback: null,
    // 新增回调函数用于处理收到的通知
    onNotificationCallback: null,

    // 设置消息处理回调函数
    setMessageCallback(callback) {
        this.onMessageCallback = callback
    },

    // 设置通知处理回调函数
    setNotificationCallback(callback) {
        this.onNotificationCallback = callback
    }
}

// 监听窗口关闭事件，当窗口关闭时-每一个页面关闭都会触发-扩张需求业务
window.onbeforeunload = function () {
    // 在窗口关闭时关闭 WebSocket 连接
    websocket.Close()
}

// 浏览器刷新重新连接
// 刷新页面后需要重连-并且是在登录之后
if (performance.getEntriesByType('navigation')[0].type === 'reload') {
    if(!isEmpty(localStorage.getItem("User_Info")))
    {
        const userid=JSON.parse(localStorage.getItem("User_Info")).user_id
        // 确保页面完全加载后再进行连接
        nextTick(()=>{
            // 刷新后重连
            websocket.Init(userid)
        })
    }
}

// 重连方法
function reconnect() {
    // 判断是否主动关闭连接或未登录
    if (isReconnecting||isEmpty(localStorage.getItem("User_Info"))) {
        return
    }
    // 重连定时器-每次WebSocket错误方法onerror触发它都会触发
    reconnectTimer && clearTimeout(reconnectTimer)
    reconnectTimer = setTimeout(function () {
        onceMessage.error('WebSocket执行断线重连...')
        const userid=JSON.parse(localStorage.getItem("User_Info")).user_id
        websocket.Init(userid)
        isReconnecting = false
    }, 4000)
}

// 暴露对象
export default websocket

