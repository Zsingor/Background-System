<template>
  <el-menu
      :default-active="$route.path"
      :background-color="persistentConfig.theme.aside.bgColor"
      :active-text-color="persistentConfig.theme.aside.fontActiveColor"
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
</template>

<script setup>
import {persistentConfig} from "@/layout/layout.js";
import MenuItem from "@/layout/pageaside/MenuItem.vue";
import {routeMenus} from "@/layout/user.js";

</script>

<style>
.el-menu {
  border: 0;
}
</style>