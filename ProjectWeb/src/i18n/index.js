import { createI18n } from 'vue-i18n'
import {isEmpty} from "@/utils/commons.js";
// 语言包
import zhCn from './language/zh-cn.js'
import en from './language/en.js'
import zhCN from 'vxe-table/lib/locale/lang/zh-CN'
import enUS from 'vxe-table/lib/locale/lang/en-US'

const config=JSON.parse(localStorage.getItem("Global_Config"))
if(isEmpty(config))
{
  config.localeLang='zhCn'
}

const messages = {
  zhCn: {
    ...zhCn,
    ...zhCN
  },
  en: {
    ...en,
    ...enUS
  }
}

const i18n = createI18n({
  legacy: false, // 设置为 false，启用 composition API 模式
  locale: config.localeLang || 'zhCn',
  messages
})
export default i18n
