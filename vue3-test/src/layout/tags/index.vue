<template>
  <div id="app-tags" :style="{width: 'calc(100% - '+sidebarWidth+'px)',left: sidebarWidth+'px'}">
    <tag-scroll>
      <div id="tag-sortable-container">
        <div class="tag-item" :class="{'is-active': isActive(tag.path)}"
             v-for="(tag,index) in persistentConfig.routeTags" :key="tag.path"
             @click="router.push(tag.path)"
             @contextmenu.prevent="openTagMenu($event,tag,index)">
          <div class="tag-content">
            <span class="tag-flag"></span>
            <span class="tag-text">{{ tag.title }}</span>
            <span v-if="isActive(tag.path)" class="tag-icon"
                  @click.prevent.stop="reloadPage()">
              <el-icon><Refresh /></el-icon>
            </span>
            <span v-if="isActive(tag.path)" class="tag-icon"
                  @click.prevent.stop="closeTag(index,route.fullPath)">
              <el-icon><Close /></el-icon>
            </span>
          </div>
        </div>
      </div>
    </tag-scroll>
  </div>
  <tag-menu/>
</template>

<script setup>
import TagMenu from "@/layout/tags/components/TagMenu.vue";
import {useRoute,useRouter} from "vue-router";
import {persistentConfig, sidebarWidth} from "@/layout/layout.js";
import {isEmpty} from "@/utils/commons.js";
import {addRouteTag, closeTag, moveToTarget, reloadPage, scrollbarRef, tagMenu} from "@/layout/tags/tag.js";
import {nextTick, onMounted, unref, watch} from "vue";
import Sortable from "sortablejs";

const route = useRoute()
const router=useRouter()

function isActive(path) {
  return path === route.fullPath;
}

// 打开标签菜单
const openTagMenu=(event, tagRoute, index)=>{
  tagMenu.selectIndex = index;
  tagMenu.seclectTag = tagRoute;
  tagMenu.menuTop = event.clientY;
  if ((event.clientX + 80) > document.body.clientWidth) {
    tagMenu.menuLeft = event.clientX - 80;
  } else {
    tagMenu.menuLeft = event.clientX;
  }
  tagMenu.showMenu = true;
}

// 监听路由
watch(() => route.path, () => {
  addRouteTag({
    title: route.meta.title,
    path: route.fullPath,
    componentName: route.name
  });
  nextTick(() => {
    const scrollWrapper = unref(scrollbarRef)
    // 路由发生改变时将目标路由滚动到所对应的标签
    if (!isEmpty(scrollWrapper)) {
      const tagChilds = scrollWrapper.wrap.children[0].children[0].children
      if (tagChilds && tagChilds.length > 0) {
        for (let i = 0; i < tagChilds.length; i++) {
          if (tagChilds[i].classList.contains("is-active")) {
            moveToTarget(tagChilds[i]);
          }
        }
      }
    }
  });
}, {immediate: true});

onMounted(() => {
  // 拖拽路由标签
  const el = document.getElementById("tag-sortable-container");
  Sortable.create(el, {
    animation: 300,        // 滚动动画时长
    delay: 50,  // 延迟多少毫秒开始拖拽
    forceFallback: true,
    scroll: true,           // 启用自动滚动
    scrollSensitivity: 80,  // 距离容器多少像素开始滚动
    scrollSpeed: 15,        // 滚动速度
    ghostClass: "sortable-ghost",
    setData: function (dataTransfer) {
      dataTransfer.setData("Text", "");
    },
    // 拖拽结束执行数据变动操作
    onEnd: evt => {
      // 删除拖拽元素原来的位置，并得到此元素
      const targetRow = persistentConfig.routeTags.splice(evt.oldIndex, 1)[0];
      // 在新的位置中插入原来的元素
      persistentConfig.routeTags.splice(evt.newIndex, 0, targetRow);
    }
  });
});

</script>

<style>

#app-tags {
  top:48px;
  height: 36px;
  background: #ffffff;
  padding: 4px 6px 2px 2px;
  box-shadow: 0 4px 4px -4px #888888;
  position: fixed;
  transition-property: width, left;
  transition-duration: 0.3s;
}

#tag-sortable-container {
  height: 28px;

  .tag-item {
    min-width: 64px;
    color: #000000FF;
    border: 1px solid #DCDFE6FF;
    border-radius: 4px;
    text-align: center;
    padding: 0 8px;
    margin-left: 4px;
    display: inline-block;
    position: relative;

    &:hover {
      cursor: pointer;
      background: #EEEEEEFF;
    }

    .tag-content {
      height: 24px;
      display: flex;
      align-items: center;
      justify-content: center;

      .tag-flag {
        display: none;
      }

      .tag-text {
        font-size: 12px;
      }

      .tag-icon {
        width: 16px;
        height: 16px;
        font-size: 12px;
        margin-left: 4px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;

        &:hover {
          background: #BDBDBDFF;
          color: #FAFAFAFF;
        }
      }
    }
  }

  .tag-item.is-active {
    background: var(--tag-bg,#3798C8FF);

    &:hover {
      background: var(--tag-bg1,#73B7D9FF);
    }

    .tag-content {
      color: var(--tag-active-font-color,#fff);

      .tag-text {
        color: var(--tag-active-font-color,#fff);
      }

      .tag-flag {
        display: inline-block;
        width: 8px;
        height: 8px;
        margin-right: 4px;
        border-radius: 50%;
        background: var(--tag-active-font-color,#fff);
        box-shadow: 0 0 4px #BDBDBDFF;
      }

      .tag-icon {
        &:hover {
          background: var(--tag-bg2,#AFD6E9FF);
          color: #FAFAFAFF;
        }
      }
    }
  }
}
</style>