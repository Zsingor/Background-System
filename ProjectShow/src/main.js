import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus' //导入element-plus
import 'element-plus/dist/index.css' //导入element-plus的样式
import * as ElementPlusIconsVue from '@element-plus/icons' //导入图标
import pinia from "@/stores/index.js"; //引入pinia实例
import router from "@/router/index.js";

// 引入国际化i18n
import i18n from '@/i18n/index.js'

// 引入全局样式
//import "@/styles/index.scss"

const app = createApp(App)

app.use(ElementPlus)
app.use(pinia)
app.use(router)
app.use(i18n)

//引入所有的element-plus的icon
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.mount('#app')