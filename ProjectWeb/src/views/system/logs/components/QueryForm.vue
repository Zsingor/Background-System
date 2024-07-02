<template>
  <el-row :gutter="8" style="padding-bottom: 8px" >
    <el-col :span="6">
      <el-input size="default" v-model="rootData.queryData.username" clearable placeholder="用户名" />
    </el-col>
    <el-col :span="6">
      <el-input size="default" v-model="rootData.queryData.module" clearable placeholder="操作模块" />
    </el-col>
    <el-col :span="6">
      <el-input size="default" v-model="rootData.queryData.operate" clearable placeholder="操作内容" />
    </el-col>
    <el-col :span="6">
      <el-input size="default" v-model="rootData.queryData.details" clearable placeholder="详细内容" />
    </el-col>
    <el-col :span="6">
      <el-input size="default" v-model="rootData.queryData.ip" clearable placeholder="IP地址" />
    </el-col>
    <el-col :span="6">
      <el-date-picker
          style="width: 100%"
          v-model="rootData.queryData.startDate"
          type="datetime"
          placeholder="操作起始时间"
          format="YYYY-MM-DD hh:mm:ss"
          value-format="x"
      />
    </el-col>
    <el-col :span="6">
      <el-date-picker
          style="width: 100%"
          v-model="rootData.queryData.endDate"
          type="datetime"
          placeholder="操作终止时间"
          format="YYYY-MM-DD hh:mm:ss"
          value-format="x"
      />
    </el-col>
    <el-col :span="24" class="centered-buttons">
      <el-button size="default" type="primary" @click="querydata">查询</el-button>
      <el-button size="default" @click="resetForm">重置</el-button>
    </el-col>
  </el-row>
</template>

<script setup>
import {inject, ref} from "vue";

const rootData=inject('rootData')
const xGrid = inject("xGrid");

const querydata=()=>{
  xGrid.value.commitProxy('query')
}

const resetForm=()=>{
  rootData.queryData = Object.assign({}, {});
  xGrid.value.commitProxy("query");
}
</script>

<style scoped>

.centered-buttons {
  text-align: center;
}

.el-col{
  margin-bottom: 10px;
}
</style>