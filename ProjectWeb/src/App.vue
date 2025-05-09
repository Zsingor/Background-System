<template>
  <el-config-provider :locale="locale">
    <router-view v-slot="{ Component }">
      <component
        :is="Component"
        :style="{
          '--aside-bg': persistentConfig.theme.aside.bgColor,
          '--aside-active-bg': persistentConfig.theme.aside.backgroundColor,
          '--header-bg': persistentConfig.theme.header.bgColor,
          '--header-bg1': persistentConfig.theme.header.bgColor1,
          '--header-font-color': persistentConfig.theme.header.fontColor,
          '--tag-bg': persistentConfig.theme.tag.bgColor,
          '--tag-bg1': persistentConfig.theme.tag.bgColor1,
          '--tag-bg2': persistentConfig.theme.tag.bgColor2,
          '--tag-active-font-color': persistentConfig.theme.tag.activeFontColor,
          '--tag-font-color': persistentConfig.theme.tag.fontColor,
        }"
      />
    </router-view>
  </el-config-provider>
</template>

<script setup>
import { persistentConfig, windowConfig } from '@/layout/layout.js'
import { i18nChangeLanguage } from '@wangeditor/editor'
import { onMounted, ref, watch, getCurrentInstance } from 'vue'
import zhCn from '@/plugins/language/zh-cn.js'
import en from '@/plugins/language/en.js'

const { proxy } = getCurrentInstance()
//用于文档样式的变化
const el = document.documentElement

let locale = ref(zhCn)
let language = ref('zh-CN')

//根据窗口大小改变设置
const onResize = () => {
  window.innerWidth > 1024
    ? (windowConfig.isPc = true)
    : (windowConfig.isPc = false)
  if (!windowConfig.isPc) {
    persistentConfig.isCollapse = true
  }else if(windowConfig.showAside){
    windowConfig.showAside=false
  }
  if (window.innerWidth <= 600) {
    windowConfig.breakpoint = 'xs'
  } else if (window.innerWidth <= 1024) {
    windowConfig.breakpoint = 'sm'
  } else {
    windowConfig.breakpoint = 'lg'
  }
}

//初始化系统语言
const languageInit = () => {
  locale.value = persistentConfig.localeLang === 'zhCn' ? zhCn : en
  language.value = persistentConfig.localeLang === 'zhCn' ? 'zh-CN' : 'en'
  i18nChangeLanguage(language.value)
}

// 开始监听persistentConfig，当属性发生改变时持久化存储并同步更改相应的配置
watch(persistentConfig, (value) => {
  //持久化存储全局设置
  localStorage.setItem('Global_Config', JSON.stringify(value))

  // 监听滚动条变化，仅限非手机尺寸
  if (windowConfig.breakpoint !== 'xs') {
    //是否使用自定义滚动条
    if (persistentConfig.scroll.openCustom) {
      el.classList.add('app-scroll')
    } else {
      el.classList.remove('app-scroll')
    }
    //根据滚动条配置改变样式
    switch (persistentConfig.scroll.size) {
      case 'large':
        el.style.setProperty('--scroll-size', '18px')
        break
      case 'medium':
        el.style.setProperty('--scroll-size', '14px')
        break
      case 'small':
        el.style.setProperty('--scroll-size', '10px')
        break
      case 'hide':
        el.style.setProperty('--scroll-size', '0px')
        break
    }
  }
})

// 监听全局语言的选择，改变系统语言
watch(
  () => persistentConfig.localeLang,
  (value) => {
    proxy.$i18n.locale = value
    //改变element-plus的国际化语言
    locale.value = value === 'zhCn' ? zhCn : en
    //改变wangEditor的国际化语言
    language.value = value === 'zhCn' ? 'zh-CN' : 'en'
    i18nChangeLanguage(language.value)
  }
)

onMounted(() => {
  onResize()

  languageInit()

  // 开启窗口变化监听
  window.addEventListener('resize', onResize, { passive: true })

  //开启页面可见度监听
  // window.addEventListener("visibilitychange", ()=>{
  //   if(document.visibilityState === 'visible'){
  //     console.log("页面显示")
  //   }else if(document.visibilityState === 'hidden'){
  //     console.log("页面隐藏")
  //   }
  // })

})
</script>

<style>
</style>
