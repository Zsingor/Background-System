<template>
  <el-row :gutter="8" style="padding-bottom: 8px" >
    <el-col :span="6">
      <el-input size="default" v-model="rootData.queryData.title" clearable placeholder="通知标题" />
    </el-col>
    <el-col :span="6">
      <el-input size="default" v-model="rootData.queryData.content" clearable placeholder="通知内容" />
    </el-col>
    <el-col :span="6">
      <el-date-picker
          style="width: 100%"
          v-model="rootData.queryData.createTime"
          type="datetime"
          placeholder="发送起始时间"
          format="YYYY-MM-DD hh:mm:ss"
          value-format="x"
      />
    </el-col>
    <el-col :span="6">
      <el-date-picker
          style="width: 100%"
          v-model="rootData.queryData.endTime"
          type="datetime"
          placeholder="发送终止时间"
          format="YYYY-MM-DD hh:mm:ss"
          value-format="x"
      />
    </el-col>
    <el-col :span="6">
      <el-button size="default" type="primary" @click="querydata">查询</el-button>
      <el-button size="default" @click="resetForm">重置</el-button>
    </el-col>
  </el-row>
</template>

<script setup>
import {inject} from "vue";

const emit=defineEmits(['query'])

const rootData=inject('rootData')

const querydata=()=>{
  emit("query")
}

const resetForm=()=>{
  rootData.queryData = Object.assign({}, {});
  emit("query")
}
</script>

<style scoped>

.el-col{
  margin-bottom: 10px;
}
</style>