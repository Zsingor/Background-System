<!--标签菜单控制面板-->
<template>
  <ul class="tag-menu" v-show="tagMenu.showMenu"
      :style="{left:tagMenu.menuLeft+'px',top:tagMenu.menuTop+'px'}">
    <li class="tag-li" @click="closeTag(tagMenu.selectIndex,route.path)">关闭页面</li>
    <li class="tag-li" @click="closeOtherTag">关闭其他</li>
    <li class="tag-li" @click="reloadPage">刷新页面</li>
  </ul>
</template>

<script setup>
import { watch } from "vue";
import { tagMenu, closeTag, closeOtherTag, reloadPage } from "../tag.js";
import {useRoute} from "vue-router";

const route = useRoute();

// 关闭标签菜单
const closeTagMenu=()=>{
  tagMenu.showMenu = false;
}

// 设置关闭标签菜单的监听事件
watch(() => tagMenu.showMenu, (value) => {
  if (value) {
    document.body.addEventListener("click", closeTagMenu);
  } else {
    document.body.removeEventListener("click", closeTagMenu);
  }
});
</script>

<style scoped >

.tag-menu {
  width: 80px;
  position: absolute;
  z-index: 5000;
  background: #ffffff;
  list-style-type: none;
  padding: 5px 0;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 400;
  color: #333;
  box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, .3);

  .tag-li {
    margin: 0;
    padding: 7px 16px;
    cursor: pointer;

    &:hover {
      background: #eee;
    }
  }
}
</style>
