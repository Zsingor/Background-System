import axios from "axios";
import {message, onceMessage} from "@/utils/message.js";
import {userInfo} from "@/layout/user.js";
import router from "@/router/index.js";
import websocket from "@/utils/WebSocket.js";

//创建一个新的axios对象
const request=axios.create({
    //baseURL:"http://192.168.1.180:5590", //后端的ip地址

    //跨域的路径，见vite.config.js
    baseURL:"/api", //代理的ip地址

    timeout:6000 //响应时间
})

// request拦截器
// 可以在请求发送前对请求做一些处理
request.interceptors.request.use(request => {
    userInfo.baseInfo=JSON.parse(localStorage.getItem("User_Info"));
    if(userInfo.baseInfo)
    {
        let token=userInfo.baseInfo.token
        if(token)
        {
            request.headers['token']=token
        }
    }
    return request;
}, error => {
    console.log('request error'+error)
    return Promise.reject(error)
});

//response拦截器
request.interceptors.response.use(response=>{
    //res即前面的result.data
    let result=response.data
    if(typeof result==='string')
    {
        result=result?JSON.parse(result):result
    }
    if(result.code===5001)
    {
        onceMessage.error(result.msg)
        router.push("/login")
        return Promise.reject();
    }
    if (result.code===5002)
    {
        onceMessage.error(result.msg)
        return Promise.reject();
    }
    return result
},error => {
    /* 网络连接失败自动处理 */
    if (error.message.indexOf("Network Error") !== -1) {
        message("无法连接服务器!", "error");
        return Promise.reject(error);
    }
    /* 网络请求超时自动处理 */
    if (error.message.indexOf("timeout") !== -1) {
        message("服务器请求超时!", "error");
        return Promise.reject(error);
    }
    /* 请求400处理 */
    if (error.response.status === 400) {
        message("请求不合法!", "error");
        return Promise.reject(error);
    }
    /* 请求404处理 */
    if (error.response.status === 404) {
        message("请求接口不存在!", "error");
        return Promise.reject(error);
    }
    if (error.response.status === 415) {
        message("请求415!", "error");
        return Promise.reject(error);
    }
    // 响应500处理
    if (error.response.status === 500) {
        message("服务器内部错误!", "error");
        return Promise.reject(error);
    }
    return Promise.reject(error);
})

export default request