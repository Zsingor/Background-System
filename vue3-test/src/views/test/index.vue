<template>
  <div>
    <div class="searchBox">
      <div class="search">
        <el-icon style="font-size: 20px;" title="搜索"><Search /></el-icon>
      </div>
      <div>
        <input type="text">
      </div>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped>
.searchBox{
  position: relative;
  background-color: #16aad8;
  display: flex;
  justify-content: space-between;
  transition: 0.5s;
  overflow: hidden;
  width: 200px;
}

.search{
  min-width: 36px;
}
</style>