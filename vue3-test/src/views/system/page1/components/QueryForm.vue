<template>
  <el-row :gutter="8" style="padding-bottom: 8px" >
    <el-col :span="8">
      <el-input size="defailt" v-model="rootData.queryData.name" clearable placeholder="名字" />
    </el-col>
    <el-col :span="8">
      <el-select v-model="rootData.queryData.sex" clearable placeholder="性别">
        <el-option v-for="item in sexList" :key="item.value" :value="item.value" :label="item.label"/>
      </el-select>
    </el-col>
    <el-col :span="8">
      <el-input size="defailt" v-model="rootData.queryData.address" clearable placeholder="地址" />
    </el-col>
    <el-col :span="8">
      <el-date-picker size="default" v-model="rootData.queryData.createdate"
                      style="width: 100%;" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="创建时间" />
    </el-col>
    <el-col :span="16">
      <el-date-picker
          v-model="rootData.queryData.date_list"
          type="daterange"
          value-format="YYYY-MM-DD"
          range-separator="至"
          start-placeholder="活动开始日期"
          end-placeholder="活动结束日期"
          size="default"
          style="width: 100%;"
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

const sexList = ref([
  {label: '男', value: 'male'},
  {label: '女', value: 'female'}
])

const querydata=()=>{
  if(rootData.queryData.date_list&&rootData.queryData.date_list.length>0)
  {
    rootData.queryData.startdate=rootData.queryData.date_list[0]
    rootData.queryData.enddate=rootData.queryData.date_list[1]
  }
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