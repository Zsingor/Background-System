import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus' //导入element-plus
import 'element-plus/dist/index.css' //导入element-plus的样式
import VxeTable from './plugins/VxeTable' //导入vxe-table
import "exceljs"
import router from './router' //导入路由
import * as ElementPlusIconsVue from '@element-plus/icons' //导入图标
import {loadConfig} from "@/setup.js";
import "@/router/permission.js"
import "@/styles/element-plus.css"

const app = createApp(App)
//关闭警告信息
app.config.warnHandler = () => null;

//初始化配置信息
loadConfig()

app.use(ElementPlus)
app.use(VxeTable)
app.use(router);

//引入所有的icon
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.mount('#app')