<template>
  <el-sub-menu v-if="item.children&&item.children.length>0" :index="`/admin${item.path}`">
    <template #title>
      <el-icon v-if="item.icon">
        <component :is="item.icon"></component>
      </el-icon>
      <span>{{ item.title }}</span>
    </template>
    <MenuItem
        v-for="(child, index) in item.children"
        :key="index"
        :item="child"
    />
  </el-sub-menu>
  <el-menu-item v-else :index="`/admin${item.path}`">
    <el-icon v-if="item.icon">
      <component :is="item.icon"></component>
    </el-icon>
    <template #title>{{ item.title }}</template>
  </el-menu-item>
</template>

<script setup>
import {defineProps} from "vue";

const props = defineProps({
  item: {
    type: Array,
    required: true,
    default: [],
  }
})
</script>

<style scoped>

</style>