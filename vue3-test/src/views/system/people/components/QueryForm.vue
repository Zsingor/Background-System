<template>
  <el-row :gutter="8" style="padding-bottom: 8px" >
    <el-col :span="6">
      <el-input size="default" v-model="rootData.queryData.name" clearable placeholder="用户名" />
    </el-col>
    <el-col :span="6">
      <el-select v-model="rootData.queryData.status" clearable placeholder="状态">
        <el-option v-for="item in statusList" :key="item.value" :value="item.value" :label="item.label"/>
      </el-select>
    </el-col>
    <el-col :span="6">
      <el-select clearable placeholder="角色"
                 v-model="rootData.queryData.roleid">
        <el-option v-for="item in rootData.rolesMenu" :key="item.id"
                   :label="item.name" :value="item.id">
        </el-option>
      </el-select>
    </el-col>
    <el-col :span="6">
      <el-input size="default" v-model="rootData.queryData.email" clearable placeholder="邮箱" />
    </el-col>
    <el-col :span="6">
      <el-input size="default" v-model="rootData.queryData.description" clearable placeholder="描述" />
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

const statusList = ref([
  {label: '未启用', value: '0'},
  {label: '已启用', value: '1'}
])

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