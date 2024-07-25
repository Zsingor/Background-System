<template>
  <router-view></router-view>
</template>

<script setup>
import { onMounted } from 'vue';

onMounted(()=>{
  //修改浏览器滚动条的样式
  const el = document.documentElement;
  el.classList.add("app-scroll");
  el.style.setProperty("--scroll-size", "10px");
})
</script>

<style scoped>

</style>
