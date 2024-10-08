<template>
  <div id="layout-wrapper">
    <PageHeader></PageHeader>
    <div
      class="sideview"
      :style="{ width: sidebarWidth + 'px' }"
      :class="{ asideShow: windowConfig.showAside }"
    >
      <PageAside />
    </div>
    <div
      class="mask"
      @click="closeMask"
      :style="{
        width: 'calc(100% - ' + sidebarWidth + 'px)',
        left: sidebarWidth + 'px',
      }"
      v-show="windowConfig.showAside"
    ></div>
    <TagView></TagView>
    <div
      class="mainview"
      :style="{
        width: 'calc(100% - ' + sidebarWidth + 'px)',
        left: sidebarWidth + 'px',
      }"
    >
      <router-view v-slot="{ Component }">
        <transition name="route-fade" mode="out-in" :key="reloadCurrentRoute">
          <Suspense>
            <keep-alive :include="includeRoutes" :exclude="excludeRoute">
              <component ref="childRouteRef" :is="Component" />
            </keep-alive>
          </Suspense>
        </transition>
      </router-view>
    </div>
    <canvas
      v-if="!persistentConfig.closeWaterMark"
      id="canvas-watermark"
      width="3000"
      height="1500"
      style="opacity: 0.3"
    >
      Your browser does not support the HTML5 canvas tag.
    </canvas>
  </div>
</template>

<script setup>
import PageAside from '@/layout/pageaside/index.vue'
import PageHeader from '@/layout/pageheader/index.vue'
import TagView from '@/layout/tags/index.vue'
import { onMounted, ref, watch } from 'vue'
import { addRouteTag } from '@/layout/tags/tag.js'
import { useRoute } from 'vue-router'
import {
  excludeRoute,
  includeRoutes,
  persistentConfig,
  reloadCurrentRoute,
  sidebarWidth,
  windowConfig,
} from '@/layout/layout.js'
import { isEmpty, parseDate } from '@/utils/commons.js'
import { drawWaterMark } from '@/utils/project.js'
import { userInfo } from '@/layout/user.js'

const route = useRoute()

// 首次进入页面将当前路由添加到路由标签中
addRouteTag({
  title: route.meta.title,
  path: route.fullPath,
  componentName: route.name,
})

//关闭遮罩
const closeMask = () => {
  if (windowConfig.showAside) {
    persistentConfig.isCollapse = true
    windowConfig.showAside = false
  }
}

// 监听路由标签的变化，同步缓存
watch(
  persistentConfig.routeTags,
  (value) => {
    if (persistentConfig.openKeepalive) {
      includeRoutes.value = value.map((item) => {
        if (!isEmpty(item.componentName)) {
          return item.componentName
        }
      })
    }
  },
  { immediate: true }
)

onMounted(() => {
  if (!persistentConfig.closeWaterMark) {
    const canvas = document.getElementById('canvas-watermark')
    if (isEmpty(userInfo.baseInfo)) {
      drawWaterMark(canvas, '游客 ' + parseDate(Date.now()), 18, 56)
    } else {
      drawWaterMark(
        canvas,
        userInfo.baseInfo.user_name + ' ' + parseDate(Date.now()),
        18,
        56
      )
    }
  }
})
</script>

<style scoped lang="scss">
#layout-wrapper {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
}

.sideview {
  height: calc(100% - $header-height);
  position: fixed;
  top: $header-height;
  left: 0;
  background-size: cover;
  transition: width 0.3s;
  background: var(--aside-bg, #e5e5e5ff);
}

.mask {
  height: calc(100% - $header-height);
  top: $header-height;
  position: fixed;
  transition-property: width, left;
  transition-duration: 0.3s;
  z-index: 900;
  background:rgba(0,0,0,0.5);
}

.mainview {
  height: calc(100% - $header-height - $tags-height);
  top: calc($header-height + $tags-height);
  position: fixed;
  padding: 2px 8px 0 8px;
  transition-property: width, left;
  transition-duration: 0.3s;
  overflow: hidden;
  overflow-y: auto;
  background: #ffffff;
}

.asideShow {
  width: 200px !important;
  z-index: 1000 !important;
}

/* 路由切换动画 */
.route-fade-leave-active,
.route-fade-enter-active {
  transition: all 0.3s;
}

.route-fade-enter-from {
  transform: translateX(-30px);
  opacity: 0;
}

.route-fade-leave-to {
  transform: translateX(30px);
  opacity: 0;
}

/* 水印的样式 */
#canvas-watermark {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1000000;
  pointer-events: none;
}
</style>
