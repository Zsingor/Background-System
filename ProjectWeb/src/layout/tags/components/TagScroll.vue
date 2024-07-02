<!--标签滚动条-->
<template>
  <el-scrollbar ref="scrollbarRef"
                :vertical="false"
                class="scroll-container"
                @wheel.native.prevent="handleScroll">
    <slot/>
  </el-scrollbar>
</template>

<script setup>
import { scrollbarRef, handleScroll } from "../tag"

</script>

<style lang="scss">
#app-tags{
  .scroll-container {
    white-space: nowrap;
    position: relative;
    overflow: hidden;
    width: 100%;
    height: 100%;

    .el-scrollbar__wrap{
      transition: all 0.3s;
    }

    .el-scrollbar__bar.is-horizontal {
      bottom: 0;
      height: 0;
    }
  }
}
</style>
