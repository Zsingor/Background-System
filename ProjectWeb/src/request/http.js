import axios from "axios";
import { userInfo } from "@/layout/user.js";

//创建一个新的axios对象
const http = axios.create({
    baseURL: "/api", //代理的ip地址
})

// 拦截器
// 可以在请求发送前对请求做一些处理
http.interceptors.request.use(request => {
    userInfo.baseInfo = JSON.parse(localStorage.getItem("User_Info"));
    if (userInfo.baseInfo) {
        let token = userInfo.baseInfo.token
        if (token) {
            request.headers['token'] = token
        }
    }
    return request;
}, error => {
    console.log('request error' + error)
    return Promise.reject(error)
});

export default http