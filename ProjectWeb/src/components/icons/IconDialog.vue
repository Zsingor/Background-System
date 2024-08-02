<template>
  <el-dialog fullscreen title="选择Icon图标" v-model="modelValue" @close="closeEvent">
    <div class="icon-wrapper">
      <el-tabs v-model="tabIndex" tab-position="left">
        <el-tab-pane label="Element-Plus Icons" name="1">
          <div style="height: 100%">
            <icon-selected class="icon-select" :icons="Object.keys(ElIcon)" @selectedIcon="setIcon"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="Ant-Design Icons" name="2">
          <div style="height: 100%">
            <icon-selected class="icon-select" :icons="Object.keys(AntIcon)" @selectedIcon="setIcon"/>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </el-dialog>
</template>

<script setup>
import * as ElIcon from '@element-plus/icons-vue'
import * as AntIcon from '@ant-design/icons-vue'
import IconSelected from "@/components/icons/IconSelected.vue";
import {ref} from "vue";


const emit = defineEmits(["setIcon", "update:modelValue"]);

const tabIndex = ref("1");

const setIcon = (value) => {
  emit('setIcon', value);
  closeEvent()
}

function closeEvent() {
  emit("update:modelValue", false);
}

</script>

<style scoped>
.icon-wrapper {
  width: 100%;
  height: 100%;
}

.icon-select {
  width: 100%;
  height: 100%;
}
</style>