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
import {
  computed,
  onMounted,
  ref,
  watchEffect,
  watch,
  getCurrentInstance,
} from 'vue'
import zhCn from '@/plugins/language/zh-cn.js'
import en from '@/plugins/language/en.js'

const { proxy } = getCurrentInstance()

//const language = ref('zh-cn')
//const locale = computed(() => (language.value === 'zh-cn' ? zhCn : en))
let locale=ref(zhCn)
let language = ref('zh-CN')

const onResize = () => {
  window.innerWidth > 1024
    ? (windowConfig.isPc = true)
    : (windowConfig.isPc = false)
  if (!windowConfig.isPc) {
    persistentConfig.isCollapse = true
  }
  if (window.innerWidth <= 600) {
    windowConfig.breakpoint = 'xs'
  } else if (window.innerWidth <= 1024) {
    windowConfig.breakpoint = 'sm'
  } else {
    windowConfig.breakpoint = 'lg'
  }
}

onResize()

// 开始监听persistentConfig，当属性发生改变时同步到localStorage进行持久化
watch(persistentConfig, (value) => {
  localStorage.setItem('Global_Config', JSON.stringify(value))
})

// 监听全局语言的选择，改变系统语言
watch(()=>persistentConfig.localeLang, (value) => {
  proxy.$i18n.locale = value
  //改变element-plus的国际化语言
  locale.value=value==="zhCn"? zhCn : en
  //改变wangEditor的国际化语言
  language.value=value==="zhCn"?"zh-CN":"en"
  i18nChangeLanguage(language.value)
})

//初始化系统语言
const languageInit=()=>{
  locale.value=persistentConfig.localeLang==="zhCn"? zhCn : en
  language.value=persistentConfig.localeLang==="zhCn"?"zh-CN":"en"
  i18nChangeLanguage(language.value)
}

onMounted(() => {
  languageInit()

  // 开启窗口变化监听
  window.addEventListener('resize', onResize, { passive: true })

  // 监听滚动条变化，仅限非手机尺寸
  const el = document.documentElement
  if (windowConfig.breakpoint !== 'xs') {
    watchEffect(() => {
      if (persistentConfig.scroll.openCustom) {
        el.classList.add('app-scroll')
      } else {
        el.classList.remove('app-scroll')
      }
    })
    watchEffect(() => {
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
    })
  }
})
</script>

<style>
</style>
