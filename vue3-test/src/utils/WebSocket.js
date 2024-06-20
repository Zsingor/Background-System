// 信息提示
import {onceMessage} from "@/utils/message.js";
import {isEmpty} from "@/utils/commons.js";
import {ElNotification} from "element-plus";
import {persistentConfig} from "@/layout/layout.js";
import {useRouter} from "vue-router";
import router from "@/router/index.js";

// WebSocket地址
const url = 'ws://127.0.0.1:7070/webSocket/'

//路由实例
// const router=useRouter()

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
            onceMessage.success("WebSocket连接成功")
        }

        // 监听WebSocket连接错误信息
        ws.onerror = (e) => {
            console.log('WebSocket重连开关', isReconnecting)
            console.log('WebSocket数据传输发生错误', e)
            onceMessage.error('WebSocket传输发生错误')
            // 打开重连
            reconnect()
        }

        // 监听WebSocket接收消息
        ws.onmessage = (e) => {
            let message=JSON.parse(e.data)
            console.log('WebSocket接收后端消息:' + message)
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
            // 心跳消息不做处理
            if (e.data === 'ok') {
                return
            }
            // 调用回调函数处理接收到的消息
            if (websocket.onMessageCallback) {
                websocket.onMessageCallback(message)
            }
        }
    },

    // WebSocket连接关闭方法
    Close() {
        // 关闭断开重连机制
        isReconnecting = true
        ws.close()
        onceMessage.error('WebSocket断开连接')
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

    // 设置消息处理回调函数
    setMessageCallback(callback) {
        this.onMessageCallback = callback
    }
}

// 监听窗口关闭事件，当窗口关闭时-每一个页面关闭都会触发-扩张需求业务
window.onbeforeunload = function () {
    // 在窗口关闭时关闭 WebSocket 连接
    websocket.Close()
    console.log('WebSocket窗口关闭事件触发')
}

// 浏览器刷新重新连接
// 刷新页面后需要重连-并且是在登录之后
if (performance.getEntriesByType('navigation')[0].type === 'reload') {
    if(!isEmpty(localStorage.getItem("User_Info")))
    {
        const userid=JSON.parse(localStorage.getItem("User_Info")).user_id
        // 延迟一定时间再执行 WebSocket 初始化，确保页面完全加载后再进行连接
        setTimeout(() => {
            console.log('WebSocket执行刷新后重连...')
            // 刷新后重连
            // 获取username（假设为测试username写死，现在是动态获取）
            websocket.Init(userid)
        }, 200) // 适当调整延迟时间
    }
}

// 重连方法
function reconnect() {
    console.log('WebSocket重连开关', isReconnecting)
    // 判断是否主动关闭连接或未登录
    if (isReconnecting||isEmpty(localStorage.getItem("User_Info"))) {
        return
    }
    // 重连定时器-每次WebSocket错误方法onerror触发它都会触发
    reconnectTimer && clearTimeout(reconnectTimer)
    reconnectTimer = setTimeout(function () {
        console.log('WebSocket执行断线重连...')
        const userid=JSON.parse(localStorage.getItem("User_Info")).user_id
        websocket.Init(userid)
        isReconnecting = false
    }, 4000)
}

// 暴露对象
export default websocket

