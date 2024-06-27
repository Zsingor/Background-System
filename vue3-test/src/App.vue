<template>
  <el-config-provider :locale="locale">
    <router-view v-slot="{ Component }" >
      <component :is="Component" :style="{
      '--aside-bg': persistentConfig.theme.aside.bgColor,
      '--header-bg': persistentConfig.theme.header.bgColor,
      '--header-bg1': persistentConfig.theme.header.bgColor1,
      '--header-font-color': persistentConfig.theme.header.fontColor,
      '--tag-bg': persistentConfig.theme.tag.bgColor,
      '--tag-bg1': persistentConfig.theme.tag.bgColor1,
      '--tag-bg2': persistentConfig.theme.tag.bgColor2,
      '--tag-active-font-color': persistentConfig.theme.tag.activeFontColor,
      '--tag-font-color': persistentConfig.theme.tag.fontColor,
    }"/>
    </router-view>
  </el-config-provider>
</template>

<script setup>

import {persistentConfig, windowConfig} from "@/layout/layout.js";
import {computed, onMounted, ref, watchEffect} from "vue";
import zhCn from '@/plugins/language/zh-cn.js'
import en from '@/plugins/language/en.js'

const language = ref('zh-cn')
const locale = computed(() => (language.value === 'zh-cn' ? zhCn : en))

const onResize=()=>{
  window.innerWidth > 1024 ? windowConfig.isPc = true : windowConfig.isPc = false;
  if (window.innerWidth <= 600) {
    windowConfig.breakpoint = "xs";
  } else if (window.innerWidth <= 1024) {
    windowConfig.breakpoint = "sm";
  } else {
    windowConfig.breakpoint = "lg";
  }
}

onResize()

onMounted(()=>{
  // 开启窗口变化监听
  window.addEventListener("resize", onResize, {passive: true});

  // 监听滚动条变化，仅限非手机尺寸
  const el = document.documentElement;
  if (windowConfig.breakpoint !== "xs") {
    watchEffect(() => {
      if (persistentConfig.scroll.openCustom) {
        el.classList.add("app-scroll");
      } else {
        el.classList.remove("app-scroll");
      }
    });
    watchEffect(() => {
      switch (persistentConfig.scroll.size) {
        case "large":
          el.style.setProperty("--scroll-size", "18px");
          break;
        case "medium":
          el.style.setProperty("--scroll-size", "14px");
          break;
        case "small":
          el.style.setProperty("--scroll-size", "10px");
          break;
        case "hide":
          el.style.setProperty("--scroll-size", "0px");
          break;
      }
    });
  }
})

</script>

<style>
* {
  box-sizing: border-box;
}

body{
  background-image: url("./assets/loading.gif");
  background-position:center;
  background-repeat:no-repeat;
}

html, body, dl, dd, ul, ol, h1, h2, h3, h4, h5, h6, pre, form, fieldset, legend, input, textarea, p, blockquote, figure, hr, menu, dir, thead, tbody, tfoot, th, td {
  margin: 0;
  padding: 0;
}

html, body, #app {
  width: 100%;
  height: 100%;
}

/* 重置浏览器上的滚动条 */
.app-scroll *::-webkit-scrollbar {
  width: var(--scroll-size) !important;
  height: var(--scroll-size) !important;
}

.app-scroll *::-webkit-scrollbar-thumb {
  background-color: rgba(161, 163, 169, 0.5);
  border-radius: calc(var(--scroll-size) - 4px) !important;
}

.app-scroll *::-webkit-scrollbar-thumb:hover {
  background-color: rgba(161, 163, 169, 0.75);
}

.app-scroll *::-webkit-scrollbar-thumb:active {
  background-color: rgba(161, 163, 169, 1);
}
</style>