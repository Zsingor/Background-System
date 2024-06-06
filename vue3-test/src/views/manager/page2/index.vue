<template>
  <div>
    页面二
  </div>
</template>

<script setup>
import {onMounted} from "vue";

defineOptions({
  name: 'page2'
})

onMounted(()=>{
  console.log("加载")
})
</script>

<style scoped>

</style>