<template>
  <el-scrollbar wrap-class="scrollbar-wrapper">
    <el-menu
        :default-active="$route.path"
        :background-color="persistentConfig.theme.aside.bgColor"
        :active-text-color="persistentConfig.theme.aside.fontActiveColor"
        :active-background-color="persistentConfig.theme.aside.backgroundColor"
        :text-color="persistentConfig.theme.aside.fontColor"
        :collapse-transition="false"
        :collapse="persistentConfig.isCollapse"
        :router="true"
    >
      <MenuItem
          v-for="(route, index) in routeMenus"
          :key="index"
          :item="route"
      />
    </el-menu>
  </el-scrollbar>
</template>

<script setup>
import {persistentConfig} from "@/layout/layout.js";
import MenuItem from "@/layout/pageaside/MenuItem.vue";
import {routeMenus} from "@/layout/user.js";

</script>

<style lang="scss">
.el-menu {
  border: 0;
}


.el-menu-item.is-active {
  background-color: var(--aside-active-bg, #0ce18f) !important;
  border-radius: 6px;
}
</style>
