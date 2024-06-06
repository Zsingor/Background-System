<template>
  <div>
    <el-divider content-position="left">
      <h3>通用设置</h3>
    </el-divider>
    <el-form label-width="140px" label-position="left" size="default">
      <el-form-item label="开启缓存：">
        <el-switch v-model="persistentConfig.openKeepalive"/>
      </el-form-item>
      <el-form-item label="关闭水印：">
        <el-switch v-model="persistentConfig.closeWaterMark"/>
      </el-form-item>
      <el-form-item label="表格分页默认大小：" error="修改后需要刷新浏览器才可生效！">
        <el-select v-model="persistentConfig.defaultPageSize">
          <el-option v-for="size in pageSizes" :key="size"
                     :label="size" :value="size">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="编辑Drawer位置：">
        <el-select v-model="persistentConfig.drawerPosition">
          <el-option v-for="position in drawerPositions" :key="position.value"
                     :label="position.label" :value="position.value">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <el-divider content-position="left">
      <h3>滚动条设置</h3>
    </el-divider>
    <el-form label-width="140px" label-position="left" size="small">
      <el-form-item label="开启自定义滚动条：">
        <el-switch size="default" v-model="persistentConfig.scroll.openCustom"/>
      </el-form-item>
      <el-form-item label="自定义滚动条尺寸：" error="该属性仅支持webkit内核的浏览器">
        <el-select v-model="persistentConfig.scroll.size" size="default">
          <el-option v-for="size in scrollSizes" :key="size.value"
                     :label="size.label" :value="size.value">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import {persistentConfig} from "@/layout/layout.js";
import {pageSizes} from "@/utils/tableconfig.js";

const drawerPositions = [
  {
    label: "左边",
    value: "ltr"
  },
  {
    label: "右边",
    value: "rtl"
  },
  {
    label: "上面",
    value: "ttb"
  },
  {
    label: "下面",
    value: "btt"
  }
];

const scrollSizes = [
  {
    label: "偏大",
    value: "large"
  },
  {
    label: "中等",
    value: "medium"
  },
  {
    label: "偏小",
    value: "small"
  },
  {
    label: "隐藏",
    value: "hide"
  }
];
</script>

<style scoped>
.global-setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;

  .label {
    font-weight: 400;
    font-size: 14px;
    color: #525252;
  }
}
</style>