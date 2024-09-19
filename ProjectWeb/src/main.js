import { createApp } from 'vue'
import App from './App.vue'

import ElementPlus from 'element-plus' //导入element-plus
import 'element-plus/dist/index.css' //导入element-plus的样式

import * as ElementPlusIconsVue from '@element-plus/icons-vue' //导入图标
import * as AntIcon from '@ant-design/icons-vue'   //导入图标

import VxeTable from './plugins/VxeTable' //导入vxe-table
import router from './router' //导入路由
import "@/router/permission.js" //导入路由守卫
import "exceljs" //导入excel文件导出工具

import { loadConfig } from "@/setup.js";
import registerComponents from '@/components/index.js' //导入组件自动化注册
import pinia from "@/stores/index.js"; //引入pinia实例
// 引入国际化i18n
import i18n from '@/i18n/index.js'

// 引入全局样式
import "@/styles/index.scss"

const app = createApp(App)
//关闭警告信息
app.config.warnHandler = () => null;
//声明全局使用的baseurl
app.config.globalProperties.$baseUrl = import.meta.env.VITE_BASE_API;

//初始化配置信息
loadConfig()

app.use(pinia)
app.use(ElementPlus)
app.use(VxeTable)
app.use(router)
app.use(i18n)

//导入全部自定义的组件
Object.keys(registerComponents).forEach((key) => {
    app.component(key, registerComponents[key]);
})

const keysList = ['BellFilled', 'CameraFilled', 'ChromeFilled', 'DeleteFilled', 'HomeFilled', 'PhoneFilled', 'PictureFilled', 'StarFilled', 'VideoCameraFilled','WalletFilled','WarningFilled']

//引入所有的ant-design的icon
for (const [key, component] of Object.entries(AntIcon)) {
    if(keysList.indexOf(key)==-1){
        app.component(key, component)
    }
}

//引入所有的element-plus的icon
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.mount('#app')
