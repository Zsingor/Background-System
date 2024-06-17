<template>
  <div id="layout-wrapper">
    <PageHeader></PageHeader>
    <div class="sideview" :style="{width: sidebarWidth+'px'}">
      <PageAside/>
    </div>
    <TagView></TagView>
    <div class="lineview">
    </div>
    <div class="mainview" :style="{width: 'calc(100% - '+sidebarWidth+'px)',left: sidebarWidth+'px'}">
      <router-view v-slot="{ Component }">
        <transition name="route-fade" mode="out-in" :key="reloadCurrentRoute">
          <keep-alive :include="includeRoutes">
            <component ref="childRouteRef" :is="Component" />
          </keep-alive>
        </transition>
      </router-view>
    </div>
    <canvas
        v-if="!persistentConfig.closeWaterMark"
        id="canvas-watermark"
        width="3000"
        height="1500"
        style="opacity: 0.5;"
    >
      Your browser does not support the HTML5 canvas tag.
    </canvas>
  </div>
</template>

<script setup>
import PageAside from "@/layout/pageaside/index.vue";
import PageHeader from "@/layout/pageheader/index.vue";
import TagView from "@/layout/tags/index.vue"
import {onMounted, ref, watch} from 'vue'
import router from "@/router/index.js";
import {addRouteTag} from "@/layout/tags/tag.js";
import {useRoute} from "vue-router";
import {includeRoutes, persistentConfig, reloadCurrentRoute, sidebarWidth} from "@/layout/layout.js";
import {isEmpty, parseDate} from "@/utils/commons.js";
import {drawWaterMark} from "@/utils/project.js";
import {userInfo} from "@/layout/user.js";

const route = useRoute()

// 首次进入页面将当前路由添加到路由标签中
addRouteTag({
  title: route.meta.title,
  path: route.fullPath,
  componentName: route.name,
});

// 开始监听persistentConfig，当属性发生改变时同步到localStorage进行持久化
watch(persistentConfig, (value) => {
  localStorage.setItem("Global_Config", JSON.stringify(value));
});

// 监听路由标签的变化，同步缓存
watch(
    persistentConfig.routeTags,
    (value) => {
      if(persistentConfig.openKeepalive)
      {
        includeRoutes.value = value.map((item) => {
          if (!isEmpty(item.componentName)) {
            return item.componentName;
          }
        });
      }
    },
    { immediate: true }
);

onMounted(() => {
  if (!persistentConfig.closeWaterMark) {
    const canvas = document.getElementById("canvas-watermark");
    if(isEmpty(userInfo.baseInfo))
    {
      drawWaterMark(
          canvas,
          "游客 "+ parseDate(Date.now()),
          16,
          56
      );
    }
    else
    {
      drawWaterMark(
          canvas,
          userInfo.baseInfo.user_name+" "+ parseDate(Date.now()),
          16,
          56
      );
    }
  }
});

</script>

<style>

#layout-wrapper {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
}

.sideview {
  height: calc(100% - 48px);
  position: fixed;
  top: 48px;
  left: 0;
  background-size: cover;
  transition: width 0.3s;
  background: var(--aside-bg,#E5E5E5FF);
}

.mainview {
  height: calc(100% - 92px);
  top: 92px;
  position: fixed;
  padding: 0 8px 0 8px;
  transition-property: width, left;
  transition-duration: 0.3s;
  overflow: hidden;
  overflow-y: auto;
  background: #ffffff;
}

.lineview {
  height: 2px;
  width: 100%;
  background: #888888;
  opacity: 0.3;
  margin-bottom: 10px;
}

/* 路由切换动画 */
.route-fade-leave-active,.route-fade-enter-active {
  transition: all 0.3s;
}

.route-fade-enter-from{
  transform: translateX(-30px);
  opacity: 0;
}

.route-fade-leave-to{
  transform: translateX(30px);
  opacity: 0;
}

#canvas-watermark {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1000000;
  pointer-events: none;
}
</style>