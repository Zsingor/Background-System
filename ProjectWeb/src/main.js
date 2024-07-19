import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus' //导入element-plus
import 'element-plus/dist/index.css' //导入element-plus的样式
import VxeTable from './plugins/VxeTable' //导入vxe-table
import router from './router' //导入路由
import * as ElementPlusIconsVue from '@element-plus/icons' //导入图标
import * as echarts from 'echarts' //导入echarts
import "exceljs" //导入excel文件导出工具
import {loadConfig} from "@/setup.js";
import "@/router/permission.js"


// import "@/styles/element-plus.css"
// import "@/styles/vxe-table.css"
// import "@/styles/nprogress.scss"
// 引入全局样式
import "@/styles/index.scss"

const app = createApp(App)
//关闭警告信息
app.config.warnHandler = () => null;
app.config.globalProperties.$baseUrl=import.meta.env.VITE_BASE_API;

//初始化配置信息
loadConfig()

app.use(ElementPlus)
app.use(VxeTable)
app.use(router);

// 全局挂载 echarts
app.provide('echarts', echarts);

//引入所有的icon
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.mount('#app')
