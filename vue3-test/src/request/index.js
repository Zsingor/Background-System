import axios from "axios";

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
    return request;
}, error => {
    return error;
});

//response拦截器
request.interceptors.response.use(response=>{
    //res即前面的result.data
    let result=response.data
    if(typeof result==='string')
    {
        result=result?JSON.parse(result):result
    }
    return result
},error => {
    console.log('response error'+error)
    return Promise.reject(error)
})

export default request