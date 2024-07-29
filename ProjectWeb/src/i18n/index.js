import { createI18n } from 'vue-i18n'
// 语言包
import zhCn from './language/zh-cn.js'
import en from './language/en.js'
import {isEmpty} from "@/utils/commons.js";
import { persistentConfig } from '@/layout/layout.js'

const config=JSON.parse(localStorage.getItem("Global_Config"))
if(isEmpty(config))
{
  config.localeLang='zhCn'
}

const i18n = createI18n({
  legacy: false, // 设置为 false，启用 composition API 模式
  locale: config.localeLang || 'zhCn',
  messages: {
    zhCn,
    en,
  },
})
export default i18n
