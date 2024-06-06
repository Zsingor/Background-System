<template>
  <el-drawer title="全局设置" append-to-body
             :size="drawerSize"
             :modal="false"
             class="scrollbar-wrapper"
             :direction="persistentConfig.drawerPosition"
             v-model="controls.showGlobalSetting">
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <div class="drawer_content">
        <el-tabs v-model="tabIndex">
          <el-tab-pane label="基础设置" name="1">
            <BaseSetting/>
          </el-tab-pane>
          <el-tab-pane label="主题设置" name="2">
            <ThemeSetting/>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-scrollbar>
  </el-drawer>
</template>

<script setup>

import {computed, inject, ref} from "vue";
import BaseSetting from "@/layout/pageheader/components/setting/BaseSetting.vue";
import ThemeSetting from "@/layout/pageheader/components/setting/ThemeSetting.vue";
import {persistentConfig, windowConfig} from "@/layout/layout.js";

const controls = inject('controls')
const tabIndex = ref('1')

const drawerSize = computed(() => {
  switch (windowConfig.breakpoint) {
    case 'xs':
      return '100%'
    case 'sm':
      if (persistentConfig.drawerPosition==='ttb' || persistentConfig.drawerPosition==='btt'){
        return '80%'
      }else{
        return '50%'
      }
    case 'lg':
      if (persistentConfig.drawerPosition==='ttb' || persistentConfig.drawerPosition==='btt'){
        return '60%'
      }else{
        return '30%'
      }
  }
})

</script>

<style scoped>
.drawer_content{
  margin-right: 8px;
}
</style>